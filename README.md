<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- A1 (SE-2)
-->
# Project: *mvn-fun* - Project Build with *Maven*

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

*Maven* is a popular build-tool for Java.

Challenges:

1. [*What is a Build Tool?*](#1-what-is-a-build-tool-for)

1. [*Maven in 5 Minutes*](#2-maven-in-5-minutes)

1. [*Build the Project*](#3-build-the-project)

1. [*Maven Build Lifecycle*](#4-maven-build-lifecycle)

1. [*Build a stand-alone .jar*](#5-build-a-stand-alone-jar)

1. [*Check project into local git repository*](#6-check-project-into-a-local-git-repository)

1. [*Factorizer.java*](#7-factorizerjava)

1. [*Refactor the Package Structure*](#8-refactor-the-package-structure)

1. [*Unit Tests*](#9-unit-tests)

1. [*Javadoc*](#10-javadoc)

1. [*Release*](#11-release)

1. [*Host Project in a Remote Repository*](#12-host-project-in-a-remote-repository)


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 1 What is a *Build Tool* for?

Read the
[summary](https://stackoverflow.com/questions/7249871/what-is-a-build-tool)
and answer questions with 1-3 bullets each:

1. What is the goal of the *"project build process"*?

1. When does the *"project build process"* start and with what result does it end?

1. What are tasks of *"project build"*?

1. Why are build tools or build automation used?

1. Which other build tools exist (other than *Maven*)?

1. What is a *Continuous Integration (CI)* build?

1. What are *Nightly builds*?


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 2. Maven in 5 Minutes

Perform the
[*Maven in 5 Minutes*](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
challenge, which starts with installing *maven* (or test maven has been installed).
You may write down the start time and end time to see whether you manage the
challenge in 5 minutes.

Test maven:

```sh
mvn --version           # run maven asking for the installed verion
```

Output should be something similar to (know what to when you see *"mvn: command
not found"*):

```
Maven home: C:\opt\maven
Java version: 21, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21
Default locale: en_US, platform encoding: UTF-8
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

Make sure, environment variables *maven* uses have been set matching your system.
See example [*.bashrc*](https://github.com/sgra64/dotfiles/blob/main/.bashrc.path)
to set variables in your *.bashrc* or *.zshrc* (Mac) file.

Type:

```sh
echo $JAVA_HOME         # show the value of the JAVA_HOME environment variable
--> /c/Program Files/Java/jdk-21

echo $MAVEN_HOME        # show the path where maven is installed on your system
--> /c/opt/maven

echo $M2_HOME           # show the path where maven stores dependencies (.jar)
--> /c/Sven1/svgr2/tmp/svgr/.m2
```

`cd` to a workspace to create a new *maven* project.

Perform step *"Creating a Project"* of the *Maven in 5 Minutes* tutorial.
It creates a new project: *my-app*.

<!-- 
Create project directory 'my-app' with the demo maven project inside:
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DarchetypeVersion=1.5 -DinteractiveMode=false
 -->

`cd` into the project.

```sh
ls -la                  # show the content of the project
```

Output shows a file *pom.xml* and a directory *src*. Directory *.mvn* stores
local *maven* settings (not needed for now):

```
total 8
drwxr-xr-x 1 svgr2 Kein    0 Oct 16 21:25 .
drwxr-xr-x 1 svgr2 Kein    0 Oct 16 21:25 ..
drwxr-xr-x 1 svgr2 Kein    0 Oct 16 21:25 .mvn
-rw-r--r-- 1 svgr2 Kein 3191 Oct 16 21:25 pom.xml   <-- file for the maven build process
drwxr-xr-x 1 svgr2 Kein    0 Oct 16 21:25 src       <-- project source code
```

```sh
find src                # show the source tree
```
```
src
src/main
src/main/java
src/main/java/com
src/main/java/com/mycompany
src/main/java/com/mycompany/app
src/main/java/com/mycompany/app/App.java        <-- Java source file with main()
src/test
src/test/java
src/test/java/com
src/test/java/com/mycompany
src/test/java/com/mycompany/app
src/test/java/com/mycompany/app/AppTest.java    <-- Sample JUnit test file
```

Mind the structure of the *src* tree:

```sh
src/main/java/com/mycompany/app/App.java
^^^^^^^^^^^^^ - - - - - - - - - - - - - - - - - maven path to Java sources

              ^^^^^^^^^^^^^^^^^ - - - - - - - - groupId: 'com.mycompany.app'

                                ^^^^^^^^- - - - class 'App' in package 'com.mycompany.app'
```

Tests mirror the structure under `src/test/java`.

Open file `App.java` in your IDE:

```java
package com.mycompany.app;      // <-- groupId appears as package: 'com.mycompany.app'

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

Open file [*pom.xml*](pom.xml) in your IDE:

```xml
<groupId>com.mycompany.app</groupId>        <!-- groupId    -->
<artifactId>my-app</artifactId>             <!-- artifactId -->
<version>1.0-SNAPSHOT</version>             <!-- version    -->

<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <maven.compiler.release>17</maven.compiler.release>
</properties>

<dependencies>
  <!-- two dependencies -->

  <!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api -->
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>     <!--  scope means: dependency effecitv only for tests   -->
                            <!--  no <version> means: 'latest'   -->
  </dependency>

  <!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params -->
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <scope>test</scope>
  </dependency>
</dependencies>
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 3. Build the Project

Continue with *Maven in 5 Minutes* with step: *Build the Project*.

```sh
mvn compile             # compile the project from 'src' to 'target'
```

A new folder appears in the project directory: `target`. Show the content od the folder
and correlate the location of the compiled class `App.class` unter `target` to the location
of the source file `App.java` in the `src` tree:

```
find target
```
```
target/
target/classes
target/classes/com
target/classes/com/mycompany
target/classes/com/mycompany/app
target/classes/com/mycompany/app/App.class      <-- compiled App.java
target/generated-sources
target/generated-sources/annotations
target/maven-status
target/maven-status/maven-compiler-plugin
target/maven-status/maven-compiler-plugin/compile
target/maven-status/maven-compiler-plugin/compile/default-compile
target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
```

Run the program from `target/classes`:

```sh
java -cp target/classes com.mycompany.app.App
```
```
Hello World!
```

Package the project:

```sh
mvn package
```

Show the result in `target`:

```sh
ls -la target
```
```
drwxr-xr-x 1    0 Oct 16 21:57 ./
drwxr-xr-x 1    0 Oct 16 21:56 ../
drwxr-xr-x 1    0 Oct 16 21:56 classes/
drwxr-xr-x 1    0 Oct 16 21:56 generated-sources/
drwxr-xr-x 1    0 Oct 16 21:56 generated-test-sources/
drwxr-xr-x 1    0 Oct 16 21:56 maven-archiver/
drwxr-xr-x 1    0 Oct 16 21:56 maven-status/
-rw-r--r-- 1 2751 Oct 16 21:57 my-app-1.0-SNAPSHOT.jar      <-- packaged .jar file
drwxr-xr-x 1    0 Oct 16 21:56 surefire-reports/
drwxr-xr-x 1    0 Oct 16 21:56 test-classes/
```

Run the packaged *.jar* from `target/my-app-1.0-SNAPSHOT.jar` as result
of the *maven* build process:

```sh
java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
```
```
Hello World!
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 4. Maven Build Lifecycle

Learn about the
[*Maven Build Lifecycle*](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
and write down what actions of the following commands perform:

- mvn *validate*
- mvn *compile*
- mvn *test*
- mvn *package*
- mvn *verify*
- mvn *install*
- mvn *deploy*
- mvn *clean*

Maven internally creates *CLASSPATH* from dependencies in *pom.xml*.
Show *CLASSPATH*:

```sh
mvn dependency:build-classpath              # show CLASSPATH and write to file
mvn dependency:build-classpath -Dmdep.outputFile=.classpath     # '.classpath'
cat .classpath
```

*CLASSPATH* refers to *.jar* files *maven* downloaded with dependencies
in *pom.xml* from the
[*Maven Repository*](https://mvnrepository.com/). *Maven* caches downloaded
dependencies at the `M2_HOME` directory, usually under a folder `.m2`
in the *HOME* directory.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 5. Build a stand-alone *.jar*

Change the print line from: *"Hello World!"* to: *"Hello Maven!"*.

*"Re-Build"* (recompile and repackage) the resulting artifact:

```sh
mvn clean compile package           # clean the project (remove 'target') and re-build
mvn clean package                   # shorter form (package performs compile, if not compiled)

ls -la target/*.jar
```
```
-rw-r--r-- 1 svgr2 Kein 2751 Oct 16 22:03 target/my-app-1.0-SNAPSHOT.jar
```

The *.jar* includes the compiled class *App.class*:

```sh
jar tfv target/my-app-1.0-SNAPSHOT.jar
```

Output shows the packaged content of the *.jar* file:

```
     0 Thu Oct 16 22:14:14 CEST 2025 META-INF/
   116 Thu Oct 16 22:14:14 CEST 2025 META-INF/MANIFEST.MF
     0 Thu Oct 16 22:14:12 CEST 2025 com/
     0 Thu Oct 16 22:14:12 CEST 2025 com/mycompany/
     0 Thu Oct 16 22:14:12 CEST 2025 com/mycompany/app/
     0 Thu Oct 16 22:14:14 CEST 2025 META-INF/maven/
     0 Thu Oct 16 22:14:14 CEST 2025 META-INF/maven/com.mycompany.app/
     0 Thu Oct 16 22:14:14 CEST 2025 META-INF/maven/com.mycompany.app/my-app/
   549 Thu Oct 16 22:14:12 CEST 2025 com/mycompany/app/App.class
  3659 Thu Oct 16 22:14:00 CEST 2025 META-INF/maven/com.mycompany.app/my-app/pom.xml
    68 Thu Oct 16 22:14:14 CEST 2025 META-INF/maven/com.mycompany.app/my-app/pom.properties
```

Run the resulting *.jar* as a stand-alone program:

```sh
java -jar target/my-app-1.0-SNAPSHOT.jar
```

An error occurs.:

```
no main manifest attribute, in target/my-app-1.0-SNAPSHOT.jar
```

Google the problem or ask your AI how to solve the problem. Some hints are
[*here*](https://stackoverflow.com/questions/9689793/cant-execute-jar-file-no-main-manifest-attribute).

Answer questions:

1. What is the *MANIFEST* file for?

1. Where is it?

1. How can the prblem be solved?

Inspect the *MANIFEST* file that is currently packaged in the *.jar*:

```sh
jar xf target/my-app-1.0-SNAPSHOT.jar META-INF/MANIFEST.MF &&
    cat META-INF/MANIFEST.MF &&
    rm -rf META-INF
```

Outout is the content of file *MANIFEST.MF* packaged in the *.jar*:

```
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.4.2
Build-Jdk-Spec: 21
```

<!-- 
# adjust <mainClass> to: com.mycompany.app.App
# install under <build>:
<build>
  <pluginManagement>
    ...
  </pluginManagement>
  <plugins>
    <plugin>
      <artifactId>maven-jar-plugin</artifactId>
      <version>3.4.2</version>
      <configuration>
          <archive>
          <manifest>
              <addClasspath>true</addClasspath>
              <classpathPrefix>lib/</classpathPrefix>
              <mainClass>com.mycompany.app.App</mainClass>
          </manifest>
          </archive>
      </configuration>
    </plugin>
  </plugins>
</build>
# 
# create patch file:    diff -Naru pom_orig.xml pom.xml > pom.patch
# apply patch:          patch pom.xml < pom.patch
 -->

Fix the problem following the advice from the article, re-build, re-package
and re-run the program:

```sh
mvn clean package                               # clean rebuild and package

java -jar target/my-app-1.0-SNAPSHOT.jar        # re-run
```

The *.jar* file is now executable *"stand-alone"*:

```
Hello World!
```

Check that *MANIFEST.MF* now includes the main-class:

```sh
jar xf target/my-app-1.0-SNAPSHOT.jar META-INF/MANIFEST.MF &&
    cat META-INF/MANIFEST.MF &&
    rm -rf META-INF
```

Outout is the content of file *MANIFEST.MF* packaged in the *.jar*:

```
Manifest-Version: 1.0
Created-By: Maven JAR Plugin 3.4.2
Build-Jdk-Spec: 21
Main-Class: com.mycompany.app.App           <-- main class is now present
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->


&nbsp;

## 6. Check Project Into a Local *git* Repository

Create a local *git* repository with an empty root commit. It is useful to have an
empty root commit on a repository to be able to create branches off the root commit
that contain nothing.

1. Next, create [*.gitignore*](https://github.com/sgra64/se1-play/blob/main/.gitignore)
    and commit to the *main* branch.

1. Then, commit *pom.xml* and *src*.

1. Remove unwanted content: `git clean -fd`.

Show the *commit log* and make sure it has proper messages. Change if commit
messages don't match:

```
530de68 (HEAD -> main) add pom.xml, src
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```

Test commits contain the correct files.

Test the first commit after the root commit (id: *96b05a4* - you may adjust the
id):

```sh
# compare two commits specified by their 'id'
git diff 02082e3..96b05a4 --name-status

# compare two commits relative from 'HEAD'
git diff HEAD~2..HEAD~1 --name-status

# show files that have been changed in the specified commit
git diff-tree --no-commit-id --name-only -r 530de68
```
```
A       .gitignore      <-- '.gitignore' was added in commit '96b05a4'
```

Repeat for the last commit:

```
A       pom.xml
A       src/main/java/com/mycompany/app/App.java
A       src/test/java/com/mycompany/app/AppTest.java
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 7. *Factorizer.java*

Create a feature-branch: `factorizer` and make sure *you are* on the new branch:

```sh
git branch -avv             # show branches
```
```
* factorizer 530de68 add pom.xml, src       <-- * marks the currently active branch
  main       530de68 add pom.xml, src
```

The commit log also shows the new branch *factorizer* that it currently pointing
to the last commit of the *main* branch:

```sh
git log --oneline
```

Branches *factorizer* and *main* point to the same commit.
HEAD points to *factorizer*:

```
530de68 (HEAD -> factorizer, main) add pom.xml, src
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```

Modify class *App.java* such the it accepts numbers from the command line and
outputs the prime factors of these numbers.

```sh
export CLASSPATH="target/classes"

java com.mycompany.app.App 3 27 1092 65536 10952347 100000039
```
```
Hello Factorizer!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

&nbsp;

Create a `Factorizer` *component*. Remember the properties of a Java component
implementation. A component implementation has:

- a public interface `Factorizer.java` and

- a non-public implementation class `FactorizerImpl.java`.

- Implement the component as a *(lazy) singleton*.

The interface `Factorizer.java` is:

```java
/**
 * Interface of a singleton component that factorizes numbers
 * into prime factors.
 */
public interface Factorizer {

    /**
     * Method accepts numbers as {@code args} and outputs lines with the
     * number {@code n}, its factors and an indicator whether {@code n} is a
     * prime number.
     * <p>For example:</p>
     * <pre>
     * {@code - n=3 -> [3] (prime number)
     * - n=27 -> [3, 3, 3]
     * - n=1092 -> [2, 2, 3, 7, 13]
     * - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
     * - n=10952347 -> [7, 23, 59, 1153]
     * - n=100000039 -> [100000039] (prime number)
     * }
     * </pre>
     * @param args numbers to factorize
     */
    public void run(String[] args);

    /**
     * Method accepts a number {@code n} and returns its factors.
     * @param n number to factorize
     * @return factors
     */
    public List<Integer> factorize(Integer n);

    /**
     * Return reference to singleton instance of (hidden) implementation class
     * that implements the {@link Factorizer} interface.
     * @return reference to instance that implements the {@link Factorizer} interface
     */
    static Factorizer getInstance() {
        return FactorizerImpl.getInstance();
    }
}
```

Implementation class *FactorizerImpl.java* has a method: *run(String[] args)*
that receives the *args* when class *App.java* runs.

Be ambitious and process *args* as a Java stream:

```java
@Override
public void run(String[] args) {
    Stream.of(args)
        .forEach(f -> {
            System.out.println(
                String.format(" - n=%d -> %s%s", 0, List.of(), " (isPrime)"));
        });
}
```

Test implementation with this *run()* function:

```sh
java com.mycompany.app.App 3 27 1092 65536 10952347 100000039
```
```
Hello Factorizer!
 - n=0 -> [] (isPrime)
 - n=0 -> [] (isPrime)
 - n=0 -> [] (isPrime)
 - n=0 -> [] (isPrime)
 - n=0 -> [] (isPrime)
 - n=0 -> [] (isPrime)
```

Complete the implementation such that the program produces the correct output:

```
Hello Factorizer!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

If the program works, review the changes:

```sh
git status
```
```
On branch factorizer
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   src/main/java/com/mycompany/app/App.java

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        src/main/java/com/mycompany/app/Factorizer.java
        src/main/java/com/mycompany/app/FactorizerImpl.java
```

Compare changes made to file *App.java*:

```sh
git diff src/main/java/com/mycompany/app/App.java
```
```diff
diff --git a/src/main/java/com/mycompany/app/App.java b/src/main/java/com/mycompany/app/App.java
index 735f1e0..1935c12 100644
--- a/src/main/java/com/mycompany/app/App.java
+++ b/src/main/java/com/mycompany/app/App.java
@@ -5,6 +5,9 @@ package com.mycompany.app;
  */
 public class App {
     public static void main(String[] args) {
-        System.out.println("Hello World!");
+        System.out.println("Hello Factorizer!");
+        // 
+        Factorizer factorizer = Factorizer.getInstance();
+        factorizer.run(args);
    }
}
```

If everything looks good, commit to the `factorizer` branch. Make sure the
commit line matches:

```sh
git log --oneline                               # show commit log
```
```
83534b9 (HEAD -> factorizer) factorizer implementation completed
530de68 (main) add pom.xml, src                 <-- main branch
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```

Show the files that comprise that commit:

```sh
git diff-tree --name-only -r HEAD
```
```
83534b9f22ae51d0d48c96b0571f93e19fc805d3
src/main/java/com/mycompany/app/App.java
src/main/java/com/mycompany/app/Factorizer.java
src/main/java/com/mycompany/app/FactorizerImpl.java
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 8. Refactor the Package Structure

Remember what
[*Refactoring*](https://refactoring.guru/refactoring)
is in Software Engineering.

[*Martin Fowler*](https://martinfowler.com/)
has written a book on the topic with *Kent Beck*:
*"Refactoring, Improving the Design of Existing Code (2nd Edition)"*,
Addison-Wesley (2018).

The current package structure: `com.mycompany.app` comes from the initial
*maven* project setup.

Refactor this structure on branch factorizer to: `de.factorizer`

Consider the impact this refactoring will have:

- in *pom.xml*, write down the new [*GAV*]()
    coordinates for *groupId*, *artifactId* and version (same).

- to Java source files under: *src/main/java/com/mycompany*,

- to Java test files under: *src/test/java/com/mycompany*.

Make sure you start from a *"clean working tree"* such that you can reset to
the commit with a properly working program:

```sh
git status
```
```
On branch factorizer
nothing to commit, working tree clean
``

Perform the changes starting with *pom.xml* and then for *source* and
*test* paths.

Rebuild the project and re-run:

```sh
java de.factorizer.App 3 27 1092 65536 10952347 100000039
```

With proper refactoring, output will appear:

```
Hello Factors!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

The structure of `src` and `target` reflect the refactoring:

```sh
find src                    # show the 'src' structure after refactoring
find target                 # show the 'target' structure after refactoring
```
```
src
src/main
src/main/java
src/main/java/de
src/main/java/de/factorizer
src/main/java/de/factorizer/App.java
src/main/java/de/factorizer/Factorizer.java
src/main/java/de/factorizer/FactorizerImpl.java
src/test
src/test/java
src/test/java/de
src/test/java/de/factorizer
src/test/java/de/factorizer/AppTest.java
```
```
target
target/classes
target/classes/de
target/classes/de/factorizer
target/classes/de/factorizer/App.class
target/classes/de/factorizer/Factorizer.class
target/classes/de/factorizer/FactorizerImpl$Factors.class
target/classes/de/factorizer/FactorizerImpl.class
target/generated-sources
target/generated-sources/annotations
target/maven-status
target/maven-status/maven-compiler-plugin
target/maven-status/maven-compiler-plugin/compile
target/maven-status/maven-compiler-plugin/compile/default-compile
target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
```

Show the impact of the refactoring had on modified files
(*"refactoring footprint"*):

```sh
git status                  # show new and modified files
```
```
On branch factorizer
Changes not staged for commit:
  (use "git add/rm <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   pom.xml
        deleted:    src/main/java/com/mycompany/app/App.java
        deleted:    src/main/java/com/mycompany/app/Factorizer.java
        deleted:    src/main/java/com/mycompany/app/FactorizerImpl.java
        deleted:    src/test/java/com/mycompany/app/AppTest.java

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        src/main/java/de/
        src/test/java/de/

no changes added to commit (use "git add" and/or "git commit -a")
```

Commit the changes to the `factorizer` branch and show the commit log:

```sh
git log --oneline           # show the commit log
```
```
c25e65f (HEAD -> factorizer) refactoring to 'de.factorize'
83534b9 factorizer implementation completed
530de68 (main) add pom.xml, src                      <-- 'main' branch
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```

Build and run the *.jar*:

```sh
mvn clean package           # clean build and package .jar

ls -la target/*.jar         # show .jar file
```
```
-rw-r--r-- 1 svgr2 Kein 6327 Oct 17 00:09 target/factorizer-1.0-SNAPSHOT.jar
```

Run the *.jar*:

```sh
java -jar target/factorizer-1.0-SNAPSHOT.jar 3 27 1092 65536 10952347 100000039
```
```
Error: Could not find or load main class com.mycompany.app.App
Caused by: java.lang.ClassNotFoundException: com.mycompany.app.App
```

- What is the cause of this error?

- How can it be fixed?

Fix the problem, re-build and re-run:

```sh
java -jar target/factorizer-1.0-SNAPSHOT.jar 3 27 1092 65536 10952347 100000039
```

The *.jar* is now also working:

```
Hello Factorizer!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

Unfortunately, we missed the *"bug"* for properly building the *.jar* for
committing the refactoring.

- Can a commit be *"fixed"* (altered, changed, updated) after it was made?

- What can be done to include the fix in the *factorizer* branch?

- Can the buggy commit be removed and replaced with the proper one?

- What are conditions for that?

Try to remove the buggy commit and replace with proper one.

After replacement, the proper commit should appear in the commit log:

```sh
git log --oneline
```
```
86aecee (HEAD -> factorizer) refactoring to 'de.factorize' (jar working)
83534b9 factorizer implementation completed
530de68 (main) add pom.xml, src
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 9. Unit Tests

Create JUnit tests for method: `factorize(int n)` that test:

1. regular cases: `n=1, n=2, n=3, n=4, n=27, n=65536, n=10952347, n=100000039 (prime number)`.

1. valid corner cases: `n=0, n=2147483646 (MAX_INT-1), n=2147483647 (MAX_INT)` -- corner
    cases test valid input boundaries.

1. (invalid) exception cases: `n=-1, n=-10, n=-2147483648` -- exception cases test that
    the `factorize(int n)` method throw an `IllegalArgumentException` with message:
    `negative argument`.


You can use methods in the `FactorizerTests` test class:

```java
{
    /**
     * Test regular cases: n=1, n=2, n=3, n=4, n=27, n=65536, n=10952347,
     * n=100000039 (prime number).
     */
    @Test
    @Order(100)
    void test100_factorize_regular_cases() {
        // test code
    }

    /**
     * Test valid corner cases: n=0, n=2147483646 (MAX_INT-1), n=2147483647 (MAX_INT).
     */
    @Test
    @Order(200)
    void test200_factorize_corner_cases() {
        // test code
    }

    /**
     * Test (invalid) exception cases: n=-1, n=-10, n=-2147483648.
     * Exception cases that test that method {@code factorize(int n)} throws
     * {@code IllegalArgumentException} with message: "illegal negative parameter: n".
     */
    @Test
    @Order(300)
    void test300_factorize_exception_cases() {
        // test code
    }
}
```

Develop tests in the IDE and make tests pass for all cases.

Make sure tests pass with *maven* as well:

```sh
mvn test                                # run JUnit tests
```
```
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running de.factorizer.AppTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.094 s -
- in de.factorizer.AppTest
[INFO] Running de.factorizer.FactorizerTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.040 s -
- in de.factorizer.FactorizerTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.071 s
[INFO] Finished at: 2025-04-09T22:13:13+02:00
[INFO] ------------------------------------------------------------------------
```

Run tests also in your IDE:


<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/vscode-tests.png" width="1200"/>


&nbsp;

When everything works, commit tests to the `factorizer` branch:

```sh
git log --oneline                       # show commit log
```
```
19c8646 (HEAD -> factorizer) add unit tests: FactorizerTests.java
86aecee refactoring to 'de.factorize' (with jar working)
83534b9 factorizer implementation completed
530de68 (main) add pom.xml, src                      <-- 'main' branch
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```

The *maven*
[*Surefire Report Plugin*](https://maven.apache.org/surefire/maven-surefire-report-plugin/)
creates test reports.

Create test reports in `target/surefire-reports` (*.txt, *.xml) and
`target/site` (HTML):

```sh
mvn site -DgenerateReports=false        # generate .css
mvn surefire-report:report              # generate test reports under 'target/surefire-reports'

# show test-report
cat cat target/surefire-reports/de.factorizer.FactorizerTests.txt
```
```
-------------------------------------------------------------------------------
Test set: de.factorizer.FactorizerTests
-------------------------------------------------------------------------------
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.047 s -- in de.factorizer.FactorizerTests
```

Open the HTML test report in a browser (if you don't have or cannot invoke *chrome* as command,
open avweb-browser and navigate to the *.html* file):

```sh
chrome target/site/surefire-report.html
```

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/test-report-clean.png" width="600"/>

[*Test report*](https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/test-report-error.png)
with a failed *test100*.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 10. *Javadoc*

*Javadoc* is *Java's* documentation method and toolset based on
[*Java doc strings*](https://de.wikipedia.org/wiki/Javadoc)
that are included in Java comments.

The `javadoc` compiler parses `.java` files and "*compiles*" HTML documentation
from *Java doc strings*.

Invoke the `javadoc` compiler:

```sh
javadoc -d target/docs -Xdoclint:-missing $(find src/main/java -name '*.java')
```
```
Loading source file src\main\java\de\factorizer\App.java...
Loading source file src\main\java\de\factorizer\Factorizer.java...
Loading source file src\main\java\de\factorizer\FactorizerImpl.java...
Constructing Javadoc information...
Creating destination directory: "target/docs\"
Building index for all the packages and classes...
Standard Doclet version 21+35-LTS-2513
Building tree for all the packages and classes...
Generating target\docs\de\factorizer\App.html...
Generating target\docs\de\factorizer\Factorizer.html...
Generating target\docs\de\factorizer\package-summary.html...
Generating target\docs\de\factorizer\package-tree.html...
Generating target\docs\overview-tree.html...
Building index for all classes...
Generating target\docs\allclasses-index.html...
Generating target\docs\allpackages-index.html...
Generating target\docs\index-all.html...
Generating target\docs\search.html...
Generating target\docs\index.html...
Generating target\docs\help-doc.html...
```

Open the HTML in a browser:

```sh
chrome target/docs/index.html
```

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/javadoc-factorizer.png" width="600"/>


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 11. Release

Create a *release*-branch off the *main*-branch (not branch *factorizer*):

```sh
git switch main                         # switch to 'main' branch

git switch -c release                   # branch new 'release' branch off the 'main' branch

git branch                              # show branches
```

Branches are (with `release` being the current branch):

```
  factorizer
  main
* release           <-- current branch
```

Show the current content of the `release` branch:

```sh
find src                                # show content of the `release` branch
```

The release branch has the structure of the main branch with packages `com.mycompany`.
There are no `Factorizer` and `FactorizerTests` classes:

```
src
src/main
src/main/java
src/main/java/com
src/main/java/com/mycompany
src/main/java/com/mycompany/app
src/main/java/com/mycompany/app/App.java
src/test
src/test/java
src/test/java/com
src/test/java/com/mycompany
src/test/java/com/mycompany/app
src/test/java/com/mycompany/app/AppTest.java
```

Run the program:

```sh
java com.mycompany.app.App 3 27 1092 65536 10952347 100000039
```

Output shows no factorization:

```
Hello world!
```

Change the `version` tag in `pom.xml` from `1.0-SNAPSHOT` to `RELEASE-1.0.0`:

```xml
<version>RELEASE-1.0.0</version>
```

Commit the change to the *release*-branch:

```sh
git add .
git commit -m "update pom.xml with version 'RELEASE-1.0.0'"

git log --oneline           # show new commit on 'release' branch
```
```
fc49946 (HEAD -> release) update pom.xml with version 'RELEASE-1.0.0'
530de68 (main) add pom.xml, src
96b05a4 add .gitignore
02082e3 (tag: root) root commit (empty)
```


&nbsp;

Merge the *factorizer*-branch into the *release*-branch.

<!--
```sh
git merge factorizer-de --squash
```
-->

You will likely see merge conflicts:

```
Auto-merging pom.xml
CONFLICT (content): Merge conflict in pom.xml
Automatic merge failed; fix conflicts and then commit the result.
```

Show the status of the *open merge*:

```sh
git status                  # show the status of the open merge
```
```
On branch release
You have unmerged paths.
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Changes to be committed:
        deleted:    src/main/java/com/mycompany/app/App.java
        new file:   src/main/java/de/factorizer/App.java
        new file:   src/main/java/de/factorizer/Factorizer.java
        new file:   src/main/java/de/factorizer/FactorizerImpl.java
        renamed:    src/test/java/com/mycompany/app/AppTest.java -> src/test/ja
va/de/factorizer/AppTest.java
        new file:   src/test/java/de/factorizer/FactorizerTests.java

Unmerged paths:
  (use "git add <file>..." to mark resolution)
        both modified:   pom.xml
```


&nbsp;

Fix the merge conflict and re-build and re-package the application:

```sh
mvn clean package

ls -la target/*.jar
```
```
-rw-r--r-- 1 svgr2 Kein 6363 Oct 17 01:12 target/factorizer-RELEASE-1.0.0.jar
```

Make sure the project builds and runs properly before closing the merge:

```sh
mvn clean compile           # clean rebuild before running the code

mvn test                    # run unit tests -> BUILD SUCCESS

java -jar target/factorizer-RELEASE-1.0.0.jar 3 27 1092 65536 10952347 100000039
```
```
Hello Factorizer!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

Close (commit) the merge with: `merge factorizer, RELEASE-1.0.0`
and show status and commit log:

```sh
git status                          # show the status of the open merge
```
```
On branch release                   # working tree is clean
nothing to commit, working tree clean
```

```sh
git log --oneline --all --graph     # show commit log with branch-graph
```
```
*   8b4f4e0 (HEAD -> release) merge factorizer, RELEASE-1.0.0
|\
| * 19c8646 (factorizer) add unit tests: FactorizerTests.java
| * 86aecee refactoring to 'de.factorize' (with jar working)
| * 83534b9 factorizer implementation completed
* | fc49946 update pom.xml with version 'RELEASE-1.0.0'
|/
* 530de68 (main) add pom.xml, src
* 96b05a4 add .gitignore
* 02082e3 (tag: root) root commit (empty)
```

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-log-after-merge.png" width="600"/>


&nbsp;

The resulting commit graph is:

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-branches.png" width="600"/>


&nbsp;

Show the structure after the merge with packages `de.factorizer`
and with `Factorizer` and `FactorizerTests` classes:

```sh
find src                                # show content of the `release` branch
```
```
src
src/main
src/main/java
src/main/java/de
src/main/java/de/factorizer
src/main/java/de/factorizer/App.java
src/main/java/de/factorizer/Factorizer.java
src/test
src/test/java
src/test/java/de
src/test/java/de/factorizer
src/test/java/de/factorizer/AppTest.java
src/test/java/de/factorizer/FactorizerTests.java
```

Perform a final test on the *release*-branch:

```sh
mvn clean compile test package          # clean re-build, test and package

mvn site -DgenerateReports=false        # generate test report
mvn surefire-report:report
cat cat target/surefire-reports/de.factorizer.FactorizerTests.txt
```
```
-------------------------------------------------------------------------------
Test set: de.factorizer.FactorizerTests
-------------------------------------------------------------------------------
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.047 s -- in de.factorizer.FactorizerTests
```

Open the
[*test report*](https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/test-report-clean.png)
in a browser:

```sh
chrome target/site/surefire-report.html
```

Perform a final run of the created *.jar*:

```sh
java -jar target/factorizer-RELEASE-1.0.0.jar 9 99 999 9999 4999
```
```
Hello Factorizer!
 - n=9 -> [3, 3]
 - n=99 -> [3, 3, 11]
 - n=999 -> [3, 3, 3, 37]
 - n=9999 -> [3, 3, 11, 101]
 - n=4999 -> [4999] (prime number)
```

The final articact: `factorizer-RELEASE-1.0.0.jar` can now be distributed
or pushed to an *artifact repository*:


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 12. Host Project in a Remote Repository

Create a new repository: `mvn-fun` and push branches:

- `main`,

- `factorizer`,

- `release`

to the remote repository.

Use your account at
[*BHT GitLab*](https://gitlab.bht-berlin.de/) or
[*GitHub*](https://github.com)
(or another remote repository site) to host the remote repository.

<!-- Results should show no errors:

<img src="https://github.com/sgra64/c-fun/blob/markup/img/show-versions-2.png?raw=true" width="800"/> -->


<!-- relative paths work for tags and branches -->
<!-- - Step 1 (tag: [*t0*](https://github.com/sgra64/se1-play/tree/t0)) - -->
<!-- - Step 1 (tag: [*t0*](../../tree/t0)) -
    initial commit with [*.gitignore*](.gitignore) `README.md` files.

- Step 2 (tag: [*t1*](../../tree/t1)) -
    commit with the [*.vscode*](.vscode) settings folder for the *VSCode* IDE.

- Step 3 (tag: [*t2*](../../tree/t2)) -
    commit with `.env.sh`, the script to *source* the project
    (see: setup the project environment).

- Step 4 (tag: [*root*](../../tree/root)) -
    commit with `src`, `tests` and `resources` folders added.

- Step 5: a separate branch: [*libs*](../../tree/libs)
    containing *.jar* - libraries are added that are required by the project. -->

<!-- 
The following steps must be performed by a developer on a laptop
for *onboarding* the project.

- Step 6, section [*Getting the Project*](#getting-the-project-se1-play).

- Step 7, section [*Project Setup*](#project-setup).

- Step 8, section [*Project Build*](#project-build).

- Step 9, section [*Running the Application*](#running-the-application).

- Summary: [*Complete Project Content*](#complete-project-content)
 -->
