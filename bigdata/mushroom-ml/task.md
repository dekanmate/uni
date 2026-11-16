# Gomba Osztályozási Feladat 🍄

- **Határidő:** lásd TMS
- **Beküldés:** [tms.inf.elte.hu](https://tms.inf.elte.hu)
- **Értékelés:** A beadandóra kapott jegyet a modell által elért pontosság határozza meg:
  - 0.95 -> **5**
  - 0.92 -> **4**
  - 0.90 -> **3**
  - 0.80 -> **2**

---

## Feladat

A beadandó megoldásához az `input.csv` fájlban található adatokat kell beolvasni, feldolgozni és egy osztályozási feladatot megoldani. A fájlban minden sor egy begyűjtött gomba tulajdonságait írja le.

### Attribútumok:
- **cap-diameter:** a kalap átmérője
- **stem-height:** a törzs magassága
- **stem-width:** a törzs szélessége
- **date:** a rekord adatbázisba való betöltésének a napja (nem a begyűjtés napja!)
- **cap-shape:** a kalap alakja
- **cap-color:** a kalap színe
- **has-ring:** van-e egy gyűrűje vagy sem
- **ring-type:** a gyűrű típusa
- **class:** ehető-e vagy sem (`p` - mérgező, `e` - ehető)

Az osztályozási feladat során azt kell eldöntenünk egy gombáról az attribútumai alapján, hogy ehető-e vagy sem (`class`).

---

## Beküldés lépései

1. Készíts egy osztályozó modellt, és osztályozd a validációs adathalmazban található gombákat (`validation.csv`).
2. Az osztályozó modell `predict()` függvényének a kimenetét mentsd el fájlba a következő utasítással:
   ```python
   np.savetxt('pred.txt', prediction, fmt='%s')
   ```
3. Töltsd fel a kódodat (`.py` vagy `.ipynb` fájl) a `pred.txt`-vel együtt egy ZIP fájlba csomagolva a TMS rendszerbe.
4. Az automatikus kiértékelő rendszer kiírja, hogy milyen pontosságot sikerült elérned.

> **Tipp:** A megoldáshoz a gyakorlatokon megismert osztályozó modelleket lehet felhasználni, szükség lehet adattisztítási lépésekre, az adatok átalakítására, modellek paraméterhangolására, releváns attribútumok kiválasztására és egyéb műveletekre.