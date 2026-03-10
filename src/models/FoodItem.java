package models;

public class FoodItem extends Product {

    // Initialize Food specific variables
    boolean isVegan;
    int calories;
    int proteinAmount;
    String flavor;
    String base;

    // Initializing Product
    public FoodItem(String productName, String productType, Double price, boolean isVegan, int calories, int proteinAmount, String base) {
        super(productName, productType, price);
        this.isVegan = isVegan;
        this.calories = calories;
        this.proteinAmount = proteinAmount;
        this.base = base;
        super.setProductModel("Food");
    }

    public FoodItem(String productName, String productType, Double price) {
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

}
