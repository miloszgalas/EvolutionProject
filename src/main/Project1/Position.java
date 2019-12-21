package Project1;

import javafx.geometry.Pos;

import java.util.Random;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += x * 31;
        hash += y * 17;
        return hash;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Position other) {
        if (x <= other.x && y <= other.y)
            return true;
        else
            return false;
    }

    public boolean follows(Position other) {
        if (x >= other.x && y >= other.y)
            return true;
        else
            return false;
    }

    public boolean positionInBounds(Position t1, Position t2) {
        if (this.follows(t1) && this.precedes(t2))
            return true;
        return false;
    }

    public Position upperRight(Position other) {
        return new Position(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Position lowerLeft(Position other) {
        return new Position(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    public Position subtract(Position other) {
        return new Position(x - other.x, y - other.y);
    }


    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Position))
            return false;
        Position that = (Position) other;
        if (that.x == x && y == that.y)
            return true;
        else
            return false;
    }

    public void makePositionInBounds(int maxX, int maxY) {
        if (x == -1)
            x = maxX - 1;
        if (y == -1)
            y = maxY - 1;
        if (x == maxX)
            x = 0;
        if (y == maxY)
            y = 0;
    }

    public Position opposite() {
        return new Position(-x, -y);
    }

    static public Position randomPosition(int maxX, int maxY) {
        Random generator = new Random();
        return new Position(generator.nextInt(maxX), generator.nextInt(maxY));
    }
}

