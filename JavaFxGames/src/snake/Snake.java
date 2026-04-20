package snake;

import java.util.ArrayList;

public class Snake{

    private ArrayList<Position> positions;
    Position head;
    int length;
    // private Direction direction;
    
    public Snake(){
        positions = new ArrayList<>();
        length = 3;
        positions.add(new Position(25,50));
        positions.add(new Position(50,50));
        positions.add(new Position(75,50));
        // this.direction = Direction.RIGHT;
        setHead();
    }
    public void setHead(){
        head = positions.get(positions.size() -1);
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
    public void moveLeft(){
        head = new Position(head.getX() - GameVariables.TILE_SIZE, head.getY());
        positions.add(head);
    }
    public void moveRight(){
        head = new Position(head.getX() + GameVariables.TILE_SIZE, head.getY());
        positions.add(head);
    }
    public void moveDown(){
        head = new Position(head.getX(), head.getY() + GameVariables.TILE_SIZE);
        positions.add(head);
    }
    public void moveUp(){
        head = new Position(head.getX(), head.getY() - GameVariables.TILE_SIZE);
        positions.add(head);
    }
}