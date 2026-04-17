package snake.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import snake.controller.GameController;

public class SnakeGameApp extends Application {
    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/snake-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        GameController controller = loader.getController();
        controller.bindScene(scene);

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        controller.onWindowShown();
    }
}
