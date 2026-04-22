package snake;

import java.util.ArrayList;

public class Snake{

    private ArrayList<Position> positions;
    private Position head;
    private int length;
    private Direction direction;
    
    public Snake(){
        positions = new ArrayList<>();
        length = 3;

        positions.add(new Position(25,50));
        positions.add(new Position(50,50));
        positions.add(new Position(75,50));
        this.direction = Direction.RIGHT;
        // setHead();

        head = positions.get(positions.size() - 1);
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
    }
}