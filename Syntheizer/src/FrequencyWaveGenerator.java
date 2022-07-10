public class FrequencyWaveGenerator implements AudioComponent{
   AudioComponent input_;

   public FrequencyWaveGenerator(){
   }

    @Override
    public AudioClip getClip() {
       AudioClip original = input_.getClip();
       AudioClip result = new AudioClip();

        double phase =0;

        for(int i=0; i<AudioClip.TOTAL_SAMPLES;i++){
            phase += 2 * Math.PI * original.getSample(i)/AudioClip.sampleRate;
            result.setSample(i,(int)(Short.MAX_VALUE*Math.sin(phase)));
        }
        return result;
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public void connectInput(int index, AudioComponent input) {
        if(index!=0){
            System.out.println("ERROR!");
            System.exit(-1);
        }
        input_ =input;
    }
}
