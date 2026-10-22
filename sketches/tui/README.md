# TUI sketches (throwaway output layer)

Three quick takes on an ASCII dashboard for Flatpack Empire, all drawing the same fake store.
Nothing here touches `src/`. `FakeStore` is a toy stand-in for the engine snapshot so the
renderers have something to move; swap it for the real snapshot once v4.2 exists.

| Sketch | Run | What it shows |
|---|---|---|
| A | `./run.sh jline glyph` | One store cell per character: box-drawing zones, `o O @` shoppers, `▓░` stock |
| B | `./run.sh jline halfblock` | Two pixels per character with `▀` + truecolor: a tiny pixel-art floor |
| C | `./run.sh lanterna glyph` | Same screen as A, but Lanterna owns the terminal (also takes `halfblock`) |

Windows: `run.bat jline glyph` etc. Use Windows Terminal; the old console host mangles truecolor.

Keys: `1`-`5` open/close a checkout lane, `u` pop a crate off the dock stack, `f` flood Bay 1,
`space` pause, `q` quit. The terminal should be at least 100x30.

From IntelliJ: add the two jars in `lib/` as a library, run `LanternaTui`. JLine needs a real
terminal, so A and B only work from a terminal window, while Lanterna opens its own Swing window
when there is no terminal.

`--dump N` prints one frame after N ticks without a terminal (e.g. `java -cp "out:lib/*" JLineTui halfblock --dump 240`),
which is how the comparison screenshots were made.

## Files

- `FakeStore.java` fake snapshot: zones, shoppers, five lane queues, a LIFO dock, a flood
- `Frame.java` cell buffer (char + 24-bit fg/bg) with a diff that writes only changed cells
- `Screens.java` the dashboard layout; `glyphFloor` is sketch A, `halfBlockFloor` is sketch B
- `JLineTui.java` raw mode, alternate screen and input via JLine, output via `Frame.diff`
- `LanternaTui.java` the same frames pushed through Lanterna's `Screen`
