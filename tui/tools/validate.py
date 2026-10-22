"""Checks snapshot or command files against the contract.

Works on the fixtures and on anything the engine writes, so it doubles as the
acceptance check for the v4.2 boundary:

    python tui/tools/validate.py tui/fixtures/samples.ndjson
    java ... --headless > run.ndjson && python tui/tools/validate.py run.ndjson

Accepts .ndjson (one object per line) and .json (one object). Needs the
jsonschema package (pip install jsonschema).
"""

import json
import sys
from pathlib import Path

from jsonschema import Draft202012Validator

CONTRACT = Path(__file__).resolve().parents[1] / "contract"
VALIDATORS = {
    kind: Draft202012Validator(json.loads((CONTRACT / f"{kind}.schema.json").read_text(encoding="utf-8")))
    for kind in ("snapshot", "command")
}


def load(path):
    text = path.read_text(encoding="utf-8")
    if path.suffix == ".json":
        return [(1, json.loads(text))]
    return [(n, json.loads(line)) for n, line in enumerate(text.splitlines(), 1) if line.strip()]


def invariants(snap):
    """Cross-field rules a schema cannot express."""
    problems = []
    customers = snap["customers"]
    if sum(customers["byState"].values()) != customers["inStore"]:
        problems.append("customers.byState does not add up to customers.inStore")
    if len(snap["checkout"]["lanes"]) != snap["checkout"]["lanesUnlocked"]:
        problems.append("checkout.lanes length differs from checkout.lanesUnlocked")
    if sum(i["units"] for i in snap["inventory"]["items"]) != snap["inventory"]["totalUnits"]:
        problems.append("inventory.items units do not add up to inventory.totalUnits")
    if [s["slot"] for s in snap["inbound"]["slots"]] != list(range(7)):
        problems.append("inbound.slots must be slots 0..6 in order")
    if snap["tick"] != (snap["clock"]["day"] - 1) * snap["clock"]["ticksPerDay"] + snap["clock"]["tickOfDay"]:
        problems.append("tick does not match clock.day and clock.tickOfDay")
    zones = snap.get("zones")
    if zones and sum(z["occupancy"] for z in zones) != customers["inStore"]:
        problems.append("zones occupancy does not add up to customers.inStore")
    return problems


def check(path, stream):
    errors = 0
    last_tick = last_seq = seed = None
    for line_no, obj in load(path):
        kind = obj.get("type")
        if kind not in VALIDATORS:
            print(f"{path}:{line_no}: unknown message type {kind!r}")
            errors += 1
            continue
        schema_errors = list(VALIDATORS[kind].iter_errors(obj))
        for err in schema_errors:
            where = "/".join(str(p) for p in err.absolute_path) or "(root)"
            print(f"{path}:{line_no}: {where}: {err.message}")
        errors += len(schema_errors)
        if kind != "snapshot" or schema_errors:
            continue
        for problem in invariants(obj):
            print(f"{path}:{line_no}: {problem}")
            errors += 1
        if stream:
            if last_tick is not None and obj["tick"] < last_tick:
                print(f"{path}:{line_no}: tick went backwards")
                errors += 1
            if seed is not None and obj["seed"] != seed:
                print(f"{path}:{line_no}: seed changed mid-run")
                errors += 1
            for event in obj["events"]:
                if last_seq is not None and event["seq"] != last_seq + 1:
                    print(f"{path}:{line_no}: event seq jumped from {last_seq} to {event['seq']}")
                    errors += 1
                last_seq = event["seq"]
            last_tick, seed = obj["tick"], obj["seed"]
    return errors


def main(argv):
    if not argv:
        print(__doc__)
        return 2
    stream = "--stream" in argv
    total = 0
    for arg in (a for a in argv if a != "--stream"):
        errors = check(Path(arg), stream)
        print(f"{arg}: {'ok' if errors == 0 else f'{errors} problem(s)'}")
        total += errors
    return 1 if total else 0


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
