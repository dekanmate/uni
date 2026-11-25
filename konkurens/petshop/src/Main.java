import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import petshop.Cashier;
import petshop.PetShop;
import petshop.Utils;
import petshop.customer.Child;
import petshop.customer.Parent;
import petshop.pet.*;

public class Main {
    public static void main(String[] args) {
        // 1. hozzuk létre a PetShop példányt
        PetShop petShop = new PetShop();

        // 2. hozzuk létre az állatokat és adjuk hozzá őket a bolthoz (legalább annyi állat legyen, mint gyerek)
        Pet chineseHamster = new ChineseHamster("Kínai csíkos hörcsög", 1, Utils.getRandomState());
        petShop.addPet(chineseHamster);
        Pet cornSnake = new CornSnake("Vörös gabonasikló", 2, Utils.getRandomState());
        petShop.addPet(cornSnake);
        Pet guineaPig = new GuineaPig("Tengerimalac", 3, Utils.getRandomState());
        petShop.addPet(guineaPig);
        Pet senegalParrot = new SenegalParrot("Szenegáli afropapagáj", 4, Utils.getRandomState());
        petShop.addPet(senegalParrot);
        Pet turhkishAngora = new TurkishAngora("Török angóramacska", 5, Utils.getRandomState());
        petShop.addPet(turhkishAngora);

        // 3. hozzuk létre a pénztárost, a szülőket és a gyerekeiket (mindent az indítás előtt)
        Cashier cashier = new Cashier(petShop);

        Parent family1Parent = new Parent("1. család szülője", petShop, 2);
        Child family1Child1 = new Child("1. család I. gyereke", family1Parent);
        Child family1Child2 = new Child("1. család II. gyereke", family1Parent);

        Parent family2Parent = new Parent("2. család szülője", petShop, 3);
        Child family2Child1 = new Child("2. család I. gyereke", family2Parent);
        Child family2Child2 = new Child("2. család II. gyereke", family2Parent);
        Child family2Child3 = new Child("2. család III. gyereke", family2Parent);
        
        // 4. hozzuk létre az ExecutorService-t (legyen elegendő szál az ÖSSZES feladathoz, különben a szimuláció holtpontra juthat)
        ExecutorService executor = Executors.newFixedThreadPool(13);
        
        // 5. indítsuk el az állatokat, a pénztárost, a szülőket és a gyerekeket
        List<Future<?>> futures = new ArrayList<>();

        executor.submit(cashier);
        executor.submit(chineseHamster);
        executor.submit(cornSnake);
        executor.submit(guineaPig);
        executor.submit(senegalParrot);
        executor.submit(turhkishAngora);
        
        futures.add(executor.submit(family1Parent));
        futures.add(executor.submit(family2Parent));
        futures.add(executor.submit(family1Child1));
        futures.add(executor.submit(family1Child2));
        futures.add(executor.submit(family2Child1));
        futures.add(executor.submit(family2Child2));
        futures.add(executor.submit(family2Child3));

        // 6. várjuk meg az összes vásárlói feladat (szülők és gyerekek) befejezését
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                // vásárló feladat kivétel
            }
        }

        // 7. shutdownNow()
        executor.shutdownNow();

        // 8. awaitTermination()
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 9. írjuk ki a napi bevételt
        System.out.println(String.format("Napi bevétel: %d", petShop.getRevenue()));
    }
}