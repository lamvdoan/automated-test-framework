# Java and JUnit framework

## Summary
This test framework can test an API, browsers, mobile.

## Set up
1. Install the latest Java JDK
2. Install the latest Gradle
3. Load the build.gradle file when opening this project in Intellij.

## How to run a test
1. Run the gradle build

        gradle build

2. Look for a class that contains "Test"
3. Right click > Run

## Appium Set Up
1. brew install node
2. npm install -g appium
3. npm install wd
4. npm install -g appium-doctor
5. Add this to your ~/.bashrc file:
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home
   export PATH=$JAVA_HOME/bin:$PATH

6. Make sure all is good by: appium-doctor --android
7. To make sure Appium loads: appium --session-override (session-override is for if you want to run tests mulitple times.
  There is an appium bug that will return a 500 if you tried to run tests after the first run.)

## Set up Appium Inspector
1. Connect a device to your laptop
2. Open Appium client
3. Click on the Android Button
4. Set App APK = {APK path of desired device} e.g. /Users/Lam.Doan/Downloads/apk/ankh-release2.1.apk
5. Set Device Name to something meaningful (value doesn't matter).
6. Click on: Launch
7. Once it starts listening, click on the Magnifying glass
8. Once it loads, click on Refresh

## To Run an Appium Test
1. Run the command: appium --session-override
2. (Once it starts listening) Run a test: SampleAppiumTest.irtPressButtons