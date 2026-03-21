package models;

public class TransactionReceipt {
    private int quantity;
    private String productName;
    private String productType;
    private String productModel;
    private double discount;
    private String discountType;
    private double finalPrice;
    private String productColorPrimary;
    private String productColorSecondary;
    private String productCondition;

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
    }

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice, double discount, String discountType) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.discountType = discountType;
    }

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice, String productColorPrimary) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
        this.productColorPrimary = productColorPrimary;
    }

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice, String productColorPrimary, String productColorSecondary) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
        this.productColorPrimary = productColorPrimary;
        this.productColorSecondary = productColorSecondary;
    }

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice, double discount, String discountType, String productColorPrimary) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.discountType = discountType;
        this.productColorPrimary = productColorPrimary;
    }

    public TransactionReceipt(String productName, String productType, String productModel, int Quantity, double finalPrice, double discount, String discountType, String productColorPrimary, String productColorSecondary) {
        this.productName = productName;
        this.productType = productType;
        this.productModel = productModel;
        this.quantity = Quantity;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.discountType = discountType;
        this.productColorPrimary = productColorPrimary;
        this.productColorSecondary = productColorSecondary;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductModel() {
        return productModel;
    }

    public String getProductColorPrimary() {
        return productColorPrimary;
    }

    public String getProductColorSecondary() {
        return productColorSecondary;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }


}
