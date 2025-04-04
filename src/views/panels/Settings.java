package views.panels;

import models.Database;
import models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * See on vaheleht Seaded paneel ehk avaleht. Siit saab valida mängu jaoks sõna kategooria ja käivitada mängu. See on
 * üks kolmest vahelehest (esimene). JPanel vaikimisi (default) aknahaldur (Layout Manager) on FlowLayout.
 */
public class Settings extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * GridBagLayout jaoks JComponent paigutamiseks "Excel" variandis
     */
    private final GridBagConstraints gbc = new GridBagConstraints();
    /**
     * See silt (JLabel) näitab reaalset kuupäeva ja jooksvat kellaaega
     */
    private JLabel lblRealTime;
    /**
     * Sisaldab äraarvatava sõna kategooriat (andmebaasist). Algul "Kõik kategooriad"
     */
    private JComboBox<String> cmbCategory;
    /**
     * Uue mängu alustamise nupp
     */
    private JButton btnNewGame;
    /**
     * Suunab vahelehele Edetabel
     */
    private JButton btnLeaderboard;
    /**
     * Silt, mis näitab valitud andmebaasi faili
     */
    private JLabel lblDatabaseFile;
    /**
     * Lipp, mis näitab, kas andmebaasi vahetus on käimas
     */
    private boolean isChangingDatabase = false;

    /**
     * Settings JPanel konstruktor
     * @param model mudel mis loodud MainApp-is
     */
    public Settings(Model model) {
        this.model = model;

        // Seadistame Settings paneeli
        setLayout(new GridBagLayout()); // Kasutame GridBagLayout tsentreerimiseks
        setBackground(new Color(255,250,200)); // JSettings paneeli taustavärv

        gbc.fill = GridBagConstraints.HORIZONTAL; // Täidab lahtri horisontaalselt (kõik lahtrid on "sama laiad")
        gbc.insets = new Insets(2,2,2,2); // Iga lahtri ümber 2px tühja ruumi

        JPanel components = new JPanel(new GridBagLayout()); // Siia pannakse kõik komponendid settings paneeli omad
        components.setBackground(new Color(140,185,250)); // Komponentide paneeli tausta värv

        // Lisame natuke äärist, et oleks visuaalselt parem
        components.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /*
         Kuna components panel on Settings konstruktoris loodud ei saa ma seda paneeli mujal kasutada, kui annan
         argumendina kaasa JPaneli meetodile ja saan teises meetodis seda sama paneeli kasutada. Siia on vaja ju
         komponendid peale panna
         */
        setupUIComponents(components);

        // Paigutame components paneeli settings paneeli keskele
        GridBagConstraints centerConstraints = new GridBagConstraints();
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 0;
        centerConstraints.weightx = 1.0;
        centerConstraints.weighty = 1.0;
        centerConstraints.fill = GridBagConstraints.NONE; // Ei venita komponenti
        centerConstraints.anchor = GridBagConstraints.CENTER; // Kinnitame keskele

        add(components, centerConstraints);
    }

    /**
     * Meetod mis loob kõik komponendid settings paneelile
     * @param components paneel kuhu komponendid paigutada
     */
    private void setupUIComponents(JPanel components) {
        // Esimene rida üle kahhe veeru kuupäev ja kellaaja JLabel
        lblRealTime = new JLabel("Siia tuleb reaalne aeg", JLabel.CENTER);
        gbc.gridx = 0; // Esimene veerg (column)
        gbc.gridy = 0; // Esimene rida (row)
        gbc.gridwidth = 2; // Pane kaks veergu kokku (merge)
        components.add(lblRealTime, gbc); // Pane objekt paneelile

        // Andmebaasi valik
        JLabel lblDatabase = new JLabel("Andmebaas");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Muuda tagasi üks komponent veergu
        components.add(lblDatabase, gbc);

        // Andmebaasi valikute loomine
        String[] databases = {
                "hangman_words_ee.db", // Eesti keele sõnad, tühi edetabel
                "hangman_words_ee_test.db", // Eesti keele sõnad, täidetud edetabel
                "hangman_words_en.db" // Ingliskeelsed sõnad
        };

        JComboBox<String> cmbDatabase = new JComboBox<>(databases);
        cmbDatabase.setSelectedItem(model.getDatabaseFile()); // Vaikimisi valitud andmebaas
        gbc.gridx = 1;
        gbc.gridy = 1;
        components.add(cmbDatabase, gbc);

        // Lisa kuular andmebaasi valikule
        cmbDatabase.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedDB = (String) e.getItem();
                // Kui sama andmebaas, ära tee midagi
                if (selectedDB.equals(model.getDatabaseFile())) {
                    return;
                }

                // Märgime, et toimub andmebaasi vahetus
                isChangingDatabase = true;

                model.setDatabaseFile(selectedDB);
                // Loome uue andmebaasi ühenduse uue faili jaoks
                new Database(model);

                // Värskendame kategooriaid vaikselt
                model.setSelectedCategory(model.getChooseCategory());
                updateCategoryComboBoxSilently();

                // Näitame kasutajale teadet andmebaasi vahetuse kohta
                JOptionPane.showMessageDialog(this,
                        "Andmebaas on vahetatud: " + selectedDB,
                        "Andmebaasi vahetus",
                        JOptionPane.INFORMATION_MESSAGE);

                // Lõpetame andmebaasi vahetuse
                isChangingDatabase = false;
            }
        });

        // Kategooria valik (rida 2)
        JLabel lblCategory = new JLabel("Sõna kategooria");
        gbc.gridx = 0;
        gbc.gridy = 2;
        components.add(lblCategory, gbc);

        cmbCategory = new JComboBox<>(model.getCmbCategories());
        gbc.gridx = 1;
        gbc.gridy = 2;
        components.add(cmbCategory, gbc);

        // Andmebaasi failinimi
        JLabel lblFile = new JLabel(model.getDatabaseFile(), JLabel.CENTER);
        lblFile.setForeground(Color.RED);
        lblFile.setFont(new Font("Verdana", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Üle kahe veeru
        components.add(lblFile, gbc);

        // Loome muutuja klassi tasandil juurdepääsuks
        this.lblDatabaseFile = lblFile;

        // Nupud (rida 4)
        btnNewGame = new JButton("Uus mäng");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; //Üle ühe veeru
        components.add(btnNewGame, gbc);

        btnLeaderboard = new JButton("Edetabel");
        gbc.gridx = 1;
        gbc.gridy = 4;
        components.add(btnLeaderboard, gbc);
    }

    // Meetod kategooriate uuendamiseks ilma kuulareid käivitamata
    private void updateCategoryComboBoxSilently() {
        if (cmbCategory != null) {
            // Eemalda ajutiselt kõik kuularid
            ItemListener[] listeners = cmbCategory.getItemListeners();
            for (ItemListener listener : listeners) {
                cmbCategory.removeItemListener(listener);
            }

            // Salvesta praegune valik
            String currentSelection = (String) cmbCategory.getSelectedItem();

            // Tühjenda ja täida uuesti
            cmbCategory.removeAllItems();
            for (String category : model.getCmbCategories()) {
                cmbCategory.addItem(category);
            }

            // Vali kategooria ilma sündmusi käivitamata
            if (currentSelection != null && cmbCategory.getItemCount() > 0) {
                cmbCategory.setSelectedItem(currentSelection);
            } else if (cmbCategory.getItemCount() > 0) {
                cmbCategory.setSelectedIndex(0);
            }

            // Taasta kuularid
            for (ItemListener listener : listeners) {
                cmbCategory.addItemListener(listener);
            }

            // Uuenda ka kuvatud andmebaasi failinime
            if (lblDatabaseFile != null) {
                lblDatabaseFile.setText(model.getDatabaseFile());
            }
        }
    }

    /**
     * Kas andmebaasi vahetus on käimas
     * @return true kui andmebaasi vahetus on käimas
     */
    public boolean isChangingDatabase() {
        return isChangingDatabase;
    }

    // Komponentide getterid

    public JLabel getLblRealTime() {
        return lblRealTime;
    }

    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    public JButton getBtnNewGame() {
        return btnNewGame;
    }

    public JButton getBtnLeaderboard() {
        return btnLeaderboard;
    }
}