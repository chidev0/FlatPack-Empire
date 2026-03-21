package models;

public enum ZoneType {
    WAREHOUSE_GRID("Furniture Warehouse"),
    MARKET_HALL("Market Hall"),
    SHOWROOM("Showroom"),
    FOOD_COURT("Restaurant"),
    CHECKOUT("Checkout"),
    ENTRANCE("Entrance"),
    STAFF_ONLY("Staff Area"),
    DAMAGES("Damages"),
    RECEIVING("Receiving");

    private final String locationName;

    private ZoneType(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return this.locationName;
    }
}
