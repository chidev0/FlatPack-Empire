# TUI contract notes (proposal v1)

Status: **proposal for review**. Nothing here is implemented in the engine, and nothing under `src/` was touched. The snapshot and command boundary is manny's v4.2 work; these files describe what the TUI would read and send, so it can be built against fixtures first.

| File | What it is |
|---|---|
| `contract/snapshot.schema.json` | Engine to UI, one object per tick |
| `contract/command.schema.json` | UI to engine, one object per player action |
| `fixtures/samples.ndjson` | The three sample snapshots: day 1 small, day 45 mid, day 85 megastore |
| `fixtures/v42-minimum.ndjson` | What the engine could emit today: required fields only, every planned panel absent |
| `fixtures/commands.ndjson` | One example of each command |
| `fixtures/pretty/` | The same snapshots, indented for reading |
| `tools/make_fixtures.py` | Regenerates the fixtures (authoring only, never imported by the TUI) |
| `tools/validate.py` | Checks any snapshot or command file against the schemas, plus a few cross-field rules |

`validate.py` also works on real engine output, so it can serve as the acceptance check for v4.2: `java ... --headless > run.ndjson`, then `python tui/tools/validate.py --stream run.ndjson`.

## 1. The short version

Source of truth was the local dump (`codebase_dump.txt`, ahead of GitHub v4.0), with roadmap rev 2 and spec rev 2 for planned work.

Most of what the TUI prompt describes is not in the code yet. There is no debt clock, 90-day limit, reputation, zone instances, layout grid, aisle entities, staff, supplier or win/loss. What does exist is enough for a real first screen: the clock, cash and the day's revenue goal, customer counts by `CustomerState`, the checkout lanes (the most complete subsystem), inventory per product line, damages, the truck at the dock, the 7-slot `inboundManifest`, the event strings and the end-of-day summary that `DayManager.handleDayEnd()` prints.

So the contract has three layers:

- **exists**: a field or getter exists today, and the engine copies it.
- **derivable**: no new mechanic. The engine computes it at the boundary from state it already has, for example revenue so far today, or headcount per `CustomerState`.
- **planned**: needs a mechanic from a roadmap version. Optional in the schema. When it is absent or null, the UI shows a "not yet wired" stub.

Required fields are exactly what v4.2 must emit. `fixtures/v42-minimum.ndjson` is that minimum.

## 2. Conventions

- Every field carries `x-status`, and usually `x-source` (the Java class, field or getter) and `x-roadmap` (the version that brings it).
- Money is integer cents (`cashCents`). Ticks, counts, percentages and tile coordinates are integers. Only reputation, Pallas pressure, damage chance and spawn rate are decimals.
- Enum values are the Java enum names verbatim (`GameStatus`, `CustomerState`, `ZoneType`, `ProductType`, `UpgradeType`).
- `catalogId` is the `ProductCatalog` field name (`AlexDeskW`, `BillyBookcaseBr`, `Swedish_Meatballs`). It is the only stable handle on a product line today, because `Product.sku` is a fresh UUID per unit. v4.4's JSON catalogue should replace it.
- The schema is strict (`additionalProperties: false`) so typos in hand-written output fail validation. The UI itself ignores unknown keys at runtime. Any change bumps `schemaVersion`.
- No presentation data in the snapshot: no colours, glyphs or layout hints. The UI decides how to draw, and the engine decides what is true. That keeps the contract usable by Unreal later.
- The engine makes every judgement call. "Low stock", "crowded" and "met the goal" arrive as engine verdicts (`alert`, `lowStock`, `metRevenueGoal`), and `fillPct` and `occupancyPct` arrive precomputed, so the UI does no game arithmetic at all.

## 3. Transport: newline-delimited JSON over stdin/stdout

**Recommendation: stdio now, the same lines over a localhost socket at v17.**

The TUI starts the engine as a child process (`--engine "java -cp out core.Main --headless"`), reads one snapshot per line from its stdout and writes one command per line to its stdin. Reasons:

1. **Least Java to hand-write.** A writer on stdout and a reader thread on stdin are about the smallest boundary possible: no ports, no connection lifecycle, no reconnect logic. It also runs inside one terminal command.
2. **It tests v4.2's own goal.** v4.2's done-when is "`grep System.out` finds nothing outside `ui/`". On a stdio transport, any stray print corrupts the stream and `validate.py` reports it at once.
3. **Recording and replay are free.** A run is just the engine's stdout saved to a file. Once v4.1 lands, re-running the same seed with the recorded command log should produce byte-identical snapshot lines, so `diff` becomes the G0 determinism check.
4. **Nothing is lost later.** The framing is "one JSON object per line" either way. At v17, when Unreal arrives (the architecture doc recommends localhost TCP with protobuf), only the place the lines come from changes. The message shapes map one-to-one onto protobuf messages.

A socket would be better if the engine had to keep its own console UI while the TUI watches, or if several clients connected at once. Neither is needed before v17.

What headless mode needs in the engine, in the order it matters:

- Keep a handle on the real stdout for the protocol (UTF-8, flushed after every line), then point `System.out` at `System.err`. Every stray print then lands in a log instead of the stream. The TUI sends the engine's stderr to `engine.log`. This one step makes stdio workable before v4.2 removes the prints.
- Do not start `InputListener` (JLine raw mode would fight the TUI for the terminal) and do not register `ConsoleNarrator` (it clears the screen every tick).
- Read command lines on their own thread and hand them to the engine thread through a thread-safe queue. That is the v4.1 "thread-safe handoff" item. The adapter can turn each JSON command into the `String[]` that `CommandQueue` already takes, so pause and speed become ordinary `Command` classes in your existing registry.
- Write the snapshot in the output phase of the call order.
- When stdin closes, the UI has gone: stop cleanly.

## 4. Message flow

- **Cadence.** One snapshot per tick, written when `subTick == 0`. Systems run on every sub-tick (11 per tick), so writing on every engine loop would send 11 near-identical snapshots per tick.
- **While paused.** No ticks happen, but the engine writes one snapshot after each command it handles, so the UI sees the result.
- **Events.** `events[]` holds only events raised since the previous snapshot, with a `seq` that increases by one each time. A gap tells the UI it missed some. The first snapshot after the UI connects may carry up to 50 recent events as backlog, which is why each fixture has several.
- **Commands.** Each command gets exactly one `commandResults[]` entry, `APPLIED` or `REJECTED`, with the tick it was applied at. That tick is what the v4.1 command log should record. `Command.execute()` already returns a `String`, which can be the `message`.
- **Day end.** `lastDaySummary` stays in every snapshot until the next day closes. The UI shows its end-of-day modal when `lastDaySummary.day` changes. Proposal: drop `Printer.pause(10000)` from `handleDayEnd()`. In live mode the UI sends `pause` when the modal opens and `resume` when the player dismisses it. Headless runs never wait.
- **Selection is local.** Picking a lane, zone or aisle never goes to the engine. The existing `time`, `info` and `lane` commands set the engine-side `UIState` that only `ScreenRenderer` reads, so they stay with the built-in console UI and are not part of this contract.

## 5. Field map

### What the prompt asked for

| Concept | Contract fields | Today in the code | Status |
|---|---|---|---|
| Tick and seed | `tick`, `seed` | `GameClock.getTick()`; no seed (six unseeded `new Random()`) | tick exists; seed v4.1 (null until then) |
| Day and time | `clock.day`, `tickOfDay`, `timeLabel`, `isStartOfDay`, `isEndOfDay` | `GameClock` methods | exists (day is derived, see quirk 6) |
| Day X/90 | `clock.runLengthDays` | nothing | v5 |
| Speed | `clock.speedMultiplier` | `GameState` stores a sleep in ms (50, 25, 12, 6) | derivable |
| Paused | `status == "PAUSED"` | `GameStatus` | exists |
| Cash | `finance.cashCents` | `GameState.CURRENT_BALANCE` | exists |
| Debt and days on the clock | `finance.debt.*`, `consecutiveNegativeCashDays` | nothing | v5 |
| Revenue | `finance.revenueTodayCents`, `revenueGoalCents` | goal in `Day`; `Day.revenue` only set at day end | goal exists; running revenue derivable |
| Conversion | `customers.conversionPct` | every customer buys today | v6 |
| Reputation C/P/Q | `reputation.*` | nothing | **not placed in rev 2**, see question 5 |
| Zones | `zones[]` | `ZoneType` enum only | v6 occupancy, v9 footprint and capacity |
| Layout grid | `layout`, `zones[].footprint`, `aisles[].footprint` | nothing | v9 |
| Flow between zones | `zoneFlows[]` | nothing | v6 |
| Customer markers | `markers[]` | nothing | WALK_OUT v6, STUCK v9, COMPLAINT has no roadmap slot |
| Aisles and bays | `aisles[]` | `Location(aisle, bin)` and `Product.setWarehouseLocation` exist, nothing assigns them | v9; `staffAssigned` v8 |
| Manifest slots | `inbound.slots[7]` | `DeliveryManager.inboundManifest` (`Product[7][]`) | contents exist; `arrivalDay` and `status` derivable |
| Supplier, lead time, delay | `slots[].supplier`, `leadTimeDays`, `delayReason` | `ProcurementManager` is empty | v7 stub |
| Staff | `staff.*` | `SimConfig.STARTING_CASHIER` constant, unused | v8; named members v16 |
| Events | `events[]` | strings via `EventBuffer.enqueueEvent` | text exists; `seq`, `tick` derivable; `severity` and `code` v4.2 |
| Win/loss | `outcome` | nothing | v5; endings read reputation |
| Pallas | `market.pallas.*` | nothing | v10 stub |
| Restaurant | `restaurant.*` | `ZoneType.FOOD_COURT` and `Swedish_Meatballs` exist | v10 stub |
| Returns and repair | `returns.*` | `ProductState.AS_IS` exists, unused | v12 stub |

### Found in the code, not in the prompt

These are real today, so they get real panels from the first engine build:

| Concept | Contract fields | Source |
|---|---|---|
| Checkout lanes | `checkout.lanes[]`: queue length, processing, items scanned, cart size, ticks to next scan, running total | `CheckoutManager.activeLanes`, `CheckoutLaneSnapshot`, `CheckoutLane.getLaneSize()` |
| Lanes unlocked | `checkout.lanesUnlocked`, `maxLanes` | `GameState.CURRENT_CHECKOUT_LANES`, `SimConfig.MAX_CASHIER_LANES` (5) |
| Customers by state | `customers.byState` | `Customer.getCustomerState()` across `customerList` and the lane queues |
| Customer counts | `customers.inStore`, `shopping`, `trafficToday`, `customerTier` | `Day`, `GameState` |
| Inventory per line | `inventory.items[]`, `totalUnits`, `damagedUnits` | `InventoryManager.getInventorySnapshot()`, `DamagesManager.size()` |
| The truck at the dock | `dock.*` | `DeliveryTruck`, `UnloadManifest`, `GameState.TRUCK_STATUS` |
| End-of-day summary | `lastDaySummary` | what `DayManager.handleDayEnd()` prints today |
| Engine internals | `debug.*` | sleep ms, spawn rate, `TaskScheduler.totalScheduled()` |

### Commands

| Command | Args | Engine today | Status |
|---|---|---|---|
| `pause` | none | `GameState.pauseSimulation()` | method exists, but see quirk 4 |
| `resume` | none | nothing can resume | v4.1 |
| `setSpeed` | `multiplier`: 1, 2, 4, 8 | `GameClock.setSimulationSpeed(int)` | method exists |
| `upgrade` | `upgradeType`: TRUCK, CHECKOUT_LANE | `UpgradeManager.upgrade(UpgradeType)`, not constructed in `Main`, charges nothing | method exists; v13 |
| `orderStock` | `catalogId`, `units`, `arrivalDay` | `DeliveryManager.scheduleInboundProduct(slot, Product)`, no caller | method exists; v7 |

The UI shows commands that are not wired yet as disabled.

## 6. Engine quirks that reach the contract

These are things to know when writing the v4.2 boundary, not fixes. Nothing in `src/` was changed.

**These will break a per-tick snapshot:**

1. **`TransactionReceipt.calculateTotal()` adds onto its own `total` field on every call.** `buildLaneSnapshot()` calls it, so a snapshot written every tick would inflate every lane's payout. It is the same bug as the lane view inflating payouts, except the lane view hits one lane and a snapshot hits all of them. The snapshot must read the total without calling it, or `calculateTotal()` must sum into a local variable.
2. **`buildLaneSnapshot()` throws on an idle lane.** `getLaneTransaction()` is null before the first customer, and `getItemsProcessed()` dereferences `currentCustomer`, which `resetCurrentCustomer()` sets to null. The contract says idle lanes report zeros.
3. **Stray prints on stdout.** `DayManager`, `CommandQueue`, `DeliveryManager`, the `InvalidCommandArgsException` constructor, and `Customer.populateCart()` (once per item picked) all print, and `ConsoleNarrator` clears the screen each tick. Headless mode plus the stderr redirect in section 3 handles this until v4.2 removes them.
4. **Pausing ends the game loop.** `GameEngine.advance()` loops only `while` the status is `RUNNING`, and `Main` calls it once, so `pauseSimulation()` would make it return for good. The v4.1 loop needs to keep running while paused so it can handle `resume`.
5. **Systems run on every sub-tick.** `GameClock.advance()` counts `subTick` 0 to 10 (11 steps, because the check is `subTick == 10`), and `GameEngine.tick()` runs every system on each step. Hence "write on `subTick == 0`".

**These shape a field:**

6. **Day numbering.** `GameState.CURRENT_DAY` starts at 0 and first increments at tick 600, so the dashboard reads DAY 0 on the first day and `DayManager` makes `Day(1)` twice. The contract derives `day = tick / TICKS_PER_DAY + 1`.
7. **Day 1 never gets a truck.** By the time `DeliveryManager.tick()` checks `isNewDay()`, the first `advance()` has already moved `subTick` to 1, so `arrivalTick` is only rolled from day 2. The fixtures show this: `todayArrivalTimeLabel` is null on day 1.
8. **Manifest slots change meaning during the day.** Slot 0 is today until the truck arrives, then `advanceInboundSchedule()` shifts it and slot 0 becomes tomorrow, hence `arrivalDay` on every slot. Nothing calls `scheduleInboundProduct()`, so a live run shows seven empty slots. `advanceInboundSchedule()` also reads `inboundManifest[i + 1]` up to index 7, which is out of bounds once slots 1 to 6 all hold cargo.
9. **The truck never leaves.** `truckAtDock` is never set back to false after unloading, and nothing calls `updateTruckStatus()`, so `truckStatus` is always "Idle". `UnloadManifest` is cumulative for the one registered truck.
10. **Three truck tiers.** `GameState.CURRENT_TRUCK_TIER`, `DeliveryTruck.truckTier` and the static `UpgradeManager.getTruckTier()` can disagree. The contract reads `GameState`.
11. **`Day.metRevenueGoal()` uses `BigDecimal.equals`**, which is true only for an exact match at the same scale. `compareTo(...) >= 0` is probably the intent. The contract carries whatever the engine decides.
12. **`EventBuffer` is a typing animation.** It keeps three lines, dequeues one event at a time and bakes the time into the string. The snapshot needs its own list of raw events with their tick. Until the v4.2 event bus, `enqueueEvent()` could also append to a list the snapshot writer drains.

**Worth knowing:**

13. `Product.stockLevel` is never set, so `findLowStockItems(threshold)` returns every product. `lowStock` stays planned until a real rule exists.
14. Only `IDLE`, `SHOPPING` and `AWAITING_ITEM_TASK` are ever assigned, so the other four `byState` counts read 0 until v6.
15. `CommandBootstrapper` never assigns `checkoutManager`. That matters if the JSON adapter reuses `CommandRegistry`, because `lane` will NPE.
16. `TICKS_PER_DAY` is 600 (10 AM to 8 PM, one tick per open minute) while the spec uses 1,440. v4.1 settles it. The snapshot carries `ticksPerDay` so the UI never hardcodes 600.
17. `setSimulationSpeed()` accepts 8x, while rev 2's v4.1 lists 1x, 2x and 4x.

## 7. Fields I wish existed

Beyond the planned fields above, these would make the boundary easier to write or the UI less dependent on engine internals:

| Wish | Why | Where it would come from |
|---|---|---|
| `GameClock.formatTime(int tickOfDay)` | Format any tick, not only the current one. Needed for ETA labels and event times. | v4.2 |
| A `subTick` getter | `subTick` is private and static today | v4.2 |
| A stable line id on `Product` | So stock, manifests and orders can name a product line | v4.4 |
| `inbound.slots[].etaCountdownLabel` | "in 0:26" without the UI subtracting ticks | v4.2 (derivable) |
| `checkout.lanes[].avgWaitTicks` | Shows why a queue is bad without UI arithmetic | v6 |
| `finance.profitTodayCents` | Revenue minus costs, once costs exist | v5 |
| `finance.revenueByHourCents[]` | A small sparkline in the top bar | v14 |
| `layout.fixtures[]` (racks, displays, walls per tile) | A richer tile view than zone fills | v9 layout compiler |
| `inventory.byMaterial` | Stock per `MaterialType`, which already exists | derivable now |

## 8. Ideas from the references

Each would need new data, so none is built. They are listed with the fields they would need, for you to decide.

| Idea | Fields it would need | Roadmap fit |
|---|---|---|
| Alerts panel with a count in its title | `alerts[]`: `id`, `code`, `severity`, `text`, `sinceTick`, `target {kind, id}`. Events filtered to warn and alert can stand in, but they never clear when the problem is fixed. | v4.2 event bus, rules from v6 onward |
| Selected-zone card (type, racks, capacity, staff, status) | Mostly covered by `zones`, `aisles` and `staff.assignments`. Would add `zone.status` and hazard readings such as water level for the Bay 1 flood. | v9, hazards v11 |
| Ledger tab | `ledger.recentLines[]`: `tick`, `account`, `amountCents`, `memo` | v5 |
| Autosave countdown in the status bar | `save.lastSavedDay`, `save.nextAutosaveDay` | v15 |
| Calendar date in the header | `clock.date`. Needs a decision on the run's start date. | open |
| Equipment condition line | `equipment[]`: `id`, `kind`, `condition`, `status` | v11 (wear, M5) |
| Showroom room-set changes | `layout.workOrders[]`: `id`, `zoneId`, `kind`, `status`, `etaDay` | v9 (M11) |

Not adopted, per the brief: drones, store stability, fika and break tracking, supply-route maps and anything multi-store.

## 9. Open questions

1. **Transport:** stdio now, the same lines over a socket at v17? (recommended)
2. **Day end:** drop `Printer.pause(10000)` and let the UI pause and resume around its modal? (recommended)
3. **Money:** integer cents everywhere? (recommended; decimal strings are the alternative)
4. **Call order:** write the snapshot after the win/loss check instead of before it (clock, systems, economy, events, win/loss, output), so the final snapshot already carries the outcome? (recommended; otherwise the result shows one tick late)
5. **Reputation:** rev 2 never places it. Its inputs arrive across v5 to v12. Ship all three axes with v5's win/loss, or one axis at a time as their inputs land?
6. **Speed:** keep 8x, or drop to 1x, 2x and 4x as rev 2's v4.1 says?
7. **Fixtures:** the three samples fill the planned panels the main screen needs (debt, reputation, zones, aisles, staff, flows, markers) so the UI can be built at scale. The four groups you asked to stub (supplier details, Pallas, restaurant, returns), and supplier names, are left empty so those panels render as stubs. `v42-minimum` shows the screen as the engine can feed it today. Is that the right split?
