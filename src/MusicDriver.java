import javax.sound.midi.*;
import java.io.*;

public class MusicDriver {
    private static String trackName;
    private static boolean loop;
    private static int timer = 0;
    private static int TIMER_BASE = 25;
    private static Sequencer SEQ;

    private MusicDriver(){}

    public static void update(){
        if(timer == 1){
            startTrack();
        }
        if(timer > 0){
            --timer;
        }
    }
    public static void playTrack(String trackName, boolean loop){
        if(!ResourceManager.isSoundOn()){
            return;
        }
        if(SEQ == null){
            initSEQ();
        }
        if(trackName.equals(MusicDriver.trackName)){
            return;
        }
        MusicDriver.trackName = trackName;
        MusicDriver.loop = loop;
        timer = TIMER_BASE;
            SEQ.stop();
    }
    public static void startTrack(){
        if(!ResourceManager.isSoundOn()){
            return;
        }
        if(SEQ == null){
            initSEQ();
        }
        try {
            String path = ResourceManager.getTrackPath(trackName);

            File file = new File(path);
            InputStream is = new BufferedInputStream(new FileInputStream(file));

            SEQ.setSequence(is);

            if (loop) {
                SEQ.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            } else {
                SEQ.setLoopCount(0);
            }

            SEQ.stop();

            SEQ.start();
        }catch(InvalidMidiDataException e){
        }catch(IOException e){
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void stopTrack(){
        SEQ.stop();
        trackName = "";
    }

    public static void init(){
        initSEQ();
        if(ResourceManager.isSoundOn()) {
            playTrack("menu", true);
        }
    }

    private static void initSEQ(){
        try{
            SEQ = MidiSystem.getSequencer();
            //try to connect to the right device
            MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
            for(MidiDevice.Info info : midiDevices){
                String name = info.getName();
                if(name.equals(ResourceManager.getDefaultSynth())){
                    //if we found the correct device hook it up with the sequencer
                    MidiDevice midiOutDevice = MidiSystem.getMidiDevice(info);
                    Receiver midiOutReceiver = midiOutDevice.getReceiver();
                    //clear out the current receiver //do I need this?
                    for(Receiver r : SEQ.getReceivers()){
                        r.close();
                    }
                    //clear out the current transmitters
                    for(Transmitter t : SEQ.getTransmitters()){
                        t.close();
                    }
                    //as far as I can tell getTransmitter actually creates a new transmitter
                    //and adds it to the list of transmitters of the midi device
                    Transmitter transmitter = SEQ.getTransmitter();
                    if(transmitter.getReceiver() != null) {
                        transmitter.getReceiver().close();
                    }
                    //add the new receiver
                    transmitter.setReceiver(midiOutReceiver);
                    //very important apparently lol
                    midiOutDevice.open();
                    break;
                }
            }
            SEQ.open();
        }
        catch(Exception e){
        }
    }
}
