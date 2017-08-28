/*
Global variables:

	- killerName : name of the killer entity (can be a mob)
	- killedName : name of the killed player
	- killerIsPlayer : 'true' or 'false' if the killer is a player
	- worldName : the world name of the killed player
	- runCommands(...) : a function which take multiples string commands to execute

*/

//GIVE SCRIPT


if (killerIsPlayer && worldName == "spawn") {
	MAX_AMOUNT = 10;
	MIN_AMOUNT = 1;
	ITEM_ID = "log";
	amount = Math.floor(Math.random() * MAX_AMOUNT) + MIN_AMOUNT;
	cmd1 = "give " + killerName + " " + ITEM_ID + " " + amount;
	cmd2 = "say coucou t'es mort";
	runCommands(cmd1, cmd2);
	
}

runCommands("say 5", "say 4", "say 3", "say 2", "say 1", "say BOOOOM")


//DEBUG SCRIPT:

//"say " + "[KilledCommands] " + killedName + " was killed by " + killerName + "(killerIsPlayer=" + killerIsPlayer + ") in world: " + worldName
