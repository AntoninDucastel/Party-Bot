package music;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import command.Commandes;
import command.Commandes.ExecutorType;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class MusicCommand {

	private final MusicManager manager = new MusicManager();
	
	/* MENU COMMANDES
	 * 	-play
	 * 	-skip
	 * 	-clear
	 * 	-show
	 * 	-leave
	 */
	
	@Commandes(name="play", type=ExecutorType.USER, description="En avant la musique !!\n\t\t=play *URLmusique*\n")
	private void  play(Guild guild, TextChannel textChannel, User user, String command, MusicManager track) {
		
		if (guild == null) return;
		
		if (!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()) {
			VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
			if(voiceChannel == null) {
				textChannel.sendMessage("Vous devez être connecté à un salon vocal.").queue();
				return;
			}
			guild.getAudioManager().openAudioConnection(voiceChannel);
		}
		manager.loadTrack(textChannel, command.replaceFirst("play ", ""));
	}
	
	@Commandes(name="skip", type=ExecutorType.USER, description="On passe à la suivante ?\n\t\t=skip\n")
	private void skip(Guild guild, TextChannel textChannel, String command) {
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()) {
			textChannel.sendMessage("Le player n'as pas de piste en cours.").queue();
			return;
		}
		manager.getPlayer(guild).skipTrack();
		textChannel.sendMessage("La lecture est passé à la piste suivante.").queue();
	}
	
	@Commandes(name="clear", type=ExecutorType.USER, description="Effacer la liste d'attente.\n\t\t=clear\n")
	private void clear(TextChannel textChannel) {
		MusicPlayer player = manager.getPlayer(textChannel.getGuild());
		
		if(player.getListener().getTracks().isEmpty()) {
			textChannel.sendMessage("Il n'y a pas de piste dans la liste d'attente.").queue();
			return;
		}
		
		player.getListener().getTracks().clear();
		textChannel.sendMessage("La liste d'attente a été vidé").queue();
	}

	@Commandes(name="leave", type=ExecutorType.USER, description="Au revoir.\n\t\t=leave\n")
	private void leave(Guild guild, TextChannel textChannel, User user, String command) {
		MusicPlayer player = manager.getPlayer(textChannel.getGuild());
		if(!guild.getAudioManager().isConnected()) {
			textChannel.sendMessage("Je suis déjà hors du salon. :smile:").queue();
		}
		else {
			//DEBUT - Faire quitter le bot du salon
			manager.getPlayer(guild).getAudioPlayer().stopTrack();
			player.getListener().getTracks().clear();
			manager.getPlayer(guild).skipTrack();
			//FIN - Faire quitter le bot du salon
			textChannel.sendMessage("J'ai quitté le salon. Ré-invitez-moi vite !!! :blush:").queue();
		}
	}
	
	@Commandes(name="info", type=ExecutorType.USER, description="C'est quoi cette musique ?\n\t\t=info\n")
	private void info(Guild guild, TextChannel textChannel, User user, String command, AudioPlaylist playlist, AudioTrack track) {
		
		MusicPlayer player = manager.getPlayer(textChannel.getGuild());
		
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()) {
			textChannel.sendMessage("Je ne suis pas en train de diffuser de musique. :cry:").queue();
		}
		else {
			//textChannel.sendMessage("Voici le programme :\n" /*+ playlist.getTracks().size()*/).queue();

				textChannel.sendMessage("Le nom du titre actuel est : ** *" + player.getAudioPlayer().getPlayingTrack().getInfo().title + "* **.").queue();
		}
	}// FIN DE METHODE "info"
}//FIN DE CLASSE
