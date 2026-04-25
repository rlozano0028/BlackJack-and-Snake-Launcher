package snake;

import java.util.Random;

public enum Direction {
    // UP,
    // DOWN,
    // LEFT,
    // RIGHT

    /*
        Looking at a (+,-) plane

        right = x + 1
        left = x - 1
        up = y - 1
        down = y + 1
    */
    RIGHT(1, 0),
    LEFT(-1, 0),
    UP(0, -1),
    DOWN(0, 1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    //prevents snake from moving into itself 
    public boolean isOpposite(Direction other) {
        return this.dx == -other.dx && this.dy == -other.dy;
    }

    //spawns snake in random direction
    public static Direction getRandomDirection() {
        Direction[] directions = values();
        int index = new Random().nextInt(directions.length);
        return directions[index];
    }
}
