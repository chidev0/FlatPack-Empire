package models;

import products.MaterialType;
import products.ProductColor;
import products.ProductModel;
import products.ProductType;

import java.math.BigDecimal;

public class FurnitureItem extends Product {

    // Initialize variables.
    private String dimensions;
    private MaterialType material;

    // Initialize our Product
    public FurnitureItem(String productName, ProductType productType, BigDecimal price, ProductColor color, String description, String dimensions) {
        super(productName, productType, price, color, description);
        this.dimensions = dimensions;
        super.setProductModel(ProductModel.FURNITURE);
    }

    public FurnitureItem(String productName, ProductType productType, BigDecimal price, ProductColor color, String description, MaterialType material) {
        super(productName, productType, price, color, description);
        super.setProductModel(ProductModel.FURNITURE);
        this.material = material;
    }


    // Override Java toString with custom FurnitureItem output
    public String toString() {
        String skuString = super.getSku().toString();
        skuString = skuString.substring(0,7);
        return "[ Warehouse ] - [" + skuString + "] " + getProduct() + " " + getType() + " (" + getColor() + ")";

    }

    // Getter and Setter for material.
    public MaterialType getMaterial() {
        return this.material;
    }

    public void setMaterial(MaterialType material) {
        this.material = material;
    }

    // Getter and Setter for dimensions.
    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public FurnitureItem copy() {
        FurnitureItem tempItem = new FurnitureItem(this.getProduct(), this.getType(), this.getPrice(), this.getColor(), this.getDescription(), this.getMaterial());
        tempItem.setState(getState());
        if (this.dimensions != null) {
            tempItem.setDimensions(getDimensions());
        }
        return tempItem;

    }


}
