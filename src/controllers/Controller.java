package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ButtonSend;
import listeners.ComboboxChange;
import models.Model;
import views.View;

public class Controller {
    public Controller(Model model, View view) {

        // Comboboxi funktsionaalsus
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        // Uus mäng funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // Katkestamis nupp tööle
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        // Send nupp tööle
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));

    }
}