public class Mixers implements AudioComponent {
    //two variables
    float frequency_;
    AudioComponent[] inputs;

    public Mixers( int numInputs ) {
        //frequency_ = frequency;
        inputs = new AudioComponent[ numInputs ];
    }

    @Override
    public AudioClip getClip() {
        AudioClip temp = new AudioClip();

        for (int i = 0; i < inputs.length; i++) {
            AudioComponent ac = inputs[i];
            if( ac != null ) {
                AudioClip clip = ac.getClip();
                for (int j = 0; j < AudioClip.TOTAL_SAMPLES; j++) {
                    temp.setSample(j, clip.getSample(j) + temp.getSample(j));
                }
            }
        }
        return temp;
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public void connectInput(int index, AudioComponent input) {
        // check to make sure index between 0 and length -1
        if(index<0 || index>inputs.length-1){
            System.out.println("ERROR!");
            System.exit(-1);
        }
        inputs[index] = input;
    }
}
