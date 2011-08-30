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


-- pipe the output of defaults through a few more commands
set cmd to "defaults read NSGlobalDomain AppleLanguages | awk '{gsub(/[^a-zA-Z-]/,\"\");print}' | grep -v '^$'"
set langs to do shell script cmd

-- get the first item in the list
set lang to item 1 of (split of langs by return)
return lang