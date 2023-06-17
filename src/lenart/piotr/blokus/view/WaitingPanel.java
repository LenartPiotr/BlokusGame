package lenart.piotr.blokus.view;

import lenart.piotr.blokus.engine.client.IClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel label1;
    private JPanel listPanel;
    private final IClient client;
    private int maxUsers;
    private int users;

    private String[] nicks;

    public WaitingPanel(IClient client) {
        this.client = client;
        setupClientCallbacks();

        users = 0;
        maxUsers = 0;

        client.invoke("getMaxPlayersCount", 0);
        client.invoke("getPlayersCount", 0);

        add(mainPanel);

        label1.setText("waiting for host...");
        refillMainLabel();
    }

    private void setupClientCallbacks() {
        client.on("setMaxPlayersCount", data -> {
            maxUsers = (int) data;
            refillMainLabel();
        });
        client.on("setPlayersCount", data -> {
            users = (int) data;
            refillMainLabel();
            client.invoke("getPlayersNicks", 0);
        });
        client.on("setPlayersNicks", data -> {
            nicks = (String[]) data;
            refillClientsList();
        });
    }

    private void refillMainLabel() {
        label1.setText(users + " / " + maxUsers + " users");
    }

    private void refillClientsList() {
        listPanel.removeAll();
        listPanel.setLayout(new GridLayout());
        for (int i = 0; i < users; i++) {
            JLabel label = new JLabel();
            label.setText(nicks[i]);
            listPanel.add(label);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}
