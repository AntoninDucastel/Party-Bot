package command.defaut;

import java.awt.Color;
import java.util.Random;

import command.CommandMap;
import command.Commandes;
import command.Commandes.ExecutorType;
import event.MainBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandDefault {

	private final MainBot mainBot;
	private final CommandMap commandMap;
	
	public CommandDefault(MainBot mainBot, CommandMap commandMap) {
		this.mainBot = mainBot;
		this.commandMap = commandMap;
	}
	
	@Commandes (name="stop",type=ExecutorType.CONSOLE)
	private void Stop() {
		mainBot.setRunning(false);
	}
	
	/*@Commandes(name="info",type=ExecutorType.USER)
	private void info(User user, MessageChannel channel) {
		channel.sendMessage(user.getAsMention() + " est dans le channel " + channel.getName()
						    "Voici la liste des commandes que je suis capable d'executer :\n  - bonjour\n  - info\n  - iloveyou\n(Noubliez pas le préfixe '=' devant chacune de mes commandes)").complete();
		if (channel instanceof TextChannel) {
			TextChannel textChannel = (TextChannel)channel;
			if(!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) return;
		}
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(user.getName(), null, user.getAvatarUrl()+"?size=256" );
		builder.setTitle("Informations");
		builder.setDescription("Voici la liste des commandes que je suis capable d'executer :"
				+ "\n  [>](1) bonjour "
				+ "\n  [>](1) info "
				+ "\n  [>](1) iloveyou "
				+ "\n  [>](1) game "
				+ "\n  (Noubliez pas le préfixe '=' devant chacune de mes commandes)");
		builder.setColor(Color.orange);
		
		channel.sendMessage(builder.build()).queue();
	}*/
	
	@Commandes(name="bonjour",type=ExecutorType.USER, description="Dites-moi simplement bonjour.\n\t\t=bonjour\n")
	private void bonjour(User user, MessageChannel channel) {
		channel.sendMessage("Bonjour " + user.getAsMention() + " !!  :grin:").complete();
	}
	
	@Commandes(name="iloveyou",type=ExecutorType.USER, description="Parce que l'on a toujours besoin d'amour.\n\t\t=iloveyou\n")
	private void iloveyou(User user, MessageChannel channel) {
		channel.sendMessage("Moi aussi je vous aime " + user.getAsMention() + " !! :heart:").complete();
	}
	
	@Commandes(name="game",type=ExecutorType.USER, description="Recrutement d'une armée pour jouer.\n\t\t=game *nomdujeu*\n")
	private void game(User user, MessageChannel channel, String[] args) {
		StringBuilder builder = new StringBuilder();
		for (String str : args) {
			if (builder.length() > 0) builder.append(" ");
			builder.append(str);			
		}
		channel.sendMessage(user.getAsMention() + " cherche des volontaires pour jouer à " + builder + " !!").complete();
	}
	
	
	@Commandes(name="status", description="Changer le nom du jeu.\n\t\t=status *nomdemonstatus*\n", power=10)
	private void status(JDA jda, String[] args) {
		StringBuilder builder = new StringBuilder();
		for (String str : args) {
			if (builder.length() > 0) builder.append(" ");
			builder.append(str);			
		}
		
		jda.getPresence().setGame(Game.playing(builder.toString()));
		
	}
	
	@Commandes(name="privilege",type=ExecutorType.USER, description="Permet de définir l'autorisation de mes commandes.\n\t\t=privilege *priorité(0-10)* *utilisateur*\n", power=10)
	private void power(User user,MessageChannel channel, Message message, String[] args) {			
		if(args.length == 0 || message.getMentionedUsers().size() == 0) {
			channel.sendMessage("privilege <power> <@User>").queue();
			return;
		}
		
		int power = 0;
		try {
			power = Integer.parseInt(args[0]);
		}catch(NumberFormatException nfe){
			channel.sendMessage("Le Power doit être un nombre compris entre 1 et 10.").queue();
			return;
		}
		
		User target = message.getMentionedUsers().get(0);
		commandMap.addUserPower(target, power);
		channel.sendMessage("Le niveau de privilège de " + target.getAsMention() + " à été initialisé à " + power + ". :smirk:").queue();
	}
	
	Random r = new Random();
	int valeur = 0 + r.nextInt(10 - 0);

	@Commandes(name="dice",type=ExecutorType.USER, description="Un petit lancer de dé ?.\n\t\t=dice *taille du dé* \n")
	private void dice(User user, MessageChannel channel, Message message, String[] args) {

		int taille = Integer.parseInt(args[0]);
		
		Random random = new Random();
		int resultat = 0 + random.nextInt(taille - 0);
		
		channel.sendMessage(user.getAsMention() + " lance un dé " + taille + "." ).queue();
		if(taille == 100) {
			if(resultat == 1) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Ouch, c'est un échec critique pour " + user.getAsMention() + " ! :confounded:").queue();
			}
			else if(resultat >= 2 && resultat < 20) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n C'est pas très bon tout ca... :grimacing:").queue();
			}
			else if(resultat >= 20 && resultat < 24) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Mhh, plutôt moyen, n'est-ce pas ? :grimacing:").queue();
			}
			else if(resultat == 24) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Oh, comme le jour de naissance de mon créateur !! :heart: :tada: :tada:").queue();
			}
			else if(resultat >= 25 && resultat < 40) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Mhh, plutôt moyen, n'est-ce pas ? :grimacing:").queue();
			}
			else if(resultat >= 40 && resultat < 42) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Pas trop mal, mais peux mieux faire. :thinking:").queue();
			}
			else if(resultat == 42) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Oh, on dirait que " + user.getAsMention() + " à trouver un sens à sa vie ! :four: :two:").queue();
			}
			else if(resultat >= 43 && resultat < 60) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Pas trop mal, mais peux mieux faire. :thinking:").queue();
			}
			else if(resultat >= 60 && resultat < 69) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Eh, mais c'est plutôt bien cette affaire. :sunglasses:").queue();
			}
			else if(resultat == 69) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Evidement, il n'y a que " + user.getAsMention() + " pour tomber sur un nombre pareil... :flushed:").queue();
			}
			else if(resultat >= 70 && resultat < 80) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Eh, mais c'est plutôt bien cette affaire. :sunglasses:").queue();
			}
			else if(resultat >= 80 && resultat < 99) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n Woaw, c'est un beau score ça ! :astonished:").queue();
			}
			else if(resultat == 100) {
				channel.sendMessage("Le résultat du lancer est de " + resultat + " :yum: \n INCROYABLE !! Un lancer digne de " + user.getAsMention() + " ! :dizzy_face:   :100: !!").queue();
			}
		}
	}
}
