package models;

import products.MaterialType;
import products.ProductType;

import java.math.BigDecimal;

public class FurnitureItem extends Product {

    // Initialize variables.
    String dimensions;
    boolean assemblyRequired;
    MaterialType material;

    // Initialize our Product
    public FurnitureItem(String productName, ProductType productType, BigDecimal price, String color, String description, String dimensions, boolean assemblyRequired) {
        super(productName, productType, price, color, description);
        this.dimensions = dimensions;
        this.assemblyRequired = assemblyRequired;
        super.setProductModel("Furniture");
    }

    public FurnitureItem(String productName, ProductType productType, BigDecimal price, String color, String description, MaterialType material) {
        super(productName, productType, price, color, description);
        super.setProductModel("Furniture");
        this.material = material;
    }


    // Override Java toString with custom FurnitureItem output
    public String toString() {
        String skuString = super.getSku().toString();
        skuString = skuString.substring(0,7);
        return "[ Warehouse ] - [" + skuString + "] " + getProduct() + " " + getType() + " (" + getColor() + ")";

    }


}
