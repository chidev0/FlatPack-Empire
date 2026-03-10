package models;

public class FurnitureItem extends Product {

    // Initialize variables.
    String dimensions;
    boolean assemblyRequired;

    // Initialize our Product
    public FurnitureItem(String productName, String productType, Double price, String color, String dimensions, boolean assemblyRequired) {
        super(productName, productType, price, color);
        this.dimensions = dimensions;
        this.assemblyRequired = assemblyRequired;
        super.setProductModel("Furniture");
    }

    public FurnitureItem(String productName, String productType, Double price, String color) {
        super(productName, productType, price, color);
        super.setProductModel("Furniture");
    }


    // Override Java toString with custom FurnitureItem output
    public String toString() {
        String skuString = super.getSku().toString();
        skuString = skuString.substring(0,7);
        return "[ Warehouse ] - [" + skuString + "] " + getProduct() + " " + getType() + " (" + getColor() + ")";

    }


}
