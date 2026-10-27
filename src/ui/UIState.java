package ui;

public class UIState {

    private ActiveViewContext activeViewContext;
    private ScreenMode currentScreenMode;

    public enum ScreenMode {
        MAIN_MENU,
        SEED_ENTRY,
        IN_GAME,
        LOAD_GAME,
    }

    public void setActiveViewContext(ActiveViewContext activeViewContext) {
        this.activeViewContext = activeViewContext;
    }

    public ActiveViewContext getActiveViewContext() {
        return activeViewContext;
    }

    public ScreenMode getCurrentScreenMode() {
        return currentScreenMode;
    }

    public void setCurrentScreenMode(ScreenMode screenMode) {
        this.currentScreenMode = screenMode;
    }

}
