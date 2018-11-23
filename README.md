# Universal-Parse-Tool
JavaFX / Java all in one tool to parse out any CDR from any gateway with auto detection of CDR type.

App auto detects a voice based 2N and Quescom CDR file(s) or a Hypermedia SMS CDR and parses out each individual sims total usage
over the period of the data.

#How it works  

App reads the.log file a line at a time and splits it up with a REGEX, from here the bits of data I want are pulled out and
written to a SQLite DB.  After the whole process is complete the code then quieries the DB and displays the results in a suitable fashion in the UI.

#Lessons  

Uses way better OOP principles and for the first time I'v explored and used Interfaces.  On top of that I've structured the file tree
into neat and organised packages for ease of maintnenance.

#Result   

Tool has been rolled out to my support colleagues who are using it
