Provided files:
	- ./src/* : the source files of the plugin
	- ./plugins/KillCommands.jar : a build of the current version
	- ./plugins/KillCommands/config.yml : config file
	- ./plugins/KillCommands/cmd.js : an exemple of javascript command
	
#HOW TO USE:

When a player dies (from any sources), it name ('killedName'), the killer name ('killerName'), and a boolean ('killerIsPlayer') are injected into a javascript program, which is suppose to generate a command to be run server-side (with server permissions)
