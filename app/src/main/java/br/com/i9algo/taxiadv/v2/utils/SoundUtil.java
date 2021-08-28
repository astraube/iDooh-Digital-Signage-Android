package br.com.i9algo.taxiadv.v2.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import br.com.i9algo.taxiadv.R;

/**
 * Created by Taxi ADV on 24/03/2016.
 */
public class SoundUtil {

    public static void setSoundOff(Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

    public static void setSoundNormalize(Context context) {
        int max = getSoundMaxLevel(context);
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max/2, AudioManager.FLAG_PLAY_SOUND);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, max/2, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

    public static void setSoundMax(Context context) {
        int max = getSoundMaxLevel(context);
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max, AudioManager.FLAG_PLAY_SOUND);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, max, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

    public static void setSoundLevel(Context context, int soundLevel) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, soundLevel, 0);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, soundLevel, AudioManager.FLAG_PLAY_SOUND);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, soundLevel, AudioManager.FLAG_ALLOW_RINGER_MODES);
    }

    public static int getSoundLevel(Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        return volume_level;
    }

    public static int getSoundMaxLevel(Context context) {
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int volume_level = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return volume_level;
    }

    public static void playSoundBattery(final Context context) {
        try {
            setSoundNormalize(context);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

            MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_bateria_fraca);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SoundUtil.setSoundOff(context);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSoundCoolNotify(final Context context) {
        try {
            setSoundNormalize(context);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();

            MediaPlayer mp = MediaPlayer.create(context, R.raw.cool_notification2);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SoundUtil.setSoundOff(context);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
