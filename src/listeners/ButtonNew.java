package listeners;

import models.Database;
import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
    private final Model model;
    private final View view;

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

        // Võta valitud kategooria mudelilt
        String category = model.getSelectedCategory();

        // Võta andmebaasist uus juhuslik sõna vastavalt kategooriale
        new Database(model).setWordByCategory(category);

        // Lähtesta valede tähtede nimekiri
        model.clearLetters();

        // Nulli vigade arv
        model.setMistakes(0);

        // Määra võllapuu pildiks indeksiga 0 olev pilt
        view.updateLblImage(0);

        // Näita kasutajale allkriipsudega sõna pikkust
        view.updateLblResult(null);

        // Fookus tekstikasti
        view.getGameBoard().getTxtChar().requestFocusInWindow();
    }

}