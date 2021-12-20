package com.rpdescriptions.plugin;

import com.rpdescriptions.plugin.commands.ForceDescCmd;
import com.rpdescriptions.plugin.commands.SetDescCmd;
import com.rpdescriptions.plugin.commands.ViewDescCmd;
import com.rpdescriptions.plugin.listeners.InteractAtEntityListener;
import com.rpdescriptions.plugin.misc.Config;
import com.rpdescriptions.plugin.misc.PAPIExpansion;
import com.rpdescriptions.plugin.services.DescriptionService;
import com.rpdescriptions.plugin.services.message.MessageService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Config config;
	private Config databaseConfig;

	private MessageService     messageService;
	private DescriptionService descriptionService;

	public void onEnable() {
		loadFiles();
		setupServices();
		registerEvents();
		registerCommands();

		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
			new PAPIExpansion(descriptionService, config).register();
	}
	
	private void loadFiles() {
		config         = new Config(this, getDataFolder(), "config", "config.yml");
		databaseConfig = new Config(this, getDataFolder(), "database");
	}

	void setupServices() {
		messageService     = new MessageService(config);
		descriptionService = new DescriptionService(databaseConfig);
	}

	void registerListener(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	void registerEvents() {
		registerListener(new InteractAtEntityListener(config, messageService, descriptionService));
	}

	void registerCommandExecutor(String name, CommandExecutor executor) {
		PluginCommand command = getCommand(name);
		if (command == null)
			getLogger().warning("Attempted to register command \"" + name + "\" but none was found in the plugin.yml");
		else
			command.setExecutor(executor);
	}

	void registerCommands() {
		registerCommandExecutor("forcedesc", new ForceDescCmd(messageService, descriptionService, databaseConfig));
		registerCommandExecutor("setdesc", new SetDescCmd(messageService, descriptionService, databaseConfig));
		registerCommandExecutor("viewdesc", new ViewDescCmd(messageService, descriptionService));
	}
}
