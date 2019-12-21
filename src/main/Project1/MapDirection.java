package Project1;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum MapDirection {
    N(0), NE(1), E(2), SE(3), S(4), SW(5), W(6), NW(7);

    int rotation;

    MapDirection(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return rotation;
    }

    static MapDirection randomDirection() {
        Random generator = new Random();
        return directionMap.get(generator.nextInt(8));
    }

    public static Map<Integer, MapDirection> directionMap = new HashMap<>();

    public MapDirection rotate(int rotation) {
        return directionMap.get((this.getRotation() + rotation) % 8);
    }


    public Position toUnitVector() {
        switch (this) {
            case N:
                return new Position(0, 1);
            case S:
                return new Position(0, -1);
            case E:
                return new Position(1, 0);
            case W:
                return new Position(-1, 0);
            case NE:
                return new Position(1, 1);
            case SE:
                return new Position(1, -1);
            case SW:
                return new Position(-1, -1);
            case NW:
                return new Position(-1, 1);
        }
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case N:
                return "N";
            case S:
                return "S";
            case E:
                return "E";
            case W:
                return "W";
            case NE:
                return "NE";
            case SE:
                return "SE";
            case SW:
                return "SW";
            case NW:
                return "NW";
        }
        return null;
    }
}
