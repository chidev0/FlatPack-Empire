package chIKEA;

public class FurnitureItem extends Product {

    // Initialize variables.
    String dimensions;
    boolean assemblyRequired;

    // Initialize our Product
    public FurnitureItem(String productName, String productType, Double price, String color, String dimensions, boolean assemblyRequired) {
        super();
        super.setProduct(productName);
        super.setPrice(price);
        this.dimensions = dimensions;
        this.assemblyRequired = assemblyRequired;
    }

    public FurnitureItem(String productName, String productType, Double price, String color) {
        super(productName, productType, price, color);
    }


    // Override Java toString with custom FurnitureItem output
    public String toString() {
        return "[ Warehouse ] - " + getProduct() + " " + getType() + " (" + getColor() + ")";
    }


}
