#! /bin/bash

wget wangweiran.space/res/ubuntu/vimrc.local
wget wangweiran.space/res/ubuntu/.gitconfig

sudo mv vimrc.local /etc/vim/vimrc.local
mv .gitconfig ~/.gitconfig

exit 0
