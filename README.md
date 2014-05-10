Alltiny Chorus
==============

# What it is
Alltiny Chorus is a Java application rendering sheet music on screen and play the music using MIDI. Chorus is created for singers to support when learning new pieces of music. So one of main features is to mute selected voices.

# What it currently does not
Chorus is still work in progress. Following features are missing:
- render and interpret repetitions (also D.C., D.S.)
- render beat notations on screen (C, 3/4, ...)
- render and interpret fermatas

# What it might become
Chorus currently not supports any editing. You need to edit your music files as XML in a propriate editor. In the final version Chorus should have an edit-mode and as well be apropriate for layouting sheet music.

# How to build?
alltiny-chorus uses gradle for building. To compile and assemble the executable jar file do:
>>cd alltiny-chorus/chorus
>>gradle clean jar
the executable jar file can be found in:
>>alltiny-chorus/chorus/build/libs/

# How to set up my development environment?
Depending on whether you use IntelliJ IDEA or Eclipse, gradle may create the project files for you:
* for IntelliJ IDEA
>>cd alltiny-chorus
>>gradle idea
* for Eclipse
>>cd alltiny-chorus
>>gradle eclipse