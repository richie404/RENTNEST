import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class FXAnimations {

    // ✅ Fade in animation
    public static void fadeIn(Node node, double durationSeconds) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.seconds(durationSeconds), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // ✅ Fade out animation
    public static void fadeOut(Node node, double durationSeconds) {
        FadeTransition ft = new FadeTransition(Duration.seconds(durationSeconds), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    // ✅ Slide in from bottom
    public static void slideInUp(Node node, double durationSeconds) {
        node.setTranslateY(50);
        node.setOpacity(0);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(node.translateYProperty(), 50),
                        new KeyValue(node.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(durationSeconds),
                        new KeyValue(node.translateYProperty(), 0),
                        new KeyValue(node.opacityProperty(), 1))
        );
        tl.play();
    }

    // ✅ Subtle zoom effect
    public static void zoomIn(Node node, double durationSeconds) {
        node.setScaleX(0.9);
        node.setScaleY(0.9);
        node.setOpacity(0);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(node.scaleXProperty(), 0.9),
                        new KeyValue(node.scaleYProperty(), 0.9),
                        new KeyValue(node.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(durationSeconds),
                        new KeyValue(node.scaleXProperty(), 1),
                        new KeyValue(node.scaleYProperty(), 1),
                        new KeyValue(node.opacityProperty(), 1))
        );
        tl.play();
    }
}
