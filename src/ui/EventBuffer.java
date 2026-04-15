package ui;

public class EventBuffer {

    private static ViewMode currentView;
    public static void buildActivePanel() {

    }

    public static void buildInventoryView() {}

    public static void setCurrentView(ViewMode mode) {
        EventBuffer.currentView = mode;
    }
}
