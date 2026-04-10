package products;


public enum ProductState {
    ON_TRUCK("In Transit"),
    DAMAGED("Damaged"),
    IN_INVENTORY("Store Inventory"),
    LIMBO("Limbo"),
    AS_IS("As is"),
    SOLD("Sold"),
    IN_CART("Reserved");

    private final String UI_Label;

    private ProductState(String UI_Label) {
        this.UI_Label = UI_Label;
    }

    public String getUI_Label() {return UI_Label;}

    public boolean canTransitionTo(ProductState newState) {
        switch (this) {
            case ON_TRUCK:
                return (newState == IN_INVENTORY || newState == DAMAGED);
            case DAMAGED:
                return (newState == AS_IS);
            case IN_INVENTORY:
                return (newState == LIMBO || newState == DAMAGED || newState == IN_CART);
            case LIMBO:
                return (newState == ON_TRUCK || newState == DAMAGED || newState == IN_INVENTORY || newState == SOLD);
            case AS_IS, SOLD:
                return false;
            case IN_CART:
                return (newState == SOLD || newState == LIMBO);
        }
        throw new RuntimeException("Illegal Product State");
    }

    }

