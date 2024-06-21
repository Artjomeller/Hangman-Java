package helpers;

import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RealTimer implements ActionListener {
    /**
     * Vaade mis on loodud View-s
     */
    private final View view;
    /**
     * Reaalne aeg. NB! java.swing.timer! Vaata ka GameTimer
     */
    private final Timer timer;

    public RealTimer(View view) {
        this.view = view;
        timer = new Timer(1000, this);
    }

    /**
     * ActionListener kohustuslik override meetod
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        show();
    }
    /**
     * Käivitab aja näitamise
     */
    public void start() {
        timer.start();
    }

    /**
     * Peatab aja näitamise
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Näitab jooksvat aega
     */
    public void show() {
        String strTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        view.getSettings().getLblRealTime().setText(strTime); // Settings paneeli aja label
    }

    public String getDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}