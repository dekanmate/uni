package petshop;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import petshop.pet.Pet;


public class Utils {
    public static int getRandomSec(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from * 1000, to * 1000 + 1);
    }

    public static boolean getRandomBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static State getRandomState() {
        State[] all = State.values();
        return all[ThreadLocalRandom.current().nextInt(all.length)];
    }

    public static Pet getRandomPetFromList(List<Pet> list) {
        if (list.isEmpty()) {
            return null;
        }
        
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
