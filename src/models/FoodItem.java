package models;

import products.ProductModel;
import products.ProductType;

import java.math.BigDecimal;

public class FoodItem extends Product {

    // Initialize Food specific variables
    private boolean isVegan;
    private int calories;
    private int proteinAmount;
    private String flavor;
    private String base;
    private boolean hasProtein = false;
    private boolean hasBase = false;
    private boolean hasFlavor = false;
    private boolean hasCalories = false;

    // Initializing Product
    public FoodItem(String productName, ProductType productType, BigDecimal price, String description, boolean isVegan, int calories, int proteinAmount, String base) {
        super(productName, productType, price, description);
        this.isVegan = isVegan;
        this.calories = calories;
        this.proteinAmount = proteinAmount;
        this.base = base;
        super.setProductModel(ProductModel.FOOD);
        this.hasProtein = true;
        this.hasBase = true;
        this.hasCalories = true;
    }

    public FoodItem(String productName, ProductType productType, BigDecimal price, String description) {
        super(productName, productType, price, description);
        super.setProductModel(ProductModel.FOOD);
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


    public boolean HasCalories() {
        return hasCalories;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean hasProtein() {
        return this.hasProtein;
    }

    public void setProtein(int proteinAmount) {
        this.proteinAmount = proteinAmount;
        this.hasProtein = true;
    }

    public int getProteinAmount() {
        return this.proteinAmount;
    }

    public boolean hasBase() {
        return this.hasBase;
    }

    public void setBase(String base) {
        this.base = base;
        this.hasBase = true;
    }

    public String getBase() { return this.base; }

    public boolean veganStatus() {
        return this.isVegan;
    }

    public void setVegan(boolean vegan) {
        this.isVegan = vegan;
    }

    // Method for Rebuilding products (replaces old inventory manager method)
    @Override
    public FoodItem copy() {
        FoodItem tempItem = new FoodItem(this.getProduct(), this.getType(), this.getPrice(), this.getDescription());
        tempItem.setState(this.getState());
        if (this.hasProtein) {
            tempItem.setProtein(this.proteinAmount);
        }
        if (this.hasBase) {
            tempItem.setBase(this.base);
        }
        if (this.isVegan) {
            tempItem.setVegan(true);
        }
        if (this.hasCalories) {
            tempItem.setCalories(this.calories);
        }
        return tempItem;

    }


}
