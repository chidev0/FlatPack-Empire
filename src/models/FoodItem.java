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
    }

    // Override Javas toString method to FoodProduct tailored output.
    @Override
    public String toString() {
        return "[ Cafeteria ] - " + this.base + " " + this.getProduct();
    }

}
