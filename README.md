Use: http://www.freeformatter.com/json-formatter.html#ad-output to see the JSON examples better

Register - Query

Add sticker:
	Receive json of multiple pid (players id), sid (sticker id), starting level (1)
	Generate new row with unique pstid, with passed information, and cross reference information with sid from the sticker reference table (using the given initial values) 
	ex: {"1":[{"pid":5}, {"sid":12}, {"current_lvl":1}], "2":[{"pid":2}, {"sid":3}, {"current_lvl":1}]}

Remove stickers:
	Receive json of all the pstid (player sticker table id)
	delete the pstid from the players_sticker 
	Example: {{"1":[{"ptsid":5}]}, {"2":[{"ptsid":8}]}}

Update Sticker:
	Receive json of every field in the sticker and update the appropriate details
	Example:
	{"1":[{"pstud":5}, {"pid":1}, {"sid":12}, {"name":"my name"}, {"color":1}, {"current_lvl":1}, {"current_exp": 120}, {"current_speed": 25}, {"current_reach": 12}, {"spaid":1}, {"saaid":2}, {"evolve":1}], "2":[{"pstud":5}, {"pid":1}, {"sid":12}, {"name":"my name"}, {"color":1}, {"current_lvl":1}, {"current_exp": 120}, {"current_speed": 25}, {"current_reach": 12}, {"spaid":1}, {"saaid":2}, {"evolve":1}]}

	Note: We'll probably have to seperate this out to specfic types of updates instead of updating everything, but this will be fine for now

Get stickers:
	Reveives the pid, returns json of every field

Add equipment:
	Receive pid (players id), eid (equipment id), starting level (1)
	generates a new row inside the player equipment table
	ex: similar to sticker

Remove equipments:
	Receive the etid (equipment table id)
	delete the etid from the players_equipment
	ex: similar to sticker

Update Equipment:
	receives every field to the equipment and update the apporopriate details
	Example: similar to sticker

Get equipments:
	Reveives the pid, returns everything
	Example: similar to sticker

Create new player:
	Recieves username, fname, and lname


Update player:
	Receive json of all the fields from the player and update their information

Get Player:
	Receives the player id and username.
	Verify that the pid and the username matches
	Receive json of all the fields 


Accept/Reject friends requests:
	Recieves two pid (the user's and their friend's)
	make two entry in their table: user with friend and friend with user
	For now, only be able to add friends and no verification necessary

Get friends list:
	Reveives the pid, returns everything

Have a get for all of the reference schema, except for race that returns a json
