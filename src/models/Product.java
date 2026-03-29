package models;
import products.ProductColor;
import products.ProductModel;
import products.ProductState;
import products.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public class Product implements Comparable<Product> {

    private String productName;
    private ProductType productType;
    private BigDecimal price;
    private int stockLevel;
    private ZoneType zone;
    private ProductColor color;
    private UUID sku;
    private ProductModel productModel;
    private ProductState state = ProductState.LIMBO;
    private Location warehouseLocation;
    private String description;

    // Product Constructor
    public Product(String productName, ProductType productType, BigDecimal price, ProductColor color, String description) {
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.color = color;
        this.sku = UUID.randomUUID();
        this.description = description;
    }

    // Product Constructor w/o price variable declaration
    public Product(String productName, ProductType productType, ProductColor color) {
        this.productName = productName;
        this.productType = productType;
        this.color = color;
        this.sku = UUID.randomUUID();
    }

    // Product Constructor w/o color String declaration.
    public Product(String productName, ProductType productType, BigDecimal price, String description) {
        this.productName = productName;
        this.productType = productType;
        this.price = price;
        this.description = description;
        this.sku = UUID.randomUUID();
    }

    // Default Product Constructor
    public Product() {this.sku = UUID.randomUUID();}

    // Create Getter Setter for Color (WIP - Check to see if Color matches list of accepted Colors)
    public ProductColor getColor() { return color; }

    public void setColor(ProductColor color) { this.color = color; }

    // Create Getter Setter for Product Name
    public String getProduct() {
        return productName;
    }

    public void setProduct(String productName) {
        this.productName = productName;
    }

    // Create Getter for SKU
    public UUID getSku() {
        return sku;
    }

    public void generateSku() {
        this.sku = UUID.randomUUID();
    }

    // Create Getter Setter for Product Type
    public ProductType getType() { return productType; }

    public void setType(ProductType productType) { this.productType = productType; }

    // Create Getter for Product Model Type
    public ProductModel getProductModel() { return productModel; }
    public void setProductModel(ProductModel model) {
        this.productModel = model;
    }

    // Create Getter Setter for Price (Cannot be negative)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Illegal Price");
        }
        else {
            this.price = price;
        }
    }

    // Getter and Setter for description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Create Getter Setter for stockLevel (Stock cannot be negative)
    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        if (stockLevel < 0) {
            throw new RuntimeException("Illegal Stock Level");
        }
        else {
            this.stockLevel = stockLevel;
        }
    }

    // Create Getter Setter for aisleLocation
    public ZoneType getZone() {
        if (zone == null) {
            throw new RuntimeException("Zone has not been assigned yet.");
        }
        return zone;
    }

    public void setZone(ZoneType zone) {
        this.zone = zone;
    }

    public Location getWarehouseLocation() {
        if (warehouseLocation == null) {
            throw new RuntimeException("Location has not been assigned yet.");
        }
        return warehouseLocation;
    }

    public void setWarehouseLocation(int aisle, int bin) {
        if (getZone() == ZoneType.WAREHOUSE_GRID) {
            this.warehouseLocation = new Location(aisle, bin);
            return;
        }
        throw new RuntimeException("Cannot set location. Zone Received: " + getZone() + ". Zone required: " + ZoneType.WAREHOUSE_GRID);
    }

    // Getter Setter Methods for product state
    public ProductState getState() {
        return state;
    }

    public void setState(ProductState newState) {
        if (this.state.canTransitionTo(newState)) {
            this.state = newState;
            return;
        }
        throw new RuntimeException("Illegal Set Modifier: Current State - " + this.state + ". Target State - " + newState);
    }
    // Create (applyEmployeeDiscount) method
    public BigDecimal applyEmployeeDiscount(BigDecimal percent) {
        BigDecimal employee_price = this.price.multiply(BigDecimal.valueOf(1.00).subtract(percent));
        // Checks if new price is less than zero, if it is sets it to be zero.
        if (employee_price.compareTo(BigDecimal.ZERO) < 0) {
            employee_price = BigDecimal.ZERO;
        }
        return employee_price;
    }
    // Method for comparing two products to each other.
    public int compareTo(Product other) {
        return this.price.compareTo(other.price);
    }

    public Product copy() {
        throw new RuntimeException("Expected a subclass call.");
    }


}
