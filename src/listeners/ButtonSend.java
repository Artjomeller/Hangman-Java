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

    private void victory() {
        String currentDate = view.getRealTimer().getDate();
        String playerName = JOptionPane.showInputDialog(null, "Palun siseta teie nimi:", "Palju õnne, sa võitsid!", JOptionPane.QUESTION_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            System.out.println("Mänguja nimi: " + playerName);
        } else {
            playerName = "Tundmatu"; // Kui nimi jäeti tühjaks või tühistati
            System.out.println("Kasutaja ei sisestanud nime, kasutame 'Tundmatu'");
        }

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
            JTabbedPane tabbedPane = (JTabbedPane) view.getGameBoard().getParent();
            tabbedPane.setEnabledAt(0, true); // Luba seaded vaheleht
            tabbedPane.setEnabledAt(2, true); // Luba edetabel vaheleht
        }

        // Uuenda edetabelit
        view.getLeaderBoard().updateScoresTable();
    }
}
