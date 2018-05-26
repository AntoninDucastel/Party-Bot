package music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class MusicManager {

	private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
	private final Map<String, MusicPlayer> player = new HashMap<>();
	
	public MusicManager() {
		AudioSourceManagers.registerRemoteSources(manager);
		AudioSourceManagers.registerLocalSource(manager);
	}
	
	public synchronized MusicPlayer getPlayer(Guild guild) {
		if(!player.containsKey(guild.getId()))player.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
		return player.get(guild.getId());
	}
	
	public void loadTrack(final TextChannel channel, final String source) {
		MusicPlayer player = getPlayer(channel.getGuild());
		
		channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());
		
		manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				StringBuilder builder = new StringBuilder();
				builder.append("J'ai ajouté la musique ** *").append(track.getInfo().title).append("* ** à la playlist. :heart:\n");
				channel.sendMessage(builder.toString()).queue();
				player.playTrack(track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				StringBuilder builder = new StringBuilder();
				builder.append("J'ai ajouté la playlist ** *").append(playlist.getName()).append("* ** :heart:\n");
				
				for (int i = 0; i < playlist.getTracks().size() && i < 5; i++) {
					AudioTrack track = playlist.getTracks().get(i);
					player.playTrack(track);
				}
				channel.sendMessage(builder.toString()).queue();
			}
			
			/*public void showplaylist(AudioPlaylist playlist) {
				StringBuilder builder = new StringBuilder();
				builder.append("Voici le programme de la playlist **").append(playlist.getName()).append("**\n");
				
				for (int i = 0; i < playlist.getTracks().size() && i < 5; i++) {
					AudioTrack track = playlist.getTracks().get(i);
					builder.append("J'ai ajouté la musique **").append(track.getInfo().title).append("** à la playlist. :heart:\n");
					channel.sendMessage(builder.toString()).queue();
				}
				channel.sendMessage(builder.toString()).queue();
			}*/
			
			
			@Override
			public void noMatches() {
				channel.sendMessage("La piste " + source + " n'a pas été trouvé.").queue();
				
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Impossible de jouer la piste car : " + exception.getMessage() + ".").queue();
				
			}
		});
	}
}
