# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# initialize new terminal (bash, zsh/Mac), script is called in settings.json
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# 
# probe terminal is opened on Mac with zsh and
type setopt 2>/dev/null | grep builtin >/dev/null
[ $? = 0 ] && \
    source ~/.zshrc ||
    source ~/.bashrc

# source the project when opening a new terminal
for env in "env.sh" ".env/env.sh"; do
    [ -f "$env" ] && source "$env" && break
done
