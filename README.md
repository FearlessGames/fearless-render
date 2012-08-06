Fearless-Renderer
=================

Building and running
--------------------
Install JDK 1.6 or later
set JAVA_HOME to install path
Build with: "gradlew build"
To run Main example: gradlew runExample -Pex=Main
The example needs to be run from gradle before it can be run from intellij (for copying native libs).
Running the examples can also help all deps being downloaded.



Git globals setup
-----------------
* git config --global core.autocrlf true # (if on Windows)
* git config --global core.autocrlf input # (elsewhere)
* git config --global core.safecrlf true
* git config --global branch.autosetuprebase always



Updating intellij project files
--------------------------------
If gradlew changes version or if a lib version is updated intellijs project files need to be synced.
Start with linking the root build.gradle file with jetgradle (might need to reopen project / restart intellij after)
Some of the dependencies in the jetgradle tree should be red/green/black (red = conflict, green = gradle, blue = intellij, black = ok).
If its all black the gradle integration is probably not working.

In project structure dialog remove all libs, and remove the from modules dependencies.
Refresh the jetgradle tab, most should show up as green. Go through the different subprojectes and select the green entries and right click on them and select import.
Several new libs will now be commited and the .iml files will be updated to use them.
Build, Test, Commit!