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
    private final String[] heading = new String[]{"Koht", "Kuupäev", "Nimi", "Sõna", "Tähed", "Aeg"};
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

        int koht = 1; // Järjekorranumber (koht edetabelis)
        for (DataScore ds : model.getDataScores()) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String name = ds.playerName();
            String word = ds.word().toUpperCase();
            String rawChars = ds.missedChars().toUpperCase();
            String chars = rawChars.replace("[", "").replace("]", "").replace(" ", ", ");
            String humanTime = convertSecToMMSS(ds.timeSeconds());

            boolean found = false;
            for (int row = 0; row < dtm.getRowCount(); row++) {
                if (dtm.getValueAt(row, 2).equals(name)) {
                    dtm.setValueAt(gameTime, row, 1);
                    dtm.setValueAt(word, row, 3);
                    dtm.setValueAt(chars, row, 4);
                    dtm.setValueAt(humanTime, row, 5);
                    found = true;
                    break;
                }
            }

            if (!found) {
                dtm.addRow(new Object[]{koht, gameTime, name, word, chars, humanTime});
                koht++; // Suurendame järjekorranumbrit uue rea lisamisel
            }
        }
    }

    private String convertSecToMMSS(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void createLeaderboard() {
        // Tabeli automaatne laiuse kohandamine on vaikimisi lubatud
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Kerimisriba vasakul servas, kui vaja
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Mõõdame täpse laiuse, mida on vaja kuupäeva jaoks, kasutades vastavat fonti
        FontMetrics fm = table.getFontMetrics(table.getFont());
        // Näidiskuupäev, mis on tavaliselt kõige pikem võimalik
        String sampleDate = "99.99.9999 99:99:99";
        int dateWidth = fm.stringWidth(sampleDate) + 10; // Lisa natuke puhvrit

        // Määrame eelistatud laiused vastavalt sinu soovidele
        // Koht - väike laius
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        // Kuupäev - täpselt õige laius, et näidata kogu kuupäeva
        table.getColumnModel().getColumn(1).setPreferredWidth(dateWidth);
        table.getColumnModel().getColumn(1).setMinWidth(dateWidth);
        table.getColumnModel().getColumn(1).setMaxWidth(dateWidth);

        // Nimi - suurem laius, et mahutada pikemad nimed
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(100);

        // Sõna - suurem laius, et mahutada pikemad sõnad
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(100);

        // Tähed - suurem laius, et mahutada rohkem tähti
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setMinWidth(55);

        // Aeg - väiksem laius, kuna formaat on lühike (mm:ss)
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setMinWidth(40);
        table.getColumnModel().getColumn(5).setMaxWidth(60);

        // Lubame tabeli veergude laiuse muutmist lohistamisega
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(true);

        // Tabeli sisu pole muudetav
        table.setDefaultEditor(Object.class, null);

        // Lahtrite joondamine
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Koht
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Kuupäev
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Aeg

        // Tekstiveergude sisu vasaku serva joondamine
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer); // Nimi
        table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer); // Sõna
        table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer); // Tähed

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