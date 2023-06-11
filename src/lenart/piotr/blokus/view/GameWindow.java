package lenart.piotr.blokus.view;

import lenart.piotr.blokus.engine.client.IClientAdapter;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame{
    private final IClientAdapter clientAdapter;
    private JPanel mainPanel;

    public GameWindow(IClientAdapter clientAdapter) {
        this.clientAdapter = clientAdapter;

        setTitle("Blokus game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(400, 300));

        mainPanel.setLayout(new GridLayout());
        add(mainPanel);

        // setWaitingPanel();
        setGamePanel();
    }

    private void setWaitingPanel() {
        mainPanel.removeAll();
        mainPanel.add(new WaitingPanel(clientAdapter));
        pack();
    }

    private void setGamePanel() {
        mainPanel.removeAll();
        mainPanel.add(new GamePanel(clientAdapter));
        pack();
    }
}
