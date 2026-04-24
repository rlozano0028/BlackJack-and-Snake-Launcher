package snake;

import java.util.ArrayList;

public class Snake{

    private ArrayList<Position> positions;
    private Position head;
    private int length;
    private Direction direction;
    private boolean growNextMove = false;
    
    public Snake(){
        positions = new ArrayList<>();
        snakeSpawner();
        // length = 3;

        // positions.add(new Position(25,50));
        // positions.add(new Position(50,50));
        // positions.add(new Position(75,50));
        
        // positions.add(new Position(GameVariables.TILE_SIZE, GameVariables.TILE_SIZE * 2));
        // positions.add(new Position(GameVariables.TILE_SIZE * 2, GameVariables.TILE_SIZE * 2));
        // positions.add(new Position(GameVariables.TILE_SIZE * 3, GameVariables.TILE_SIZE * 2));
        // this.direction = Direction.RIGHT;
        // // setHead();

        // head = positions.get(positions.size() - 1);
        // direction = Direction.RIGHT;
    }
    public void setHead(Position head){
        // head = positions.get(positions.size() -1);

        // head = new Position( 
        //     head.getX() + direction.getDx() * GameVariables.TILE_SIZE,
        //     head.getY() + direction.getDy() * GameVariables.TILE_SIZE
        // );

        this.head = head;
    }
    public ArrayList<Position> getPositions(){
        return this.positions;
    }
    public int getLength(){
        return length;
    }
    // public void setDirection(Direction newDirection){
    //     direction = newDirection;
    // }
    public Position getHead(){
        return head;
    }
   public void setDirection(Direction newDirection) {
        if (!newDirection.isOpposite(this.direction)) {
            this.direction = newDirection;
        }
    }
    public Direction getDirection(){
        return direction;
    }
    // public void moveLeft(){
    //     head = new Position(head.getX() - GameVariables.TILE_SIZE, head.getY());
    //     positions.add(head);
    // }
    // public void moveRight(){
    //     head = new Position(head.getX() + GameVariables.TILE_SIZE, head.getY());
    //     positions.add(head);
    // }
    // public void moveDown(){
    //     head = new Position(head.getX(), head.getY() + GameVariables.TILE_SIZE);
    //     positions.add(head);
    // }
    // public void moveUp(){
    //     head = new Position(head.getX(), head.getY() - GameVariables.TILE_SIZE);
    //     positions.add(head);
    // }

    //simplified move method
    public void move() {
        int newX = head.getX() + direction.getDx() * GameVariables.TILE_SIZE;
        int newY = head.getY() + direction.getDy() * GameVariables.TILE_SIZE;

        head = new Position(newX, newY);
        positions.add(head);

        if (!growNextMove) {
            positions.remove(0);
        } else {
            length++;
            growNextMove = false;
        }

        // if (checkCollision()) {
        //     //reset game when collision
        //     reset(); 
        // }
    }

    public void grow() {
        // length++;
        growNextMove = true;
    }

    public boolean checkSelfCollision() {
        for (int i = 0; i < positions.size() - 1; i++) {
            if (head.equals(positions.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkWallCollision() {
        return head.getX() < 0 || head.getX() >= GameVariables.WIDTH ||
               head.getY() < 0 || head.getY() >= GameVariables.HEIGHT;
    }

    public boolean checkCollision() {
        return checkSelfCollision() || checkWallCollision();
    }

    public void restart() {
        positions.clear();
        snakeSpawner();
        // positions.add(new Position(25,50));
        // positions.add(new Position(50,50));
        // positions.add(new Position(75,50));
    }

    private void snakeSpawner(){
        length = 3;
        direction = Direction.getRandomDirection();
        
        int centerCol = GameVariables.COLS / 2;
        int centerRow = GameVariables.ROWS / 2;

        int startCol = centerCol + (int)(Math.random() * 3) - 1;
        int startRow = centerRow + (int)(Math.random() * 3) - 1;

        switch (direction) {
            case RIGHT:
                positions.add(new Position((startCol - 2) * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                positions.add(new Position((startCol - 1) * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                break;

            case LEFT:
                positions.add(new Position((startCol + 2) * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                positions.add(new Position((startCol + 1) * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                break;

            case DOWN:
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, (startRow - 2) * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, (startRow - 1) * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                break;

            case UP:
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, (startRow + 2) * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, (startRow + 1) * GameVariables.TILE_SIZE));
                positions.add(new Position(startCol * GameVariables.TILE_SIZE, startRow * GameVariables.TILE_SIZE));
                break;
        }
        
        head = positions.get(positions.size() - 1);
    }

    public int getScore() {
        //starting length is 3
        return length - 3;
    }
}