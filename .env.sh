# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# Set up the project environment
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# 
# generate classpath with maven dependencies in file: '.classpath-file'
mvn dependency:build-classpath -Dmdep.outputFile=.classpath-file -q

# prepend path to compiled classes 'target/classes', mind that Windows
# uses ';' as classpath separator while MacOS and Unix/Linux use ':'
[[ "$(uname)" =~ (CYGWIN|MINGW) ]] && sep=';' || sep=':'
echo "target/classes$sep$(cat .classpath-file)" > tmp && mv tmp .classpath-file
unset sep

# set CLASSPATH environment variable
export CLASSPATH=$(cat .classpath-file)

echo -e "\\\\\ \nproject environment has been set up:"
echo " - created: .classpath-file"
echo " - set CLASSPATH: \"$CLASSPATH\""
