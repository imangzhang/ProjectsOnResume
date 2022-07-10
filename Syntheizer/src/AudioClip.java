import java.util.Arrays;

public class AudioClip {

    //set a static constant duration:2.0 seconds
    public static final double duration = 2.0;
    //set a static constant sample rate: 44100
    public static final int sampleRate = 44100;
    public static final int TOTAL_SAMPLES = (int)(duration*sampleRate);

    //a member variable that contains the actual byte array
    byte[] data;

    //set the member variable a constructor with a defalt value which equals
    //duration * sample rate * 2 (1 short = 2 bytes)
    AudioClip() {
        data = new byte[(int) duration * sampleRate * 2];
    }
    //set the sample passed as an int for one short
    public void setSample(int index, int value) {

        if( value > Short.MAX_VALUE || value < Short.MIN_VALUE ) {
            System.out.println("bad value for audio clip");
        }
        //set the value for index*2
        byte b1 = (byte) (value << 8 >>> 8);
        //set the value for index*2+1
        byte b2 = (byte) (value >>> 8);

        //the lower 8 bits should be stored at array[2*i]
        data[index * 2] = b1;
        //the upper 8 bits should be stored at array[(2*i)+1]
        data[index * 2 + 1] = b2;
    }

    //get the values of an index for one short
    //value = index *2 + index
    public int getSample(int index) {
        //get the value of one of the index
        //have to use toUnsigedIn because b1 is -1, and it fills with all 1 in binary
        //if we don't use toUnsigedIn, the output will be -1 as toUnsiged will change the
        //first 1 to 0 in binary
        int b1 = Byte.toUnsignedInt(data[index * 2]);
        //get the value of the index next to the set index
        int b2 = data[index * 2 + 1];
        int value = b2 << 8 | b1;

        return value;
    }

    //return a copy of the array we set
    public byte[] getData() {

        byte[] getData = Arrays.copyOf(data, data.length);

        return getData;
    }
}
