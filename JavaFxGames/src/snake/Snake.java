package snake;

import java.util.ArrayList;

public class Snake implements Resettable {

    private ArrayList<Position> positions;
    private Position head;
    private int length;
    private Direction direction;
    private boolean growNextMove = false;
    
    public Snake(){
        positions = new ArrayList<>();
        snakeSpawner();
    }
    public void setHead(Position head){

        this.head = head;
    }
    public ArrayList<Position> getPositions(){
        return this.positions;
    }
    public int getLength(){
        return length;
    }
 
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