package com.dragondevl.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AudioUtil {
    public static final int AUDIO_MANAGER_AUTH_FAIL = -1;

    private AudioManager mAudioManager;
    private static volatile AudioUtil sInstance;

    private AudioUtil(Context mContext) {
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static AudioUtil getInstance(Context mContext) {
        if (sInstance == null) {
            synchronized (AudioUtil.class) {
                if (sInstance == null) {
                    sInstance = new AudioUtil(mContext);
                }
            }
        }
        return sInstance;
    }

    //获得铃声最大音量
    public int getRingMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //获得多媒体最大音量
    public int getMediaMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //通话最大音量
    public int getCallMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //系统最大音量
    public int getSystemMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //取得当前多媒体音量
    public int getMediaVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //获取当前铃声音量
    public int getRingVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //获取当前系统音量
    public int getSystemVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //提示音最大音量
    public int getAlarmMaxVolume() {
        if (mAudioManager != null) {
            return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        } else {
            return AUDIO_MANAGER_AUTH_FAIL;
        }
    }

    //设置多媒体音量
    public void setMediaVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    //设置多媒体音量
    public void setSystemVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
        }
    }

    //设置多媒体音量
    public void setRingVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
        }
    }

    //设置通话音量
    public void setCallVolume(int volume) {
        if (mAudioManager != null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volume, AudioManager.STREAM_VOICE_CALL);
        }
    }

    //通话时可设置免提或者喇叭
    public void setSpeakerStatus(boolean on) {
        if (mAudioManager != null) {
            if (on) {
                mAudioManager.setSpeakerphoneOn(true);
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                int maxCallVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, maxCallVolume, AudioManager.STREAM_VOICE_CALL);
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                mAudioManager.setSpeakerphoneOn(false);
            }
        }
    }

}
