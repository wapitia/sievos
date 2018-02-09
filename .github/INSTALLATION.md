# Local Installation Steps

* Set up local Git environment
  1. Install Cygwin 
  2. Install git
  
* Pull Sievos Locally (Windows)
  1. open a Cygwin console 
  2. navigate to your local git workspace directory, such as 
     /cygdrive/C/Users/cmorgan/git-workspace
  3. Clone the Sievos git repository locally:
     ./git-workspace> git clone https://github.com/wapitia/sievos.git
     This will pull the latest Sievos source into git-workspace/sievos
  
* Invoke Gradle to build the project (Cygwin)
  4. ./git-workspace> cd ./sievos
  6. ./sievos> ./gradlew.bat wrapper
     This updates the .gradle directory and its project files.
  7. ./sievos> ./gradlew.bat eclipse
     This will install the Eclipse-specific IDE meta files (.classpath, .project,
     .settings) as well as download locally all 3rd party jars needed (plus more).
  8. ./sievos> ./gradlew.bat build
   
* Build your IDE project (Eclipse IDE)
  * Install the latest version of Eclipse from http://www.eclipse.org/downloads/
  * Open Eclipse, and install the latest Scala IDE
  * File -> Import -> Gradle -> Existing Gradle Project
  * Find the Sievos root project directory at /cygdrive/C/Users/cmorgan/git-workspace
  
