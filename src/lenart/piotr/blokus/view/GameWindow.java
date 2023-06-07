package lenart.piotr.blokus.view;

import lenart.piotr.blokus.engine.client.IClientAdapter;

import javax.swing.*;

public class GameWindow extends JFrame{
    private JPanel mainPanel;

    public GameWindow(IClientAdapter clientAdapter) {
        add(mainPanel);
    }
}
