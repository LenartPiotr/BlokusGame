package lenart.piotr.blokus.view;

import lenart.piotr.blokus.engine.client.IClient;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame{
    private final IClient client;
    private JPanel mainPanel;

    public GameWindow(IClient client) {
        this.client = client;

        setTitle("Blokus game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(400, 300));

        mainPanel.setLayout(new GridLayout());
        add(mainPanel);

        setWaitingPanel();
    }

    private void setWaitingPanel() {
        mainPanel.removeAll();
        mainPanel.add(new WaitingPanel(client));
        pack();
        client.on("startGame", ignored -> setGamePanel());
    }

    private void setGamePanel() {
        mainPanel.removeAll();
        mainPanel.add(new GamePanel(client, this::setWaitingPanel));
        pack();
    }
}
