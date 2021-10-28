package global;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SimpleAudioPlayer {

	private String filePath = "C:\\Users\\Evan_\\Desktop\\UTC Senior\\CPSC 4900\\Sounds\\dealing_card";
	private String filePath2 = "C:\\Users\\Evan_\\Desktop\\UTC Senior\\CPSC 4900\\Sounds\\card_contact";
	private AudioInputStream aIS;
	private Clip clip;
		
	
	public SimpleAudioPlayer(String fp) throws UnsupportedAudioFileException, IOException, LineUnavailableException 
	{
		// create AudioInputStream object
		aIS = AudioSystem.getAudioInputStream(new File(fp).getAbsoluteFile());
			              
		// create clip reference
		clip = AudioSystem.getClip();
		// open audioInputStream to the clip
		clip.open(aIS);
		clip.loop(0);
	 }
	
	public SimpleAudioPlayer(String fp, int loops) throws UnsupportedAudioFileException, IOException, LineUnavailableException 
	{
		// create AudioInputStream object
		aIS = AudioSystem.getAudioInputStream(new File(fp).getAbsoluteFile());
			              
		// create clip reference
		clip = AudioSystem.getClip();
		// open audioInputStream to the clip
		clip.open(aIS);
		clip.loop(loops);
	 }
	
	public void play() 
    {
        //start the clip
        clip.start();
    }
	
	public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException 
    {
        clip.stop();
        clip.close();
    }
	
	public Clip getClip() 
    {
        //start the clip
        return clip;
    }
	
	public void resetAudioStream(String fp) throws UnsupportedAudioFileException, IOException, LineUnavailableException 
	{
		aIS = AudioSystem.getAudioInputStream(
				new File(fp).getAbsoluteFile());
		clip.open(aIS);
		clip.loop(0);
	}
}
