import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioTest{
	java.io.InputStream is= getClass().getResourceAsStream("Images/BLACK_PANTHER_Trailer_2_Version_Music_Proper_Official_Movie_Soundtrack_Complete_Theme_Song_MOKU-s75eUxUxjZw.wav"); 
	AudioInputStream audioInputStream;
	Timer timer = new Timer();
	Clip clip = null;
	boolean bool = false;
	public AudioTest() {
		java.io.InputStream is= getClass().getResourceAsStream("Images/BLACK_PANTHER_Trailer_2_Version_Music_Proper_Official_Movie_Soundtrack_Complete_Theme_Song_MOKU-s75eUxUxjZw.wav"); 
		AudioInputStream audioInputStream = null;
	}
	public void playAudio() {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//AudioInputStream audioInputStream = null;
		/*
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();
		timer.schedule(new ReminderTask(),30*1000);
		System.out.println("SUP");
	}
	class ReminderTask extends TimerTask{


		public void run() {
			// TODO Auto-generated method stub
			clip.stop();
		}

	}
	public static void main(String[] args) {
		AudioTest audio = new AudioTest();
		audio.playAudio();
		
	}
}
