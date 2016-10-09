package com.example.administrator.employeenote.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by Administrator on 2016/10/3.
 */

public class PlayerSingleton extends MediaPlayer {

    private static MediaPlayer instance = null;

    public static MediaPlayer getInstance(Context context, Uri uri) {    //对获取实例的方法进行同步
        synchronized (MediaPlayer.class) {
            if (instance == null)
                instance = MediaPlayer.create(context, uri);
        }
        return instance;
    }


}
