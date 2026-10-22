"""Writes the sample snapshots in tui/fixtures/.

Fixture authoring only. The numbers are illustrative, chosen to be internally
consistent (headcounts add up, aisle stock adds up to inventory); they are not
simulation output. The TUI never imports this file.

Run from the repo root:  python tui/tools/make_fixtures.py
"""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
FIXTURES = ROOT / "fixtures"
TICKS_PER_DAY = 600  # SimConfig.TICKS_PER_DAY
SEED = 20711022

# ProductCatalog, as it is today (names change in v4.4).
CATALOG = {
    "AlexDeskW": ("Alex", "DESK", 7499, "WHITE", "FURNITURE"),
    "AlexDeskB": ("Alex", "DESK", 6499, "BLACK", "FURNITURE"),
    "AlexDresserW": ("Alex", "DRESSER", 5499, "WHITE", "FURNITURE"),
    "AlexDresserB": ("Alex", "DRESSER", 5799, "BLACK", "FURNITURE"),
    "BillyBookcaseBr": ("Billy", "BOOKCASE", 13999, "BROWN", "FURNITURE"),
    "Swedish_Meatballs": ("Meatball", "HOT_FOOD", 599, None, "FOOD"),
}
FURNITURE = [k for k, v in CATALOG.items() if v[4] == "FURNITURE"]

# ZoneType.getLocationName()
ZONE_LABEL = {
    "WAREHOUSE_GRID": "Furniture Warehouse",
    "MARKET_HALL": "Market Hall",
    "SHOWROOM": "Showroom",
    "FOOD_COURT": "Restaurant",
    "CHECKOUT": "Checkout",
    "ENTRANCE": "Entrance",
    "STAFF_ONLY": "Staff Area",
    "DAMAGES": "Damages",
    "RECEIVING": "Receiving",
}

STATES = ["IDLE", "AWAITING_ITEM_TASK", "SHOPPING", "READY_FOR_CHECKOUT",
          "QUEUED_FOR_CHECKOUT", "CHECKING_OUT", "EXITING_STORE"]


def time_label(tick_of_day, formatted=True):
    """Same output as GameClock.getCurrentTime(formatted), for authoring only."""
    hours = [10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8]
    i = tick_of_day // 60
    minute = tick_of_day - 60 * i
    label = f"{hours[i]}:{minute:02d}"
    if not formatted:
        return label
    return label + (" AM" if 60 * i < 120 else " PM")


def abs_tick(day, tick_of_day):
    return (day - 1) * TICKS_PER_DAY + tick_of_day


def clock(day, t, speed, sub_tick=0, planned=True):
    c = {
        "day": day,
        "tickOfDay": t,
        "subTick": sub_tick,
        "ticksPerDay": TICKS_PER_DAY,
        "timeLabel": time_label(t),
        "isStartOfDay": t <= 30,
        "isEndOfDay": t >= 540,
        "speedMultiplier": speed,
    }
    if planned:
        c["runLengthDays"] = 90
    return c


def lane(number, queue, cart=0, done=0, ticks=0, total=0, cashier=None):
    entry = {
        "laneNumber": number,
        "queueLength": queue,
        "isLaneProcessing": cart > 0,
        "laneStatus": "Processing Customer" if cart > 0 else "Awaiting Customer (Idle)",
        "itemsProcessed": done,
        "cartSize": cart,
        "ticksUntilNextScan": ticks,
        "transactionTotalCents": total,
    }
    if cashier is not None:
        entry["cashierStaffId"] = cashier
    return entry


def inventory(units_by_id, in_cart_by_id, damaged, planned=True, low=()):
    items = []
    for cid, (name, ptype, price, color, model) in CATALOG.items():
        if cid not in units_by_id:
            continue
        item = {
            "catalogId": cid,
            "name": name,
            "productType": ptype,
            "color": color,
            "model": model,
            "priceCents": price,
            "units": units_by_id[cid],
            "inCart": in_cart_by_id.get(cid, 0),
        }
        if planned:
            item["lowStock"] = cid in low
        items.append(item)
    return {
        "totalUnits": sum(units_by_id.values()),
        "damagedUnits": damaged,
        "items": items,
    }


def slot(i, arrival_day, contents=None, status=None, eta=None):
    contents = contents or {}
    units = sum(contents.values())
    return {
        "slot": i,
        "arrivalDay": arrival_day,
        "status": status or ("SCHEDULED" if units else "EMPTY"),
        "units": units,
        "contents": [{"catalogId": c, "units": u} for c, u in contents.items()],
        "etaTimeLabel": eta,
    }


def zone(zid, ztype, occupancy, capacity, rects, queue=None, alert=None):
    return {
        "id": zid,
        "type": ztype,
        "label": ZONE_LABEL[ztype],
        "occupancy": occupancy,
        "capacity": capacity,
        "occupancyPct": round(100 * occupancy / capacity),
        "queueLength": queue,
        "footprint": [{"x": x, "y": y, "w": w, "h": h} for x, y, w, h in rects],
        "alert": alert,
    }


def alert(code, severity, text):
    return {"code": code, "severity": severity, "text": text}


def aisles(zone_id, xs, y, h, w, capacity, fills, staffed, stock_plan):
    """fills: percent per aisle; stock_plan: list of catalog ids per aisle."""
    out = []
    for n, (x, fill, ids) in enumerate(zip(xs, fills, stock_plan), start=1):
        units = round(capacity * fill / 100)
        split = [units // len(ids)] * len(ids)
        split[0] += units - sum(split)
        aid = f"A{n:02d}"
        if fill == 0:
            a = alert("STOCK_OUT", "alert", f"Aisle {aid} is out of {ids[0]}")
        elif fill < 15:
            a = alert("LOW_STOCK", "warn", f"Aisle {aid} is low on {ids[0]} ({units} left)")
        else:
            a = None
        out.append({
            "id": aid,
            "number": n,
            "label": f"Aisle {n:02d}",
            "zoneId": zone_id,
            "bins": 6,
            "units": units,
            "capacity": capacity,
            "fillPct": fill,
            "stock": [{"catalogId": c, "units": u} for c, u in zip(ids, split)],
            "staffAssigned": 1 if aid in staffed else 0,
            "footprint": [{"x": x, "y": y, "w": w, "h": h}],
            "alert": a,
        })
    return out


def shelf_units(aisle_list):
    totals = {}
    for a in aisle_list:
        for s in a["stock"]:
            totals[s["catalogId"]] = totals.get(s["catalogId"], 0) + s["units"]
    return totals


def merge_units(shelf, in_cart, extra=None):
    units = {cid: shelf.get(cid, 0) + in_cart.get(cid, 0) for cid in set(shelf) | set(in_cart)}
    for cid, u in (extra or {}).items():
        units[cid] = units.get(cid, 0) + u
    return units


def by_state(counts):
    return dict(zip(STATES, counts))


def staff(on_shift, rostered, lanes, aisle_ids, dock, zones_):
    assignments = [{"targetKind": "lane", "targetId": str(n), "count": 1} for n in range(1, lanes + 1)]
    assignments += [{"targetKind": "aisle", "targetId": a, "count": 1} for a in aisle_ids]
    if dock:
        assignments.append({"targetKind": "dock", "targetId": "dock", "count": dock})
    assignments += [{"targetKind": "zone", "targetId": z, "count": c} for z, c in zones_]
    assert sum(a["count"] for a in assignments) == on_shift
    return {"onShift": on_shift, "rostered": rostered, "assignments": assignments}


def events(day, rows, start_seq):
    out = []
    for n, (t, severity, text, code, zone_id) in enumerate(rows):
        e = {
            "seq": start_seq + n,
            "tick": abs_tick(day, t),
            "timeLabel": time_label(t, formatted=False),
            "severity": severity,
            "text": text,
        }
        if code is not None or zone_id is not None:
            e["code"] = code
            e["zoneId"] = zone_id
        out.append(e)
    return out


def debt(remaining, due_in, next_day, next_cents):
    return {
        "principalCents": 30_000_000,
        "remainingCents": remaining,
        "dueDay": 90,
        "daysUntilDue": due_in,
        "nextPaymentDay": next_day,
        "nextPaymentCents": next_cents,
    }


# --------------------------------------------------------------------------
# Day 1: small store. Engine-scale numbers (SimConfig start balance, one lane,
# tier 1 truck, the four starting stock lines), plus planned panels filled in.
# --------------------------------------------------------------------------
def day01():
    day, t = 1, 160
    a = aisles("warehouse", [11, 15, 19], 1, 6, 2, 40, [75, 75, 73], {"A01"},
               [["AlexDeskW"], ["AlexDeskB"], ["BillyBookcaseBr"]])
    units = merge_units(shelf_units(a), {}, {"Swedish_Meatballs": 30})
    return {
        "schemaVersion": 1,
        "type": "snapshot",
        "seed": SEED,
        "tick": abs_tick(day, t),
        "status": "RUNNING",
        "clock": clock(day, t, 1),
        "finance": {
            "cashCents": 163_999,
            "dayStartingBalanceCents": 150_000,
            "revenueTodayCents": 13_999,
            "revenueGoalCents": 500_000,
            "debt": debt(30_000_000, 89, 7, 450_000),
            "consecutiveNegativeCashDays": 0,
        },
        "reputation": {"commercial": 0.5, "people": 0.5, "quality": 0.5},
        "customers": {
            "inStore": 1,
            "shopping": 1,
            "trafficToday": 2,
            "customerTier": 1,
            "byState": by_state([0, 0, 1, 0, 0, 0, 0]),
            "conversionPct": 100,
            "walkOutsToday": 0,
        },
        "checkout": {"lanesUnlocked": 1, "maxLanes": 5, "lanes": [lane(1, 0, cashier="s01")]},
        "inventory": inventory(units, {}, 0),
        "inbound": {
            "truckCapacity": 50,
            "todayArrivalTimeLabel": None,
            "slots": [slot(i, day + i) for i in range(7)],
        },
        "dock": {
            "truckAtDock": False, "unitsOnTruck": 0, "truckTier": 1, "truckCapacity": 50,
            "damageChancePct": 15, "truckStatus": "Idle",
            "unloadManifest": {"totalProcessed": 0, "inventoryCount": 0, "damageCount": 0},
        },
        "zones": [
            zone("showroom", "SHOWROOM", 1, 20, [(0, 0, 9, 8)]),
            zone("warehouse", "WAREHOUSE_GRID", 0, 20, [(9, 0, 15, 8)]),
            zone("entrance", "ENTRANCE", 0, 6, [(0, 8, 5, 4)]),
            zone("checkout", "CHECKOUT", 0, 8, [(5, 8, 9, 4)], queue=0),
            zone("receiving", "RECEIVING", 0, 4, [(14, 8, 10, 4)]),
        ],
        "layout": {
            "revision": 1, "width": 24, "height": 12,
            "portals": [
                {"id": "p1", "x": 2, "y": 8, "zoneIds": ["entrance", "showroom"]},
                {"id": "p2", "x": 9, "y": 3, "zoneIds": ["showroom", "warehouse"]},
                {"id": "p3", "x": 11, "y": 8, "zoneIds": ["warehouse", "checkout"]},
                {"id": "p4", "x": 5, "y": 10, "zoneIds": ["checkout", "entrance"]},
                {"id": "p5", "x": 20, "y": 8, "zoneIds": ["warehouse", "receiving"]},
            ],
        },
        "zoneFlows": [
            {"fromZoneId": "entrance", "toZoneId": "showroom", "perHour": 1},
            {"fromZoneId": "showroom", "toZoneId": "warehouse", "perHour": 1},
            {"fromZoneId": "warehouse", "toZoneId": "checkout", "perHour": 1},
            {"fromZoneId": "checkout", "toZoneId": "entrance", "perHour": 1},
        ],
        "markers": [],
        "aisles": a,
        "staff": staff(2, 2, 1, ["A01"], 0, []),
        "events": events(day, [
            (38, "info", "Looks like we got a customer. Customers shopping: 1", None, None),
            (121, "info", "Customer being processed in lane 1 (1)", None, None),
            (139, "info", "Lane 1 - Processing Billy BOOKCASE  (1/1)", None, None),
            (139, "info", "Transaction has completed", None, None),
            (140, "info", "Finished processing Customer in lane 1", None, None),
            (150, "debug", "Command dequeued, attempting execute", None, None),
            (152, "info", "Looks like we got a customer. Customers shopping: 1", None, None),
        ], 1),
        "commandResults": [],
        "lastDaySummary": None,
        "outcome": {"state": "IN_PROGRESS", "reason": None, "ending": None},
        "debug": {"simDelayMs": 50, "spawnRate": 0.001, "scheduledTasks": 1},
    }


# --------------------------------------------------------------------------
# Day 45: mid-size store, paused by the player, truck due later this morning.
# --------------------------------------------------------------------------
def day45():
    day, t = 45, 52
    xs = [15, 17, 19, 21, 23, 25, 27, 29]
    fills = [86, 71, 22, 64, 58, 11, 90, 47]
    plan = [["AlexDeskW"], ["AlexDeskB"], ["BillyBookcaseBr"], ["AlexDresserW"],
            ["AlexDresserB", "AlexDeskW"], ["AlexDresserB"], ["BillyBookcaseBr"], ["AlexDeskB", "AlexDresserW"]]
    a = aisles("warehouse", xs, 1, 10, 1, 60, fills, {"A03", "A05", "A08"}, plan)
    in_cart = {"AlexDeskW": 22, "AlexDeskB": 18, "BillyBookcaseBr": 25, "AlexDresserW": 14, "AlexDresserB": 9}
    units = merge_units(shelf_units(a), in_cart, {"Swedish_Meatballs": 140})
    lanes = [lane(1, 3, 9, 4, 12, 39_496, "s01"), lane(2, 2, 7, 1, 30, 13_999, "s02"),
             lane(3, 3, 12, 8, 5, 51_492, "s03")]
    return {
        "schemaVersion": 1,
        "type": "snapshot",
        "seed": SEED,
        "tick": abs_tick(day, t),
        "status": "PAUSED",
        "clock": clock(day, t, 2, sub_tick=4),
        "finance": {
            "cashCents": 1_842_055,
            "dayStartingBalanceCents": 1_765_030,
            "revenueTodayCents": 77_025,
            "revenueGoalCents": 1_980_000,
            "debt": debt(17_140_000, 45, 49, 450_000),
            "consecutiveNegativeCashDays": 0,
        },
        "reputation": {"commercial": 0.61, "people": 0.47, "quality": 0.66},
        "customers": {
            "inStore": 64,
            "shopping": 52,
            "trafficToday": 71,
            "customerTier": 2,
            "byState": by_state([6, 10, 34, 2, 8, 3, 1]),
            "conversionPct": 59,
            "walkOutsToday": 2,
        },
        "checkout": {"lanesUnlocked": 3, "maxLanes": 5, "lanes": lanes},
        "inventory": inventory(units, in_cart, 295, low={"AlexDresserB"}),
        "inbound": {
            "truckCapacity": 100,
            "todayArrivalTimeLabel": "11:18 AM",
            "slots": [
                slot(0, 45, {"BillyBookcaseBr": 40, "AlexDresserB": 60}, "DUE_TODAY", "11:18 AM"),
                slot(1, 46, {"AlexDeskW": 50, "AlexDeskB": 50}),
                slot(2, 47),
                slot(3, 48, {"AlexDresserW": 30, "Swedish_Meatballs": 20}),
                slot(4, 49), slot(5, 50), slot(6, 51),
            ],
        },
        "dock": {
            "truckAtDock": False, "unitsOnTruck": 0, "truckTier": 2, "truckCapacity": 100,
            "damageChancePct": 7.5, "truckStatus": "Idle",
            "unloadManifest": {"totalProcessed": 3020, "inventoryCount": 2725, "damageCount": 295},
        },
        "zones": [
            zone("showroom", "SHOWROOM", 18, 40, [(0, 0, 14, 8)]),
            zone("market-hall", "MARKET_HALL", 14, 30, [(0, 8, 14, 6)]),
            zone("entrance", "ENTRANCE", 3, 15, [(0, 14, 14, 4)]),
            zone("warehouse", "WAREHOUSE_GRID", 12, 45, [(14, 0, 18, 12)],
                 alert=alert("LOW_STOCK", "warn", "1 aisle low on stock")),
            zone("checkout", "CHECKOUT", 11, 30, [(14, 12, 18, 6)], queue=8),
            zone("restaurant", "FOOD_COURT", 6, 24, [(32, 0, 8, 7)], queue=2),
            zone("receiving", "RECEIVING", 0, 6, [(32, 7, 8, 5)]),
            zone("damages", "DAMAGES", 0, 4, [(32, 12, 4, 6)]),
            zone("staff", "STAFF_ONLY", 0, 8, [(36, 12, 4, 6)]),
        ],
        "layout": {
            "revision": 7, "width": 40, "height": 18,
            "portals": [
                {"id": "p1", "x": 6, "y": 14, "zoneIds": ["entrance", "market-hall"]},
                {"id": "p2", "x": 6, "y": 8, "zoneIds": ["market-hall", "showroom"]},
                {"id": "p3", "x": 14, "y": 4, "zoneIds": ["showroom", "warehouse"]},
                {"id": "p4", "x": 14, "y": 10, "zoneIds": ["market-hall", "warehouse"]},
                {"id": "p5", "x": 22, "y": 12, "zoneIds": ["warehouse", "checkout"]},
                {"id": "p6", "x": 14, "y": 15, "zoneIds": ["checkout", "entrance"]},
                {"id": "p7", "x": 32, "y": 3, "zoneIds": ["warehouse", "restaurant"]},
                {"id": "p8", "x": 32, "y": 9, "zoneIds": ["warehouse", "receiving"]},
            ],
        },
        "zoneFlows": [
            {"fromZoneId": "entrance", "toZoneId": "market-hall", "perHour": 54},
            {"fromZoneId": "market-hall", "toZoneId": "showroom", "perHour": 47},
            {"fromZoneId": "showroom", "toZoneId": "warehouse", "perHour": 38},
            {"fromZoneId": "market-hall", "toZoneId": "warehouse", "perHour": 9},
            {"fromZoneId": "warehouse", "toZoneId": "restaurant", "perHour": 12},
            {"fromZoneId": "warehouse", "toZoneId": "checkout", "perHour": 41},
            {"fromZoneId": "checkout", "toZoneId": "entrance", "perHour": 36},
        ],
        "markers": [{"kind": "WALK_OUT", "zoneId": "checkout", "x": 19, "y": 15, "tick": abs_tick(day, 47)}],
        "aisles": a,
        "staff": staff(9, 11, 3, ["A03", "A05", "A08"], 1, [("showroom", 1), ("restaurant", 1)]),
        "events": events(day, [
            (0, "info", "Inbound truck due at 11:18 AM (100 boxes)", "TRUCK_DUE", "receiving"),
            (21, "info", "Aisle A03 restocked: Billy BOOKCASE +24", "RESTOCK", "warehouse"),
            (33, "warn", "Aisle A06 is low on AlexDresserB (7 left)", "LOW_STOCK", "warehouse"),
            (40, "info", "Customer being processed in lane 2 (7)", "CHECKOUT_STARTED", "checkout"),
            (44, "debug", "Lane 3 - Processing Alex DRESSER  (8/12)", "CHECKOUT_ITEM", "checkout"),
            (47, "warn", "Customer left the queue at lane 2 after 18 min", "QUEUE_ABANDONED", "checkout"),
            (49, "info", "Finished processing Customer in lane 1", "CHECKOUT_COMPLETE", "checkout"),
            (50, "info", "Looks like we got a customer. Customers shopping: 52", "CUSTOMER_SPAWNED", "entrance"),
            (51, "info", "Weekly payment due on day 49: $4,500.00", "DEBT_PAYMENT_DUE", None),
        ], 18_204),
        "commandResults": [
            {"id": "c-0012", "command": "pause", "status": "APPLIED", "appliedTick": abs_tick(day, t), "message": None},
        ],
        "lastDaySummary": {
            "day": 44,
            "totalCustomers": 1712,
            "revenueCents": 1_802_440,
            "revenueGoalCents": 1_850_000,
            "metRevenueGoal": False,
            "startingBalanceCents": 1_684_510,
            "endingBalanceCents": 1_765_030,
            "lossesCents": 31_200,
            "profitCents": 80_520,
            "debtPaidCents": 0,
            "conversionPct": 61,
            "walkOuts": 22,
            "damagedUnits": 7,
        },
        "outcome": {"state": "IN_PROGRESS", "reason": None, "ending": None},
        "debug": {"simDelayMs": 25, "spawnRate": 0.012, "scheduledTasks": 52},
    }


# --------------------------------------------------------------------------
# Day 85: megastore, 16 aisles, 168 customers, every lane busy, 5 days left.
# --------------------------------------------------------------------------
def day85():
    day, t = 85, 430
    xs = [31 + 2 * k for k in range(16)]
    fills = [92, 78, 64, 0, 55, 81, 12, 70, 48, 9, 88, 66, 73, 35, 58, 95]
    plan = []
    for k in range(16):
        ids = [FURNITURE[k % 5]]
        if k % 3 == 2:
            ids.append(FURNITURE[(k + 2) % 5])
        plan.append(ids)
    staffed = {"A02", "A04", "A07", "A10", "A13", "A16"}
    a = aisles("warehouse", xs, 10, 9, 1, 120, fills, staffed, plan)
    in_cart = {"AlexDeskW": 96, "AlexDeskB": 81, "BillyBookcaseBr": 118, "AlexDresserW": 64, "AlexDresserB": 57}
    units = merge_units(shelf_units(a), in_cart, {"Swedish_Meatballs": 240})
    queues = [7, 6, 8, 5, 5]
    lanes = [lane(n, q, c, d, s, tot, f"s{n:02d}") for n, q, c, d, s, tot in [
        (1, 7, 11, 6, 14, 52_394), (2, 6, 9, 2, 22, 19_498), (3, 8, 14, 13, 3, 98_986),
        (4, 5, 6, 5, 9, 34_496), (5, 5, 12, 0, 41, 0),
    ]]
    assert sum(queues) == 31
    now = abs_tick(day, t)
    return {
        "schemaVersion": 1,
        "type": "snapshot",
        "seed": SEED,
        "tick": now,
        "status": "RUNNING",
        "clock": clock(day, t, 4),
        "finance": {
            "cashCents": 6_131_240,
            "dayStartingBalanceCents": 3_026_215,
            "revenueTodayCents": 3_105_025,
            "revenueGoalCents": 3_850_000,
            "debt": debt(2_280_000, 5, 90, 2_280_000),
            "consecutiveNegativeCashDays": 0,
        },
        "reputation": {"commercial": 0.74, "people": 0.52, "quality": 0.81},
        "customers": {
            "inStore": 168,
            "shopping": 126,
            "trafficToday": 1940,
            "customerTier": 2,
            "byState": by_state([9, 21, 92, 4, 31, 5, 6]),
            "conversionPct": 64,
            "walkOutsToday": 37,
        },
        "checkout": {"lanesUnlocked": 5, "maxLanes": 5, "lanes": lanes},
        "inventory": inventory(units, in_cart, 482, low={"AlexDresserB"}),
        "inbound": {
            "truckCapacity": 100,
            "todayArrivalTimeLabel": None,
            "slots": [
                slot(0, 86, {"AlexDeskW": 40, "BillyBookcaseBr": 60}),
                slot(1, 87, {"AlexDresserB": 70, "AlexDeskB": 30}),
                slot(2, 88, {"AlexDresserW": 60}),
                slot(3, 89),
                slot(4, 90, {"BillyBookcaseBr": 25, "Swedish_Meatballs": 15}),
                slot(5, 91), slot(6, 92),
            ],
        },
        "dock": {
            "truckAtDock": False, "unitsOnTruck": 0, "truckTier": 2, "truckCapacity": 100,
            "damageChancePct": 7.5, "truckStatus": "Idle",
            "unloadManifest": {"totalProcessed": 6420, "inventoryCount": 5938, "damageCount": 482},
        },
        "zones": [
            zone("showroom-1", "SHOWROOM", 30, 60, [(0, 0, 16, 9)]),
            zone("showroom-2", "SHOWROOM", 22, 50, [(16, 0, 14, 9)]),
            zone("restaurant", "FOOD_COURT", 18, 48, [(30, 0, 12, 9)], queue=4),
            zone("receiving", "RECEIVING", 0, 12, [(42, 0, 22, 9)]),
            zone("market-hall", "MARKET_HALL", 28, 60, [(0, 9, 30, 11)]),
            zone("warehouse", "WAREHOUSE_GRID", 26, 80, [(30, 9, 34, 11)],
                 alert=alert("STOCK_OUT", "alert", "1 aisle out of stock, 2 low")),
            zone("entrance", "ENTRANCE", 8, 30, [(0, 20, 14, 6)]),
            zone("checkout", "CHECKOUT", 36, 40, [(14, 20, 30, 6)], queue=31,
                 alert=alert("CROWDED", "warn", "31 waiting across 5 lanes")),
            zone("damages", "DAMAGES", 0, 6, [(44, 20, 10, 6)]),
            zone("staff", "STAFF_ONLY", 0, 16, [(54, 20, 10, 6)]),
        ],
        "layout": {
            "revision": 23, "width": 64, "height": 26,
            "portals": [
                {"id": "p1", "x": 6, "y": 20, "zoneIds": ["entrance", "market-hall"]},
                {"id": "p2", "x": 8, "y": 9, "zoneIds": ["market-hall", "showroom-1"]},
                {"id": "p3", "x": 16, "y": 4, "zoneIds": ["showroom-1", "showroom-2"]},
                {"id": "p4", "x": 22, "y": 9, "zoneIds": ["showroom-2", "market-hall"]},
                {"id": "p5", "x": 30, "y": 4, "zoneIds": ["showroom-2", "restaurant"]},
                {"id": "p6", "x": 30, "y": 14, "zoneIds": ["market-hall", "warehouse"]},
                {"id": "p7", "x": 36, "y": 9, "zoneIds": ["restaurant", "warehouse"]},
                {"id": "p8", "x": 52, "y": 9, "zoneIds": ["warehouse", "receiving"]},
                {"id": "p9", "x": 34, "y": 20, "zoneIds": ["warehouse", "checkout"]},
                {"id": "p10", "x": 14, "y": 23, "zoneIds": ["checkout", "entrance"]},
            ],
        },
        "zoneFlows": [
            {"fromZoneId": "entrance", "toZoneId": "market-hall", "perHour": 162},
            {"fromZoneId": "market-hall", "toZoneId": "showroom-1", "perHour": 140},
            {"fromZoneId": "showroom-1", "toZoneId": "showroom-2", "perHour": 121},
            {"fromZoneId": "showroom-2", "toZoneId": "market-hall", "perHour": 64},
            {"fromZoneId": "showroom-2", "toZoneId": "restaurant", "perHour": 45},
            {"fromZoneId": "restaurant", "toZoneId": "warehouse", "perHour": 41},
            {"fromZoneId": "market-hall", "toZoneId": "warehouse", "perHour": 58},
            {"fromZoneId": "warehouse", "toZoneId": "checkout", "perHour": 97},
            {"fromZoneId": "checkout", "toZoneId": "entrance", "perHour": 88},
        ],
        "markers": [
            {"kind": "WALK_OUT", "zoneId": "checkout", "x": 20, "y": 22, "tick": now - 3},
            {"kind": "WALK_OUT", "zoneId": "checkout", "x": 31, "y": 23, "tick": now - 9},
            {"kind": "STUCK", "zoneId": "warehouse", "x": 46, "y": 14, "tick": now - 2},
            {"kind": "COMPLAINT", "zoneId": "showroom-2", "x": 21, "y": 5, "tick": now - 6},
        ],
        "aisles": a,
        "staff": staff(16, 19, 5, sorted(staffed), 2, [("showroom-1", 1), ("showroom-2", 1), ("restaurant", 1)]),
        "events": events(day, [
            (402, "info", "Truck unloaded: 100 boxes, 7 damaged", "TRUCK_UNLOADED", "receiving"),
            (409, "alert", "Aisle A04 is out of AlexDresserB", "STOCK_OUT", "warehouse"),
            (414, "warn", "Aisle A10 is low on BillyBookcaseBr (11 left)", "LOW_STOCK", "warehouse"),
            (418, "info", "Customer being processed in lane 5 (12)", "CHECKOUT_STARTED", "checkout"),
            (421, "warn", "Customer left the queue at lane 3 after 22 min", "QUEUE_ABANDONED", "checkout"),
            (422, "warn", "Checkout queues are long: 31 waiting", "CROWDED", "checkout"),
            (424, "warn", "Complaint logged in Showroom 2", "COMPLAINT", "showroom-2"),
            (426, "info", "Finished processing Customer in lane 4", "CHECKOUT_COMPLETE", "checkout"),
            (427, "warn", "Customer left the queue at lane 1 after 14 min", "QUEUE_ABANDONED", "checkout"),
            (428, "warn", "A shopper is stuck in the warehouse near aisle A08", "STUCK", "warehouse"),
            (429, "info", "Looks like we got a customer. Customers shopping: 126", "CUSTOMER_SPAWNED", "entrance"),
            (430, "alert", "Debt clock: 5 days left, $22,800.00 remaining", "DEBT_CLOCK", None),
        ], 401_877),
        "commandResults": [
            {"id": "c-0042", "command": "upgrade", "status": "REJECTED", "appliedTick": now,
             "message": "Maximum Cashier Lanes Reached"},
        ],
        "lastDaySummary": {
            "day": 84,
            "totalCustomers": 2104,
            "revenueCents": 3_790_880,
            "revenueGoalCents": 3_700_000,
            "metRevenueGoal": True,
            "startingBalanceCents": 2_512_005,
            "endingBalanceCents": 3_026_215,
            "lossesCents": 52_300,
            "profitCents": 514_210,
            "debtPaidCents": 450_000,
            "conversionPct": 63,
            "walkOuts": 41,
            "damagedUnits": 11,
        },
        "outcome": {"state": "IN_PROGRESS", "reason": None, "ending": None},
        "debug": {"simDelayMs": 12, "spawnRate": 0.03, "scheduledTasks": 126},
    }


# --------------------------------------------------------------------------
# What v4.2 can emit from today's engine: required fields only, every planned
# panel absent. Day 2 at 10:45 AM: a truck is due at 11:37 AM but nothing has
# ever been ordered, so all seven manifest slots are empty.
# --------------------------------------------------------------------------
def v42_minimum():
    day, t = 2, 45
    units = {"AlexDeskW": 28, "AlexDeskB": 29, "BillyBookcaseBr": 29, "Swedish_Meatballs": 29}
    return {
        "schemaVersion": 1,
        "type": "snapshot",
        "seed": 4242,
        "tick": abs_tick(day, t),
        "status": "RUNNING",
        "clock": clock(day, t, 1, planned=False),
        "finance": {
            "cashCents": 186_095,
            "dayStartingBalanceCents": 186_095,
            "revenueTodayCents": 0,
            "revenueGoalCents": 40_426,
        },
        "customers": {
            "inStore": 1,
            "shopping": 1,
            "trafficToday": 1,
            "customerTier": 1,
            "byState": by_state([1, 0, 0, 0, 0, 0, 0]),
        },
        "checkout": {"lanesUnlocked": 1, "maxLanes": 5, "lanes": [lane(1, 0)]},
        "inventory": inventory(units, {}, 0, planned=False),
        "inbound": {
            "truckCapacity": 50,
            "todayArrivalTimeLabel": "11:37 AM",
            "slots": [{k: v for k, v in slot(i, day + i).items() if k != "etaTimeLabel"} for i in range(7)],
        },
        "dock": {
            "truckAtDock": False, "unitsOnTruck": 0, "truckTier": 1, "truckCapacity": 50,
            "damageChancePct": 15, "truckStatus": "Idle",
            "unloadManifest": {"totalProcessed": 0, "inventoryCount": 0, "damageCount": 0},
        },
        "events": events(day, [
            (31, "info", "Looks like we got a customer. Customers shopping: 1", None, None),
        ], 23),
        "commandResults": [],
        "lastDaySummary": {
            "day": 1,
            "totalCustomers": 3,
            "revenueCents": 36_095,
            "revenueGoalCents": 500_000,
            "metRevenueGoal": False,
            "startingBalanceCents": 150_000,
            "endingBalanceCents": 186_095,
        },
    }


COMMANDS = [
    {"schemaVersion": 1, "type": "command", "id": "c-0001", "seenTick": 26_440, "command": "setSpeed", "args": {"multiplier": 2}},
    {"schemaVersion": 1, "type": "command", "id": "c-0012", "seenTick": 26_452, "command": "pause", "args": {}},
    {"schemaVersion": 1, "type": "command", "id": "c-0013", "seenTick": 26_452, "command": "resume", "args": {}},
    {"schemaVersion": 1, "type": "command", "id": "c-0042", "seenTick": 50_829, "command": "upgrade", "args": {"upgradeType": "CHECKOUT_LANE"}},
    {"schemaVersion": 1, "type": "command", "id": "c-0043", "seenTick": 50_830, "command": "orderStock",
     "args": {"catalogId": "AlexDresserW", "units": 40, "arrivalDay": 87}},
]


def write_json(path, obj):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(obj, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def write_ndjson(path, objs):
    lines = [json.dumps(o, separators=(",", ":"), ensure_ascii=False) for o in objs]
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main():
    samples = {"day01-small": day01(), "day45-mid": day45(), "day85-mega": day85()}
    for name, snap in samples.items():
        write_json(FIXTURES / "pretty" / f"{name}.json", snap)
    write_ndjson(FIXTURES / "samples.ndjson", samples.values())
    minimum = v42_minimum()
    write_json(FIXTURES / "pretty" / "v42-minimum.json", minimum)
    write_ndjson(FIXTURES / "v42-minimum.ndjson", [minimum])
    write_ndjson(FIXTURES / "commands.ndjson", COMMANDS)
    print(f"wrote fixtures to {FIXTURES}")


if __name__ == "__main__":
    main()
