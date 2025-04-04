# Project: *mvn-fun* - Project Build with *Maven*

*Maven* is a popular build-tool for Java.

Challenges:

1. [*What is a Build Tool?*](#1-what-is-a-build-tool-for)

1. [*Maven in 5 Minutes*](#2-maven-in-5-minutes)

1. [*Build the Project*](#3-build-the-project)

1. [*Maven Build Lifecycle*](#4-maven-build-lifecycle)

1. [*Build a stand-alone .jar*](#5-build-a-stand-alone-jar)

1. [*Check project into local git repository*](#6-check-project-into-local-git-repository)

1. [*Factorizer.java*](#7-factorizer-java)

1. [*Adjust the Package Structure*](#8-adjust-the-package-structure)

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

Write down the start time and end time of the task to see whether
you managed in 5 minutes.

Perform the
[*Maven in 5 Minutes*](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
challenge starting with installing maven.

Test maven:

```sh
mvn --version           # run maven asking for the installed verion
```

Output is something similar to (know what to when when you see *"mvn: command not found"*):

```
Maven home: C:\opt\maven
Java version: 21, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk
-21
Default locale: en_US, platform encoding: UTF-8
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

Perform step *"Creating a Project"* - `cd` into the project. Draw the structure under
the `src` directory.

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

Google the problem or ask your AI how to solve the problem. You can also find hints
[*here*](https://stackoverflow.com/questions/9689793/cant-execute-jar-file-no-main-manifest-attribute).

Fix the problem, re-build and re-run:

```sh
mvn clean package                               # re-build the program

java -jar target/my-app-1.0-SNAPSHOT.jar        # re-run
```
```sh
Hello Maven!                                    # corect output
```


&nbsp;

## 6. Check Project Into Local *git* Repository

Create a local *git* repository and check the project into the *main* branch.

Make sure, only source files are checked into git and no created artifacts
(create a matching *.gitignore*).


&nbsp;

## 7. *Factorizer.java*

Create a feature-branch: `factorizer` and change the program such that it accepts
input as a number and outputs the prime factors of this number:

```sh
export CLASSPATH="target/classes"

java com.mycompany.app.App 27 1092 10952347
```

Output:

```
 * n=27 -> [3, 3, 3]
 * n=1092 -> [2, 2, 3, 7, 13]
 * n=10952347 -> [7, 23, 59, 1153]
```

Create a component class: `Factorizer.java` as a (lazy) singleton with a method

```
factorize(int n);       --> return prime factors of 'n'
```


&nbsp;

## 8. Adjust the Package Structure

The current package structure: `com.mycompany.app` comes from the initial *maven* project
setup.

Change this structure in the feature-branch to: `de.factorizer`

Rebuild the project and re-run:

```sh
java de.factorizer.Factorizer 27 1092 10952347
```

Output:

```
 * n=27 -> [3, 3, 3]
 * n=1092 -> [2, 2, 3, 7, 13]
 * n=10952347 -> [7, 23, 59, 1153]
```


&nbsp;

## 9. JUnit Tests

Create JUnit tests for method: `factorize(int n)` that test:

- regular cases: n=1, n=2, n=3, n=4, n=27, n=1092, n=10952347

- valid corner cases: n=0, n=2147483647 (MAX_INT)

- invalid exception cases: n=-1, n=-10, n=-2147483648 -- exception cases test that
    the `factorize(int n)` method throw an `IllegalArgumentException` with message:
    `negative argument`.

Make sure all tests pass:

```sh
mvn test                    # run JUnit tests
```
```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```


&nbsp;

## 10. Release

Create a *release*-branch off the *main*-branch.

Merge the *feature*-branch into the *release*-branch.

Change the build-process on the *release*-branch such it produces a releasable artifact:

```sh
factorizer-RELEASE-1.0.0.jar        # artifact to release
```

Make sure all tests pass on the *release*-branch:

```sh
mvn clean test                      # run JUnit tests
```
```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```

```sh
mvn clean test package              # re-build, test, package
```

Check the released artifact has properly been created:

```
ls -la target
```
```
-rw-r--r-- 1 2888 Apr  4 01:38 factorizer-RELEASE-1.0.0.jar
```

Perform a final run:

```sh
java de.factorizer.Factorizer 27 1092 10952347
```

Output:

```
 * n=27 -> [3, 3, 3]
 * n=1092 -> [2, 2, 3, 7, 13]
 * n=10952347 -> [7, 23, 59, 1153]
```

Commit with message `RELEASE-1.0.0` to the *release*-branch and tag the commit
with `RELEASE-1.0.0`.

Push branches:

- `main`,

- `factorizer`,

- `release`

 to a remote repository: `mvn-fun` you can create at BHT GitLab or another remote
 Git server.


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
