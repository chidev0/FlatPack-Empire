package products;

public enum ProductType {
    DESK("Desk"),
    DRESSER("Dresser"),
    BOOKCASE("Bookcase"),
    HOT_FOOD("Hot"),
    COLD_FOOD("Cold"),
    SNACK("Snack");

    private final String uiLabel;

    private ProductType(String uiLabel) {this.uiLabel = uiLabel;}

    public String getUiLabel() {
        return uiLabel;
    }
}
