import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class SynthApp extends Application {

    public AnchorPane canvas = new AnchorPane();
    public static Circle speaker_;
    public static ArrayList<AudioComponent> speakerConnections_ = new ArrayList<>();

    class AudioListener implements LineListener {
        public AudioListener(Clip c) {
            clip_ = c;
        }


        @Override
        public void update(LineEvent event) {
            if (event.getType() == LineEvent.Type.STOP) {
                System.out.println("close clip");
                clip_.close();
            }
        }

        private Clip clip_;
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        //print out basic info
        System.out.println("Hello from start");
        System.out.println("javafx version:" +System.getProperty("javafx.runtime.version"));

        //title of the GUI
        primaryStage.setTitle("Ang's GUI APP");

        //setup borderpane
        BorderPane bp =new BorderPane();

        //setup top bar
        HBox top =new HBox();
        top.setStyle("-fx-background-color: #ae96b5");
        top.setPrefSize(500,40);
        top.setAlignment(Pos.CENTER);

        //setup bottom bar
        HBox bottom = new HBox();
        bottom.setStyle("-fx-background-color: #ae96b5");
        bottom.setPrefSize(500,40);
        bottom.setAlignment(Pos.CENTER);

        //setup right side-bar
        VBox rightSide = new VBox();
        rightSide.setStyle("-fx-background-color: #ae96b5");
        rightSide.setPrefSize(150,100);
        //rightSide.setAlignment(Pos.CENTER);


        //set up a text message
//
//        Text msge = new Text("Hello, Welcome");
//        msge.setFont(Font.font("Verdana",10));
//        msge.setFill(Color.rgb(164,117,191));


        //add music buttons in the menu bar

        //call method of building piano buttons
        musicBT(top);

        //call method of building Audio component menu
        creatAudioComponent(rightSide);

        //add a button at bottom bar
        Button play = new Button("Play");
        play.setPrefSize(60,30);
        bottom.getChildren().add(play);
        play.setOnAction(e -> playNetwork());


        //set canvas color
        canvas.setStyle("-fx-background-color: #cbc4cf");

        //setup circle on canvas
        speaker_ = new Circle(20);
        speaker_.setFill(Color.rgb(174,150, 181));
        canvas.getChildren().add(speaker_);
        speaker_.setLayoutX(590);
        speaker_.setLayoutY(230);

   /*     Button playMixWave = new Button("Play Mix Wave");*/

    /*    //add a slider to menu (volume adjuster)
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);*/


        //set element's positions in board pane
         //bp.getChildren().add(menu);

        //add piano buttons to border pane
        bp.setTop(top);
        //add play button at border pane
        bp.setBottom(bottom);
        //add audio component menu at border pane
        bp.setRight(rightSide);
        //add canvas to center at border pane
        bp.setCenter(canvas);

        //adding actions to bottons
        /*playSinWave.setOnAction((e -> {
            try {
                handlePressSine(msge);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }));
        playMixWave.setOnAction((e -> {
            try {
                handlePressMix(msge);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }));

        slider.setOnDragDetected(e -> handleSlider(slider, msge));
        menu.setOnMouseDragged(e -> handleDrag(e));

        //adding elements to menu
        menu.getChildren().add(playSinWave);
        menu.getChildren().add(playMixWave);
        menu.getChildren().add(slider);
        menu.getChildren().add(msge);
        menu.setSpacing(10);
        menu.setPadding(new Insets(5,5,5,5));

*/
        //show
        Scene scene = new Scene(bp,800,350,Color.PLUM);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void playNetwork() {
        System.out.println("Playing Network");
        for(AudioComponent ac : speakerConnections_){
            Clip c = null;
            try{
                System.out.println(("Playing " + ac.toString()));
                AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

                c = AudioSystem.getClip();

                AudioListener listener = new AudioListener(c);
                byte[] data =ac.getClip().getData();

                c.open(format16, data, 0, data.length);

                c.start();

                c.addLineListener(listener);


            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            }

            }
        }

    private void creatAudioComponent(VBox rightSide) {
        rightSide.setPadding(new Insets(10));
        rightSide.setSpacing(30);

        Label label = new Label("Audio Components");
        label.setUnderline(true);


        Button sineWaveAc = new Button("Sine Wave AC");
        Button mixWaveAc =new Button("Mix Wave AC");
        Button linearRamp = new Button("Linear Ramp AC");


        rightSide.getChildren().addAll(label,sineWaveAc, mixWaveAc,linearRamp);
        sineWaveAc.setOnAction(e -> createAcComponent("SineWave"));
        mixWaveAc.setOnAction(e -> createAcComponent("Mixer"));
        linearRamp.setOnAction(e -> createAcComponent("LinearRamp"));
    }


    private void createAcComponent(String componentName){
        switch (componentName){
            case "SineWave" :
                SineWave sw =new SineWave(440);
                AudioComponentWidget acw =new AudioComponentWidget(sw, canvas,"SineWave");
                break;

            case "Mixer" :
                VolumeAdjuster v_C = new VolumeAdjuster(0.3f);
                v_C.connectInput(0, C_do);
                VolumeAdjuster v_E = new VolumeAdjuster(0.3f);
                v_E.connectInput(0, E_mi);
                VolumeAdjuster v_G = new VolumeAdjuster(0.3f);
                v_G.connectInput(0, G_sol);
                Mixers mixers = new Mixers(3);
                //test of C major chord
                mixers.connectInput(0, v_C);
                mixers.connectInput(1, v_E);
                mixers.connectInput(2, v_G);
                AudioComponentWidget acwMix = new AudioComponentWidget(mixers,canvas,"Mixer");
                break;

            case "LinearRamp":
                //TEST OD LINEARRAMP

                LinearRamp linearRamp = new LinearRamp(50,2000);

                FrequencyWaveGenerator frequencyWaveGenerator = new FrequencyWaveGenerator();
                frequencyWaveGenerator.connectInput(0,linearRamp);
                //frequencyWaveGenerator.getClip();
                AudioComponentWidget acLinear = new AudioComponentWidget(frequencyWaveGenerator,canvas,"LinearRamp");
                break;
        }

    }

    private void musicBT(HBox top) {
        Button C = new Button("C");
        C.setOnAction(e -> playKeys("C"));
        Button D = new Button("D");
        D.setOnAction(e -> playKeys("D"));
        Button E = new Button("E");
        E.setOnAction(e -> playKeys("E"));
        Button F = new Button("F");
        F.setOnAction(e -> playKeys("F"));
        Button G = new Button("G");
        G.setOnAction(e -> playKeys("G"));
        Button A = new Button("A");
        A.setOnAction(e -> playKeys("A"));
        Button B = new Button("B");
        B.setOnAction(e -> playKeys("B"));
        Button Chord = new Button("Chord");
        Chord.setOnAction(e -> playKeys("Chord"));
        top.getChildren().addAll(C,D,E,F,G,A,B,Chord);
        top.setSpacing(20);
        //top.setPadding(new Insets(5,5,5,5));
    }


    //method for menu bar moving around


/*    private void handleDrag(javafx.scene.input.MouseEvent e) {
        System.out.println("Drag" +e);
        HBox hBox =(HBox) e.getSource();

        hBox.setLayoutX(e.getSceneX());
        hBox.setLayoutY(e.getSceneY());
    }*/

    //methods
    //method for slider bar
/*    private Text sliderText = new Text();
    private void handleSlider(Slider slider, Text msge) {
    msge.setText(Double.toString( slider.getValue()));
        double value = slider.getValue();
        System.out.println("handle slider: " + value);
        sliderText.setText(String.format("%3.2f",value));
    }*/

    //method for sine wave botton

    private void playKeys(String note ) {
        //msge.setText(" Sine Wave is Processed");
        AudioClip clip;



        switch (note) {
            case "C" -> clip = C_do.getClip();
            case "D" -> clip = D_re.getClip();
            case "E" -> clip = E_mi.getClip();
            case "F" -> clip = F_fa.getClip();
            case "A" -> clip = A_la.getClip();
            case "B" -> clip = B_si.getClip();
            case "G" -> clip = G_sol.getClip();
            default -> {
                VolumeAdjuster v_C = new VolumeAdjuster(0.3f);
                v_C.connectInput(0, C_do);
                VolumeAdjuster v_E = new VolumeAdjuster(0.3f);
                v_E.connectInput(0, E_mi);
                VolumeAdjuster v_G = new VolumeAdjuster(0.3f);
                v_G.connectInput(0, G_sol);
                Mixers mixers = new Mixers(3);
                //test of C major chord
                mixers.connectInput(0, v_C);
                mixers.connectInput(1, v_E);
                mixers.connectInput(2, v_G);
                clip = mixers.getClip();
            }
        }
        try {
            Clip c = AudioSystem.getClip();
            AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

            AudioListener listener = new AudioListener(c);
            c.open(format16, clip.getData(), 0, clip.getData().length);

            c.start();
            c.addLineListener(listener);

            c.loop(0);

            System.out.println("playing sound");

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    //method for sine wave botton
  /*  private void handlePressMix(Text msge) throws LineUnavailableException {
        msge.setText(" Mix Wave Processed");

        Clip c = AudioSystem.getClip();
        AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);

        int octave = 2;
        AudioComponent C = new SineWave((int) (261.6 * octave));

        AudioComponent E = new SineWave((int) (261.6 * octave));


        AudioComponent G = new SineWave((int) (261.6 * octave));

        VolumeAdjuster v_C = new VolumeAdjuster(0.3f);
        v_C.connectInput(0, C);

        VolumeAdjuster v_E = new VolumeAdjuster(0.3f);
        v_E.connectInput(0, E);

        VolumeAdjuster v_G= new VolumeAdjuster(0.3f);
        v_G.connectInput(0, G);

        Mixers mixers = new Mixers( 3);
       //test of C major chord
       mixers.connectInput(0,v_C);
       mixers.connectInput(1,v_E);
       mixers.connectInput(2,v_G);
      AudioClip clip1 = mixers.getClip();


        c.open(format16, clip1.getData(), 0, clip1.getData().length);

        c.start();

        c.loop(0);


    }*/
    int octave = 1;
    AudioComponent C_do = new SineWave((int) (261.6 * octave));
    AudioComponent D_re = new SineWave( (int) (293.6 * octave));
    AudioComponent E_mi = new SineWave( (int) (329.6*octave));
    AudioComponent F_fa = new SineWave( (int) (349.2*octave));
    AudioComponent G_sol = new SineWave( 392*octave);
    AudioComponent A_la = new SineWave( 440*octave);
    AudioComponent B_si = new SineWave( (int) 493.8 * octave);

}
