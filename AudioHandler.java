package music;

import net.dv8tion.jda.core.audio.AudioSendHandler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

public class AudioHandler implements AudioSendHandler{

	private final AudioPlayer audioPlayer;
	private AudioFrame lastFrame;
	
	public AudioHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}
	
	@Override
	public boolean canProvide() {
		if (lastFrame == null) lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		byte[] data = canProvide() ? lastFrame.data : null;
		lastFrame = null;
		
		return data;
	}
	
	@Override
	public boolean isOpus(){
		return true;
	}

}
