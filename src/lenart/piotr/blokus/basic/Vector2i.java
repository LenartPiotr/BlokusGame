package lenart.piotr.blokus.basic;

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
    public double length() { return Math.sqrt(_x*_x + _y*_y); }

    public boolean inRange(Vector2i bound) { return _x>=0 && _y>=0 && _x<bound._x && _y<bound._y; }
}
