# sievos

I don't know how you found it here, but welcome! 
As the Sievos initiative becomes more fleshed out, you'll see what some of the milestones and goals are, so stay tuned.

This is a study of combining the following technologies together into one cohesive group of projects.

*  **Gradle**. The ability to manage multiple modules into one root project. Here, the root project is "sievos" and it has 
   three sub-project modules, "sievos-core", "sievos-lex-lang", and "sievos-lex-impl".
*  **Java**. Java is one of Sievos' primary languages, because it forms the basis of both the Scala runtime and the
             Antlr generated source files.
*  **Scala**. Scala is one of Sievos' primary langagues.
*  **Antlr**. The Antlr parsing module generates target code in the form of .java files. These files must be generated,
   compiled, and referenced by the .java and .scala sources in some dependant modules, so that has to work together.
   
The Gradle config files settings.gradle and gradle.properties are tweaked within each module to set up the proper sequence
of depenedencies and build order. I'll try to document some of the "gotchas" that I encounter as I go along.

Please contribute! Let's document the installation and usage of this project together. One of the goals is that a
user may download and work with this project. Please start with the [Installation Steps](https://github.com/wapitia/sievos/blob/master/.github/INSTALLATION.md)

