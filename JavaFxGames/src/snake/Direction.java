package snake;

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

    private int dx;
    private int dy;

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
}
