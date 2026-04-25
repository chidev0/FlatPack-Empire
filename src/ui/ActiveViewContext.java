package ui;

import models.CheckoutLaneSnapshot;

import java.util.UUID;

public class ActiveViewContext {

    private UUID selectedProductSku;
    private CheckoutLaneSnapshot selectedLaneSnapshot;
    private int selectedCustomerId;
    private String searchQuery;
    private String statusMessage;
    private ViewMode viewMode;
    private String productName;
    private String productType;
    private CheckoutLaneSnapshot[] laneSnapshotArray;

    public ActiveViewContext(UUID selectedProductSku, ViewMode viewMode) {
        this.selectedProductSku = selectedProductSku;
        this.viewMode = viewMode;
    }

    public ActiveViewContext(CheckoutLaneSnapshot[] laneSnapshotArray, ViewMode viewMode) {
        this.laneSnapshotArray = laneSnapshotArray;
        this.viewMode = viewMode;
    }

    public ActiveViewContext(CheckoutLaneSnapshot selectedLaneId) {
        this.selectedLaneSnapshot = selectedLaneId;
        this.viewMode = ViewMode.LANE_VIEW;
    }

    public ActiveViewContext(String productName, String productType, ViewMode viewMode) {
        this.productName = productName;
        this.productType = productType;
    }

    public int getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(int selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }

    public CheckoutLaneSnapshot getSelectedLaneSnapshot() {
        return selectedLaneSnapshot;
    }

    public void setSelectedLaneSnapshot(CheckoutLaneSnapshot selectedLaneSnapshot) {
        this.selectedLaneSnapshot = selectedLaneSnapshot;
    }

    public UUID getSelectedProductSku() {
        return selectedProductSku;
    }

    public void setSelectedProductSku(UUID selectedProductSku) {
        this.selectedProductSku = selectedProductSku;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ViewMode getViewMode() {
        return viewMode;
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }
}
