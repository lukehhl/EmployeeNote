package com.example.administrator.employeenote.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.activity.ShowLogoActivity;

/**
 * Created by GE11522 on 2016-9-9.
 */
public class LogoView extends View implements Runnable {
    Bitmap bitmap;
    ShowLogoActivity showlogoactivity;

    public LogoView(Context context) {
        super(context);
        showlogoactivity = (ShowLogoActivity) context;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gesaclogo);
        bitmap = Bitmap.createScaledBitmap(bitmap, ShowLogoActivity.width, ShowLogoActivity.height, true);
        new Thread(this).start();

    }

    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Thread.sleep(2000);//延迟多久进入画面


        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            showlogoactivity.gotoMainActivity();
        }
    }

}
