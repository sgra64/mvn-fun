#!/bin/bash
# 
cpfile=".vscode/cp.txt"     # file with CLASSPATH
delombok="target/generated-sources/delombok"

# output classpath to file '.vscode/cp.txt', append 'target/classes'
function mkcp() {
    echo "create classpath in '$cpfile'"
    mvn -q dependency:build-classpath -Dmdep.outputFile=$cpfile
    [ "$(egrep '\\' $cpfile)" ] && local sep=";" || local sep=":"
    echo "${sep}target/classes" >> $cpfile
}

# run application
function run() {
    # mvn exec:java -q
    [ -f "$cpfile" ] &&
        java -cp @$cpfile mvn.jdbc.application.Application ||
        echo "could not find CLASSPATH file: '$cpfile', create with 'mkcp'"
}

function javadoc() {
    # insert in 'pom.xml' into section: 'plugins.build':
    # - - - - - - - - - - -
    # <!-- https://maven.apache.org/plugins/maven-javadoc-plugin/usage.html -->
    # <plugin>
    #   <groupId>org.apache.maven.plugins</groupId>
    #   <artifactId>maven-javadoc-plugin</artifactId>
    #   <version>3.12.0</version>
    #   <configuration>
    #       <!-- suppress javadoc warnings -->
    #       <doclint>all,-missing</doclint>
    #       <sourcepath>${delombok.output}</sourcepath>
    #   </configuration>
    # </plugin>
    # 
    [ -d "$delombok" -o -f "$delombok" ] && echo "rm -rf $delombok" && rm -rf "$delombok"
    # 
    local dir="target/reports/apidocs"
    [ -d "$dir" -o -f "$dir" ] && echo "rm -rf $dir" && rm -rf "$dir"
    # 
    mvn javadoc:javadoc &&
        echo "delomboked 'src/main/java' to '$delombok'" &&
        echo -e "- compiled javadoc from '$delombok'\n- to: '$dir/index.html'"
}

function delombok() {
    # de-lombok 'src/main' and output to 'target/generated-sources/delombok'
    # 
    # insert into pom.xml:
    # - - - - - - - - - - -
    # <properties>      <!-- add to <properties> -->
    #   <lombok.version>1.18.20.0</lombok.version>
    #   <lombok.sourceDirectory>src/main/java</lombok.sourceDirectory>
    #   <lombok.outputDirectory>${project.build.directory}/generated-sources/delombok</lombok.outputDirectory>
    #   <delombok.output>${project.build.directory}/generated-sources/delombok</delombok.output>
    # </properties>
    # 
    # <!-- insert into section: 'plugins.build': -->
    # <plugin>
    #   <groupId>org.projectlombok</groupId>
    #   <artifactId>lombok-maven-plugin</artifactId>
    #   <version>${lombok.version}</version>
    #   <dependencies>
    #     <dependency>
    #         <groupId>org.projectlombok</groupId>
    #         <artifactId>lombok</artifactId>
    #         <version>1.18.30</version>
    #     </dependency>
    #   </dependencies>
    #   <executions>
    #     <execution>
    #         <phase>generate-sources</phase>
    #     <goals>
    #         <goal>delombok</goal>
    #     </goals>
    #     <configuration>
    #         <encoding>UTF-8</encoding>
    #         <addOutputDirectory>false</addOutputDirectory>
    #     </configuration>
    #     </execution>
    #   </executions>
    # </plugin>
    # 
    local dir="target/generated-sources/delombok"
    [ -d "$dir" -o -f "$dir" ] && echo "rm -rf $dir" && rm -rf "$dir"
    # 
    mvn lombok:delombok &&
        echo "delomboked 'src/main/java' to '$delombok'"
}

function delombok_2() {
    # de-lombok 'src/main' and output to 'target/delombok'
    # 
    local lombok=""
    for d in . .. ../..; do
        local lib="$d/lombok-1.18.42.jar"
        [ -f "$lib" ] && lombok="$lib" && echo "found: $lib" && break
    done
    if [ "$lombok" ]; then
        java -cp @$cpfile -jar $lombok delombok "src/main" -d "target/delombok" 2> /dev/null
        echo "de-lomboked to: 'target/delombok'"
    else
        echo "could not find 'lombok-1.18.42.jar', download from maven repository"
    fi
}
