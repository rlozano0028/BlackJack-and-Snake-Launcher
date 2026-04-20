package snake;

public class Position {
    
    private int xPosition;
    private int yPosition;

    public Position(int xPosition, int yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    public int getX(){
        return this.xPosition;
    }
    public int getY(){
        return this.yPosition;
    }
}
