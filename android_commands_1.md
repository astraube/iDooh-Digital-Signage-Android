SEE: http://adbshell.com/commands/adb-shell-netstat
SEE: https://gist.github.com/otikev/95d0e163704367048b1d0efcefcc7151
SEE: https://www.androidjungles.com/adb-fastboot-commands/

# fastboot update
adb reboot-bootloader
adb reboot bootloader
fastboot flash system system.img
fastboot flash recovery recovery.zip
fastboot flash boot boot-new-V2.img
fastboot reboot
-----------------------------------------------
fastboot devices
fastboot -i 0x0fce oem unlock 0x <insert your unlock code>
fastboot oem unlock
-----------------------------------------------
# recovery mode
adb reboot recovery
adb sideload update.zip
-----------------------------------------------

# mount remount system ready-only
su
mount -o remount,rw none /system 
# comandos
mount -o remount,ro none /system
-----------------------------------------------

# advanced-settings
adb shell am start -n com.android.settings/.Settings
adb shell am start -a android.intent.action.MAIN -n com.android.settings/.Settings
-----------------------------------------------

# listar packages
adb shell pm list packages -f

# trim cache files to reach the given free space
adb shell pm trim-caches

adb shell pm clear br.com.i9algo.taxiadv

adb shell content call --uri content://br.com.i9algo.taxiadv.Commands --method sendGeoTest

# Keep the data and cache directories around after package removal.
adb shell pm uninstall -k br.com.i9algo.taxiadv

adb shell pm uninstall br.com.i9algo.taxiadv

adb shell pm list packages -f br.com.i9algo.taxiadv

adb shell am start -a android.intent.action.MAIN "br.com.i9algo.taxiadv/.v2.views.LauncherActivity"
-----------------------------------------------

# abrir configurações de GPS
adb shell am start -a android.settings.LOCATION_SOURCE_SETTINGS
-----------------------------------------------

##### Arterar Configuracoes
##### 
##### ADB DEBUG call_put(...)
#####
# ativar GPS
# alta precisao
adb shell settings put secure location_providers_allowed gps,network
adb shell settings put secure location_providers_allowed '+gps,network'
adb shell settings put secure location_providers_allowed -gps,network

# tempo de desligamento da tela
settings put system screen_off_timeout -1

# Seguranca - Nenhum Bloqueio de Tela
settings put global voice_unlock_screen null
settings put global voice_unlock_and_launch1 null
settings put global voice_unlock_and_launch2 null
settings put global voice_unlock_and_launch3 null

# Permanecer Ativo enquanto estiver carregando
settings put global stay_on_while_plugged_in 0
settings put global stay_on_while_plugged_in 3

# ativar adb
settings put global adb_enabled 1

SEE: https://developer.android.com/reference/android/provider/Settings.Global

### Ativar modo avião
`settings put global airplane_mode_on 1`
`am broadcast -a android.intent.action.AIRPLANE_MODE`

### Desativar modo avião
`settings put global airplane_mode_on 0`
`am broadcast -a android.intent.action.AIRPLANE_MODE`

-----------------------------------------------
# Stop Process
pc $ adb -d shell
android $ su
android # ps
android # kill <process id from ps output>

#Stop APP
adb shell stop br.com.i9algo.taxiadv
adb shell am force-stop br.com.i9algo.taxiadv
-----------------------------------------------

# wifi-settings
adb shell am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings
-----------------------------------------------

# HOME
adb shell am start -c android.intent.category.HOME -a android.intent.action.MAIN
-----------------------------------------------

# broadcast-boot-completed
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
-----------------------------------------------

# press-menu-button
adb shell input keyevent 82
-----------------------------------------------

# botão de liga/desliga tela
adb shell input keyevent 26
-----------------------------------------------

# copy databases
adb shell run-as br.com.i9algo.taxiadv chmod 666 /data/data/br.com.i9algo.taxiadv/databases/taxiadv.db
adb pull /data/data/br.com.i9algo.taxiadv/databases/taxiadv.db
-----------------------------------------------

# printscreen
adb shell screencap /sdcard/screen.png
# copiar para o computador
adb pull /sdcard/screen.png screen.png
-----------------------------------------------

# video da tela
adb shell screenrecord /sdcard/demo.mp4 <press Ctrl-C to stop recording>
adb pull /sdcard/demo.mp4
adb shell screenrecord --bit-rate 7000000 /sdcard/demo.mp4 <7Mbps>
adb shell screenrecord --size <WIDTHxHEIGHT>
adb shell screenrecord --time-limit <TIME max 180 seconds>
# copiar para o computador
adb pull /sdcard/demo.mp4
-----------------------------------------------


# atualizar google play service
adb install -r -d -l [app name]
adb install -r -d -l com.google.android.gms-1-update-play-service-30.10.2018.apk

-r	A instalação de sobreposição é permitida.
-s	Instale o aplicativo para o sdcard.
-d	Permitir instalação de substituição degradada.

-----------------------------------------------