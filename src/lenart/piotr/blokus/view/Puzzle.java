package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.ICallback1;
import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;
import lenart.piotr.blokus.engine.puzzle.SimplePuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Puzzle extends JPanel {
    private final IPuzzle puzzle;
    private final Vector2i size;
    private final Vector2i origin;
    private final int color;

    private ICallback1<IPuzzle> clickListener;
    private boolean lighter;
    private boolean moreLighter;

    private Color getColor(int index) {
        return switch (index) {
            case 0 -> Color.red;
            case 1 -> Color.orange;
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

    public Puzzle(IPuzzle puzzle, Vector2i minSize, boolean mustSquare, int color) {
        Vector2i puzzleSize = puzzle.getMaxBound().add(puzzle.getMinBound().reverse()).add(new Vector2i(1,1));
        puzzleSize = Vector2i.over(puzzleSize, minSize, Math::max);
        if (mustSquare) {
            int max = Math.max(puzzleSize.x(), puzzleSize.y());
            puzzleSize = new Vector2i(max, max);
        }
        size = puzzleSize;
        origin = puzzle.getMinBound().reverse();
        this.puzzle = puzzle;
        this.color = color;
        lighter = false;
        moreLighter = false;
    }

    @Override
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0,0,w,h);
        Color fillColor = getColor(color);
        if (lighter) fillColor = getLighterColor(fillColor, 120);
        if (lighter && moreLighter) fillColor = getLighterColor(fillColor, 40);
        double fieldWidth = w / (double)size.x();
        double fieldHeight = h / (double)size.y();
        for (Vector2i v : puzzle.getFields()) {
            Vector2i pos = v.add(origin);
            g.setColor(Color.black);
            g.fillRect((int)Math.floor(pos.x() * fieldWidth) - 1, (int)Math.floor(pos.y() * fieldHeight) - 1, (int)Math.ceil(fieldWidth) + 1, (int)Math.ceil(fieldHeight) + 1);
            g.setColor(fillColor);
            g.fillRect((int)Math.floor(pos.x() * fieldWidth), (int)Math.floor(pos.y() * fieldHeight), (int)Math.ceil(fieldWidth), (int)Math.ceil(fieldHeight));
        }
    }

    public void setOnClick(ICallback1<IPuzzle> click) {
        this.clickListener = click;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickListener.run(puzzle.copy());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                moreLighter = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moreLighter = false;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lighter = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lighter = false;
                repaint();
            }
        });
    }
}
