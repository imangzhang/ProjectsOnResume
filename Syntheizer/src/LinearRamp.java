public class LinearRamp implements AudioComponent{
    float start_;
    float stop_;

    public LinearRamp (float start, float stop){
        start_= start;
        stop_=stop;
    }

    @Override
    public AudioClip getClip() {
    AudioClip sample  = new AudioClip();

    for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            //math function
        sample.setSample(i, (int)(start_*(AudioClip.TOTAL_SAMPLES -i)+ stop_*i)/(AudioClip.TOTAL_SAMPLES));
        }
    return sample;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(int index, AudioComponent input) {
        assert(false);

    }
}
