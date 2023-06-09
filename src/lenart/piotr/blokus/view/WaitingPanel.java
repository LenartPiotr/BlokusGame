package lenart.piotr.blokus.view;

import lenart.piotr.blokus.engine.client.IClientAdapter;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;

import javax.swing.*;
import java.awt.*;

public class WaitingPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel label1;
    private JPanel listPanel;
    private final IClientAdapter clientAdapter;
    private final int maxUsers;
    private int users;

    public WaitingPanel(IClientAdapter clientAdapter) {
        this.clientAdapter = clientAdapter;
        maxUsers = clientAdapter.getMaxPlayersCount();
        users = clientAdapter.getPlayersCount();

        clientAdapter.onChangePlayerCount(newCount -> {
            users = newCount;
            refillClientsList();
            refillMainLabel();
        });

        add(mainPanel);

        label1.setText("waiting for host...");
        refillMainLabel();
        refillClientsList();
    }

    private void refillMainLabel() {
        label1.setText(users + " / " + maxUsers + " users");
    }

    private void refillClientsList() {
        listPanel.removeAll();
        listPanel.setLayout(new GridLayout());
        for (int i = 0; i < users; i++) {
            JLabel label = new JLabel();
            try {
                label.setText(clientAdapter.getPlayerName(i));
            } catch (WrongActionException e) {
                label.setText("Cannot get nick of this player");
            }
            listPanel.add(label);
        }
    }
}
