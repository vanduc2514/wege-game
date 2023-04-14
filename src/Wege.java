import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Wege extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WegeBoard wegeBoard = new WegeBoard(6 , 6);
        Scene scene = new Scene(wegeBoard.drawBoard());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
