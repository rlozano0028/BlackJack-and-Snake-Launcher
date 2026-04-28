package snake;

import java.util.Objects;

public class Position {
    
    private final int xPosition;
    private final int yPosition;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (!(obj instanceof Position)) 
            return false;
        Position other = (Position) obj;
        return this.xPosition == other.xPosition && this.yPosition == other.yPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPosition, yPosition);
    }
}
