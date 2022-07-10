
import javafx.application.Application;
/*
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Main {
    public static void main(String[] args) throws LineUnavailableException {
// Get properties from the system about samples rates, etc.
// AudioSystem is a class from the Java standard library.

        Clip c = AudioSystem.getClip(); //terrible name, different from our AudioClip class

// This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

        //sound text for mixer
        int octave = 2;
        AudioComponent C_do = new SineWave((int) (261.6 * octave));
        AudioComponent D_re = new SineWave( (int) (293.6 * octave));
        AudioComponent E_mi = new SineWave( (int) (329.6*octave));
        AudioComponent F_fa = new SineWave( (int) (349.2*octave));
        AudioComponent G_sol = new SineWave( 392*octave);
        AudioComponent A_la = new SineWave( 440*octave);
        AudioComponent B_si = new SineWave( (int) 493.8 * octave);
        AudioClip clip = C_do.getClip();



        VolumeAdjuster v_E = new VolumeAdjuster(0.3f);
        v_E.connectInput(0, E_mi);

        VolumeAdjuster v_C = new VolumeAdjuster(0.3f);
        v_C.connectInput(0, C_do);

        VolumeAdjuster v_D = new VolumeAdjuster(0.3f);
        v_D.connectInput(0, D_re);

        VolumeAdjuster v_F = new VolumeAdjuster(0.3f);
        v_F.connectInput(0,F_fa);

        VolumeAdjuster v_G = new VolumeAdjuster(0.3f);
        v_G.connectInput(0, G_sol);

        VolumeAdjuster v_A = new VolumeAdjuster(0.3f);
        v_A.connectInput(0, A_la);

        VolumeAdjuster v_B = new VolumeAdjuster(0.3f);
        v_B.connectInput(0, B_si);
//        Mixers mixers = new Mixers( 7);
//        //test of C major chord
//        mixers.connectInput(0,v_C);
//        mixers.connectInput(1,v_E);
//        mixers.connectInput(2,v_G);
      /// clip = v_A.getClip();

        //TEST OD LINEARRAMP
//        LinearRamp linearRamp = new LinearRamp(50,2000);
//       // clip = linearRamp.getClip();
////
//        FrequencyWaveGenerator frequencyWaveGenerator = new FrequencyWaveGenerator();
//        frequencyWaveGenerator.connectInput(0,linearRamp);
//        clip=frequencyWaveGenerator.getClip();




*//*     SOUND TEST FOR THE FIRST SOUND!!!YAY!!!
        AudioClip clip;
        SineWave sw1 = new SineWave(1000);
        VolumeAdjuster v1 = new VolumeAdjuster(0.5f);
        v1.connectInput(0,sw1);

        SineWave sw2 =new SineWave((1200));
        VolumeAdjuster v2 =new VolumeAdjuster(0.5f);
        v2.connectInput(0,sw2);

        Mixers mixers =new Mixers(2);
        mixers.connectInput(0,v1);
        mixers.connectInput(1,v2);
        clip =mixers.getClip();*//*



        c.open( format16, clip.getData(), 0, clip.getData().length ); // Reads data from our byte array to play it.

        System.out.println( "About to play..." );
        Application.launch(AudioComponentWidget.class);

        c.start(); // Plays it.
        //c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total

// Makes sure the program doesn't quit before the sound plays.
        while( c.getFramePosition() < AudioClip.TOTAL_SAMPLES || c.isActive() || c.isRunning() ){
            // Do nothing.
        }

        System.out.println( "Done." );
        c.close();

    }
}*/

public class Main{
    public static void main(String[] args){
        Application.launch(SynthApp.class);
    }
}