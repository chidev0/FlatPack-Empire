package engine;
import java.util.Random;

public class SimRandom {
private Random spawn;
private Random shopping;
private Random checkout;
private Random delivery;
private Random damage;
private Random economy;
private Random seedGenerator = new Random();
private GameState gameState;

public SimRandom(GameState gameState) {
    this.gameState = gameState;
    gameState.setGameSeed(seedGenerator.nextLong());
    spawn = new Random(gameState.getGameSeed() ^ "spawn".hashCode());
    shopping = new Random(gameState.getGameSeed() ^ "shopping".hashCode());
    checkout = new Random(gameState.getGameSeed() ^ "checkout".hashCode());
    delivery = new Random(gameState.getGameSeed() ^ "delivery".hashCode());
    damage = new Random(gameState.getGameSeed() ^ "damage".hashCode());
    economy = new Random(gameState.getGameSeed() ^ "economy".hashCode());
}


    public Random getCheckout() {
        return checkout;
    }

    public Random getDamage() {
        return damage;
    }

    public Random getDelivery() {
        return delivery;
    }

    public Random getEconomy() {
        return economy;
    }

    public Random getShopping() {
        return shopping;
    }

    public Random getSpawn() {
        return spawn;
    }
}
