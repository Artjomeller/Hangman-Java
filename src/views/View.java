package views;

import helpers.GameTimer;
import helpers.RealTimer;
import models.Model;
import views.panels.GameBoard;
import views.panels.LeaderBoard;
import views.panels.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * See on põhivaade ehk JFrame kuhu peale pannakse kõik muud JComponendid mida on mänguks vaja.
 * JFrame vaikimisi (default) aknahaldur (Layout Manager) on BorderLayout
 */
public class View extends JFrame {
    /**
     * Klassisisene, mille väärtus saadakse VIew konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * Vaheleht (TAB) Seaded ehk avaleht
     */
    private final Settings settings;
    /**
     * Vaheleht (TAB) Mängulaud
     */
    private final GameBoard gameBoard;
    /**
     * Vaheleht (TAB) Edetabel
     */
    private final LeaderBoard leaderBoard;
    /**
     * Sellele paneelile tulevad kolm eelnevalt loodud vahelehte (Settings, GameBoard ja LeaderBoard)
     */
    private JTabbedPane tabbedPane;

    private final GameTimer gameTimer;
    private final RealTimer realTimer;

    /**
     * View konstruktor. Põhiakna (JFrame) loomine ja sinna paneelide (JPanel) lisamine ja JComponendid
     * @param model mudel mis loodi MainApp-is
     */
    public View(Model model) {
        this.model = model; // MainApp-is loodud mudel

        setTitle("Poomismäng 2024 õpilased"); // JFrame titelriba tekst
        setPreferredSize(new Dimension(500, 250));
        setResizable(false);
        getContentPane().setBackground(new Color(250,210,205)); // JFrame taustavärv (rõõsa)

        // Loome kolm vahelehte (JPanel)
        settings = new Settings(model);
        gameBoard = new GameBoard(model);
        leaderBoard = new LeaderBoard(model, this);

        createTabbedPanel(); // Loome kolme vahelehega tabbedPaneli

        add(tabbedPane, BorderLayout.CENTER); // Paneme tabbedPaneli JFramele. JFrame layout on default BorderLayout

        // Loome mänguaja objekti
        gameTimer = new GameTimer(this);
        // Loome ja käivitame päris aja
        realTimer = new RealTimer(this);
        realTimer.start();
    }

    private void createTabbedPanel() {
        tabbedPane = new JTabbedPane(); // Tabbed paneli loomine

        tabbedPane.addTab("Seaded", settings); // Vaheleht Seaded paneeliga settings
        tabbedPane.addTab("Mängulaud", gameBoard); // Vaheleht Mängulaud paneeliga gameBoard
        tabbedPane.addTab("Edetabel", leaderBoard); // Vaheleht Edetabel paneeliga leaderBoard

        tabbedPane.setEnabledAt(1, false); // Vahelehte mängulaud ei saa klikkida
    }

    /**
     * Meetod mis tekitab mängimise olukorra.
     */
    public void hideButtons() {
        tabbedPane.setEnabledAt(0, false); // Keela seaded vaheleht
        tabbedPane.setEnabledAt(2, false); // Keela edetabel vaheleht
        tabbedPane.setEnabledAt(1, true); // Luba mängulaud vaheleht
        tabbedPane.setSelectedIndex(1); // Tee mängulaud vaheleht aktiivseks

        gameBoard.getBtnSend().setEnabled(true); // Nupp Saada on klikitav
        gameBoard.getBtnCancel().setEnabled(true); // Nupp Katkesta on klikitav
        gameBoard.getTxtChar().setEnabled(true); // Sisestuskast on aktiivne
    }

    /**
     * Meetod mis tekitab mitte mängimise olukorra. Vastupidi hideButtons() meetodil
     */
    public void showButtons() {
        tabbedPane.setEnabledAt(0, true); // Luba seaded vaheleht
        tabbedPane.setEnabledAt(2, true); // Luba edetabel vaheleht
        tabbedPane.setEnabledAt(1, false); // Keela mängulaud vaheleht
        // tabbedPane.setSelectedIndex(0); // Tee seaded vaheleht aktiivseks. Peale mängu pole see hea, sest ei näe lõppseisus

        gameBoard.getBtnSend().setEnabled(false); // Nupp Saada ei ole klikitav
        gameBoard.getBtnCancel().setEnabled(false); // Nupp Katkesta ei ole klikitav
        gameBoard.getTxtChar().setEnabled(false); // Sisestuskast ei ole aktiivne
        gameBoard.getTxtChar().setText(""); // Tee sisestuskast tühjaks
    }

    /**
     * Vahetab vahekaardi edetabeli peale ja uuendab edetabeli sisu
     */
    public void showLeaderboard() {
        // Uuenda edetabelit enne kuvamist
        leaderBoard.updateScoresTable();

        // Keela mängulaua vaheleht, kui see on lubatud
        if (tabbedPane.isEnabledAt(1)) {
            tabbedPane.setEnabledAt(1, false);
        }

        // Luba seaded ja edetabel vahekaardid
        tabbedPane.setEnabledAt(0, true);
        tabbedPane.setEnabledAt(2, true);

        // Vaheta aktiivne vahekaart edetabeli peale
        tabbedPane.setSelectedIndex(2);
    }

    /**
     * Tagastab tabbedPane komponendi
     * @return JTabbedPane komponent
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    // GETTERID Paneelide (vahelehetede)
    public Settings getSettings() {
        return settings;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Mänguaja objekt .stop() .setRunning() jne
     * @return mänguaja objekti
     */
    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public RealTimer getRealTimer() {
        return realTimer;
    }

    /**
     * Vormindab äraarvatava sõna kasutajale näitamiseks, lisades tähemärkide vahele tühikud
     *
     * @param word äraarvatav sõna (koos allkriipsudega)
     * @return vormindatud sõna, kus tähemärkide vahel on tühikud
     */
    public String formatGuessedWord(String word) {
        StringBuilder spacedWord = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            // Kuvame tähed suurtähtedena vastavalt tagasisidele
            spacedWord.append(Character.toUpperCase(word.charAt(i)));
            if (i < word.length() - 1) {
                spacedWord.append(" ");
            }
        }

        return spacedWord.toString();
    }

    /**
     * Uuendab äraarvatava sõna kuvamist. Kui character on null, siis näitab ainult allkriipse.
     * Kui character on määratud, siis uuendab guessedWord ja näitab seda.
     *
     * @param character täht, mida kasutaja sisestas või null uue mängu alustamisel
     */
    public void updateLblResult(String character) {
        if (character == null) {
            // Uue mängu alguses näita ainult allkriipse
            StringBuilder kriipsud = new StringBuilder();
            kriipsud.append("_".repeat(model.getWord().length()));
            model.setGuessedWord(kriipsud.toString());
            gameBoard.getLblResult().setText(formatGuessedWord(kriipsud.toString()));
        } else {
            // Sõna äraarvasmise ajal, uuenda näidatavat sõna
            model.updateGuessedWord(character);
            // Kasutame eraldi formateeritud sõna kuvamiseks
            gameBoard.getLblResult().setText(formatGuessedWord(model.getGuessedWord()));
        }
    }

    /**
     * Uuendab võllapuu pilti vastavalt vigade arvule
     *
     * @param mistakes vigade arv
     */
    public void updateLblImage(int mistakes) {
        List<String> imageIcons = model.getImageFiles();

        if (mistakes >= 0 && mistakes < imageIcons.size()) {
            ImageIcon newIcon = new ImageIcon(imageIcons.get(mistakes));
            gameBoard.getLblImage().setIcon(newIcon);
        } else {
            System.out.println("Vigade arv on väljaspool pildi indeksite vahemikku: " + mistakes);
            gameBoard.getLblImage().setIcon(new ImageIcon());
        }
    }
}