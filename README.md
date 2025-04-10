# Project: *mvn-fun* - Project Build with *Maven*

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

1. [*JUnit Tests*](#9-junit-tests)

1. [*Release*](#10-release)


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



&nbsp;

## 2. Maven in 5 Minutes

Write down the start time and end time of the task and see whether you managed in 5 minutes.

Perform the
[*Maven in 5 Minutes*](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
challenge starting with installing maven (or testing maven has been installed).

Test maven:

```sh
mvn --version           # run maven asking for the installed verion
```

Output should be something similar to (know what to when when you see *"mvn: command not found"*):

```
Maven home: C:\opt\maven
Java version: 21, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk
-21
Default locale: en_US, platform encoding: UTF-8
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

Make sure, environment variables maven uses have been set matching your system.
See example [*.bashrc*](https://github.com/sgra64/dotfiles/blob/main/.bashrc.path)
how to set variables.

```sh
echo $JAVA_HOME         # output value of JAVA_HOME environment variable
--> /c/Program Files/Java/jdk-21

echo $MAVEN_HOME        # path where maven is installed on your system
--> /c/opt/maven

echo $M2_HOME           # path where maven stores downloaded dependencies (.jar)
--> /c/Sven1/svgr2/tmp/svgr/.m2
```

Perform step *"Creating a Project"* - `cd` into the project. Draw the structure under
the `src` directory.

<!-- 
Create project directory 'my-app' with the demo maven project inside:
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app \
        -DarchetypeArtifactId=maven-archetype-quickstart \
        -DarchetypeVersion=1.5 -DinteractiveMode=false
 -->

Open file [*pom.xml*](pom.xml). Which data-format is used for this file?

Open the source-file `App.java`

```java
package com.mycompany.app;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

Relate the package structure `com.mycompany.app` to the location of the file in
the src-tree:

```sh
find src
```
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
target
target/classes
target/classes/com
target/classes/com/mycompany
target/classes/com/mycompany/app
target/classes/com/mycompany/app/App.class
target/generated-sources
target/generated-sources/annotations
target/maven-status
target/maven-status/maven-compiler-plugin
target/maven-status/maven-compiler-plugin/compile
target/maven-status/maven-compiler-plugin/compile/default-compile
target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst
target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst
```

Run the program:

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
total 12
drwxr-xr-x 1    0 Apr  4 00:33 ./
drwxr-xr-x 1    0 Apr  4 00:24 ../
drwxr-xr-x 1    0 Apr  4 00:24 classes/
drwxr-xr-x 1    0 Apr  4 00:24 generated-sources/
drwxr-xr-x 1    0 Apr  4 00:33 generated-test-sources/
drwxr-xr-x 1    0 Apr  4 00:33 maven-archiver/
drwxr-xr-x 1    0 Apr  4 00:24 maven-status/
-rw-r--r-- 1 2872 Apr  4 00:33 my-app-1.0-SNAPSHOT.jar      <-- packaged .jar file
drwxr-xr-x 1    0 Apr  4 00:33 surefire-reports/
drwxr-xr-x 1    0 Apr  4 00:33 test-classes/
```

Run the packaged result of the build process: `my-app-1.0-SNAPSHOT.jar`:

```sh
java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
```
```
Hello World!
```



&nbsp;

## 4. Maven Build Lifecycle

Learn about the
[*Maven Build Lifecycle*](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
and write down actions the following commands perform:

- mvn *validate*
- mvn *compile*
- mvn *test*
- mvn *package*
- mvn *verify*
- mvn *install*
- mvn *deploy*
- mvn *clean*

Maven internally manages `CLASSPATH` from dependencies in `pom.xml`:

```sh
mvn dependency:build-classpath              # show CLASSPATH or write to file
mvn dependency:build-classpath -Dmdep.outputFile=.classpath     # '.classpath'
cat .classpath
```

`CLASSPATH` refers to `.jar` files maven downloaded from `pom.xml` depeendencies
from the
[*Maven Repository*](https://mvnrepository.com/)
(or *"Maven Central" repository*) to the local cache located at `M2_HOME`
(usually under a folder `.m2` in the `HOME` directory).



&nbsp;

## 5. Build a stand-alone *.jar*

Change the print line from: *"Hello World!"* to: *"Hello Maven!"*.

*"Re-Build"* (recompile and repackage) the resulting artifact:

```sh
mvn clean compile package               # clean the project (remove 'target') and re-build
mvn clean package                       # shorter form (package performs compile, if not compiled)
```

Run the resulting *.jar* as a stand-alone program:

```sh
java -jar target/my-app-1.0-SNAPSHOT.jar
```

An error occurs.:

```
no main manifest attribute, in target/my-app-1.0-SNAPSHOT.jar
```

Google the problem or ask your AI how to solve the problem. You may also find hints
[*here*](https://stackoverflow.com/questions/9689793/cant-execute-jar-file-no-main-manifest-attribute).

<!-- 
# adjust <mainClass> to: com.mycompany.app.App
<plugin>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.4.2</version>
    <!-- insert begin -- >
    <configuration>
        <archive>
        <manifest>
            <addClasspath>true</addClasspath>
            <classpathPrefix>lib/</classpathPrefix>
            <mainClass>com.mycompany.app.App</mainClass>
        </manifest>
        </archive>
    </configuration>
    <!-- insert end -- >
</plugin>
# 
# create patch file:    diff -Naru pom_orig.xml pom.xml > pom.patch
# apply patch:          patch pom.xml < pom.patch
 -->

Fix the problem, re-build and re-run:

```sh
mvn clean package                               # re-build the program

java -jar target/my-app-1.0-SNAPSHOT.jar        # re-run
```
```sh
Hello Maven!                                    # corect output
```



&nbsp;

## 6. Check Project Into a Local *git* Repository

Create a local *git* repository with an empty root commit (to create clean branches).

Next, commit a [*.gitignore*](https://github.com/sgra64/se1-play/blob/main/.gitignore)
file to the `main` branch.

Then, commit `pom.xml` and sources to the `main` branch.
Remove unwanted content: `git clean -fd`.

The project directory should now contain:

```sh
ls -la                                          # show project directory
```
```
total 21
drwxr-xr-x 1 svgr2 Kein    0 Apr  9 12:25 .
drwxr-xr-x 1 svgr2 Kein    0 Apr  9 12:15 ..
-rw-r--r-- 1 svgr2 Kein  499 Apr  9 12:25 .classpath
drwxr-xr-x 1 svgr2 Kein    0 Apr  9 12:25 .git
-rw-r--r-- 1 svgr2 Kein 1331 Apr  9 12:13 .gitignore
-rw-r--r-- 1 svgr2 Kein 3511 Apr  9 12:05 pom.xml
drwxr-xr-x 1 svgr2 Kein    0 Apr  9 11:48 src
drwxr-xr-x 1 svgr2 Kein    0 Apr  9 12:06 target
```

The local *git* repository should contain three commits on the `main` branch:

```sh
git log --oneline                               # show commit log
```
```
1b0d048 (HEAD -> main) add pom.xml src
da96b73 add .gitignore
b5d633b (tag: root) root commit (empty)
```



&nbsp;

## 7. *Factorizer.java*

Create a feature-branch: `factorizer` and modify the program such that it accepts
numbers passed through the command line and outputs the prime factors of these numbers:

```sh
export CLASSPATH="target/classes"

java com.mycompany.app.App 3 27 1092 65536 10952347 100000039
```

Output shows factors of `n` and an indicator of prime numbers:

```
Hello Factors!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

Create a component class: `Factorizer.java` as a (lazy) singleton with a method

```java
factorize(int n);           // --> return prime factors of positive 'n'
```

Commit changes to the `factorizer` branch.

```sh
git log --oneline                               # show commit log
```
```
2a606d3 (HEAD -> factorizer) add Factorizer.java    <-- 'factorizer' branch
1b0d048 (main) add pom.xml src                      <-- 'main' branch
da96b73 add .gitignore
b5d633b (tag: root) root commit (empty)
```



&nbsp;

## 8. Refactor the Package Structure

The current package structure: `com.mycompany.app` comes from the initial *maven* project
setup.

Refactor this structure in the feature-branch to: `de.factorizer`

Rebuild the project and re-run:

```sh
java de.factorizer.App 3 27 1092 65536 10952347 100000039
```

Output:

```
Hello Factors!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```

The structure of the `src` tree after refactoring is:

```sh
find src                    # show 'src' structure after refactoring
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
```

Show the impact of the refactoring on modified files:

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
        deleted:    src/test/java/com/mycompany/app/AppTest.java

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        src/main/java/de/
        src/test/java/de/

no changes added to commit (use "git add" and/or "git commit -a")
```

Commit the changes to the `factorizer` branch and show the commit log:

```sh
git log --oneline                       # show commit log
```
```
2c4f957 (HEAD -> factorizer) refactoring to 'de.factorize' structure
2a606d3 add Factorizer.java
1b0d048 (main) add pom.xml src                      <-- 'main' branch
da96b73 add .gitignore
b5d633b (tag: root) root commit (empty)
```



&nbsp;

## 9. JUnit Tests

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

Commit tests to the `factorizer` branch:

```sh
git log --oneline                       # show commit log
```
```
fc9b7e5 (HEAD -> factorizer) add tests FactorizerTests.java
2c4f957 refactoring to 'de.factorize' structure
2a606d3 add Factorizer.java
1b0d048 (main) add pom.xml src                      <-- 'main' branch
da96b73 add .gitignore
b5d633b (tag: root) root commit (empty)
```



&nbsp;

## 10. Release

Create a *release*-branch off the *main*-branch:

```sh
git switch main                         # switch to 'main' branch

git checkout -b release                 # branch new 'release' branch off the 'main' branch

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

The release branch has structure of the main branch with packages `com.mycompany`
and there are no `Factorizer` and `FactorizerTests` classes:

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


Change the `version` tag in `pom.xml` from `1.0-SNAPSHOT` to `RELEASE-1.0.0`:

```xml
<version>RELEASE-1.0.0</version>
```

Commit the change to the *release*-branch:

```sh
git add .
git commit -m "update pom.xml with RELEASE-1.0.0 version"

git log --oneline           # show new commit on 'release' branch
```
```
62ea783 (HEAD -> release) update pom.xml with RELEASE-1.0.0 version
1b0d048 (main) add pom.xml src
da96b73 add .gitignore
b5d633b (tag: root) root commit (empty)
```


&nbsp;

Merge the *factorizer*-branch into the *release*-branch:

```
Auto-merging pom.xml
CONFLICT (content): Merge conflict in pom.xml
Automatic merge failed; fix conflicts and then commit the result.
```

Resolve the merge conflict and show the status of the *open merge*:

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
        renamed:    src/test/java/com/mycompany/app/AppTest.java
                                -> src/test/java/de/factorizer/AppTest.java
        new file:   src/test/java/de/factorizer/FactorizerTests.java

Unmerged paths:
  (use "git add <file>..." to mark resolution)
        both modified:   pom.xml
```

Close (commit) the merge with: `merge branch factorizer, RELEASE-1.0.0`
and show status and commit log:

```sh
git status                      # show the status of the open merge

On branch release               # working tree is clean
nothing to commit, working tree clean
```

```sh
git log --graph --oneline       # show commit log with branch-graph
```

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-log-after-merge.png" width="600"/>


```
*   4316014 (HEAD -> release) merge branch factorizer, RELEASE-1.0.0
|\
| * fc9b7e5 (factorizer) add tests FactorizerTests.java
| * 2c4f957 refactoring to 'de.factorize' structure
| * 2a606d3 add Factorizer.java
* | 62ea783 update pom.xml with RELEASE-1.0.0 version
|/
* 1b0d048 (main) add pom.xml src
* da96b73 add .gitignore
* b5d633b (tag: root) root commit (empty)
```


&nbsp;

The resulting commit graph is:

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-branches.png" width="600"/>


&nbsp;

Show the new structure after the merge with packages `de.factorizer`
and now with `Factorizer` and `FactorizerTests` classes:

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


Make sure all tests pass on the *release*-branch:

```sh
mvn clean compile test                  # run JUnit tests
```
```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

```sh
mvn clean compile test package          # re-build, test, package
```

Check the released artifact has properly been created:

```
ls -la target
```

A final artifact `factorizer-RELEASE-1.0.0.jar` to release has been created:

```
-rw-r--r-- 1 svgr2 Kein 5825 Apr 10 22:59 factorizer-RELEASE-1.0.0.jar
```

Perform a final run with the created `.jar`:

```sh
java -jar target/factorizer-RELEASE-1.0.0.jar 3 27 1092 65536 10952347 100000039
```
```
Hello Factors!
 - n=3 -> [3] (prime number)
 - n=27 -> [3, 3, 3]
 - n=1092 -> [2, 2, 3, 7, 13]
 - n=65536 -> [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]
 - n=10952347 -> [7, 23, 59, 1153]
 - n=100000039 -> [100000039] (prime number)
```


Commit with message `"changed version in pom.xml to: RELEASE-1.0.0"` to the *release*-branch
and tag the commit with `RELEASE-1.0.0`.

Push branches:

- `main`,

- `factorizer`,

- `release`

to a remote repository: `mvn-fun` you can create at
[*BHT GitLab*](https://gitlab.bht-berlin.de/)
or another Git service such as
[*GitHub*](https://github.com/).


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
