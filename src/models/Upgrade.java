package models;

import engine.GameState;
import upgrades.UpgradePrerequisite;
import upgrades.UpgradeType;
import engine.SimConfig;

import java.math.BigDecimal;

public class Upgrade {

    private String upgradeName;
    private UpgradeType upgradeType;
    private BigDecimal cost;
    private UpgradePrerequisite prerequisite;
    private boolean startingTier = false;
    private boolean unlocked;

    // Truck Specific Upgrade Quirks
    private double damageChance;
    private int capacity;

    public Upgrade(String name, UpgradeType type, BigDecimal cost, UpgradePrerequisite prerequisite, boolean startingTier, boolean unlocked) {
        this.upgradeName = name;
        this.upgradeType = type;
        this.cost = cost;
        this.prerequisite = prerequisite;
        this.startingTier = startingTier;
        this.unlocked = unlocked;
    }

    // Truck Specific Constructor

    public Upgrade(String name, UpgradeType type, BigDecimal cost, UpgradePrerequisite prerequisite, boolean startingTier, boolean unlocked, double damageChance, int capacity) {
        this.upgradeName = name;
        this.upgradeType = type;
        this.cost = cost;
        this.prerequisite = prerequisite;
        this.startingTier = startingTier;
        this.damageChance = damageChance;
        this.capacity = capacity;
        this.unlocked = unlocked;
    }

    // Getters and Setters for upgrade information

    public String getUpgrade() {
        return upgradeName;
    }

    public void setUpgrade(String upgradeName) {
        this.upgradeName = upgradeName;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public UpgradePrerequisite getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(UpgradePrerequisite prerequisite) {
        this.prerequisite = prerequisite;
    }

    public double getDamageChance() {
        return damageChance;
    }

    public void setDamageChance(double damageChance) {
        this.damageChance = damageChance;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean startingTier() {
        return startingTier;
    }

    public void setStartingTier(boolean startingTier) {
        this.startingTier = startingTier;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
