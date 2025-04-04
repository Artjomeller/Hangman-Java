package listeners;

import models.Model;
import views.View;
import views.panels.Settings;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ComboboxChange implements ItemListener {
    private final Model model;
    private final View view;

    public ComboboxChange(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // https://stackoverflow.com/questions/330590/why-is-itemstatechanged-on-jcombobox-is-called-twice-when-changed
        if(e.getStateChange() == ItemEvent.SELECTED) { // Without this check, two choices will occur in a row
            // Kontrolli, kas andmebaasi vahetus on käimas
            if (view.getSettings().isChangingDatabase()) {
                return; // Kui andmebaasi vahetus käib, ära näita teadet
            }

            String selectedCategory = e.getItem().toString();
            model.setSelectedCategory(selectedCategory); // Set selected category for next new game

            // Näita kategooria muutmise teadet
            JOptionPane.showMessageDialog(null,
                    "Valitud kategooria: " + selectedCategory,
                    "Kategooria valimine",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}