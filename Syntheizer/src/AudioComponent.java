
public interface AudioComponent {

    //return the current sound produced by this component
    AudioClip getClip();

    //check if there is anything connected as an input
    boolean hasInput();

    //connect another device to this input.
    // For most classes implementing this interface,
    // this method will just store a reference to the AudioComponent
    // parameter.
    void connectInput(int index, AudioComponent input);

}
