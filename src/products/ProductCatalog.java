package products;

import models.FoodItem;
import models.FurnitureItem;

import java.math.BigDecimal;

public class ProductCatalog {
    FurnitureItem AlexDeskW = new FurnitureItem("Alex", ProductType.DESK, BigDecimal.valueOf(74.99), "White", "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDeskB = new FurnitureItem("Alex", ProductType.DESK,BigDecimal.valueOf(64.99), "Black", "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDresserW = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(54.99), "White", "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem AlexDresserB = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(57.99), "Black", "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
    FurnitureItem BillyBookcaseBr = new FurnitureItem("Billy", ProductType.DRESSER,BigDecimal.valueOf(139.99), "Brown", "Elegance only comes once.", MaterialType.OAK_VENEER);
    FoodItem Swedish_Meatballs = new FoodItem("Meatball", ProductType.HOT_FOOD, BigDecimal.valueOf(5.99), false, 30, 4, "Chicken");

}
