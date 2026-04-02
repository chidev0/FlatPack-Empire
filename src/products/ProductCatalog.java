package products;

import models.FoodItem;
import models.FurnitureItem;

import java.math.BigDecimal;

public class ProductCatalog {
    FurnitureItem AlexDeskW = new FurnitureItem("Alex", ProductType.DESK, BigDecimal.valueOf(74.99), ProductColor.WHITE, "Forged in the fires of industrial compression and blessed by three Swedish engineers on their lunch break.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDeskB = new FurnitureItem("Alex", ProductType.DESK,BigDecimal.valueOf(64.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDresserW = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(54.99), ProductColor.WHITE, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDresserB = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(57.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem BillyBookcaseBr = new FurnitureItem("Billy", ProductType.BOOKCASE,BigDecimal.valueOf(139.99), ProductColor.BROWN, "Elegance only comes once.", MaterialType.OAK_VENEER);
    FoodItem Swedish_Meatballs = new FoodItem("Meatball", ProductType.HOT_FOOD, BigDecimal.valueOf(5.99), "Straight from the motherland", false, 30, 4, "Chicken");

}
