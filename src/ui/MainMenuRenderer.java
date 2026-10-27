package ui;

public class MainMenuRenderer {
    public MainMenuRenderer() {
    }

    public String[] menuBox = new String[] {"01 New Game","02 Custom Seed", "03 Load Game [COMING SOON]", "04 Exit"};

    public void load() {
        Printer.box("Welcome to FlatPack Empire", menuBox, 15, ConsoleStyle.BLUE);
    }

    public void handleInput() {

    }

}
