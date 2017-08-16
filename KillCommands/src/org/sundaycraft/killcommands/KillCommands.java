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
		try {
			this.run(killed.getName(), killer == null ? "" : killer.getName(), killer instanceof Player);
		} catch (ScriptException e) {
			log("'PlayerDeathEvent' javascript program is not valid, error:");
			log(e.getLocalizedMessage());
		}
	}

	public final void run(String killerName, String killedName, boolean killerIsPlayer) throws ScriptException {
		String cmd = "var " + VAR_KILLER_NAME + "='" + killerName + "';var " + VAR_KILLED_NAME + "='" + killedName
				+ "';var " + VAR_KILLER_IS_PLAYER_NAME + "=" + killerIsPlayer + ";" + this.playerDeathCommand;
		String res = (String) this.engine.eval(cmd);
		if (res != null && res.length() > 0) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), res);
		}
	}

	private static final void log(String str) {
		System.out.println("[KillCommands] " + str);
	}
}
