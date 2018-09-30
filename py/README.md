# Python programok
Tesztprogramok az alkalmazás számításainak tesztelésére.  
Ezekben a programokban a sebesség minding **km/h**, az idő **óra**, a távolság pedig **km**

## fly_distance.py
Kiszámolja, hogy a szúnyog mekkora távolságot fog megtenni a két ember között a szélsebesség figyelembe vételével  
*(A szél mindig jobbra vagy balra fúj)*  
  
```
python3 fly_distance.py [emberASebesseg] [emberBSebesseg] [emberekTavolsaga] [szunyogSebessege] [szelsebesseg] [pontossag]
```

- **emberASebesseg**: Az *A* ember sebessége
- **emberBSebesseg**: Az *B* ember sebessége
- **emberekTavolsaga**: Az emberek távolsága
- **szunyogSebessege**: A szúnyog sebessége
- **szelsebesseg**: A szél sebessége. Pozitív érték jobbra fúj, negatív balra
- **pontossag**: Mivel a számítás egy végtelen sorozat, ezért nagyobb értékek pontosabb megközelítést adnak *(Ajánlott 1000)*  
  
## fly_time.py
Kiszámolja, hogy a szúnyognak mikor kell elindulni, ha egy adott távot szeretne megtenni
*(A szél mindig jobbra vagy balra fúj)*  
  
```
python3 fly_time.py [emberASebesseg] [emberBSebesseg] [emberekTavolsaga] [szunyogSebessege] [szelsebesseg] [pontossag] [celtavolsag]
```

- **emberASebesseg**: Az *A* ember sebessége
- **emberBSebesseg**: Az *B* ember sebessége
- **emberekTavolsaga**: Az emberek távolsága
- **szunyogSebessege**: A szúnyog sebessége
- **szelsebesseg**: A szél sebessége. Pozitív érték jobbra fúj, negatív balra
- **pontossag**: Mivel a számítás egy végtelen sorozat, ezért nagyobb értékek pontosabb megközelítést adnak *(Ajánlott 1000)*
- **celtavolsag**: A távolság amit a szúnyog meg szeretne tenni
