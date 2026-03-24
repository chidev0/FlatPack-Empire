package models;

import products.ProductType;

import java.math.BigDecimal;

public class FoodItem extends Product {

    // Initialize Food specific variables
    boolean isVegan;
    int calories;
    int proteinAmount;
    String flavor;
    String base;
    boolean hasProtein = false;
    boolean hasBase = false;
    boolean hasFlavor;

    // Initializing Product
    public FoodItem(String productName, ProductType productType, BigDecimal price, boolean isVegan, int calories, int proteinAmount, String base) {
        super(productName, productType, price);
        this.isVegan = isVegan;
        this.calories = calories;
        this.proteinAmount = proteinAmount;
        this.base = base;
        super.setProductModel("Food");
        this.hasProtein = true;
        this.hasBase = true;
    }

    public FoodItem(String productName, ProductType productType, BigDecimal price) {
        super(productName, productType, price);
        super.setProductModel("Food");
    }

    // Override Javas toString method to FoodProduct tailored output.
    @Override
    public String toString() {

        String skuString = super.getSku().toString();
        skuString = skuString.substring(0,7);
        return "[ Cafeteria ] - [" + skuString + "] " + this.base + " " + this.getProduct();
    }

    public boolean isVegan() {return isVegan;}

    // Methods for evaluating if FoodItem object has additional attributes.
    public boolean hasProtein() {
        return this.hasProtein;
    }

    public void setProtein(int proteinAmount) {
        this.proteinAmount = proteinAmount;
    }

    public int getProteinAmount() {
        return this.proteinAmount;
    }

    public boolean hasBase() {
        return this.hasBase;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getBase() {
        return this.base;
    }

    public boolean veganStatus() {
        return this.isVegan;
    }

    public void setVegan(boolean vegan) {
        this.isVegan = vegan;
    }


}
