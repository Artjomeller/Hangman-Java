package listeners;

import models.Database;
import models.Model;
import views.View;
import views.panels.GameBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ButtonNew implements ActionListener {
    private Model model;
    private View view;

    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.hideButtons();
        if(!view.getGameTimer().isRunning()) {  //Mängu aeg ei jookse
            view.getGameTimer().setSeconds(0); // Sek nullida
            view.getGameTimer().setMinutes(0); // Min nullida
            view.getGameTimer().setRunning(true); // aeg jooksma
            view.getGameTimer().startTime(); // Käivita aeg
        } else {
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);
        }
        // TODO Siit jätkub õpilaste arendus

        String category = model.getSelectedCategory();
        new Database(model).setWordByCategory(category);
        view.updateLblResult(null);
    }

}