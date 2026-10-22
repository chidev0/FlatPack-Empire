import java.util.Iterator;

/**
 * Draws the whole dashboard into a Frame. Same chrome for both sketches; only the floor differs:
 * GLYPH draws one store cell per character, HALFBLOCK draws two "pixels" per character with '▀'.
 * Layout targets 100x30. Smaller terminals get clipped.
 */
final class Screens {
    enum Style { GLYPH, HALFBLOCK }

    static final int FLOOR_X = 1, FLOOR_Y = 2;

    static Frame draw(FakeStore s, int w, int h, Style style, String status) {
        Frame f = new Frame(w, h);
        header(f, s);
        f.box(0, 1, 62, 22, Frame.LINE, Frame.BG, null);
        if (style == Style.GLYPH) glyphFloor(f, s); else halfBlockFloor(f, s);
        lanes(f, s, 62, 1, 38, 10);
        dock(f, s, 62, 11, 38, 12);
        feed(f, s, 0, 23, 100, 5);
        prompt(f, s, 0, 28, status);
        if (w < 100 || h < 30) f.text(1, h - 1, " resize to at least 100x30 (now " + w + "x" + h + ") ", Frame.BG, Frame.YELLOW);
        return f;
    }

    // ---- chrome -----------------------------------------------------------

    static void header(Frame f, FakeStore s) {
        f.fill(0, 0, 100, 1, ' ', Frame.FG, Frame.PANEL);
        f.text(0, 0, " FLATPACK EMPIRE ", Frame.BG, Frame.CYAN);
        f.text(18, 0, "DAY " + s.day, Frame.FG, Frame.PANEL);
        f.text(26, 0, s.clock(), Frame.CYAN, Frame.PANEL);
        f.text(33, 0, ">> 4x", Frame.DIM, Frame.PANEL);
        if (s.flooding && s.tick % 6 < 4) f.text(41, 0, " BAY 1 FLOOD ", Frame.BG, Frame.RED);
        String right = "CR " + s.cash() + "   walk-outs " + s.walkouts + " ";
        f.text(100 - right.length(), 0, right, Frame.FG, Frame.PANEL);
    }

    static void lanes(Frame f, FakeStore s, int x, int y, int w, int h) {
        int open = 0;
        for (boolean b : s.laneOpen) if (b) open++;
        f.box(x, y, w, h, Frame.LINE, Frame.BG, "CHECKOUT " + open + "/5 open");
        for (int l = 0; l < 5; l++) {
            int row = y + 1 + l;
            f.text(x + 2, row, "L" + (l + 1), Frame.DIM, Frame.BG);
            if (!s.laneOpen[l]) { f.text(x + 5, row, "▒ closed", Frame.DIM, Frame.BG); continue; }
            f.put(x + 5, row, '█', Frame.CYAN, Frame.BG);
            f.text(x + 7, row, FakeStore.LANE_STAFF[l], Frame.FG, Frame.BG);
            f.text(x + 14, row, "<", Frame.DIM, Frame.BG);
            int i = 0;
            for (FakeStore.Shopper p : s.lanes.get(l)) {
                if (i >= 16) break;
                f.put(x + 16 + i++, row, glyph(p), color(p), Frame.BG);
            }
            String n = String.valueOf(s.lanes.get(l).size());
            f.text(x + w - 2 - n.length(), row, n, s.lanes.get(l).size() > 6 ? Frame.RED : Frame.DIM, Frame.BG);
        }
        f.text(x + 2, y + 7, "o", Frame.GREEN, Frame.BG); f.text(x + 4, y + 7, "calm", Frame.DIM, Frame.BG);
        f.text(x + 10, y + 7, "O", Frame.YELLOW, Frame.BG); f.text(x + 12, y + 7, "annoyed", Frame.DIM, Frame.BG);
        f.text(x + 21, y + 7, "@", Frame.RED, Frame.BG); f.text(x + 23, y + 7, "leaving", Frame.DIM, Frame.BG);
        f.text(x + 2, y + 8, "[1-5] open/close a lane", Frame.DIM, Frame.BG);
    }

    static void dock(Frame f, FakeStore s, int x, int y, int w, int h) {
        f.box(x, y, w, h, Frame.LINE, Frame.BG, "DOCK  LIFO stack");
        int depth = -1, i = 0;
        for (String c : s.dock) { if (c.equals(s.needed)) { depth = i; break; } i++; }
        int row = y + 1, n = s.dock.size();
        i = 0;
        for (Iterator<String> it = s.dock.iterator(); it.hasNext() && row < y + h - 3; i++, row++) {
            String c = it.next();
            boolean need = c.equals(s.needed);
            f.text(x + 2, row, i == 0 ? "top>" : "    ", Frame.CYAN, Frame.BG);
            f.put(x + 7, row, '▐', Frame.YELLOW, Frame.BG);
            f.text(x + 8, row, String.format(" #%d %-18s", n - i, c), need ? Frame.BG : Frame.FG, need ? Frame.YELLOW : Frame.PANEL);
            f.put(x + 31, row, '▌', Frame.YELLOW, Frame.BG);
            if (need) f.text(x + 33, row, "need", Frame.YELLOW, Frame.BG);
        }
        if (n == 0) f.text(x + 2, row, "empty, next truck soon", Frame.DIM, Frame.BG);
        f.text(x + 2, y + h - 3, depth > 0 ? "pops to reach sofa: " + depth : depth == 0 ? "sofa is on top!" : "", depth > 0 ? Frame.RED : Frame.GREEN, Frame.BG);
        f.text(x + 2, y + h - 2, "[u] pop  4 min each", Frame.DIM, Frame.BG);
    }

    static void feed(Frame f, FakeStore s, int x, int y, int w, int h) {
        f.box(x, y, w, h, Frame.LINE, Frame.BG, "MARGO  ch.2");
        int row = y + 1;
        for (String e : s.events) {
            if (row >= y + h - 1) break;
            boolean first = row == y + 1;
            f.text(x + 2, row, ">", first ? Frame.CYAN : Frame.DIM, Frame.BG);
            f.text(x + 4, row, e, e.contains("ALERT") ? Frame.RED : first ? Frame.FG : Frame.DIM, Frame.BG);
            row++;
        }
    }

    static void prompt(Frame f, FakeStore s, int x, int y, String status) {
        f.text(x + 1, y, "you >", Frame.CYAN, Frame.BG);
        if (s.tick % 6 < 3) f.put(x + 7, y, '█', Frame.FG, Frame.BG);
        f.text(x + 46, y, "[u] unload  [f] flood  [space] pause  [q] quit", Frame.DIM, Frame.BG);
        if (status != null) f.text(x + 1, y + 1, status, Frame.LINE, Frame.BG);
    }

    static char glyph(FakeStore.Shopper p) {
        if (p.walkedOut) return 'x';
        return p.patience > 0.6 ? 'o' : p.patience > 0.3 ? 'O' : '@';
    }

    static int color(FakeStore.Shopper p) {
        if (p.walkedOut) return Frame.RED;
        return p.patience > 0.6 ? Frame.GREEN : p.patience > 0.3 ? Frame.YELLOW : Frame.RED;
    }

    // ---- sketch A: one store cell per character ---------------------------

    static void glyphFloor(Frame f, FakeStore s) {
        int ox = FLOOR_X, oy = FLOOR_Y;
        for (int cy = 0; cy < 20; cy++)
            for (int cx = 0; cx < 60; cx++)
                if (s.zoneAt(cx + 0.5, cy * 2 + 1) != null && (cx + cy) % 4 == 0) f.put(ox + cx, oy + cy, '·', 0x262b40, Frame.BG);

        for (FakeStore.Zone z : FakeStore.ZONES) {
            int x0 = ox + z.x(), x1 = ox + z.x() + z.w() - 1, y0 = oy + z.y() / 2, y1 = oy + (z.y() + z.h() - 1) / 2;
            for (int x = x0 + 1; x < x1; x++) { f.put(x, y0, '─', Frame.LINE, Frame.BG); f.put(x, y1, '─', Frame.LINE, Frame.BG); }
            for (int y = y0 + 1; y < y1; y++) { f.put(x0, y, '│', Frame.LINE, Frame.BG); f.put(x1, y, '│', Frame.LINE, Frame.BG); }
            f.put(x0, y0, '┌', Frame.LINE, Frame.BG); f.put(x1, y0, '┐', Frame.LINE, Frame.BG);
            f.put(x0, y1, '└', Frame.LINE, Frame.BG); f.put(x1, y1, '┘', Frame.LINE, Frame.BG);
            f.text(x0 + 1, y0, z.name(), Frame.DIM, Frame.BG);
        }

        // showroom room-sets, market bins, restaurant tables
        f.text(ox + 4, oy + 3, "▛▀▀▜", 0x9a4a7a, Frame.BG); f.text(ox + 4, oy + 4, "▙▄▄▟", 0x9a4a7a, Frame.BG);
        f.text(ox + 11, oy + 3, "▛▀▜", 0x7a5aa0, Frame.BG); f.text(ox + 11, oy + 4, "▙▄▟", 0x7a5aa0, Frame.BG);
        f.text(ox + 5, oy + 5, "▄▄▄▄▄", 0x9a7a3a, Frame.BG);
        for (int i = 0; i < 3; i++) { f.text(ox + 23 + i * 5, oy + 2, "▒▒▒", 0x8a6a2a, Frame.BG); f.text(ox + 23 + i * 5, oy + 4, "▒▒▒", 0x8a6a2a, Frame.BG); }
        for (int i = 0; i < 4; i++) { f.text(ox + 43 + i * 4, oy + 2, "▄▄", 0x9a8a5a, Frame.BG); f.text(ox + 43 + i * 4, oy + 4, "▄▄", 0x9a8a5a, Frame.BG); }

        for (int a = 0; a < s.stock.length; a++) {
            int row = oy + FakeStore.aisleY(a) / 2;
            f.text(ox + 3, row, FakeStore.AISLE_NAMES[a], Frame.DIM, Frame.BG);
            for (int x = FakeStore.SHELF_X0; x < FakeStore.SHELF_X1; x++) {
                double p = (x - FakeStore.SHELF_X0) / (double) (FakeStore.SHELF_X1 - FakeStore.SHELF_X0);
                boolean full = p < s.stock[a];
                f.put(ox + x, row, full ? '▓' : '░', full ? (s.stock[a] < 0.2 ? Frame.RED : Frame.CYAN) : 0x5a5030, Frame.BG);
            }
        }

        for (int l = 0; l < 5; l++) {
            int x = ox + FakeStore.laneX(l), row = oy + FakeStore.REGISTER_Y / 2;
            f.put(x, row, s.laneOpen[l] ? '█' : '▒', s.laneOpen[l] ? Frame.CYAN : Frame.LINE, Frame.BG);
            f.put(x, row + 1, (char) ('1' + l), Frame.DIM, Frame.BG);
        }

        f.text(ox + 49, oy + 9, "▐█████▌", 0xb08a20, Frame.BG);
        f.text(ox + 49, oy + 10, " ▀   ▀", Frame.DIM, Frame.BG);
        f.text(ox + 49, oy + 11, "T2 x" + s.dock.size(), Frame.DIM, Frame.BG);
        f.text(ox + 49, oy + 14, "x".repeat(Math.min(8, 1 + s.damaged)), Frame.RED, Frame.BG);
        f.text(ox + 27, oy + 18, "v IN / OUT v", Frame.DIM, Frame.BG);

        for (int cy = 0; cy < 20; cy++)
            for (int cx = 0; cx < 60; cx++)
                if (s.inWater(cx + 0.5, cy * 2 + 1.5)) {
                    int i = (oy + cy) * f.w + ox + cx;
                    boolean blank = f.ch[i] == ' ' || f.ch[i] == '·';
                    f.put(ox + cx, oy + cy, blank ? ((cx + cy + s.tick / 2) % 3 == 0 ? '~' : ' ') : f.ch[i],
                            blank ? Frame.WATER : Frame.KEEP, Frame.WATER_BG);
                }

        for (FakeStore.Shopper p : s.shoppers)
            f.put(ox + (int) p.x, oy + (int) (p.y / 2), glyph(p), color(p), Frame.KEEP);
    }

    // ---- sketch B: two pixels per character -------------------------------

    static int tint(FakeStore.Zone z) {
        if (z == null) return 0x0b0d14;
        return switch (z.name()) {
            case "SHOWROOM" -> 0x1c1530;
            case "MARKET HALL" -> 0x14202c;
            case "RESTAURANT" -> 0x241b14;
            case "WAREHOUSE GRID" -> 0x15181f;
            case "CHECKOUT" -> 0x0f1c1c;
            case "RECEIVING" -> 0x1e1d12;
            case "DAMAGES" -> 0x251217;
            default -> 0x0f2016;
        };
    }

    static void rect(int[] px, int x, int y, int w, int h, int c) {
        for (int yy = y; yy < y + h; yy++) for (int xx = x; xx < x + w; xx++)
            if (xx >= 0 && yy >= 0 && xx < FakeStore.W && yy < FakeStore.H) px[yy * FakeStore.W + xx] = c;
    }

    static void halfBlockFloor(Frame f, FakeStore s) {
        int W = FakeStore.W, H = FakeStore.H;
        int[] px = new int[W * H];
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++) {
                FakeStore.Zone z = s.zoneAt(x + 0.5, y + 0.5);
                int c = tint(z);
                if (z != null && (x == z.x() || x == z.x() + z.w() - 1 || y == z.y() || y == z.y() + z.h() - 1)) c = Frame.mix(c, 0xffffff, 0.10);
                px[y * W + x] = c;
            }

        rect(px, 4, 6, 4, 2, 0x9a4a7a); rect(px, 11, 6, 3, 2, 0x7a5aa0); rect(px, 5, 10, 5, 1, 0x9a7a3a);
        for (int i = 0; i < 3; i++) { rect(px, 23 + i * 5, 4, 3, 2, 0x8a6a2a); rect(px, 23 + i * 5, 8, 3, 2, 0x8a6a2a); }
        for (int i = 0; i < 4; i++) { rect(px, 43 + i * 4, 4, 2, 2, 0x9a8a5a); rect(px, 43 + i * 4, 8, 2, 2, 0x9a8a5a); }

        for (int a = 0; a < s.stock.length; a++) {
            int y = FakeStore.aisleY(a);
            for (int x = FakeStore.SHELF_X0; x < FakeStore.SHELF_X1; x++) {
                double p = (x - FakeStore.SHELF_X0) / (double) (FakeStore.SHELF_X1 - FakeStore.SHELF_X0);
                int c = p < s.stock[a] ? (s.stock[a] < 0.2 ? Frame.RED : Frame.mix(0x1f6f78, Frame.CYAN, s.stock[a])) : 0x2a2a3a;
                px[y * W + x] = c;
                px[(y + 1) * W + x] = Frame.mix(c, 0, 0.4);
            }
        }

        for (int l = 0; l < 5; l++) {
            int c = s.laneOpen[l] ? Frame.CYAN : 0x3a3f55;
            rect(px, FakeStore.laneX(l), FakeStore.REGISTER_Y, 1, 2, c);
            rect(px, FakeStore.laneX(l) - 1, FakeStore.REGISTER_Y + 1, 1, 1, Frame.mix(c, 0, 0.5));
        }
        rect(px, 49, 18, 7, 4, 0xb08a20); rect(px, 56, 19, 2, 3, 0xc8c8d0);
        for (int i = 0; i < Math.min(16, 1 + s.damaged); i++) px[(29 + i / 8 * 2) * W + 49 + i % 8] = Frame.RED;

        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
                if (s.inWater(x + 0.5, y + 0.5)) {
                    double wave = 0.5 + 0.5 * Math.sin(x * 0.7 + s.tick * 0.45 + y * 0.9);
                    px[y * W + x] = Frame.mix(Frame.mix(0x123a80, Frame.WATER, wave), px[y * W + x], 0.25);
                }

        for (FakeStore.Shopper p : s.shoppers) {
            int lx = (int) p.lx, ly = (int) p.ly, x = (int) p.x, y = (int) p.y;
            if (lx >= 0 && ly >= 0 && lx < W && ly < H && (lx != x || ly != y)) px[ly * W + lx] = Frame.mix(px[ly * W + lx], color(p), 0.35);
            if (x >= 0 && y >= 0 && x < W && y < H) px[y * W + x] = color(p);
        }

        for (int r = 0; r < H / 2; r++)
            for (int c = 0; c < W; c++)
                f.put(FLOOR_X + c, FLOOR_Y + r, '▀', px[2 * r * W + c], px[(2 * r + 1) * W + c]);

        // text labels sit on top of the pixels, taking the lower pixel as their background
        for (FakeStore.Zone z : FakeStore.ZONES) label(f, px, z.x() + 1, z.y() / 2, z.name());
        for (int a = 0; a < s.stock.length; a++) label(f, px, 2, FakeStore.aisleY(a) / 2, FakeStore.AISLE_NAMES[a]);
    }

    static void label(Frame f, int[] px, int cx, int cy, String s) {
        for (int i = 0; i < s.length(); i++)
            f.put(FLOOR_X + cx + i, FLOOR_Y + cy, s.charAt(i), 0xaab0c8, px[(cy * 2 + 1) * FakeStore.W + Math.min(FakeStore.W - 1, cx + i)]);
    }
}
