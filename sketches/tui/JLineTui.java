import java.nio.charset.StandardCharsets;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;

/**
 * Sketches A and B: JLine for raw input and the alternate screen, our own Frame diff for output.
 *   java JLineTui glyph        sketch A, one store cell per character
 *   java JLineTui halfblock    sketch B, two pixels per character
 *   java JLineTui glyph --dump 200   print one frame after 200 ticks, no terminal needed
 */
public class JLineTui {
    static final int FRAME_MS = 100;

    public static void main(String[] args) throws Exception {
        Screens.Style style = args.length > 0 && args[0].startsWith("half") ? Screens.Style.HALFBLOCK : Screens.Style.GLYPH;
        String name = style == Screens.Style.GLYPH ? "A glyph" : "B half-block";
        if (args.length > 2 && args[1].equals("--dump")) { dump(style, Integer.parseInt(args[2])); return; }

        Terminal t = TerminalBuilder.builder().system(true).build();
        Attributes saved = t.enterRawMode();
        t.puts(Capability.enter_ca_mode);
        t.puts(Capability.cursor_invisible);
        t.flush();
        NonBlockingReader in = t.reader();

        FakeStore s = new FakeStore(System.nanoTime());
        Frame prev = null;
        boolean paused = false;
        long bytes = 0, frames = 0;
        try {
            while (true) {
                long end = System.currentTimeMillis() + FRAME_MS;
                long left;
                while ((left = end - System.currentTimeMillis()) > 0) {
                    int c = in.read(left);
                    if (c < 0) continue;
                    if (c == 'q' || c == 'Q' || c == 3) return;
                    if (c >= '1' && c <= '5') s.toggleLane(c - '1');
                    else if (c == 'u') s.unload();
                    else if (c == 'f') s.startFlood();
                    else if (c == ' ') paused = !paused;
                }
                if (!paused) s.tick();

                String status = "sketch " + name + " · JLine · " + (frames == 0 ? "-" : bytes / frames) + " bytes/frame avg";
                Frame f = Screens.draw(s, t.getWidth(), t.getHeight(), style, status);
                String out = f.diff(prev);
                t.writer().write(out);
                t.flush();
                bytes += out.getBytes(StandardCharsets.UTF_8).length;
                frames++;
                prev = f;
            }
        } finally {
            t.puts(Capability.exit_ca_mode);
            t.puts(Capability.cursor_visible);
            t.setAttributes(saved);
            t.flush();
            t.close();
        }
    }

    static void dump(Screens.Style style, int ticks) {
        FakeStore s = new FakeStore(2071);
        Frame prev = null;
        long bytes = 0, first = 0;
        for (int i = 0; i < ticks; i++) {
            s.tick();
            Frame f = Screens.draw(s, 100, 30, style, null);
            int b = f.diff(prev).getBytes(StandardCharsets.UTF_8).length;
            if (i == 0) first = b; else bytes += b;
            prev = f;
        }
        new java.io.PrintStream(System.out, true, StandardCharsets.UTF_8).print(prev.dump());
        System.err.println("first frame " + first + " bytes, then " + (ticks > 1 ? bytes / (ticks - 1) : 0) + " bytes/frame avg");
    }
}
