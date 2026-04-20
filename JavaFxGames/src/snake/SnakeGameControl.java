package snake;

import snake.GameVariables;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SnakeGameControl extends Application{
    
    @Override
    public void start(Stage primaryStage){
        GamePane gamePane = new GamePane();
        
        Scene scene = new Scene(gamePane, GameVariables.WIDTH,GameVariables.HEIGHT);

        primaryStage.setTitle("SNAKE TESTING");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
