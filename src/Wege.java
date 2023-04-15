import csds132.hw4.ui.WegeGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Wege extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new WegeGame(6 , 6));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
