import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class WegeButtonBoardHandler implements EventHandler<MouseEvent> {

    private Pair<Integer, Integer> buttonLocation;

    public WegeButtonBoardHandler(Pair<Integer, Integer> buttonLocation) {
        this.buttonLocation = buttonLocation;
    }

    @Override
    public void handle(MouseEvent event) {

    }


}
