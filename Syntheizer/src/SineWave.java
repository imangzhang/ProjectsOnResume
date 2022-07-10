public class SineWave implements AudioComponent{

    //set a variable of frequency that control the sounds
    private int frequency_;


    //set a defalt value for frequency
    public SineWave(int i){
        frequency_= i;
    }

    @Override
    //first sound
    public AudioClip getClip() {
        //open a new area to store the value of the sound
        AudioClip firstSound = new AudioClip();
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            //math function
            firstSound.setSample(i, (int) (Short.MAX_VALUE * Math.sin(2 * Math.PI * frequency_ * i / AudioClip.sampleRate)));
        }
        return firstSound;
    }

    @Override
    // false
    public boolean hasInput() {
        return false;
    }

    @Override
    //false
    public void connectInput(int index, AudioComponent input) {

         assert(false);

    }
}
