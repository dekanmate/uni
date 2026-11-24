package petshop.customer;

import petshop.CheckoutRequest;
import petshop.PetShop;
import petshop.pet.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class Parent extends Customer {
    private int numberOfChildren;
    private BlockingQueue<Pet> selectedPets;
    private BlockingQueue<Boolean> familyFinished;
    private CountDownLatch paymentDone;

    public Parent(String name, PetShop petShop, int numberOfChildren) {
        super(name, petShop);
        this.numberOfChildren = numberOfChildren;
        this.selectedPets = new LinkedBlockingQueue<>();
        this.familyFinished = new LinkedBlockingQueue<>();
        this.paymentDone = new CountDownLatch(numberOfChildren);
    }

    public void paymentCompleted() {
        // a pénztár jelzi, hogy egy fizetést feldolgozott
        paymentDone.countDown();
    }

    public void addSelectedPet(Pet pet) throws InterruptedException {
        // helyezzük az állatot a selectedPets sorba
        selectedPets.put(pet);
    }

    public void awaitFamilyFinished() throws InterruptedException {
        // vegyünk ki egy tokent a familyFinished sorból
        familyFinished.take();
    }

    @Override
    public void run() {

        List<Pet> pets = new ArrayList<>();

        // 1. lépjünk be a boltba
        petShop.enterShop();
        try {
            // 2. várjuk meg, amíg minden gyerek befejezi a választást
            // 3. kérjük le a kiválasztott állatokat
            for (int i = 0; i < numberOfChildren; i++) {
                pets.add(selectedPets.take());
            }
            // 4. küldjük el a fizetési kérelmeket
            for (Pet pet : pets) {
                petShop.goToCheckout(new CheckoutRequest(this, pet));
            }
            // várjuk meg, amíg a pénztár minden fizetést feldolgozott
            paymentDone.await();
            // 5. jelezzük a gyerekeknek, hogy indulhatnak haza
            for (int i = 0; i < numberOfChildren; i++) {
                familyFinished.put(true);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 6. hagyjuk el a boltot
            petShop.leaveShop();
        }
    }
}