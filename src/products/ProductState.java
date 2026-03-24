package products;


public enum ProductState {
    ON_TRUCK("In Transit"),
    DAMAGED("Damaged"),
    IN_INVENTORY("Store Inventory"),
    LIMBO("Limbo"),
    AS_IS("As is");

    private final String UI_Wrapper;

    private ProductState(String UI_Wrapper) {
        this.UI_Wrapper = UI_Wrapper;
    }

    public String getUI_Wrapper() {return UI_Wrapper;}

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
            case AS_IS:
                return false;
        }
        throw new RuntimeException("Illegal Product State");
    }

    }

