package products;

import models.FoodItem;
import models.FurnitureItem;
import models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    public static FurnitureItem AlexDeskW = new FurnitureItem("Alex", ProductType.DESK, BigDecimal.valueOf(74.99), ProductColor.WHITE, "Forged in the fires of industrial compression and blessed by three Swedish engineers on their lunch break.", MaterialType.PARTICLE_BOARD);
    public static FurnitureItem AlexDeskB = new FurnitureItem("Alex", ProductType.DESK,BigDecimal.valueOf(64.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    public static FurnitureItem AlexDresserW = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(54.99), ProductColor.WHITE, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    public static FurnitureItem AlexDresserB = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(57.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    public static FurnitureItem BillyBookcaseBr = new FurnitureItem("Billy", ProductType.BOOKCASE,BigDecimal.valueOf(139.99), ProductColor.BROWN, "Elegance only comes once.", MaterialType.OAK_VENEER);
    public static FoodItem Swedish_Meatballs = new FoodItem("Meatball", ProductType.HOT_FOOD, BigDecimal.valueOf(5.99), "Straight from the motherland", false, 30, 4, "Chicken");

    static List<Product> catalog = List.of(AlexDeskB, AlexDeskW, AlexDresserB, AlexDresserW, BillyBookcaseBr, Swedish_Meatballs);
    public static Product productLookup(String name, String type) {
        for (Product i : catalog) {
            if (i.getProduct().equals(name)) {
                if (i.getType().getUiLabel().equals(type)) {
                    return i;
                }
            }
        }
        throw new RuntimeException("Illegal Product");
    }

}
