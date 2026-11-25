package petshop.pet;

import petshop.State;
import petshop.Utils;

public abstract class Pet implements Runnable {
    protected String name;
    protected int age;
    protected State state;
    protected boolean needsFood, needsWater, isSelected;

    public Pet(String name, int age, State state) {
        this.name = name;
        this.age = age;
        this.state = state;
    }

    public abstract int getPrice();

    public synchronized String getName() {
        return name;
    }

    public synchronized int getAge() {
        return age;
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized boolean isNeedsFood() {
        return needsFood;
    }

    public synchronized boolean isNeedsWater() {
        return needsWater;
    }

    public synchronized boolean isSelected() {
        return isSelected;
    }

    public synchronized boolean select() {
        if (!isSelected()) {
            isSelected = true;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 1. módosítsuk az állapotot és a szükségleteket
                changeRandomly();
                // 2. várjunk 2-5 másodpercet (Thread.sleep + ThreadLocalRandom)
                Thread.sleep(Utils.getRandomSec(2, 5));
            }
        } catch (InterruptedException e) {
            // 3. kezeljük a megszakítást és a leállást
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %d éves", getName(), getAge());
    }

    private synchronized void changeRandomly() {
        this.state = Utils.getRandomState();
        this.needsFood = Utils.getRandomBool();
        this.needsWater = Utils.getRandomBool();
    }
}
