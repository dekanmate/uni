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

    private List<Pet> waitForChildren() throws InterruptedException {
        List<Pet> pets = new ArrayList<>();
        for (int i = 0; i < numberOfChildren; i++) {
            pets.add(selectedPets.take());
        }
        return pets;
    }

    private void payForPets(List<Pet> pets) throws InterruptedException {
        for (Pet pet : pets) {
            petShop.goToCheckout(new CheckoutRequest(this, pet));
        }
        paymentDone.await();
    }

    private void goHome() throws InterruptedException {
        for (int i = 0; i < numberOfChildren; i++) {
            familyFinished.put(true);
        }
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
        // 1. lépjünk be a boltba
        petShop.enterShop();
        try {
            // 2. várjuk meg, amíg minden gyerek befejezi a választást
            // 3. kérjük le a kiválasztott állatokat
            List<Pet> pets = waitForChildren();
            // 4. küldjük el a fizetési kérelmeket, és várjuk meg, amíg a pénztár mindet feldolgozta
            payForPets(pets);
            // 5. jelezzük a gyerekeknek, hogy indulhatnak haza
            goHome();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 6. hagyjuk el a boltot
            petShop.leaveShop();
        }
    }
}
