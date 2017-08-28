package org.sundaycraft.killcommands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KillCommands extends JavaPlugin implements Listener {

	/** js var names */
	private static final String VAR_KILLER_NAME = "killerName";
	private static final String VAR_KILLED_NAME = "killedName";
	private static final String VAR_KILLER_IS_PLAYER_NAME = "killerIsPlayer";
	private static final String VAR_KILLED_WORLD_NAME = "worldName";
	private static final String FUNCTION_RUN_COMMANDS = "function runCommands() {" +
															"if (arguments.length == 0) {" +
														    	"return (\"say 'Erreur configuration KillCommands: Aucunes commandes precisees dans runCommands()'\");" +
														    "}" +
															"cmd = arguments[0].trim();" +
														   " for (var i = 1; i < arguments.length; i++) {" +
														    	"cmd = cmd + \";\" + arguments[i].trim();" +
															"}" +
														    "return (cmd);" +
														"}\n";

	/** js command */
	private String playerDeathCommand;

	/** javascript executor */
	private ScriptEngineManager scriptEngineManager;
	private ScriptEngine engine;

	@Override
	public void onEnable() {
		// load config
		this.saveDefaultConfig();
		this.getConfig().set("program", "cmd.js");
		this.saveConfig();

		try {
			String programName = this.getConfig().getString("program");
			this.playerDeathCommand = new String(Files.readAllBytes(Paths.get("plugins/KillCommands/" + programName)),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			log("No 'PlayerDeathEvent' javascript program set, disabling plugin");
			log(e.getLocalizedMessage());
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		// register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);

		this.scriptEngineManager = new ScriptEngineManager();
		this.engine = this.scriptEngineManager.getEngineByName("JavaScript");
	}

	/** event handler */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		LivingEntity killer = killed.getKiller();
		boolean killerIsPlayer = killer instanceof Player;
		String worldName = killed.getWorld().getName();
		try {
			this.run(killed.getName(), killer == null ? "" : killer.getName(), killerIsPlayer, worldName);
		} catch (ScriptException e) {
			log("'PlayerDeathEvent' javascript program is not valid, error:");
			log(e.getLocalizedMessage());
		}
	}

	public final void run(String killedName, String killerName, boolean killerIsPlayer, String worldName)
			throws ScriptException {
		String javascript = FUNCTION_RUN_COMMANDS + "var " + VAR_KILLER_NAME + "='" + killerName + "';var " + VAR_KILLED_NAME + "='"
				+ killedName + "';var " + VAR_KILLER_IS_PLAYER_NAME + "=" + killerIsPlayer + ";var "
				+ VAR_KILLED_WORLD_NAME + "='" + worldName + "';";
		System.out.println(javascript);
		String res = (String) this.engine.eval(javascript + this.playerDeathCommand);
		if (res != null && res.length() > 0) {
			String[] cmds = res.split(";");
			for (String cmd : cmds) {
				if (cmd.length() == 0) {
					continue;
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.trim());
			}
		}
	}

	private static final void log(String str) {
		System.out.println("[KillCommands] " + str);
	}
}