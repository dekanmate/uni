package petshop;

import petshop.pet.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PetShop {
    private AtomicInteger currentVisitors;
    private int revenue;
    private ConcurrentLinkedQueue<Pet> pets;
    private BlockingQueue<CheckoutRequest> checkoutQueue;

    public PetShop() {
        this.currentVisitors = new AtomicInteger(0);
        this.revenue = 0;
        this.pets = new ConcurrentLinkedQueue<>();
        this.checkoutQueue = new LinkedBlockingQueue<>();
    }

    public void addPet(Pet pet) {
        // adjuk hozzá az állatot a pets sorhoz
        pets.add(pet);
    }

    public Pet getRandomPet() {
        // adjunk vissza egy véletlenszerű állatot, vagy null-t, ha nincs elérhető állat
        List<Pet> available = new ArrayList<>();
        for (Pet pet : pets) {
            if (!pet.isSelected()) {
                available.add(pet);
            }
        }
        return Utils.getRandomPetFromList(available);
    }

    public void enterShop() {
        // növeljük a látogatók számát
        currentVisitors.incrementAndGet();
    }

    public void leaveShop() {
        // csökkentsük a látogatók számát
        currentVisitors.decrementAndGet();
    }

    public int getCurrentVisitors() {
        // adjuk vissza az aktuális látogatók számát
        return currentVisitors.get();
    }

    public void goToCheckout(CheckoutRequest request) throws InterruptedException {
        // adjuk hozzá a fizetési kérelmet a blokkoló sorhoz
        checkoutQueue.put(request);
    }

    public CheckoutRequest getNextCheckout() throws InterruptedException {
        // kérjük le a következő fizetési kérelmet
        return checkoutQueue.take();
    }

    public synchronized void processCheckout(CheckoutRequest request) {
        // 1. határozzuk meg az állat árát
        int price = request.getPet().getPrice();
        // 2. frissítsük a bevételt
        revenue += price;
        // 3. írjuk ki a tranzakció adatait
        System.out.println(String.format("\nEgy állatot megvásároltak:\nÁllat neve: %s\nÁra: %d\nVásárló: %s\n\nEddigi napi bevétel: %d", request.getPet().getName(), price, request.getParent().getName(), getRevenue()));
        request.getParent().paymentCompleted();
    }

    public synchronized int getRevenue() {
        return revenue;
    }
}
