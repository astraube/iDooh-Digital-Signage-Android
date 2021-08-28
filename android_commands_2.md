### Open Settings app ###
`adb shell am start -a android.settings.SETTINGS`
-----------------------------------------------

### Check App version ###
`adb shell dumpsys package <PACKAGE> | grep versionName`
-----------------------------------------------

### Force stop an app ###
`adb shell am force-stop <PACKAGE>`
-----------------------------------------------

### Enable/Disable wifi ###
`adb shell svc wifi disable [enable]`
-----------------------------------------------

### Clear shared preferences ###
`adb shell pm clear <package name>`
-----------------------------------------------

### Get device Android Id ###
`adb shell settings get secure android_id`
-----------------------------------------------

### Enable Location Services ###
`adb shell settings put secure location_providers_allowed gps,network`
-----------------------------------------------

### CHANGING A DEVICE RESOLUTION ###
http://www.wikihow.com/Change-the-Screen-Resolution-on-Your-Android

`adb shell dumpsys display | grep mBaseDisplayInfo`

Find the density value

`adb shell wm density <NEW DENSITY> && adb reboot`

ldpi => 120
mdpi => 160
hdpi => 240
-----------------------------------------------

### Get Physical screen size ###
`adb shell wm size`
-----------------------------------------------

### Start Launcher App ###
`adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME`
-----------------------------------------------

### Turn screen off/on (press power button) ###
`adb shell input keyevent 26`
-----------------------------------------------

### (press back button) ###
`adb shell input keyevent 4`
-----------------------------------------------
