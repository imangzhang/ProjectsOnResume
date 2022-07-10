import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioClipTest {
@Test
public void Test(){
    AudioClip test = new AudioClip();
    test.setSample(0, Short.MAX_VALUE);
    test.setSample(1, Short.MIN_VALUE);

    System.out.println(test.getSample(0));
    System.out.println(test.getSample(1));


    }

}