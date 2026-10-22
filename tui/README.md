# Flatpack Empire TUI

A separate-process terminal client for the Java engine. It reads snapshots and sends commands, and contains no game logic.

**Status: Step 1, contract proposal only.** There is no UI code yet. Review `CONTRACT_NOTES.md` first; the UI (Python + Textual) is built once the contract is agreed.

```
tui/
  CONTRACT_NOTES.md          readable contract, quirks, wished-for fields, open questions
  contract/
    snapshot.schema.json     engine -> UI, one per tick
    command.schema.json      UI -> engine
  fixtures/
    samples.ndjson           day 1 small, day 45 mid, day 85 megastore
    v42-minimum.ndjson       what today's engine could emit (required fields only)
    commands.ndjson          one of each command
    pretty/                  the same snapshots, indented
  tools/
    make_fixtures.py         regenerates fixtures/
    validate.py              checks snapshot or command files against the schemas
```

Check the fixtures (needs `pip install jsonschema`):

```
python tui/tools/make_fixtures.py
python tui/tools/validate.py tui/fixtures/samples.ndjson tui/fixtures/v42-minimum.ndjson tui/fixtures/commands.ndjson
```

Check a real engine run once it emits snapshots (adds tick, seed and event-sequence checks across lines):

```
python tui/tools/validate.py --stream run.ndjson
```
