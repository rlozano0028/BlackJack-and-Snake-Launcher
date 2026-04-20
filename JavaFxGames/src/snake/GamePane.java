package snake;

import javafx.scene.paint.Color;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class GamePane extends Pane {
    
    private Canvas canvas;
    private GraphicsContext gc;
    
    public GamePane(){
        canvas = new Canvas(GameVariables.WIDTH, GameVariables.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        getChildren().add(canvas);

        drawGrid();
    }

    private void drawGrid(){
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GameVariables.WIDTH, GameVariables.HEIGHT);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);

        for (int x = 0; x <= GameVariables.WIDTH; x += GameVariables.TILE_SIZE) {
            gc.strokeLine(x, 0, x, GameVariables.HEIGHT);
        }

        for (int y = 0; y <= GameVariables.HEIGHT; y += GameVariables.TILE_SIZE) {
            gc.strokeLine(0, y, GameVariables.WIDTH, y);
        }
    }
}
