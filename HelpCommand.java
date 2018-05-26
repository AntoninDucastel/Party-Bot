package command.defaut;

import java.awt.Color;

import command.CommandMap;
import command.Commandes;
import command.Commandes.ExecutorType;
import command.SimpleCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;

public class HelpCommand {

	private final CommandMap commandMap;
	
	public HelpCommand(CommandMap commandMap) {
		this.commandMap = commandMap;
	}
		
	
	@Commandes(name="help", type=ExecutorType.USER, description="Affiche les commandes.\n\t\t=help\n")
	private void help(User user, MessageChannel channel, Guild guild) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Liste des commandes que je peux executer :");
		builder.setColor(Color.orange);
		
		for (SimpleCommand command : commandMap.getCommands()){
			if (command.getExecutorType() == ExecutorType.CONSOLE) continue;
			
			if (guild != null && command.getPower() > commandMap.getPowerUser(guild,  user)) continue;
			
			builder.addField(command.getName(), command.getDescription(), false);
		}
		if(!user.hasPrivateChannel()) user.openPrivateChannel().complete();
		((UserImpl) user).getPrivateChannel().sendMessage(builder.build()).queue();
		
		channel.sendMessage(user.getAsMention() + ", je vous ai envoyé la liste des commandes disponible en message privé. :wink:").queue();
	}		
		
}
