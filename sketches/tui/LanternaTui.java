import java.awt.Color;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.virtual.DefaultVirtualTerminal;

/**
 * Sketch C: the same screens, but Lanterna owns the terminal. Its Screen keeps the back buffer,
 * diffs and redraws for us, handles resize, and opens a Swing window when there is no real
 * terminal (for example the IntelliJ run panel).
 *   java LanternaTui [glyph|halfblock]
 *   java LanternaTui glyph --dump 200
 */
public class LanternaTui {
    public static void main(String[] args) throws Exception {
        Screens.Style style = args.length > 0 && args[0].startsWith("half") ? Screens.Style.HALFBLOCK : Screens.Style.GLYPH;
        boolean dump = args.length > 2 && args[1].equals("--dump");

        Terminal term = dump
                ? new DefaultVirtualTerminal(new TerminalSize(100, 30))
                : new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(100, 30)).createTerminal();
        Screen screen = new TerminalScreen(term);
        screen.startScreen();
        screen.setCursorPosition(null);

        FakeStore s = new FakeStore(dump ? 2071 : System.nanoTime());
        boolean paused = false;
        String status = "sketch C · Lanterna (" + (style == Screens.Style.GLYPH ? "glyph" : "half-block") + ") · Screen diffs for us";
        try {
            for (int ticks = 0; !dump || ticks < Integer.parseInt(args[2]); ticks++) {
                KeyStroke k;
                while (!dump && (k = screen.pollInput()) != null) {
                    if (k.getKeyType() == KeyType.Escape || k.getKeyType() == KeyType.EOF) return;
                    if (k.getKeyType() != KeyType.Character) continue;
                    char c = k.getCharacter();
                    if (c == 'q') return;
                    if (c >= '1' && c <= '5') s.toggleLane(c - '1');
                    else if (c == 'u') s.unload();
                    else if (c == 'f') s.startFlood();
                    else if (c == ' ') paused = !paused;
                }
                if (!paused) s.tick();

                TerminalSize size = screen.doResizeIfNecessary();
                if (size == null) size = screen.getTerminalSize();
                Frame f = Screens.draw(s, size.getColumns(), size.getRows(), style, dump ? null : status);
                for (int y = 0; y < f.h; y++)
                    for (int x = 0; x < f.w; x++) {
                        int i = y * f.w + x;
                        screen.setCharacter(x, y, new TextCharacter(f.ch[i], rgb(f.fg[i]), rgb(f.bg[i])));
                    }
                screen.refresh(Screen.RefreshType.DELTA);
                if (!dump) Thread.sleep(JLineTui.FRAME_MS);
            }
            if (dump) new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8).print(readBack((DefaultVirtualTerminal) term).dump());
        } finally {
            screen.stopScreen();
        }
    }

    static TextColor rgb(int c) { return new TextColor.RGB(c >> 16 & 255, c >> 8 & 255, c & 255); }

    /** Read what Lanterna actually put on its virtual terminal back into a Frame, to prove the pipeline. */
    static Frame readBack(DefaultVirtualTerminal vt) {
        TerminalSize sz = vt.getTerminalSize();
        Frame f = new Frame(sz.getColumns(), sz.getRows());
        for (int y = 0; y < f.h; y++)
            for (int x = 0; x < f.w; x++) {
                TextCharacter tc = vt.getBufferCharacter(x, y);
                Color fg = tc.getForegroundColor().toColor(), bg = tc.getBackgroundColor().toColor();
                f.put(x, y, tc.getCharacterString().charAt(0), fg.getRGB() & 0xffffff, bg.getRGB() & 0xffffff);
            }
        return f;
    }
}
