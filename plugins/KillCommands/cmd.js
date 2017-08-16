/*
Global variables:

	- killerName : name of the killer entity (can be a mob)
	- killedName : name of the killed player
	- killerIsPlayer : 'true' or 'false' if the killer is a player
	- worldName : the world name of the killed player

*/

//GIVE SCRIPT

if (killerIsPlayer && worldName == "spawn") {
	MAX_AMOUNT = 10;
	MIN_AMOUNT = 1;
	ITEM_ID = "log";
	amount = Math.floor(Math.random() * MAX_AMOUNT) + MIN_AMOUNT;
	"give " + killerName + " " + ITEM_ID + " " + amount;
}

//DEBUG SCRIPT:

//"say " + "[KilledCommands] " + killedName + " was killed by " + killerName + "(killerIsPlayer=" + killerIsPlayer + ") in world: " + worldName
