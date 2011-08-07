on run argv
	if (count argv) is 0 then
		set PWD_LENGTH to 16
	else
		set PWD_LENGTH to item 1 of argv as number
	end if
	
	tell application "Password Assistant" to activate
	
	tell application "System Events"
		tell process "Password Assistant"
			set the value of slider 1 of group 1 of window 1 to PWD_LENGTH
			click pop up button 1 of group 1 of window 1
			click menu item 3 of menu 1 of pop up button 1 of group 1 of window 1
			set PWD to the value of combo box 1 of group 1 of window 1
		end tell
	end tell
	
	-- tell application "Password Assistant" to quit
	
	return PWD
	
end run