package chIKEA;

public class Product {

    private String productName;
    private Double price;
    private Double stockLevel;
    private String aisleLocation;

    public String getProduct() {
        return productName;
    }

    public void setProduct(String productName) {
        this.productName = productName;
    }

    // Create Getter Setter for Price (Cannot be negative)

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Invalid. Price cannot be negative");
        }
        else {
            this.price = price;
        }
    }

    // Create Getter Setter for stockLevel (Stock cannot be negative)

    public Double getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(double stockLevel) {
        if (stockLevel < 0) {
            System.out.println("Invalid. Stock level cannot be negative");
        }
        else {
            this.stockLevel = stockLevel;
        }
    }

    // Create Getter Setter for aisleLocation

    public String getAisleLocation() {
        return aisleLocation;
    }

    public void setAisleLocation(String aisleLocation) {
        this.aisleLocation = aisleLocation;
    }

    // Create (applyEmployeeDiscount) method

    public double applyEmployeeDiscount(double percent) {
        double employee_price = this.price * (1.00 - percent);
        if (employee_price < 0) {
            employee_price = 0;
        }
        return employee_price;
    }


}
