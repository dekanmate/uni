# Konkurens programozás beadandó feladat

A városban egy új kisállatkereskedés nyílt a héten. Rengeteg kisgyerek és felnőtt érkezett csodájára járni a különleges állatoknak, ezért az a feladatunk, hogy zökkenőmentes nézelődést és vásárlást biztosítsunk a vevők, a kiskedvencek és az eladók között.

## 1. részfeladat: kisállatok (`petshop.pet.Pet`)

Valósítsuk meg az absztrakt `Pet` osztályt, amely implementálja a `Runnable` interfészt. Minden állatnak legyen neve, életkora, `State` állapota, `needsFood`, `needsWater` és `isSelected` boolean adattagja.

A lehetséges állapotai: `FINE`, `STRESSED`, `PLAYFUL`, `SLEEPY`.

Készítsük el a következő leszármazott osztályokat, amelyek `getPrice()` megvalósított metódusában az árukat adjuk vissza:

| Osztály          | Ár     |
|------------------|--------|
| `CornSnake`      | 30000  |
| `ChineseHamster` | 5000   |
| `GuineaPig`      | 12000  |
| `SenegalParrot`  | 75000  |
| `TurkishAngora`  | 120000 |

Legyenek getterek és egy nevet, valamint életkort megjelenítő `toString()` metódus. A `select()` szinkronizált metódus csak az első kiválasztáskor adjon vissza igaz értéket.

A `run()` metódus 2-5 másodperccenként véletlenszerűen módosítsa az állat állapotát és szükségleteit a `Thread.sleep()` és `ThreadLocalRandom` segítségével. Az állatok a szimuláció végén szabályosan álljanak le.

## 2. részfeladat: Pénztáros (`petshop.Cashier`)

A `Cashier` implementálja a `Runnable` interfészt, és egy `PetShop` példányt tároljon. Folyamatosan feldolgozza a kasszasorba érkező fizetési kéréseket. Ha minden vásárlás befejeződött, álljon le.

## 3. részfeladat: Vásárlók (`petshop.Customer`)

A `Customer` absztrakt ősosztály implementálja a `Runnable` interfészt, tárolja a vásárló nevét, illetve a `PetShop` példányát.

A `Parent` osztály eltárolja a gyerekek számát és egy `BlockingQueue` sorban a gyerekek által kiválasztott állatokat. A gyerekek választásának, fizetésének és hazaindulásának koordinációját `BlockingQueue` vagy `CountDownLatch` segítségével valósítsuk meg.

A szülő a boltba bemegy, megvárja a gyerekek választását, minden kiválasztott állatért fizet, majd jelzi a családnak, hogy indulhatnak haza.

A `Child` osztály egy `Parent` példányt kap a konstruktorában. A gyerek 1-3 másodpercig nézelődik, véletlenszerűen választ egy még ki nem választott állatot, majd értesíti a szülőt. Ha nem sikerül választania, újra próbálkozik. A család hazaindulási jelzésére várva fejezi be a feladatát.

## 4. részfeladat: Kisállatkereskedés (`petshop.PetShop`)

A `PetShop` tárolja az aktuális látogatók számát `AtomicInteger` segítségével, a bevételt, az állatokat egy `ConcurrentLinkedQueue` sorban, a fizetési kéréseket pedig egy `BlockingQueue` sorban.

Valósítsuk meg az állatok hozzáadását és véletlenszerű kiválasztását, a belépést és kilépést, valamint a következő metódusokat:

- `goToCheckout()`: egy fizetési kérelmet hozzáadunk a sorhoz.
- `getNextCheckout()`: lekérjük a következő fizetési kérelmet.

A `processCheckout()` szinkronizált metódus dolgozza fel a fizetést, növelje meg a bevételt, és írja ki a tranzakció adatait.

Legyen egy ehhez kapcsolódó `CheckoutRequest` osztály, ami tárolja a szülőt és a megvásárolandó állatot.

## 5. részfeladat: Main

Hozzuk létre a kisállatkereskedést, töltsük fel állatokkal, és hozzunk létre családokat. Az összes állatot, szülőt és gyereket még a szimuláció indítása előtt hozzuk létre. Biztosítsuk, hogy legalább annyi állat legyen, mint ahány gyerek.

`ExecutorService` segítségével indítsuk el az állatokat, a pénztárost, a szülőket és a gyerekeket. Várjuk meg az összes vásárlói feladat befejezését, majd a `shutdownNow()` segítségével kezdeményezzük a fennmaradó feladatok leállítását. Az `awaitTermination()` segítségével várjuk meg a leállást, végül írjuk ki a napi bevételt.

A megoldás során ügyeljünk a szálbiztos állapotkezelésre, a blokkoló műveletek megszakíthatóságára és arra, hogy egyetlen feladat se maradjon végtelen ciklusban.
