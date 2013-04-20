
package com.shell.menuhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.shell.menuhelper.util.Consts;

import java.lang.reflect.Field;

public class MainActivity extends Activity {


    private static final String SERVICE_ACTION = "android.shell.Hover";

    private Button mStartButton;
    private Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.start);
        mStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Intent startIntent = new Intent(SERVICE_ACTION);
                startIntent.putExtra(Consts.STATUSBAR_HEIGHT, getStatusBarHeight());
                startService(startIntent);
            }
        });

        mStopButton = (Button) findViewById(R.id.stop);
        mStopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Intent stopIntent = new Intent(SERVICE_ACTION);
                stopService(stopIntent);
            }
        });
    }

    private int getStatusBarHeight() {
        // Rect frame = new Rect();
        // getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // return frame.top;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            // 同样可以通过这个方法去获取system_bar的height　Field名字：navigation_bar_height
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     *
     * <p>Title: getActionBarHeight</p>
     * <p>Description: 获取ActionBar的高度</p>
     * @return
     */
    private float getActionBarHeight() {
        TypedArray actionbarSizeTypedArray = this.obtainStyledAttributes(new int[] {
                android.R.attr.actionBarSize
        });

        float h = actionbarSizeTypedArray.getDimension(0, 0);
        return h;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
