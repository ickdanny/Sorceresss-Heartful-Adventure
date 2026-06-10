import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//manages the loading of all resources (visuals, audio, text files, etc)
public class ResourceManager {
    //text file in which the metadata is stored
    //e.x. data would be stored in a format [type][keyName][path]
    //in this example type would be sprite, track, dialogue, etc
    //for sheets format is [sheet][data][sheet]
    private final static String DATA_LOCATION = "./res/data.txt";

    private static Map<String, BufferedImage> images = new HashMap<>();
    //this used to have BufferedInputStreams but apparently that was bad
    private static Map<String, String> tracks = new HashMap<>();
    //n x 2 matrix of strings
    //[n][0] is the command description, [n][1] is the message
    private static Map<String, String[][]> dialogue = new HashMap<>();

    private static boolean extraUnlocked;
    private static boolean soundOn;
    private static double frameSizeMulti;
    private static String defaultSynth;

    private static final String DEFAULT_SYNTH_DEFAULT = "Microsoft GS Wavetable Synth";

    private ResourceManager(){}

    public static void init(){
        //init all resources going through DATA_LOCATION
        initGameResources();
        //check data files
        readFrameSizeMulti();
        readDefaultSynth();
        readSoundOn();
        readExtraUnlocked();
    }
    private static void initImage(String key, String path){
        try{
            images.put(key, ImageIO.read(new File(path)));
        }catch(Exception e){
        }
    }
    private static void initSheet(String dataPath, String imagePath){
        //data will be written in format [key]<x,y><width,height>
        try{
            File dataFile = new File(dataPath);
            BufferedImage image = ImageIO.read(new File(imagePath));
            Scanner dataScanner = new Scanner(dataFile);
            while(dataScanner.hasNext()){
                String line = dataScanner.nextLine();
                String key = line.substring(1, line.indexOf(']'));
                line = line.substring(line.indexOf(']') + 1);
                String v1 = line.substring(0, line.indexOf('>') + 1);
                line = line.substring(line.indexOf('>') + 1);
//                String v2 = line.substring(1, line.indexOf('>'));
                String v2 = line;

                //convert integers
                int x = Integer.parseInt(v1.substring(1, v1.indexOf(',')));
                int y = Integer.parseInt(v1.substring(v1.indexOf(',') + 1, v1.indexOf('>')));
                int width = Integer.parseInt(v2.substring(1, v2.indexOf(',')));
                int height = Integer.parseInt(v2.substring(v2.indexOf(',') + 1, v2.indexOf('>')));

                images.put(key, image.getSubimage(x, y, width, height));
            }
        }catch(Exception e){
        }
    }
    private static void initTrack(String key, String path){
        try {
            tracks.put(key, path);
        }catch(Exception e){
        }
    }
    private static void initDialogue(String key, String path){
        try{
            File dialogueFile = new File(path);
            ArrayList<String[]> arrayList = new ArrayList<>();
            Scanner dialogueScanner = new Scanner(dialogueFile);
            while(dialogueScanner.hasNext()){
                String line = dialogueScanner.nextLine();
                String command = line.substring(1, line.indexOf(']'));
                String s1;
                if(line.indexOf(']') + 1 != line.length()) {
                    line = line.substring(line.indexOf(']') + 1);
                    s1 = line.substring(1, line.indexOf(']'));
                }
                else{
                    s1 = "";
                }
                arrayList.add(new String[]{command, s1});
            }
            //write contents of array into array of arrays (which we put into the map)
            dialogue.put(key, arrayList.toArray(new String[arrayList.size()][2]));
        }catch(Exception e){
        }
    }
    public static BufferedImage getImage(String key){
        return images.get(key);
    }
    public static String getTrackPath(String key){
        return tracks.get(key);
    }
    public static String[][] getDialogue(String key){
        return dialogue.get(key);
    }

    //only called when need to swap to true
    public static void unlockExtra(){
        String fileName = "extra.txt";
        File file = new File(fileName);
        try{
            extraUnlocked = true;
            FileWriter writer = new FileWriter(file, false);
            writer.write("true");
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void toggleSound(){
        String fileName = "sound.txt";
        File file = new File(fileName);
        try{
            soundOn = !soundOn;
            FileWriter writer = new FileWriter(file, false);
            writer.write("" + soundOn);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //if sound is being turned off tell the MusicDriver
        if(!soundOn){
            MusicDriver.stopTrack();
        }else{
            //if sound is being turned ON play the menu track
            MusicDriver.playTrack("menu", true);
        }
    }
    public static boolean isExtraUnlocked() {
        return extraUnlocked;
    }
    public static boolean isSoundOn() {
        return soundOn;
    }
    public static double getFrameSizeMulti() {
        return frameSizeMulti;
    }
    public static String getDefaultSynth() {
        return defaultSynth;
    }

    private static void initGameResources(){
        File data = new File(DATA_LOCATION);
        try {
            Scanner dataScanner = new Scanner(data);
            while(dataScanner.hasNext()) {
                String line = dataScanner.nextLine();
                //should be "type" //indexof since substring ends at the index before endIndex
                String type = line.substring(1, line.indexOf(']'));
                //starts substring on char after ]
                line = line.substring(line.indexOf(']') + 1);
                String s1 = line.substring(1, line.indexOf(']'));
                line = line.substring(line.indexOf(']') + 1);
                String path = "./res/" + type + "/" + line.substring(1, line.indexOf(']'));
                switch (type) {
                    case "image":
                        initImage(s1, path);
                        break;
                    //sheet takes two paths
                    case "sheet":
                        initSheet("./res/" + type + "/" + s1, path);
                        break;
                    case "track":
                        initTrack(s1, path);
                        break;
                    case "dialogue":
                        initDialogue(s1, path);
                        break;
                }
            }
        } catch(FileNotFoundException e){
        }
    }
    private static void readExtraUnlocked(){
        String fileName = "extra.txt";
        File file = new File(fileName);
        try{
            String line;
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                line = scanner.nextLine();
                extraUnlocked = Boolean.parseBoolean(line);
            }else{
                throw new Exception();
            }
        }
        catch(FileNotFoundException e){
            //if file not found, create the file and write 1
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write("false");
                writer.close();
                extraUnlocked = false;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
        catch(Exception e){
            //if file is unreadable, delete and reset
            try {
                FileWriter writer = new FileWriter(file, false);
                writer.write("false");
                writer.close();
                extraUnlocked = false;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }
    private static void readSoundOn(){
        String fileName = "sound.txt";
        File file = new File(fileName);
        try{
            String line;
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                line = scanner.nextLine();
                soundOn = Boolean.parseBoolean(line);
            }else{
                throw new Exception();
            }
        }
        catch(FileNotFoundException e){
            //if file not found, create the file and write 1
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write("true");
                writer.close();
                soundOn = true;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
        catch(Exception e){
            //if file is unreadable, delete and reset
            try {
                FileWriter writer = new FileWriter(file, false);
                writer.write("true");
                writer.close();
                soundOn = true;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }
    private static void readFrameSizeMulti(){
        String fileName = "frame_size_multi.txt";
        File file = new File(fileName);
        try{
            String line;
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                line = scanner.nextLine();
                frameSizeMulti = Double.parseDouble(line);
            }else{
                throw new Exception();
            }
        }
        catch(FileNotFoundException e){
            //if file not found, create the file and write 1
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write("1");
                writer.close();
                frameSizeMulti = 1;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
        catch(Exception e){
            //if file is unreadable, delete and reset
            try {
                //think don't need to delete file, append->false means overwriting
                FileWriter writer = new FileWriter(file, false);
                writer.write("1");
                writer.close();
                frameSizeMulti = 1;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }
    private static void readDefaultSynth(){
        String fileName = "default_synth.txt";
        File file = new File(fileName);
        try{
            String line;
            Scanner scanner = new Scanner(file);
            if(scanner.hasNextLine()){
                line = scanner.nextLine();
                defaultSynth = line;
            }else{
                throw new Exception();
            }
        }
        catch(FileNotFoundException e){
            //if file not found, create the file and write 1
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file, false);
                writer.write(DEFAULT_SYNTH_DEFAULT);
                writer.close();
                defaultSynth = DEFAULT_SYNTH_DEFAULT;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
        catch(Exception e){
            //if file is unreadable, delete and reset
            try {
                //think don't need to delete file, append->false means overwriting
                FileWriter writer = new FileWriter(file, false);
                writer.write(DEFAULT_SYNTH_DEFAULT);
                writer.close();
                defaultSynth = DEFAULT_SYNTH_DEFAULT;
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }
}