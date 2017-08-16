/*
Global variables:

	- killerName : name of the killer entity (can be a mob)
	- killedName : name of the killed player
	- killerIsPlayer : 'true' or 'false' if the killer is a player
	- worldName : the world name of the killed player

*/

//GIVE SCRIPT
/*
if (killerIsPlayer) {
	"give " + killedName + " 1 2"
}
*/


//DEBUG SCRIPT:

"say " + "[KilledCommands] " + killedName + " was killed by " + killerName + "(killerIsPlayer=" + killerIsPlayer + ") in world: " + worldName
