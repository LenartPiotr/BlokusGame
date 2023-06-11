package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.IClientAdapter;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private JPanel mainPanel;
    private JLabel errorMessage;
    private JLabel turnLabel;
    private Board board;
    private List<JPanel> userPuzzlePanels;

    private final JLabel[] playersNick;
    private final int playerIndex;
    private final int playersCount;
    private final boolean[] surrender;

    private int turn;

    private final IClientAdapter clientAdapter;

    public GamePanel(IClientAdapter clientAdapter) {
        this.clientAdapter = clientAdapter;
        playerIndex = clientAdapter.getIndex();
        playersCount = clientAdapter.getMaxPlayersCount();
        playersNick = new JLabel[playersCount];
        surrender = new boolean[playersCount];
        for (int i = 0; i < playersCount; i++) {
            surrender[i] = false;
        }
        turn = 0;

        initializeComponents();

        add(mainPanel);

        board.setOnClickListener((position, puzzle) -> {
            errorMessage.setText("");
            try {
                if (turn == playerIndex)
                    clientAdapter.placePuzzle(puzzle, position);
            } catch (WrongActionException e) {
                errorMessage.setText(e.getTextToDisplay());
            }
        });

        clientAdapter.onChangeTurn(turn -> {
            try {
                this.turn = turn;
                turnLabel.setText("Turn: " + clientAdapter.getPlayerName(turn));
            } catch (WrongActionException e) {
                errorMessage.setText(e.getTextToDisplay());
            }
        });

        clientAdapter.onPlayerGaveUp(p -> {
            surrender[p] = true;
            playersNick[p].setForeground(Color.gray);
        });

        clientAdapter.onPuzzlePlaced((color, position, puzzle) -> {
            board.placePuzzle(puzzle, color, position);
            try {
                setPuzzlesView(userPuzzlePanels.get(color), clientAdapter.getPuzzleList(color), color == playerIndex, color);
            } catch (WrongActionException e) {
                errorMessage.setText(e.getTextToDisplay());
            }
        });
    }

    private void initializeComponents() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel header = new JPanel();
        mainPanel.add(header);

        turnLabel = new JLabel();
        try {
            turnLabel.setText("Turn: " + clientAdapter.getPlayerName(0));
        } catch (WrongActionException e) {
            turnLabel.setText("Turn: ???");
        }
        header.add(turnLabel);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        mainPanel.add(content);

        board = new Board(clientAdapter.getBoardSize());
        Dimension boardDimension = new Dimension(401, 401);
        board.setPreferredSize(boardDimension);
        board.setMaximumSize(boardDimension);
        board.setMinimumSize(boardDimension);
        content.add(board);

        JPanel puzzleMainPanel = new JPanel();
        puzzleMainPanel.setLayout(new BoxLayout(puzzleMainPanel, BoxLayout.X_AXIS));
        content.add(puzzleMainPanel);

        userPuzzlePanels = new ArrayList<>();
        try {
            for (int i = 0; i < clientAdapter.getMaxPlayersCount(); i++) {
                JPanel userPuzzlePanel = new JPanel();
                setPuzzlesView(userPuzzlePanel, clientAdapter.getPuzzleList(i), i == playerIndex, i);
                userPuzzlePanels.add(userPuzzlePanel);
                puzzleMainPanel.add(userPuzzlePanel);
            }
        } catch (WrongActionException e) {
            puzzleMainPanel.removeAll();
            puzzleMainPanel.setLayout(new BoxLayout(puzzleMainPanel, BoxLayout.X_AXIS));
            puzzleMainPanel.add(new JLabel(e.getTextToDisplay()));
        }

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
        mainPanel.add(footer);

        JLabel cancelLabel = new JLabel("X");
        cancelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                errorMessage.setText("");
            }
        });
        cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.add(cancelLabel);

        JPanel footerSpace = new JPanel();
        Dimension footerSpaceSize = new Dimension(10,10);
        footerSpace.setPreferredSize(footerSpaceSize);
        footerSpace.setMinimumSize(footerSpaceSize);
        footerSpace.setMaximumSize(footerSpaceSize);
        footer.add(footerSpace);

        errorMessage = new JLabel();
        footer.add(errorMessage);

        JButton surrenderButton = new JButton("Surrender");
        surrenderButton.addActionListener(action -> {
            if (turn == playerIndex) {
                try {
                    clientAdapter.giveUp();
                } catch (WrongActionException e) {
                    errorMessage.setText(e.getTextToDisplay());
                }
            }
        });
        footer.add(surrenderButton);
    }

    private void setPuzzlesView(JPanel panel, List<IPuzzle> list, boolean onclick, int index) throws WrongActionException {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel playerNick = new JLabel(clientAdapter.getPlayerName(index));
        if (surrender[index]) playerNick.setForeground(Color.gray);
        playersNick[index] = playerNick;
        panel.add(playerNick);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size = new Dimension(30, 30);
        Vector2i minSize = new Vector2i(5,5);
        for (IPuzzle puzzle : list) {
            Puzzle p = new Puzzle(puzzle, minSize, true, index);
            p.setPreferredSize(size);
            p.setMaximumSize(size);
            p.setMinimumSize(size);
            p.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (onclick) {
                p.setOnClick(this::pickedPuzzle);
            }
            panel.add(p);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void pickedPuzzle(IPuzzle puzzle) {
        board.setPickedPuzzle(puzzle, 0);
    }
}
