/**
 * A grid of cells (char + 24-bit foreground + background). Renderers draw into a Frame,
 * then the terminal loop writes only the cells that changed since the last frame.
 */
final class Frame {
    // Showroom Neon palette, as 0xRRGGBB
    static final int BG = 0x090b13, PANEL = 0x10131f, LINE = 0x2a3048, FG = 0xdfe2ee, DIM = 0x6d7390,
            CYAN = 0x5ef2e6, MAGENTA = 0xff4fa3, YELLOW = 0xffd23f, GREEN = 0x5dff95, RED = 0xff5a6e,
            WATER = 0x3a8cff, WATER_BG = 0x0c1f44;
    static final int KEEP = -1;   // pass as a color to leave the cell's existing color alone

    final int w, h;
    final char[] ch;
    final int[] fg, bg;

    Frame(int w, int h) {
        this.w = w; this.h = h;
        ch = new char[w * h]; fg = new int[w * h]; bg = new int[w * h];
        fill(0, 0, w, h, ' ', FG, BG);
    }

    void put(int x, int y, char c, int f, int b) {
        if (x < 0 || y < 0 || x >= w || y >= h) return;
        int i = y * w + x;
        ch[i] = c;
        if (f != KEEP) fg[i] = f;
        if (b != KEEP) bg[i] = b;
    }

    void text(int x, int y, String s, int f, int b) {
        for (int i = 0; i < s.length(); i++) put(x + i, y, s.charAt(i), f, b);
    }

    void fill(int x, int y, int fw, int fh, char c, int f, int b) {
        for (int yy = y; yy < y + fh; yy++) for (int xx = x; xx < x + fw; xx++) put(xx, yy, c, f, b);
    }

    void box(int x, int y, int bw, int bh, int f, int b, String title) {
        fill(x, y, bw, bh, ' ', FG, b);
        for (int i = 1; i < bw - 1; i++) { put(x + i, y, '─', f, b); put(x + i, y + bh - 1, '─', f, b); }
        for (int i = 1; i < bh - 1; i++) { put(x, y + i, '│', f, b); put(x + bw - 1, y + i, '│', f, b); }
        put(x, y, '┌', f, b); put(x + bw - 1, y, '┐', f, b);
        put(x, y + bh - 1, '└', f, b); put(x + bw - 1, y + bh - 1, '┘', f, b);
        if (title != null) text(x + 2, y, " " + title + " ", DIM, b);
    }

    /** ANSI for every cell that differs from prev (all cells when prev is null or a different size). */
    String diff(Frame prev) {
        boolean full = prev == null || prev.w != w || prev.h != h;
        StringBuilder sb = new StringBuilder();
        int curF = -2, curB = -2, last = -2;
        for (int i = 0; i < ch.length; i++) {
            if (!full && ch[i] == prev.ch[i] && fg[i] == prev.fg[i] && bg[i] == prev.bg[i]) continue;
            if (i != last + 1 || i % w == 0) sb.append("\033[").append(i / w + 1).append(';').append(i % w + 1).append('H');
            if (fg[i] != curF || bg[i] != curB) {
                sb.append("\033[38;2;").append(fg[i] >> 16 & 255).append(';').append(fg[i] >> 8 & 255).append(';').append(fg[i] & 255)
                  .append(";48;2;").append(bg[i] >> 16 & 255).append(';').append(bg[i] >> 8 & 255).append(';').append(bg[i] & 255).append('m');
                curF = fg[i]; curB = bg[i];
            }
            sb.append(ch[i]);
            last = i;
        }
        return sb.toString();
    }

    /** Whole frame as lines of ANSI text, for --dump (screenshots without a terminal). */
    String dump() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < h; y++) {
            int curF = -2, curB = -2;
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                if (fg[i] != curF || bg[i] != curB) {
                    sb.append(String.format("\033[38;2;%d;%d;%d;48;2;%d;%d;%dm",
                            fg[i] >> 16 & 255, fg[i] >> 8 & 255, fg[i] & 255, bg[i] >> 16 & 255, bg[i] >> 8 & 255, bg[i] & 255));
                    curF = fg[i]; curB = bg[i];
                }
                sb.append(ch[i]);
            }
            sb.append("\033[0m\n");
        }
        return sb.toString();
    }

    static int mix(int a, int b, double t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int) ((a >> 16 & 255) + ((b >> 16 & 255) - (a >> 16 & 255)) * t);
        int g = (int) ((a >> 8 & 255) + ((b >> 8 & 255) - (a >> 8 & 255)) * t);
        int bl = (int) ((a & 255) + ((b & 255) - (a & 255)) * t);
        return r << 16 | g << 8 | bl;
    }
}
