# Poomismäng (Hangman)

## Rakenduse kirjeldus
Poomismäng on klassikaline sõnade äraarvamise mäng, kus mängija peab täht-tähelt arvama ära peidetud sõna. Iga vale tähe puhul joonistub üha rohkem võllapuu elemente, kuni lõpuks on poomispilt valmis. Mängijal on võimalus võita, kui ta suudab sõna ära arvata enne, kui võllapuu saab valmis.

## Omadused
- Eestikeelne kasutajaliides
- Erinevad sõna kategooriad valimiseks
- Mänguaja mõõtmine ja salvestamine
- Edetabel parimate tulemustega
- Dünaamiliselt uuenev võllapuu illustratsioon
- Võimalus näha valesti sisestatud tähti

## Rakenduse käivitamine
1. Veenduge, et teie arvutisse on paigaldatud Java (vähemalt versioon 11)
2. Laadige alla rakenduse JAR-fail või kloonige see repositoorium
3. Käivitage rakendus käsurealt:
   ```
   java -jar poomismang.jar
   ```

   Alternatiivina võite määrata kohandatud andmebaasi asukoha:
   ```
   java -jar poomismang.jar custom_database.db
   ```

## Mängu juhend
1. Valige kategooria rippmenüüst "Seaded" vahekaardil
2. Vajutage "Uus mäng" nupule mängu alustamiseks
3. Sisestage üks täht korraga mängulaua tekstiväljale
4. Vajutage "Saada" nupule või vajutage Enter tähe kontrollimiseks
5. Korrake, kuni olete sõna ära arvanud või võllapuu on valmis
6. Võidu korral palutakse teil sisestada oma nimi edetabelisse lisamiseks

## Rakenduse struktuur
Rakendus on loodud kasutades MVC (Model-View-Controller) disainimustrit:
- **Model**: Hoiab mängu andmeid ja loogikat
- **View**: Kuvab kasutajaliidese ja graafilised elemendid
- **Controller**: Töötleb kasutaja sisendeid ja uuendab mudelit

### Peamised klassid:
- **MainApp**: Rakenduse käivituspunkt
- **Model**: Mängu andmemudel
- **View**: Põhiline graafiline kasutajaliides
- **Controller**: Kasutaja sisendi haldamine
- **Database**: Andmebaasi ühendused ja päringud
- **GameBoard**: Mängulaua kuvamine
- **Settings**: Seadete kuva
- **LeaderBoard**: Edetabeli kuva
- **ButtonSend**: "Saada" nupu funktsionaalsus
- **ButtonNew**: "Uus mäng" nupu funktsionaalsus
- **ButtonCancel**: "Katkesta" nupu funktsionaalsus

## Andmebaasi struktuur
Rakendus kasutab SQLite andmebaasi, mis sisaldab kahte tabelit:
1. **words** - Sõnad ja nende kategooriad:
    - id (INTEGER) - unikaalne identifikaator
    - word (TEXT) - äraarvatav sõna
    - category (TEXT) - sõna kategooria

2. **scores** - Edetabeli kirjed:
    - playertime (TEXT) - mängu lõppemise aeg
    - playername (TEXT) - mängija nimi
    - guessword (TEXT) - äraarvatav sõna
    - wrongcharacters (TEXT) - valesti sisestatud tähed
    - gametime (INTEGER) - mängu kestus sekundites

## Tehnoloogiad
- Java Swing - kasutajaliidese loomiseks
- JDBC - andmebaasi ühendused
- SQLite - andmete salvestamine

## Autorid
Poomismängu põhiversiooni autor: [Marko Livental]
Täiendatud versioon: [Artjom Eller]