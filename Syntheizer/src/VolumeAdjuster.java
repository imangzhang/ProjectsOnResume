public class VolumeAdjuster implements AudioComponent {
//Filters are audio components that have input
// components connected to them.

    //set a variable: scale
    float scale_;
    AudioComponent input_;

    public VolumeAdjuster(float scale) {
        //set defalt value for scale
        scale_ = scale;
        //inputs = new AudioComponent[1];
        //AudioClip input = new AudioClip();

    }

        @Override
        public AudioClip getClip() {
        //set an AudioClip and put the input AudioClip in there
        AudioClip original = input_.getClip();
        //define an empty AudioClip
        AudioClip result = new AudioClip();

        //
        for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            //value = scale * original value
            int value = (int)(scale_ * original.getSample(i));
            //use clamping to limit the output to provent it will have wired sounds come out
            value = Math.min(Short.MAX_VALUE, Math.max(Short.MIN_VALUE,value));

            //put all new value to new AudioClip
            result.setSample(i, value);

        }
        return result;

    }

        @Override
        //true
        public boolean hasInput () {
            return true;
        }

        @Override
        //to make sure index !=0
        public void connectInput(int index, AudioComponent input){
            // if index != 0 then error
            if(index!=0){
                System.out.println("ERROR!");
                System.exit(-1);
            }
            input_ = input;
        }
}



