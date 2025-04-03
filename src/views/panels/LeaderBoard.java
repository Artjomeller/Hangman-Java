package views.panels;

import models.Database;
import models.Model;
import models.datastructures.DataScore;
import views.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * See on edetabeli klass. See näitab andmebaasist loetud edetabelit. Seda ei saa mängimise ajal
 * vaadata.
 */
public class LeaderBoard extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * Klassisisene vaade, mille väärtus saadakse otse View-st
     */
    private final View view;
    /**
     * Tabeli päis mida näeb Edetabeli vahelehel
     */
    private final String[] heading = new String[]{"Kuupäev", "Nimi", "Sõna", "Tähed", "Mänguaeg"};
    /**
     * Loome tabeli teostuse päisega kuid andmeid pole
     */
    private final DefaultTableModel dtm = new DefaultTableModel(heading, 0);

    /**
     * Loome tabeli dtm baasil
     */
    private final JTable table = new JTable(dtm);

    /**
     * Leaderboard kontruktor
     * @param model loodud mudel MainAppis
     * @param view loodud view MainAppis
     */

    public LeaderBoard(Model model, View view) {
        this.model = model;
        this.view = view;

        setLayout(new BorderLayout());
        setBackground(new Color(250, 150, 215)); // Leaderboard paneeli taustavärv
        setBorder(new EmptyBorder(5, 5, 5, 5));

        model.setDtm(dtm);

        createLeaderboard(); // Loob edetabeli tabeli paneelile

    }

    public void updateScoresTable() {
        DefaultTableModel dtm = model.getDtm();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        new Database(model).selectScores();

        for (DataScore ds : model.getDataScores()) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String name = ds.playerName();
            String word = ds.word().toUpperCase();
            // Muudame valesti sisestatud tähed suurtähtedeks ja kuvame komadega eraldatult
            String rawChars = ds.missedChars().toUpperCase();
            // Eemaldame võimalikud kantsulud ja eraldame tähed komadega
            String chars = rawChars.replace("[", "").replace("]", "").replace(" ", ", ");
            String humanTime = convertSecToMMSS(ds.timeSeconds());

            boolean found = false;
            for (int row = 0; row < dtm.getRowCount(); row++) {
                if (dtm.getValueAt(row, 1).equals(name)) {
                    dtm.setValueAt(gameTime, row, 0);
                    dtm.setValueAt(word, row, 2);
                    dtm.setValueAt(chars, row, 3);
                    dtm.setValueAt(humanTime, row, 4);
                    found = true;
                    break;
                }
            }

            if (!found) {
                dtm.addRow(new Object[]{gameTime, name, word, chars, humanTime});
            }
        }
    }

    private String convertSecToMMSS(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void createLeaderboard() {
        // Kerimisriba vasakul servas, kui vaja
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Tabeli esimene veerg 100 px
        table.getColumnModel().getColumn(0).setPreferredWidth(120);

        // Tabeli sisu pole muudetav
        table.setDefaultEditor(Object.class, null);

        // Lahtri keskele joondamine
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);

        // Kirjuta tabelist sisu mudelisse
        new Database(model).selectScores();
        // Kontrolli kas on andmeid ja uuenda tabelit
        if(!model.getDataScores().isEmpty()) { // Kui list pole tühi
            updateScoresTable();
        } else {
            JOptionPane.showMessageDialog(view, "Esmalt tuleb mängida!");
        }
    }

}