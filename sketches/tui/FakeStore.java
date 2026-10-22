import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A toy stand-in for the engine's snapshot, so the TUI sketches have something to draw.
 * This is NOT engine code and is not meant to be copied into src/: it fakes just enough
 * motion (shoppers, queues, a LIFO dock, a flood) to judge how each renderer looks.
 */
final class FakeStore {
    static final int W = 60, H = 40;   // store floor in "units"; one unit = one half-block pixel

    record Zone(String name, int x, int y, int w, int h) {
        boolean contains(double px, double py) { return px >= x && px < x + w && py >= y && py < y + h; }
    }

    static final Zone SHOWROOM  = new Zone("SHOWROOM", 1, 1, 19, 13);
    static final Zone MARKET    = new Zone("MARKET HALL", 21, 1, 18, 13);
    static final Zone FOOD      = new Zone("RESTAURANT", 40, 1, 19, 13);
    static final Zone WAREHOUSE = new Zone("WAREHOUSE GRID", 1, 15, 29, 19);
    static final Zone CHECKOUT  = new Zone("CHECKOUT", 31, 15, 15, 19);
    static final Zone RECEIVING = new Zone("RECEIVING", 47, 15, 12, 10);
    static final Zone DAMAGES   = new Zone("DAMAGES", 47, 26, 12, 8);
    static final Zone ENTRANCE  = new Zone("ENTRANCE", 24, 35, 12, 4);
    static final Zone[] ZONES = {SHOWROOM, MARKET, FOOD, WAREHOUSE, CHECKOUT, RECEIVING, DAMAGES, ENTRANCE};

    static final String[] AISLE_NAMES = {"A01", "A02", "A03", "B01"};   // B01 is Bay 1, the one that floods
    static final int SHELF_X0 = 7, SHELF_X1 = 27;
    static int aisleY(int a) { return 17 + a * 4; }
    static int laneX(int l) { return 33 + l * 3; }
    static final int REGISTER_Y = 31;
    static final String[] LANE_STAFF = {"Dana", "self", "Ilse", "Kovacs", "self"};

    enum State { BROWSE, QUEUE, LEAVE }

    static final class Shopper {
        double x, y, lx, ly, tx, ty;
        double patience = 1;
        State state = State.BROWSE;
        int stops, dwell, lane = -1;
        boolean walkedOut;
    }

    final Random rng;
    final List<Shopper> shoppers = new ArrayList<>();
    final boolean[] laneOpen = {true, true, false, true, false};
    final List<ArrayDeque<Shopper>> lanes = new ArrayList<>();
    final int[] serveTimer = new int[5];
    final double[] stock = {0.8, 0.35, 1.0, 0.6};
    final ArrayDeque<String> dock = new ArrayDeque<>();   // push/pop only: it's a stack
    final ArrayDeque<String> events = new ArrayDeque<>();
    final String needed = "MJUKBO sofa";
    long tick;
    int day = 54, minute = 11 * 60 + 30, walkouts, damaged;
    long cashCents = 4_188_210;
    boolean flooding;
    double water;   // 0..1 of Bay 1 height

    FakeStore(long seed) {
        rng = new Random(seed);
        for (int i = 0; i < 5; i++) lanes.add(new ArrayDeque<>());
        for (String c : new String[]{needed, "LADVIK drawers", "SNURRA chair", "BRUNKEN desk", "GLIMRA lamp", "HYLLSTA shelf 4x4"}) dock.push(c);
        for (int i = 0; i < 16; i++) spawn(true);
        log("Store open. Lunch rush in 20 min.");
        log("Truck T2 docked with 6 crates.");
    }

    // ---- player actions -------------------------------------------------

    void toggleLane(int l) {
        if (l < 0 || l >= 5) return;
        laneOpen[l] = !laneOpen[l];
        log("Lane " + (l + 1) + (laneOpen[l] ? " opened." : " closed."));
        if (!laneOpen[l]) {
            for (Shopper s : lanes.get(l)) { s.state = State.BROWSE; s.stops = 99; s.dwell = 0; }
            lanes.get(l).clear();
        }
    }

    void unload() {
        if (dock.isEmpty()) { log("Dock is empty."); return; }
        String c = dock.pop();
        minute += 4;
        int a = rng.nextInt(stock.length);
        stock[a] = Math.min(1, stock[a] + 0.3);
        log(c.equals(needed) ? "Found the " + c + "! Customer served." : "Unloaded " + c + ", " + dock.size() + " left.");
    }

    void startFlood() {
        if (flooding) return;
        flooding = true;
        log("ALERT: Bay 1 is flooding.");
    }

    // ---- simulation -----------------------------------------------------

    void tick() {
        tick++;
        if (tick % 2 == 0) minute++;
        if (shoppers.size() < 36 && rng.nextDouble() < 0.22) spawn(false);

        for (Shopper s : shoppers) move(s);

        for (int l = 0; l < 5; l++) {
            int i = 0;
            for (var it = lanes.get(l).iterator(); it.hasNext(); i++) {
                Shopper s = it.next();
                s.tx = laneX(l);
                s.ty = REGISTER_Y - 1 - i * 2;
                s.patience -= 0.0015 * (1 + i * 0.6);
                if (s.patience <= 0) {
                    it.remove();
                    s.state = State.LEAVE; s.walkedOut = true; s.tx = 30; s.ty = 39;
                    walkouts++;
                    log("Lane " + (l + 1) + " walk-out, lost $" + (40 + rng.nextInt(300)) + ".");
                }
            }
            if (!laneOpen[l] || lanes.get(l).isEmpty()) continue;
            if (--serveTimer[l] > 0) continue;
            Shopper front = lanes.get(l).peekFirst();
            if (Math.hypot(front.x - front.tx, front.y - front.ty) < 1.5) {
                lanes.get(l).pollFirst();
                cashCents += 4000 + rng.nextInt(30000);
                front.state = State.LEAVE; front.tx = 28 + rng.nextInt(4); front.ty = 39;
                serveTimer[l] = (LANE_STAFF[l].equals("self") ? 14 : 8) + rng.nextInt(8);
            } else {
                serveTimer[l] = 1;
            }
        }
        shoppers.removeIf(s -> s.state == State.LEAVE && s.y >= 38.5);

        if (tick % 15 == 0) {
            int a = rng.nextInt(stock.length);
            double before = stock[a];
            stock[a] = Math.max(0, stock[a] - 0.06);
            if (before >= 0.2 && stock[a] < 0.2) log("Aisle " + AISLE_NAMES[a] + " is low on stock.");
        }
        if (tick % 70 == 0 && !dock.isEmpty()) unload();
        if (dock.isEmpty() && tick % 200 == 0) {
            for (String c : new String[]{"FLAKT fan", "GLIMRA lamp", "HYLLSTA shelf 4x4", "BRUNKEN desk"}) dock.push(c);
            log("Truck T3 docked with 4 crates.");
        }
        if (tick == 160) startFlood();
        if (flooding) {
            water = Math.min(1, water + 0.005);
            if (tick % 20 == 0 && stock[3] > 0) { stock[3] = Math.max(0, stock[3] - 0.05); damaged++; }
        }
    }

    private void spawn(boolean anywhere) {
        Shopper s = new Shopper();
        if (anywhere) {
            Zone z = browseZone();
            s.x = z.x + 1 + rng.nextDouble() * (z.w - 2);
            s.y = z.y + 2 + rng.nextDouble() * (z.h - 3);
            s.patience = 0.6 + rng.nextDouble() * 0.4;
        } else {
            s.x = 27 + rng.nextDouble() * 6;
            s.y = 38;
        }
        s.lx = s.x; s.ly = s.y;
        pickTarget(s);
        shoppers.add(s);
    }

    private Zone browseZone() {
        Zone[] b = {SHOWROOM, MARKET, FOOD, WAREHOUSE};
        return b[rng.nextInt(b.length)];
    }

    private void pickTarget(Shopper s) {
        if (s.stops >= 2 + rng.nextInt(2)) { joinQueue(s); return; }
        Zone z = browseZone();
        if (z == WAREHOUSE) {
            int a = rng.nextInt(stock.length);
            s.tx = SHELF_X0 + rng.nextDouble() * (SHELF_X1 - SHELF_X0);
            s.ty = aisleY(a) + 2.5;
        } else {
            s.tx = z.x + 1 + rng.nextDouble() * (z.w - 2);
            s.ty = z.y + 2 + rng.nextDouble() * (z.h - 3);
        }
        s.dwell = 4 + rng.nextInt(16);
        s.stops++;
    }

    private void joinQueue(Shopper s) {
        int best = -1;
        for (int l = 0; l < 5; l++)
            if (laneOpen[l] && (best < 0 || lanes.get(l).size() < lanes.get(best).size())) best = l;
        if (best < 0) { s.state = State.LEAVE; s.tx = 30; s.ty = 39; return; }
        s.state = State.QUEUE;
        s.lane = best;
        lanes.get(best).addLast(s);
    }

    private void move(Shopper s) {
        s.lx = s.x; s.ly = s.y;
        double dx = s.tx - s.x, dy = s.ty - s.y, d = Math.hypot(dx, dy);
        double sp = s.state == State.LEAVE ? 0.9 : 0.55;
        if (inWater(s.x, s.y)) sp *= 0.4;
        if (d <= sp) {
            s.x = s.tx; s.y = s.ty;
            if (s.state == State.BROWSE && --s.dwell <= 0) pickTarget(s);
        } else {
            s.x += dx / d * sp;
            s.y += dy / d * sp;
        }
    }

    // ---- read helpers for renderers ------------------------------------

    boolean inWater(double x, double y) {
        return flooding && WAREHOUSE.contains(x, y) && y >= 33 - water * 7;
    }

    Zone zoneAt(double x, double y) {
        for (Zone z : ZONES) if (z.contains(x, y)) return z;
        return null;
    }

    String clock() { return String.format("%02d:%02d", (minute / 60) % 24, minute % 60); }

    String cash() { return String.format("%,d.%02d", cashCents / 100, cashCents % 100); }

    private void log(String msg) {
        events.addFirst(clock() + "  " + msg);
        while (events.size() > 8) events.removeLast();
    }
}
