#! /bin/bash

if [ ! -d "~/.config/Code/User/" ];then
    echo "vscode is not installed"
else
    wget wangweiran.space/res/ubuntu/settings.json
    wget wangweiran.sapce/res/ubuntu/keybindings.json
    mv settings.json ~/.config/Code/User/settings.json
    mv settings.json ~/.config/Code/User/keybindings.json
fi

exit 0