package views;

import helpers.GameTimer;
import helpers.RealTimer;
import models.Model;
import models.datastructures.DataScore;
import views.panels.GameBoard;
import views.panels.LeaderBoard;
import views.panels.Settings;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * See on põhivaade ehk JFrame kuhu peale pannakse kõik muud JComponendid mida on mänguks vaja.
 * JFrame vaikimisi (default) aknahaldur (Layout Manager) on BorderLayout
 */
public class View extends JFrame {
    /**
     * Klassisisene, mille väärtus saadakse VIew konstruktorist ja loodud MainApp-is
     */
    private Model model;
    /**
     * Vaheleht (TAB) Seaded ehk avaleht
     */
    private Settings settings;
    /**
     * Vaheleht (TAB) Mängulaud
     */
    private GameBoard gameBoard;
    /**
     * Vaheleht (TAB) Edetabel
     */
    private LeaderBoard leaderBoard;
    /**
     * Sellele paneelile tulevad kolm eelnevalt loodud vahelehte (Settings, GameBoard ja LeaderBoard)
     */
    private JTabbedPane tabbedPane;

    // TODO RealTimer ka
    private GameTimer gameTimer;
    private RealTimer realTimer;

    /**
     * View konstruktor. Põhiakna (JFrame) loomine ja sinna paneelide (JPanel) lisamine ja JComponendid
     * @param model mudel mis loodi MainApp-is
     */
    public View(Model model) {
        this.model = model; // MainApp-is loodud mudel

        setTitle("Poomismäng 2024 õpilased"); // JFrame titelriba tekst
        setPreferredSize(new Dimension(500, 250));
        // TODO arenduse lõpus keela akna suurendamine
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
        tabbedPane.addTab("Edetabel", leaderBoard); // Vaheleht Mängulaud paneeliga gameBoard

        // TODO arenduse lõpus mängulaua vahelehte klikkida ei saa
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

    public RealTimer getRealTimer() { return realTimer; }

    public String formatGuessedWord(String word) {
        StringBuilder spacedWord = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            spacedWord.append(word.charAt(i));
            if (i < word.length() - 1) {
                spacedWord.append(" ");
            }
        }

        return spacedWord.toString();
    }

    public void updateLblResult(String character) {
        if (character == null) {
            String kriipsud = "";
            for (int i = 0; i < model.getWord().length(); i++) {
                kriipsud += "_";
            }
            model.setGuessedWord(kriipsud);
            gameBoard.getLblResult().setText(formatGuessedWord(kriipsud));
        } else {
            model.updateGuessedWord(character);
            gameBoard.getLblResult().setText(formatGuessedWord(model.getGuessedWord()));
        }
    }

    public void updateLblImage(int mistakes) {
        List<String> imageIcons = model.getImageFiles();

        if (mistakes >= 0 && mistakes < imageIcons.size()) {
            ImageIcon newIcon = new ImageIcon(imageIcons.get(mistakes));
            gameBoard.getLblImage().setIcon(newIcon);
        } else {
            gameBoard.getLblImage().setIcon(new ImageIcon());
        }
    }

}