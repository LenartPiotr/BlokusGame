package lenart.piotr.blokus.basic;

import java.util.Objects;

public class Vector2i {
    private int _x;
    private int _y;

    public Vector2i() { _x=0; _y=0; }
    public Vector2i(int x, int y) { _x=x; _y=y; }
    public Vector2i(Vector2i s) { _x=s._x; _y=s._y; }

    public int x() { return _x; }
    public int y() { return _y; }

    public Vector2i add(Vector2i v) { return new Vector2i(_x+v._x, _y+v._y); }
    public Vector2i sub(Vector2i v) { return new Vector2i(_x-v._x, _y-v._y); }

    public Vector2i reverse() { return new Vector2i(-_x, -_y); }
    public Vector2i reverseX() { return new Vector2i(-_x, _y); }
    public Vector2i reverseY() { return new Vector2i(_x, -_y); }
    public Vector2i rotateRight() { return new Vector2i(_y, -_x); }
    public Vector2i rotateLeft() { return new Vector2i(-_y, _x); }
    public double length() { return Math.sqrt(_x*_x + _y*_y); }

    public boolean inRange(Vector2i bound) { return _x>=0 && _y>=0 && _x<bound._x && _y<bound._y; }

    public static Vector2i over(Vector2i a, Vector2i b, IFunction method) {
        return new Vector2i(method.run(a._x, b._x), method.run(a._y, b._y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2i = (Vector2i) o;
        return _x == vector2i._x && _y == vector2i._y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y);
    }

    @Override
    public String toString() {
        return "(" + _x + ", " + _y + ")";
    }

    public static interface IFunction {
        public int run(int a, int b);
    }
}
