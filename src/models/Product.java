package models;
import java.util.UUID;

public class Product implements Comparable<Product> {

    private String productName;
    private String productType;
    private double price;
    private int stockLevel;
    private Location aisleLocation;
    private String color;
    private UUID Sku;

    // Product Constructor
    public Product(String productName, String productType, double price, String color) {
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.color = color;
        this.Sku = UUID.randomUUID();
    }

    // Product Constructor w/o price variable declaration
    public Product(String productName, String productType, String color) {
        this.productName = productName;
        this.productType = productType;
        this.color = color;
        this.Sku = UUID.randomUUID();
    }

    // Product Constructor w/o color String declaration.
    public Product(String productName, String productType, double price) {
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.Sku = UUID.randomUUID();
    }

    // Default Product Constructor
    public Product() {this.Sku = UUID.randomUUID();}

    // Create Getter Setter for Color (WIP - Check to see if Color matches list of accepted Colors)
    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    // Create Getter Setter for Product Name
    public String getProduct() {
        return productName;
    }

    public void setProduct(String productName) {
        this.productName = productName;
    }

    // Create Getter for SKU
    public UUID getSku() {
        return Sku;
    }

    // Create Getter Setter for Product Type
    public String getType() { return productType; }

    public void setType(String productType) { this.productType = productType; }

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
    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        if (stockLevel < 0) {
            System.out.println("Invalid. Stock level cannot be negative");
        }
        else {
            this.stockLevel = stockLevel;
        }
    }

    // Create Getter Setter for aisleLocation
    public String getAisleLocation() {
        if (aisleLocation == null) {
            throw new RuntimeException("Aisle Location has not been assigned yet.");
        }
        return aisleLocation.toTagFormat();
    }

    public void setAisleLocation(int Aisle, int Bin) {
        this.aisleLocation = new Location(Aisle, Bin);
    }

    // Create (applyEmployeeDiscount) method
    public double applyEmployeeDiscount(double percent) {
        double employee_price = this.price * (1.00 - percent);
        if (employee_price < 0) {
            employee_price = 0;
        }
        return employee_price;
    }
    // Method for comparing two products to each other.
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }



}
