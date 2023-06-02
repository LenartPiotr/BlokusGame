package lenart.piotr.blokus.engine.puzzle;

import lenart.piotr.blokus.basic.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplePuzzle implements IPuzzle{

    private Vector2i[] fields;
    private int rotatedRight;
    private boolean flippedHorizontally;

    public SimplePuzzle(Vector2i[] fieldList) {
        fields = new Vector2i[fieldList.length];
        for (int i = 0; i < fieldList.length; i++) fields[i] = new Vector2i(fieldList[i]);
        rotatedRight = 0;
        flippedHorizontally = false;
    }

    public SimplePuzzle(SimplePuzzle p){
        fields = new Vector2i[p.fields.length];
        for (int i = 0; i < p.fields.length; i++) fields[i] = new Vector2i(p.fields[i]);
        rotatedRight = p.rotatedRight;
        flippedHorizontally = p.flippedHorizontally;
    }

    @Override
    public Vector2i[] getFields() {
        Vector2i[] copy = new Vector2i[fields.length];
        for (int i = 0; i < fields.length; i++) copy[i] = new Vector2i(fields[i]);
        return copy;
    }

    @Override
    public void rotate(boolean right) {
        for (int i = 0; i < fields.length; i++)
            fields[i] = right ? fields[i].rotateRight() : fields[i].rotateLeft();
        if (right)
            rotatedRight++;
        else
            rotatedRight += 3;
        rotatedRight %= 4;
    }

    @Override
    public void flip(boolean horizontally) {
        flippedHorizontally = !flippedHorizontally;
        if (horizontally && rotatedRight % 2 != 0) {
            rotatedRight += 2;
            rotatedRight %= 4;
        }
        if (!horizontally && rotatedRight % 2 == 0) {
            rotatedRight += 2;
            rotatedRight %= 4;
        }
        for (int i = 0; i < fields.length; i++)
            fields[i] = horizontally ? fields[i].reverseX() : fields[i].reverseY();
    }

    @Override
    public Vector2i getMinBound() {
        int minX = 0;
        int minY = 0;
        for (Vector2i v: fields) {
            minX = Math.min(v.x(), minX);
            minY = Math.min(v.y(), minY);
        }
        return new Vector2i(minX, minY);
    }

    @Override
    public Vector2i getMaxBound() {
        int maxX = 0;
        int maxY = 0;
        for (Vector2i v: fields) {
            maxX = Math.max(v.x(), maxX);
            maxY = Math.max(v.y(), maxY);
        }
        return new Vector2i(maxX, maxY);
    }

    @Override
    public IPuzzle copy() {
        return new SimplePuzzle(this);
    }

    private boolean checkLists(Vector2i[] first, Vector2i[] second){
        if (first.length != second.length) return false;
        for (int i = 0; i < first.length; i++) {
            if (!first[i].equals(second[i])) return false;
        }
        return true;
    }

    public void flipTo(boolean flippedHorizontally) {
        if (this.flippedHorizontally != flippedHorizontally) this.flip(true);
    }

    public void rotateTo(int rot) {
        int timesToRot = (rot + 4 - rotatedRight) % 4;
        switch (timesToRot) {
            case 1 -> rotate(true);
            case 2 -> {
                rotate(true);
                rotate(true);
            }
            case 3 -> rotate(false);
        }
    }

    @Override
    public boolean isTheSame(IPuzzle puzzle) {
        IPuzzle copy = puzzle.copy();
        if (copy instanceof SimplePuzzle s){
            s.flipTo(flippedHorizontally);
            s.rotateTo(rotatedRight);
            return checkLists(fields, s.fields);
        }
        return checkLists(fields, copy.getFields());
    }

    @Override
    public String toString() {
        return "SimplePuzzle(" + rotatedRight + ", " + flippedHorizontally + ", " + Arrays.toString(fields) + '}';
    }
}
