# chIKEA

Java warehouse simulation, built from scratch. The goal is a full retail tycoon engine — but I'm building the backend first and the gameplay loop after the foundation earns it.

## Why I built this

Most learning projects stop at "here's a class with some methods." I wanted a project where the design decisions actually had consequences — where a bad data structure choice or a leaking object reference would come back to bite me.

The JCF ban on stacks and queues is intentional. ArrayStack<T> and ArrayQueue<T> are hand-rolled on primitive generic arrays because I wanted to deal with the edge cases directly: loitering references, circular index math, capacity exceptions, stale slots. Hiding behind the standard library would have defeated the point.
Current State — v3.0 Logistics Milestone

The truck works. That's the headline for v3.0.

DeliveryTruck is a real logistics class — dependency-injected, tier-based, LIFO cargo loading via a manual ArrayStack<T>, with per-item damage chance rolling during unload. Products don't just teleport into inventory. They load onto the truck, ride with an ON_TRUCK state, and get routed either into InventoryManager or DamagesManager based on a damage check. Damaged stock is kept completely separate from normal inventory, not mixed in and flagged.

The inventory layer is solid. Full OOP model with Product, FoodItem, and FurnitureItem, UUID-based SKU generation, a Location object tracking aisle, bin, and zone type, and InventoryManager handling add/remove, bulk insert, custom selection sort, low-stock scanning, and price-range filtering.

ArrayQueue<T> is fully built as a circular FIFO structure with proper modulo index math and null-clearing on dequeue. It is not yet wired into a running checkout simulation — that comes in v4.

No game loop yet. No economy or store balance yet. Pricing is still double — BigDecimal migration is queued for v3.x polish before the tick engine starts.

## Architecture

| Package    | Class                     | Role                                                                                               |
|------------|---------------------------|----------------------------------------------------------------------------------------------------|
| core       | InventoryManager          | Main inventory store — add/remove, bulk insert, selection sort, low-stock scan, price-range filter |
| core       | DamagesManager            | Holds damaged stock separately from sellable inventory                                             |
| logistics  | DeliveryTruck             | Dependency-injected truck with LIFO cargo, tier-based capacity, damage routing, upgrade support    |
| models     | Product                   | Base entity — UUID SKU, price, stock level, lifecycle state,Locationdata                           |
| models     | FoodItem                  | Food subtype with protein and vegan metadata                                                       |
| models     | FurnitureItem             | Flat-pack subtype for warehouse-facing furniture inventory                                         |
| models     | Location                  | Aisle/bin coordinate object with zone classification logic                                         |
| structures | ArrayStack<T>             | Manual LIFO structure — backs truck cargo bay                                                      |
| structures | ArrayQueue<T>             | Manual circular FIFO structure — built for upcoming checkout simulation                            |
| exceptions | CapacityExceededException | Thrown when a manual structure hits its initialized bound                                          |
| exceptions | EmptyStructureException   | Thrown on pop/dequeue from an empty structure                                                      |

## Roadmap

v3.x — polish before the loop

    Decouple System.out.println from core classes entirely — callers handle output, not the engine

    Migrate product lifecycle states from raw String literals to a ProductState enum

    Replace hardcoded aisle integer boundaries in Location with a ZoneType enum and a central zone-mapping policy

    Migrate all money fields from double to BigDecimal before the economy layer starts accumulating rounding errors

v4 — game loop foundation

    Fixed-step abstract tick engine, decoupled from wall-clock time

    Work-budget pattern: actions like truck unloading cost ticks instead of finishing in one blocking loop

    Store balance and basic economy

    ArrayQueue<T> wired into a live checkout simulation

    Progressive upgrade systems tied to the economy layer

v5+

    Customer simulation — state-driven, zone-aware, Manhattan distance movement

    Employee staffing, task queues, morale

    Full P&L, supplier orders, and restock cycles

    Eventually: LibGDX 2D rendering when the backend can actually support it

## Design Decisions Worth Noting

JCF stacks and queues are banned by design, not by accident. ArrayStack<T> and ArrayQueue<T> are manual generic array structures because the point is understanding what the standard library is doing, not skipping past it. The circular queue modulo math, the null-clearing on pop, the capacity exception boundaries — those exist because I wrote them.

main stays stable. Volatile work goes on feature branches and merges only when it holds together.

The build order is intentional: inventory and logistics before gameplay. A tycoon loop built on a shaky backend is just a shaky tycoon loop. The engine has to earn the game layer.

### Disclaimer

chIKEA is an independent educational project and is not affiliated with, endorsed by, or associated with Inter IKEA Systems B.V. or any of its subsidiaries. All product and company names are trademarks or registered trademarks of their respective owners. The project name will be changed prior to any public release.