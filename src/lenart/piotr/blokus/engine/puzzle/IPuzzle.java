package lenart.piotr.blokus.engine.puzzle;

import lenart.piotr.blokus.basic.Vector2i;

import java.util.List;

public interface IPuzzle {
    public Vector2i[] getFields();
    public void rotate(boolean right);
    public void flip(boolean horizontally);
    public Vector2i getMinBound();
    public Vector2i getMaxBound();
    public IPuzzle copy();
    public boolean isTheSame(IPuzzle puzzle);
}
