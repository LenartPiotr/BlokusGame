package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.ICallback0;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;

import javax.swing.*;

public class EndgamePanel extends JPanel {
    private final ICallback0 onEnd;
    private final EndgameData data;

    public EndgamePanel(EndgameData data, ICallback0 onEnd) {
        this.onEnd = onEnd;
        this.data = data;

        initialiseComponents();
    }

    private void initialiseComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Results");
        add(title);

        for (int i = 0; i < data.count(); i++) {
            JLabel result = new JLabel(data.get(i).getName() + " - " + data.get(i).getPoints());
            add(result);
        }

        JButton returnButton = new JButton("Exit");
        returnButton.addActionListener(x -> onEnd.run());
        add(returnButton);
    }
}
