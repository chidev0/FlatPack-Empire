package models;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

public class TransitManifest {
    private UUID sessionID;
    private List<Product> productLog = new ArrayList<>();
    private LocalDateTime timestamp;
    private String transitType;
    private String status = "Success";
    int errorCode;

    private TransitManifest(String transitType, UUID sessionID) {
        this.timestamp = LocalDateTime.now();
        this.sessionID = sessionID;
        this.transitType = transitType;
    }

    public static TransitManifest createForMovement(String transitType) {
        return new TransitManifest(transitType, UUID.randomUUID());
    }

    public void logProduct(Product p) {
        productLog.add(p);
    }

    public List<Product> getProductLog() {
        return productLog;
    }

    public String summarize() {
        return "[Session " + sessionID.toString().substring(0,5) + " - " + transitType + "]" + productLog.size() + " products adjusted at " + timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
