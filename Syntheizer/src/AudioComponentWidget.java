import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class AudioComponentWidget extends Pane {
    private AnchorPane parent_;
    private HBox baseLayout = new HBox();
    private AudioComponent audioCom_= null;
    private String name_ ="NAME NOT INITIALIZED";
    private Line line_=null;
    public Text message = new Text("Hello");
    private int frequency_ = 440;


    AudioComponentWidget(AudioComponent ac, AnchorPane parent, String componentName){

    parent_=parent;
    audioCom_ =ac;

    baseLayout.setStyle("-fx-background-color:white ;-fx-border-color: #ae96b5");
    VBox rightSide = new VBox();
    Button closeBtn =new Button("x");
    closeBtn.setOnAction(e -> close());
    Circle outputCircle = new Circle(10);
    outputCircle.setFill(Color.rgb(174,150, 181));
    outputCircle.setOnMousePressed(e -> startConnection(e, outputCircle));
    outputCircle.setOnMouseDragged(e -> moveConnection(e, outputCircle));
    outputCircle.setOnMouseReleased(e -> stopConnecting(e,outputCircle));

    rightSide.setAlignment(Pos.CENTER);
    rightSide.setPadding(new Insets(2));
    rightSide.setSpacing(5);
    rightSide.getChildren().add(closeBtn);
    rightSide.getChildren().add(outputCircle);

    VBox leftSide = new VBox();
    Text title = new Text((componentName));
    title.setOnMouseDragged(e -> handleMove (e));

    Slider slider = new Slider();
    if(componentName.equals("SineWave")){
    title = new Text(componentName + " - " + "440");
    title.setOnMouseDragged(e -> handleMove (e));


    slider.setMin(0);
    slider.setMax(999);
    slider.setValue(440);
        Text finalTitle = title;
        slider.setOnMouseDragged(e -> frequencyChange(componentName, slider , finalTitle));
    slider.setOnMouseReleased(e -> sliderReleased());
    }


    leftSide.getChildren().add(slider);
    leftSide.setAlignment(Pos.CENTER);
    leftSide.getChildren().add(title);
    //leftSide.getChildren().add(message);


    baseLayout.getChildren().add(leftSide);
    baseLayout.getChildren().add(rightSide);


    this.getChildren().add(baseLayout);
    parent.getChildren().add(this);

    AnchorPane.setLeftAnchor(this, 100.0);
    AnchorPane.setTopAnchor(this, 50.0);


    }

    private void sliderReleased() {
        SynthApp.speakerConnections_.remove(audioCom_);
        AudioComponent newAC =new SineWave(frequency_);
        audioCom_ = newAC;
        SynthApp.speakerConnections_.add(audioCom_);

    }


    private void frequencyChange(String componentName, Slider slider, Text title) {
     frequency_ = (int) slider.getValue();
        title.setText(componentName + " - "  + frequency_);
        System.out.println("handle slider: " + frequency_);
    }

    private void stopConnecting(MouseEvent e, Circle outputCircle) {

        Circle speaker = SynthApp.speaker_;

        Bounds bounds = speaker.localToScene(speaker.getBoundsInLocal());

        double distance = Math.sqrt(Math.pow(bounds.getCenterX() -e.getSceneX(), 2.0) +
                Math.pow(bounds.getCenterY() - e.getSceneY(),2.0));
        if(distance <20){
            System.out.println("less than 20");
            SynthApp.speakerConnections_.add(audioCom_);
        }
        else{
            parent_.getChildren().remove(line_);
        }

    }

    private void moveConnection(MouseEvent e, Circle outputCircle) {

        if(line_ != null){
            Bounds bounds= parent_.getBoundsInParent();
            line_.setEndX(e.getSceneX() -bounds.getMinX());
            line_.setEndY(e.getSceneY() -bounds.getMinY());
        }
    }

    private void startConnection(MouseEvent e, Circle outputCircle) {
        Bounds bounds = parent_.getBoundsInParent();
        line_ = new Line();
        line_.setStrokeWidth(3);
        line_.setStartX(e.getSceneX()-bounds.getMinX());
        line_.setStartY(e.getSceneY() -bounds.getMinY());
        line_.setEndX(e.getSceneX() - bounds.getMinX());
        line_.setEndY(e.getSceneY() - bounds.getMinY());

        parent_.getChildren().add(line_);

    }

    private void handleMove(MouseEvent e) {
        Bounds bounds = parent_.getBoundsInParent();
        AnchorPane.setTopAnchor(this,e.getSceneY() - bounds.getMinY());
        AnchorPane.setLeftAnchor(this,e.getSceneX() - bounds.getMinX());
        line_.setStartX(e.getSceneX() +160);
        line_.setStartY(e.getSceneY() +3);
    }

    private void close() {
        System.out.println("close");
        parent_.getChildren().remove(this);
        SynthApp.speakerConnections_.remove(audioCom_);
        if(line_ != null){
        parent_.getChildren().remove(line_);

        }
    }
}
