package petshop.customer;

import petshop.pet.Pet;
import petshop.Utils;

public class Child extends Customer {
    private Parent parent;

    public Child(String name, Parent parent) {
        super(name, parent.getPetShop());
        this.parent = parent;
    }

    private Pet choosePet() throws InterruptedException {
        Pet selectedPet = null;
        do {
            Pet choice = petShop.getRandomPet();
            if (choice != null && choice.select()) {
                selectedPet = choice;
            } else {
                Thread.sleep(100);
            }
        } while (selectedPet == null);
        return selectedPet;
    }

    @Override
    public void run() {
        try {
            // 1. lépjen be a boltba
            petShop.enterShop();
            // 2. nézelődjön 1-3 másodpercig
            Thread.sleep(Utils.getRandomSec(1, 3));
            // 3. válasszon egy elérhető állatot
            Pet selectedPet = choosePet();
            // 4. sikeres választás esetén
            parent.addSelectedPet(selectedPet);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 5. jelezze a szülőnek, hogy befejezte a választást
            // 6. várja meg, amíg a szülő engedélyezi a család távozását
            try {
                parent.awaitFamilyFinished();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 7. lépjen ki a boltból
            petShop.leaveShop();
        }
    }
}