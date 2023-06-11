package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.ICallback2;
import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {
    private Vector2i size;
    private int[][] table;

    private IPuzzle pickedPuzzle;
    private int pickedPuzzleColor;
    private Vector2i origin;
    private boolean showPuzzle;
    private ICallback2<Vector2i, IPuzzle> clickListener;

    public Board(Vector2i newSize) {
        size = new Vector2i(newSize);
        table = new int[size.x()][size.y()];
        for (int x = 0; x < size.x(); x++)
            for (int y = 0; y < size.y(); y++) {
                table[x][y] = -1;
            }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    case 1:
                        if (clickListener != null && showPuzzle && pickedPuzzle != null) clickListener.run(origin, pickedPuzzle);
                        break;
                    case 3:
                        if (pickedPuzzle != null) {
                            pickedPuzzle.flip(true);
                            repaint();
                        }
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (pickedPuzzle != null) {
                    origin = new Vector2i(
                            e.getX() / size.x(),
                            e.getY() / size.y()
                    );
                    showPuzzle = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showPuzzle = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (showPuzzle) {
                    Vector2i newOrigin = new Vector2i(
                            e.getX() / size.x(),
                            e.getY() / size.y()
                    );
                    if (!newOrigin.equals(origin)) {
                        origin = newOrigin;
                        repaint();
                    }
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (pickedPuzzle != null && showPuzzle) {
                    pickedPuzzle.rotate(e.getPreciseWheelRotation() < 0);
                    repaint();
                }
            }
        });
    }

    public void placePuzzle(IPuzzle puzzle, int color, Vector2i position) {
        for (Vector2i field: puzzle.getFields()) {
            Vector2i pos = field.add(position);
            if (pos.inRange(size)) {
                table[pos.x()][pos.y()] = color;
            }
        }
        repaint();
    }

    public void setPickedPuzzle(IPuzzle puzzle, int color) {
        this.pickedPuzzle = puzzle;
        this.pickedPuzzleColor = color;
        showPuzzle = false;
        repaint();
    }

    public void setOnClickListener(ICallback2<Vector2i, IPuzzle> callback) {
        clickListener = callback;
    }

    private Color getColor(int index) {
        return switch (index) {
            case -1 -> Color.white;
            case 0 -> Color.red;
            case 1 -> Color.yellow;
            case 2 -> Color.green;
            case 3 -> Color.blue;
            default -> Color.magenta;
        };
    }

    private Color getLighterColor(Color color, int dx) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        r = Math.min(r + dx, 255);
        g = Math.min(g + dx, 255);
        b = Math.min(b + dx, 255);
        return new Color(r, g, b);
    }

    @Override
    public void paint(Graphics g) {
        int width = this.getWidth() - 1;
        int height = this.getHeight() - 1;
        g.clearRect(0, 0, width, height);
        double fieldWidth = width / (double)size.x();
        double fieldHeight = height / (double)size.y();
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                g.setColor(getColor(table[x][y]));
                g.fillRect((int)Math.floor(x * fieldWidth), (int)Math.floor(y * fieldHeight), (int)Math.ceil(fieldWidth), (int)Math.ceil(fieldHeight));
            }
        }

        if (showPuzzle && pickedPuzzle != null) {
            g.setColor(getLighterColor(getColor(pickedPuzzleColor), 150));
            for (Vector2i v : pickedPuzzle.getFields()) {
                Vector2i pos = v.add(origin);
                if (pos.inRange(size)) {
                    g.fillRect((int)Math.floor(pos.x() * fieldWidth), (int)Math.floor(pos.y() * fieldHeight), (int)Math.ceil(fieldWidth), (int)Math.ceil(fieldHeight));
                }
            }
        }

        g.setColor(Color.black);
        for (int i = 0; i <= size.x(); i++) {
            g.drawLine((int)(i * fieldWidth), 0, (int)(i * fieldWidth), height);
        }
        for (int i = 0; i <= size.y(); i++) {
            g.drawLine(0, (int)(i * fieldHeight), width, (int)(i * fieldHeight));
        }
    }
}
