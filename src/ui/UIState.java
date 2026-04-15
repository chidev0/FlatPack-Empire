package ui;

public class UIState {

    private ActiveViewContext activeViewContext;

    public void setActiveViewContext(ActiveViewContext activeViewContext) {
        this.activeViewContext = activeViewContext;
    }

    public ActiveViewContext getActiveViewContext() {
        return activeViewContext;
    }

}
