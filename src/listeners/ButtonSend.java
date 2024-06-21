package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSend implements ActionListener {
    private Model model;
    private View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }


    private void victory () {
        String currentDate = view.getRealTimer().getDate();
        String playerName = JOptionPane.showInputDialog(null, "Palun siseta teie nimi:", "Palju õnne, sa võitsid!", JOptionPane.QUESTION_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            System.out.println("Mänguja nimi: " + playerName);
        } else {
            System.out.println("Palun kirjuta midagi.");
        }
        String playedTime = String.valueOf(view.getGameTimer().getPlayedTimeInSeconds());
        new Database(model).sendDataToTable(currentDate, playerName, model.getWord(), String.valueOf(model.getWrongLetters()), playedTime);

    }

    private void defeat () {
        JOptionPane.showMessageDialog(null, "Sa kaotasid.");
    }
    private void endGame() {
        view.showButtons();
        view.getGameTimer().setRunning(false);
        view.getGameTimer().stopTime();
        view.getGameBoard().clearGameBoard();
        view.getLeaderBoard().updateScoresTable();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String character = view.getGameBoard().getTxtChar().getText();
        if (character.isEmpty()) {
            view.getGameBoard().getLblError().setText("Palun siseta üks täht");
        } else {
            if (model.getWord().contains(character)) {
                view.updateLblResult(character);
            } else {
                model.setMistakes(model.getMistakes() + 1);
                model.addLetter(character);
                view.updateLblImage(model.getMistakes());
                view.getGameBoard().getLblError().setText("Vigased tähed: " + model.getWrongLetters());
            }
        }
        if (model.getWord().equals(model.getGuessedWord())) {
            victory();
            endGame();
        }
        if (model.getMistakes() > 11) {
            defeat();
            endGame();
        }
        view.getGameBoard().getTxtChar().setText("");
        view.getGameBoard().getTxtChar().requestFocusInWindow();

        System.out.println(model.getWord());
    }
}