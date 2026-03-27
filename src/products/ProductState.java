package products;


public enum ProductState {
    ON_TRUCK("In Transit"),
    DAMAGED("Damaged"),
    IN_INVENTORY("Store Inventory"),
    LIMBO("Limbo"),
    AS_IS("As is"),
    SOLD("Sold");

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
                return (newState == LIMBO || newState == DAMAGED);
            case LIMBO:
                return (newState == ON_TRUCK || newState == DAMAGED || newState == IN_INVENTORY);
            case AS_IS, SOLD:
                return false;
        }
        throw new RuntimeException("Illegal Product State");
    }

    }

