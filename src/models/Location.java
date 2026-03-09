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
        System.out.println("Updated Aisle");
    }

    public void setBin(int bin) {
        this.bin = bin;
        System.out.println("Updated Bin");
    }



    public String toTagFormat() {
        return "Aisle " + aisle + " : Bin " + bin;
    }

    public String getZoneType() {
        if (aisle <= 20) {
            return "Warehouse Grid";
        } else if (aisle > 20 && aisle <= 35) {
            return "Market Hall";
        }
        else if (aisle == 99) {
            return "Cafeteria";
        }
        return "Something went wrong pulling Zone Type";
    }

}
