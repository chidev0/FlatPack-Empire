package models;

public class Location {

    private int aisle;
    private int bin;

    public Location(int aisle, int bin) {
        this.aisle = aisle;
        this.bin = bin;
    }

    public int getAisle() { return aisle; }
    public int getBin() { return bin; }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    public void setBin(int bin) {
        this.bin = bin;
    }



    public String toTagFormat() {
        return "Aisle " + aisle + " : Bin " + bin;
    }

    // Removed old ZoneType method

}
