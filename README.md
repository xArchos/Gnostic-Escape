# Gnostic Escape – dokumentacja projektowa

## 1.	Wymagania
   + Zainstalowana Java oraz środowisko JavaFX (zalecana wersja to 17 lub wyżej).
## 2.	Zasady gry
   + Gra przeznaczona jest domyślnie dla 4 osób choć możliwe jest również granie w trójkę. 
   + Cechuje ją asymetryczny model rozgrywki a zatem jeden gracz (Demiurg i jednocześnie host sesji) mierzy się z 3 pozostałymi (Uciekinierami).
   + Celem Uciekinierów jest dostanie się do punktu ewakuacji oznaczonego na mapie jako czerwona flaga. 
        Aby uznane zostało ich zwycięstwo, co najmniej dwoje graczy musi trafić do punktu ewakuacji.
   + Uciekinierzy mogą przemieszczać się po mapie zbierając przy tym apteczki i przesuwając przedmioty.
   + Uciekinierzy posiadają zdrowie, o którego ilości są informowani. 
        Gdy ich zdrowie spadnie do zera, odebrana im zostaje możliwość ruchu i tym samym szansa na ucieczkę.
   + Inne elementy o których gracze są informowani to także pozycja na mapie (koordynaty x,y), ilu graczy uciekło, ilu umarło 
        oraz jakie efekty zostały na nich nałożone przez Demiurga.
   + Demiurg nie znajduje się bezpośrednio na planszy, lecz rzuca na graczy zaklęcia utrudniające im dotarcie do celu. 
        Ogranicza go mana, regenerująca się wraz z biegiem rozgrywki.
   + Demiurg posiada wgląd w statystki uciekinierów takie jak zdrowie, położenie oraz aktywnie nałożone na nich efekty.
   + Posiada również panel uroków umożliwiający rzucanie zaklęć oraz panel teleportacji, przenoszący gracza na wybrane przez Demiurga pole.
   + Demiurg wygrywa gdy zabije co najmniej 2 graczy. Pomaga mu w tym również rozrastający się obszar skażenia, zadający graczom obrażenia.
   + Inne zagrożenia dla Uciekinierów to także kolec oraz pułapka (znika przy aktywacji przez gracza)
   + Gracze posiadają na panelu również przełącznik trybu dzień-noc, który zmienia nieco wygląd planszy.
