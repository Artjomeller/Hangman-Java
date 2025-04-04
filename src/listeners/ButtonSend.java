package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ButtonSend implements ActionListener {
    private final Model model;
    private final View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
        addKeyListenerToTextField();
    }

    private void addKeyListenerToTextField() {
        view.getGameBoard().getTxtChar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        });
    }

    private void victory() {
        String currentDate = view.getRealTimer().getDate();

        // Küsime mängija nime korduva tsükliga, kuni sisestatakse sobiv nimi
        String playerName = null;
        boolean validName = false;

        while (!validName) {
            // Küsime kasutajalt nime
            playerName = JOptionPane.showInputDialog(null,
                    "Palun sisesta oma nimi:",
                    "Palju õnne, sa võitsid!",
                    JOptionPane.QUESTION_MESSAGE);

            // Kui kasutaja vajutas Cancel või sulges akna
            if (playerName == null) {
                playerName = "Tundmatu";
                validName = true;
            }
            // Kui nimi on tühi
            else if (playerName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Nime väli ei tohi olla tühi. Palun sisesta nimi.",
                        "Viga",
                        JOptionPane.ERROR_MESSAGE);
            }
            // Kontrollime, kas nimi sisaldab ainult tähti ja tühikuid
            else if (!playerName.matches("^[a-zA-ZõäöüÕÄÖÜšžŠŽ ]+$")) {
                JOptionPane.showMessageDialog(null,
                        "Nimi võib sisaldada ainult tähti. Palun sisesta ainult tähtedest koosnev nimi.",
                        "Viga",
                        JOptionPane.ERROR_MESSAGE);
            }
            // Nimi on korrektne
            else {
                validName = true;
            }
        }

        System.out.println("Mängija nimi: " + playerName);

        String playedTime = String.valueOf(view.getGameTimer().getPlayedTimeInSeconds());
        // Eemaldame nurksulud valede tähtede salvestamisel
        String wrongLetters = String.join(" ", model.getWrongLetters());

        // Salvestame tulemuse andmebaasi
        new Database(model).sendDataToTable(currentDate, playerName, model.getWord(), wrongLetters, playedTime);
    }

    private void defeat() {
        JOptionPane.showMessageDialog(null, "Sa kaotasid. Õige sõna oli: " + model.getWord());
    }

    private void endGame(boolean resetView) {
        // Peata aeg
        view.getGameTimer().setRunning(false);
        view.getGameTimer().stopTime();

        // Kui soovime UI lähtestada
        if (resetView) {
            view.getGameBoard().clearGameBoard();
            view.showButtons();
        } else {
            // Jätame mängulaua nähtavaks, aga keelame nupud
            view.getGameBoard().getBtnSend().setEnabled(false);
            view.getGameBoard().getBtnCancel().setEnabled(false);
            view.getGameBoard().getTxtChar().setEnabled(false);

            // Lubame teised vahekaardid
            view.getTabbedPane().setEnabledAt(0, true); // Luba seaded vaheleht
            view.getTabbedPane().setEnabledAt(2, true); // Luba edetabel vaheleht
        }

        // Uuenda edetabelit
        view.getLeaderBoard().updateScoresTable();

        // Küsi kasutajalt, kas ta soovib näha edetabelit
        int option = JOptionPane.showConfirmDialog(
                null,
                "Kas soovid näha edetabelit?",
                "Mäng lõppes",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            view.showLeaderboard();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String character = view.getGameBoard().getTxtChar().getText().toLowerCase();
        if (character.isEmpty()) {
            view.getGameBoard().getLblError().setText("Palun siseta üks täht");
        } else {
            if (model.getWord().contains(character)) {
                view.updateLblResult(character);
            } else {
                model.setMistakes(model.getMistakes() + 1);
                model.addLetter(character);
                view.updateLblImage(model.getMistakes());
                // Eemaldame nurksulud valede tähtede kuvamisel
                view.getGameBoard().getLblError().setText("Vigased tähed: " + String.join(" ", model.getWrongLetters()));
            }
        }

        // Kontrolli kas mäng on läbi
        if (model.getWord().equals(model.getGuessedWord())) {
            // Võit - sõna arvati ära
            victory();
            // Ära vii tagasi algusolekusse, et kasutaja näeks lahendust
            endGame(false);
        } else if (model.getMistakes() >= 11) { // Muudame > asendades >= (vastavalt tagasisidele)
            // Kaotus - võllapuu valmis
            defeat();
            endGame(true);
        } else {
            // Mäng jätkub, tühjendame tekstikasti
            view.getGameBoard().getTxtChar().setText("");
            view.getGameBoard().getTxtChar().requestFocusInWindow();
        }
    }
}