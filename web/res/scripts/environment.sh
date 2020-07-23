#!/bin/bash
# Description:
# This script develop tools, compilers and compiling dependence.

# Build basic environment
function environment()
{
    echo "Start setting basic environment up..."

    sudo apt-get install -y android-tools-fastboot -y android-tools-adb -y flex
    sudo apt-get install -y libx11-dev:i386 -y libreadline6-dev:i386 -y xsltproc
    sudo apt-get install -y python-markdown -y build-essential -y rar -y gperf
    sudo apt-get install -y libsdl1.2-dev -y libesd0-dev -y bison -y g++ -y curl
    sudo apt-get install -y gnupg -y flex -y bison -y gperf -y libxml-libxml-perl
    sudo apt-get install -y zlib1g-dev -y gcc-multilib -y g++-multilib -y dpkg-dev
    sudo apt-get install -y lib32ncurses5-dev -y x11proto-core-dev -y zip -y libc6
    sudo apt-get install -y libxml2-utils -y xsltproc -y minizip -y libswitch-perl
    sudo apt-get install -y lib32z-dev -y ccache -y tofrodos -y zlib1g-dev:i386
    sudo apt-get install -y libssl-dev -y libidn11-dev -y vim-gtk -y python3-pip 
    sudo apt-get install -y libxml-parser-perl -y libidn11 -y libncurses5-dev:i386
    sudo apt-get install -y flashplugin-installer -y aptitude -y unrar -y git-core
    sudo apt-get install -y openssl -y zlibc -y unzip -y lib32stdc++6 -y libc6:i386
    sudo apt-get install -y default-jdk -y default-jre -y python-pip -y git -y m4
    sudo apt-get install -y libgl1-mesa-dev -y libxml-simple-perl -y libx11-dev

    sudo apt-get clean
    sudo apt-get autoclean
    fastboot_path=`which fastboot`
    sudo chmod a+s $fastboot_path
}

# If u want to uninstall default JDK, command below helps purge them completely
# sudo apt-cache search java | awk '{print($1)}' | grep -E -e '^(ia32-)?(sun|oracle)-java' -e '^openjdk-' -e '^icedtea' -e '^(default|gcj)-j(re|dk)' -e '^gcj-(.*)-j(re|dk)' -e 'java-common' | xargs sudo apt-get -y remove
function jdk_setup()
{
    oracle_test=`sed -n '/default-java/p' /etc/profile`
    if [ "$oracle_test" == "" ];then
        sudo sed -i '$a\# Wangweiran add these 4 lines below to set java path for you, DO NOT EDIT/REMOVE THEM, OR CRITICAL ERRORS WILL BE CAUSED!' /etc/profile
        sudo sed -i '$a\export JAVA_HOME=/usr/lib/jvm/default-java' /etc/profile
        sudo sed -i '$a\export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar' /etc/profile
        sudo sed -i '$a\export PATH=$PATH:$JAVA_HOME/bin' /etc/profile
        echo "JAVA_HOME environment set successfully!"
    else
        echo "Skip set JAVA_HOME for it has been set jet!"
    fi

    source /etc/profile
}

function gui()
{
    echo "Installing vscode, google-chrome and filezilla..."

    if [ -e "/etc/apt/sources.list.d/google-chrome.list" ];then
        echo "google-chrome source has been setup"
    else
        wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
        sudo sh -c 'echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
    fi

    if [ -e "/etc/apt/sources.list.d/vscode.list" ];then
        echo "vscode source has been setup"
    else
        curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > packages.microsoft.gpg
        sudo install -o root -g root -m 644 packages.microsoft.gpg /etc/apt/trusted.gpg.d/
        sudo sh -c 'echo "deb [arch=amd64 signed-by=/etc/apt/trusted.gpg.d/packages.microsoft.gpg] https://packages.microsoft.com/repos/vscode stable main" > /etc/apt/sources.list.d/vscode.list'
    fi

    sudo apt-get update

    sudo apt-get install -y apt-transport-https -y code
    sudo apt-get install -y google-chrome-stable
    sudo apt-get install -y filezilla -y psensor

    echo "finish"
}

function help() {
    echo "-g or --gui to install gui software only, containing chrome, vscode, filezilla and psensor"
    echo "-a or --all to setup environment and install gui software"
    echo "-h to show this message"
    echo "Setup environment defaultly with no parameters input"

    exit 0
}

declare para_num=$#
ARGS=`getopt -o agh --long all,gui,help -- "$@"`
eval set -- "${ARGS}"

while true
do
	case "$1" in
	-a|--all)
		gui
	    environment
	    jdk_setup
		shift ;;
	-g|--gui)
		gui
		shift ;;
    -h|--help)
		help
		shift ;;
	--)
		if [ "$para_num" == "0" ];then
		    sudo apt-get update
            environment
		fi
		shift
		break ;;
	*)
		echo "Invalid Parameters";
		exit 1 ;;
	esac
done

echo
echo "Thanks for using environment builder"
echo "Version: 1.8"

exit 0
