on run argv
	if (count argv) is 0 then
		set PWD_LENGTH to 16
	else
		set PWD_LENGTH to item 1 of argv as number
	end if
	
	tell application "Password Assistant" to activate
	
	tell application "System Events"
		tell process "Password Assistant"
			-- set password length
			set the value of slider 1 of group 1 of window 1 to PWD_LENGTH
			-- open 'Type' combobox
			click pop up button 1 of group 1 of window 1
			-- select 'Memorable' in the combobox, so a new password is generated
			click menu item 3 of menu 1 of pop up button 1 of group 1 of window 1
			
			-- open passwords list
			click button 1 of combo box 1 of group 1 of window 1
			
			-- add all pws to array
			set pw_array to {}
			repeat 10 times
			    set end of pw_array to missing value
			end repeat
			repeat with i from 1 to 10
			    set item i of pw_array to the value of text field i of list 1 of scroll area 1 of combo box 1 of group 1 of window 1
			end repeat
			
		end tell
	end tell
	
	-- tell application "Password Assistant" to quit
	return pw_array
	
end run