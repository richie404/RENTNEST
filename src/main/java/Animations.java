import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public final class Animations {
    private Animations(){}

    public static void fadeIn(Node node, double ms) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(ms), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void slideUp(Node node, double ms, double distance) {
        node.setTranslateY(distance);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateYProperty(), distance),
                        new KeyValue(node.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.millis(ms),
                        new KeyValue(node.translateYProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(node.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );
        tl.play();
    }

    /** small lift on hover */
    public static void attachHoverLift(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(130), node);
            st.setToX(1.015); st.setToY(1.015); st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), node);
            st.setToX(1.0); st.setToY(1.0); st.play();
        });
    }
}
