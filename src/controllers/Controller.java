package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ButtonSend;
import listeners.ComboboxChange;
import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public Controller(Model model, View view) {
        // Comboboxi funktsionaalsus
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model, view));
        // Uus mäng funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // Katkestamis nupp tööle
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        // Send nupp tööle
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));

        // Edetabel nupp tööle
        view.getSettings().getBtnLeaderboard().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Värskenda edetabelit enne näitamist
                view.getLeaderBoard().updateScoresTable();
                // Vaheta vahelehte edetabeli peale
                view.getTabbedPane().setSelectedIndex(2);
            }
        });
    }
}