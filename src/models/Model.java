package models;

import models.datastructures.DataScore;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Model {
    private final String chooseCategory = "Kõik kategooriad";
    /**
     * See on vaikimisi andmebaasi fail kui käsurealt uut ei leotud. Andmebaasi tabelit nimed ja struktuurid peavad
     * samad olema, kuid andmed võivad erinevad olla.
     *  hangman_words_ee.db - Eestikeelsed sõnad, edetabel on tühi
     *  hangman_words_en.db - Inglisekeelsed sõnad, edetabel on tühi
     *  hangman_words_ee_test.db - Eestikeelsed sõnad, edetabel EI ole tühi
     */
    private String databaseFile = "hangman_words_ee_test.db";
    private String word = "";
    private String guessed_word = "";
    private int mistakes = 0;
    private final ArrayList<String> wrongLetters = new ArrayList<>();
    private String selectedCategory;// Vaikimisi valitud kategooria
    private String[] cmbCategories; // Rippmenüü sisu
    /**
     * Siia pannakse kõik pildid õiges järjekorras 0-12
     */
    private final List<String> imageFiles = new ArrayList<>();
    /**
     * Tabeli mugavaks kasutamiseks
     */
    private DefaultTableModel dtm;
    /**
     * Edetabeli andmed listis
     */
    private List<DataScore> dataScores = new ArrayList<>();


    public Model(String dbName) {
        if(dbName != null) {
            this.databaseFile = dbName; //käsurealt saadud andmebaas kasutusel
        }
        // System.out.println(this.databaseFile); TEST
        new Database(this);// Loome andmebaasi ühenduse
        readImagesFolder();  // Loeme võllapuu pildid mällu
        selectedCategory = chooseCategory; // Vaikimisi "Kõik kategooriad"
    }

    private void readImagesFolder() {
        String imagesFolder = "images";
        File folder = new File(imagesFolder); // Loo kausta objekt
        File[] files = folder.listFiles(); //Loe kõik failid File objekt listi
        for(File file : Objects.requireNonNull(files)){
            imageFiles.add(file.getAbsolutePath());
        }
        Collections.sort(imageFiles); //Sorteerib kasvavalt
        // System.out.println(imageFiles);
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getWord() {
        return word;
    }
    /**
     * Rippmenüü esimene valik enne kategooriaid
     * @return teksti "Kõik kategooriad"
     */

    public String getGuessedWord() { return guessed_word; }
    public void setGuessedWord(String guessedWord) { this.guessed_word = guessedWord; }

    /**
     * Uuendab ära arvatud sõna, asendades allkriipsud (_) kasutaja sisestatud tähega,
     * kui see täht esineb äraarvatavad sõnas.
     *
     * @param character kasutaja sisestatud täht
     */
    public void updateGuessedWord(String character) {
        // Kontrollime, kas character või word on null või tühjad
        if (character == null || character.isEmpty() || word == null || word.isEmpty()) {
            return;
        }

        // Teisendame tähe väiketäheks, et tagada korrektne võrdlemine
        char charToCheck = character.toLowerCase().charAt(0);

        // Teisendame sõnad char massiivideks töötlemise lihtsustamiseks
        char[] wordChars = word.toCharArray();
        char[] guessedChars = guessed_word.toCharArray();

        // Läbime sõna ja asendame allkriipsud, kui täht vastab
        boolean found = false;
        for (int i = 0; i < wordChars.length; i++) {
            if (wordChars[i] == charToCheck) {
                guessedChars[i] = charToCheck;
                found = true;
            }
        }

        // Uuendame ära arvatud sõna ainult siis, kui täht leiti
        if (found) {
            guessed_word = new String(guessedChars);
        }
    }

    public int getMistakes () { return this.mistakes; }
    public void setMistakes (int mistakes) { this.mistakes = mistakes; }
    public void addLetter(String character) { this.wrongLetters.add(character); }
    public void clearLetters() { this.wrongLetters.clear(); }
    public ArrayList<String> getWrongLetters () { return this.wrongLetters; }
    public String getChooseCategory() {
        return chooseCategory;
    }

    /**
     * Millise andmebaasiga on tegemist
     * @return andmebaasi failinimi
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Seadistab uue andmebaasi failinime
     * @param databaseFile uus andmebaasi failinimi
     */
    public void setDatabaseFile(String databaseFile) {
        // Kui sama, siis pole vaja muuta
        if (this.databaseFile.equals(databaseFile)) {
            return;
        }

        System.out.println("Vahetame andmebaasi: " + this.databaseFile + " -> " + databaseFile);
        this.databaseFile = databaseFile;

        // Tühjendame edetabeli andmed, et vältida segadust
        if (this.dataScores != null) {
            this.dataScores.clear();
        }

        // Tühjendame tabeli, kui see on olemas
        if (this.dtm != null) {
            while (this.dtm.getRowCount() > 0) {
                this.dtm.removeRow(0);
            }
        }
    }

    /**
     * Valitud kategoori
     * @return tagastab valitud kategooria
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Seadistab valitud kategooria
     * @param selectedCategory uus valitud kategooria
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Kategooriate nimed
     * @return kategooriate nimed
     */
    public String[] getCmbCategories() {
        return cmbCategories;
    }

    /**
     * Seadistab uued kategooriate nimed
     * @param cmbCategories kategooriate massiiiv
     */
    public void setCmbCategories(String[] cmbCategories) {
        this.cmbCategories = cmbCategories;
    }

    /**
     * Võllapuu pildid
     * @return võllapuu pildid listina list<String>
     */
    public List<String> getImageFiles() {
        return imageFiles;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public List<DataScore> getDataScores() {
        return dataScores;
    }

    public void setDataScores(List<DataScore> dataScores) {
        this.dataScores = dataScores;
    }
}