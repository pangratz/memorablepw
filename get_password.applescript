(*
	this script opens the Password Assistant and returns an array of passwords, as defined in the arguments:
	the arguments represent a space seperated list of password counts which shall be generated, where the list is 8-indexed, meaning the first value represents the count of passwords which shall be generated for length 8
	the maximum length is 31
	
	for example:
	osascript get_password.applescript 1 1 1 2
	
	will generate 5 passwors, where there is 1 password for length 8, 9 and 10 an two 11-length passwords
*)
on run argv
	-- there are no passwords of length 1 to 7
	set PASSWORD_COUNTS to {0, 0, 0, 0, 0, 0, 0}
	
	-- return if there are no passwords to generate
	if (count argv) is 0 then
		-- for debug reasons, we make a list with 1 password for each length
		repeat with i from 8 to 31
			set PASSWORD_COUNTS to PASSWORD_COUNTS & 1
		end repeat
	end if
	
	-- set the password counts as specified in the arguments
	set arg_count to count argv
	if (arg_count) > 31 then
		-- check that there are no password counts for password length greater that 31
		set arg_count to 31
	end if
	repeat with i from 1 to arg_count
		set PASSWORD_COUNTS to PASSWORD_COUNTS & (get item i of argv as number)
	end repeat
	
	-- initialize the list which will hold all generated passwords
	set PASSWORDS to {}
	
	tell application "Password Assistant" to activate
	
	tell application "System Events"
		tell process "Password Assistant"
			-- open 'Type' combobox
			click pop up button 1 of group 1 of window 1
			-- select 'Memorable' in the combobox, so a new password is generated
			click menu item 3 of menu 1 of pop up button 1 of group 1 of window 1
			-- open passwords list
			click button 1 of combo box 1 of group 1 of window 1
			
			-- iterate over list which holds the password counts
			repeat with pw_length from 8 to ((length of PASSWORD_COUNTS))
				-- get the password count
				set pw_count to item pw_length of PASSWORD_COUNTS
				if (pw_count > 0) then
					-- set the password length
					set the value of slider 1 of group 1 of window 1 to pw_length
					
					-- generate the passwords and fill up the list
					set i to 0
					repeat while (i < pw_count)
						-- there are 10 passwords shown ...
						set reset to false
						set pw to (the value of text field (i mod 10) of list 1 of scroll area 1 of combo box 1 of group 1 of window 1) as string
						set pw_len to length of pw
						if (pw_len is not pw_length) then
							-- password length does not match
							set reset to true
						else
							-- add generated password
							set PASSWORDS to PASSWORDS & pw
							set i to i + 1
						end if
						
						
						-- refresh password list
						if (reset or (i mod 10 is 0)) then
							click pop up button 1 of group 1 of window 1
							click menu item 3 of menu 1 of pop up button 1 of group 1 of window 1
							click button 1 of combo box 1 of group 1 of window 1
						end if
					end repeat
				end if
			end repeat
			
		end tell
	end tell
	
	tell application "Password Assistant" to quit
	return PASSWORDS
	
end run
