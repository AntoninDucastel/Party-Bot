package event;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import command.CommandMap;
import net.dv8tion.jda.core.AccountType;

public class MainBot implements Runnable{
	
	private final JDA jda;
	private final CommandMap commandMap = new CommandMap(this);
	private final Scanner scanner = new Scanner(System.in);
	private boolean running;
	
	public MainBot() throws LoginException {
		jda = new JDABuilder(AccountType.BOT).setToken("      ").buildAsync();
		jda.addEventListener(new BotListener(commandMap));
		System.out.println("Hey, je suis Party Bot !");
		System.out.println("Bot Party est dans la place !");
		
		String builder = "=help";
		jda.getPresence().setGame(Game.playing(builder.toString()));
		
	}
	
	public JDA getJda() {
		return jda;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public void run() {
		running = true;
		while (running) {
			if(scanner.hasNextLine()) commandMap.commandConsole(scanner.nextLine());
		}
		
		scanner.close();
		System.out.println("Bot hors tension.");
		jda.shutdown(); //FALSE !!!
		commandMap.save();
		System.exit(0);
	}
	
	public static void main (String[] args) {
		try {
			MainBot mainBot = new MainBot();
			new Thread(mainBot, "Party Bot").start();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
