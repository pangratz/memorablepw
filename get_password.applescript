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
	
	-- get the language specific name of keychain access
	set keychainAccess to getKeychainAccessName()
	if (keychainAccess is null) then
		return "no handler for lang " & getLang()
	end if
	set passwordAssistant to getPasswordAssistantName()
	
	-- initialize the list which will hold all generated passwords
	set PASSWORDS to {}
	
	tell application keychainAccess to activate
	
	tell application "System Events"
		tell process keychainAccess
			-- press '+' button to create a new password
			click button 4 of splitter group 1 of window keychainAccess
			-- press on the key icon to open the password asssistant
			click button 3 of sheet 1 of window keychainAccess
			
			-- set 'memorable' passwords
			click pop up button 1 of group 1 of window passwordAssistant
			click menu item 3 of menu 1 of pop up button 1 of group 1 of window passwordAssistant
			click button 1 of combo box 1 of group 1 of window passwordAssistant
			
			-- open passwords list
			click button 1 of combo box 1 of group 1 of window passwordAssistant
			
			-- iterate over list which holds the password counts
			repeat with pw_length from 8 to ((length of PASSWORD_COUNTS))
				-- get the password count
				set pw_count to item pw_length of PASSWORD_COUNTS
				if (pw_count > 0) then
					-- set the password length
					set the value of slider 1 of group 1 of window passwordAssistant to pw_length
					
					-- generate the passwords and fill up the list
					set idx to 0
					set generated_pws to 0
					repeat while (generated_pws < pw_count)
						-- there are 10 passwords shown ...
						set field_index to ((idx mod 10) + 1)
						set pw to (the value of text field field_index of list 1 of scroll area 1 of combo box 1 of group 1 of window passwordAssistant) as string
						set pw_len to length of pw
						if (pw_len is pw_length) then
							-- add generated password
							set PASSWORDS to PASSWORDS & pw
							set generated_pws to generated_pws + 1
						end if						
						
						-- refresh password list
						if ((field_index - 1) mod 10 is 0) then
							set the value of slider 1 of group 1 of window passwordAssistant to pw_length
						end if
						
						set idx to idx + 1
					end repeat
				end if
			end repeat
			
			click button 1 of sheet 1 of window keychainAccess
			
		end tell
	end tell
	
	
	tell application keychainAccess to quit
	return PASSWORDS
	
end run

on getPasswordAssistantName()
	set lang to getLang()
	if (lang is "en") then
		return "Password Assistant"
	else if (lang is "de") then
		return "Kennwortassistent"
	else
		return null
	end if
end getPasswordAssistantName

on getKeychainAccessName()
	set lang to getLang()
	if (lang is "en") then
		return "Keychain Access"
	else if (lang is "de") then
		return "SchlŸsselbundverwaltung"
	else
		return null
	end if
end getKeychainAccessName

-- a standard split function
on split of aString by sep
	local aList, delims
	tell AppleScript
		set delims to text item delimiters
		set text item delimiters to sep
		set aList to text items of aString
		set text item delimiters to delims
	end tell
	return aList
end split


on getLang()
	-- pipe the output of defaults through a few more commands
	set cmd to "defaults read NSGlobalDomain AppleLanguages | awk '{gsub(/[^a-zA-Z-]/,\"\");print}' | grep -v '^$'"
	set langs to do shell script cmd
	
	-- get the first item in the list
	set lang to item 1 of (split of langs by return)
	return lang
end getLang
