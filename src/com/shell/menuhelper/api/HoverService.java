
package com.shell.menuhelper.api;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.shell.menuhelper.FloatView;
import com.shell.menuhelper.R;
import com.shell.menuhelper.util.Consts;

import java.util.ArrayList;
import java.util.List;

public class HoverService extends Service {

    private static final boolean DEBUG = true;
    private static final String TAG = "HoverService";

    private static final String ACTION_HOME_HIDDEN = "com.shell.menuhelper.ACTION_HOME_HIDDEN";
    private static final String ACTION_APPS_SHOW = "com.shell.menuhelper.ACTION_APPS_SHOW";

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams = null;
    private FloatView mMenuView = null;
    private int mStatusBarHeight = 0;

    private ListnerHomeTask mHomeTask;

    private BroadcastReceiver mHideInLauncherReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            final String action = intent.getAction();
            if(ACTION_HOME_HIDDEN.equals(action)) {
                mMenuView.setVisibility(View.INVISIBLE);
            }
            if(ACTION_APPS_SHOW.equals(action)) {
                mMenuView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        if(DEBUG)Log.i(TAG, "onCreate invoked!");
        super.onCreate();
        mContext = getApplicationContext();
        mWindowManager = ((MyApplication)mContext).getWindowManager();
        mWmParams = ((MyApplication)mContext).getWmParams();

        registerBroadCast();

    }

    /**
     *
     * <p>Title: registerBroadCast</p>
     * <p>Description: 注册监听在应用程序显示或者在Launcer隐藏的广播</p>
     */
    private void registerBroadCast() {
        // TODO Auto-generated method stub
        IntentFilter hideOrShowFilter = new IntentFilter(ACTION_APPS_SHOW);
        hideOrShowFilter.addAction(ACTION_HOME_HIDDEN);
        mContext.registerReceiver(mHideInLauncherReceiver, hideOrShowFilter);
    }

//    /**
//     * <p>
//     * Title: initWmParams
//     * </p>
//     * <p>
//     * Description: init WindowManager LayoutParams
//     * </p>
//     */
//    private void initWmParams() {
//        // TODO Auto-generated method stub
//        mWmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // 系统提示类型,重要
//        mWmParams.format = PixelFormat.RGBA_8888;
//        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
//        mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
//        mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
//
//        // 以屏幕左上角为原点，设置x、y初始值
//        mWmParams.x = 0;
//        mWmParams.y = 0;
//        // 设置悬浮窗口长宽数据
//        mWmParams.width = 129;
//        mWmParams.height = 129;
//
//    }

    private void addMenuView() {
        // TODO Auto-generated method stub

        mMenuView.setAlpha(255);
        mMenuView.setmWmParams(mWmParams);
        mMenuView.setResId(R.drawable.menu, R.drawable.menu_pressed);
        // 调整悬浮窗口
        // mWmParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        mWindowManager.addView(mMenuView, mWmParams);

        mMenuView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (DEBUG)
                    Log.i(TAG, "mMenuView onClick invoked!");
//                new Thread() {
//                    public void run() {
//                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
//                        return;
//                    };
//                }.start();
            }
        });

//        mMenuView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                // TODO Auto-generated method stub
//                final int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (DEBUG)
//                            Log.i(TAG, "mMenuView is Down");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (DEBUG)
//                            Log.i(TAG, "mMenuView is UP");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (DEBUG)
//                            Log.i(TAG, "mMenuView is MOVE");
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        if (DEBUG)
//                            Log.i(TAG, "mMenuView is CANCEL");
//                        break;
//
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

        mMenuView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                if (DEBUG)
                    Log.i(TAG, "mMenuView onLongClick invoked!");
                return false;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if(DEBUG) Log.i(TAG, "onStartCommand invoked!");
        if(mMenuView == null) {
            mStatusBarHeight = intent.getIntExtra(Consts.STATUSBAR_HEIGHT, 0);
            mMenuView = new FloatView(mContext, mStatusBarHeight);

            mMenuView.setImageResource(R.drawable.menu_selector);

            addMenuView();

            mHomeTask = new ListnerHomeTask();
            mHomeTask.execute();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "onDestroy invoked!");
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mHomeTask != null) {
            mHomeTask.stopTask();
            mHomeTask = null;
        }
        mWindowManager.removeView(mMenuView);

        if(mHideInLauncherReceiver != null) {
            mContext.unregisterReceiver(mHideInLauncherReceiver);
        }

    }

    class ListnerHomeTask extends AsyncTask<Object, Intent, Boolean> {

        private boolean isStop = false;
        private List<String> mLauncherPkgs = null;
        private boolean lastIsHome = false;

        public ListnerHomeTask() {
            // TODO Auto-generated constructor stub
            isStop = false;
            mLauncherPkgs = getLauncherPackages();
            lastIsHome = isHome();
            sendBroadcast(lastIsHome);
        }

        public void stopTask() {
            isStop  = true;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            // TODO Auto-generated method stub
            while(!isStop) {
                boolean isHome = isHome();
                if(lastIsHome != isHome) {
                    sendBroadcast(isHome);
                    lastIsHome = isHome;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        private void sendBroadcast(boolean isHome){
            if(isHome) {
                if(DEBUG) Log.i(TAG, "In Launcher Hide");
                mContext.sendBroadcast(new Intent(ACTION_HOME_HIDDEN));
            } else {
                mContext.sendBroadcast(new Intent(ACTION_APPS_SHOW));
            }
        }

        /**
         *
         * <p>Title: isHome</p>
         * <p>Description: 判断顶端的应用程序是否是Launcher</p>
         * @return
         */
        public boolean isHome(){
            ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> rtis = mActivityManager.getRunningTasks(Integer.MAX_VALUE);

            return mLauncherPkgs.contains(rtis.get(0).topActivity.getPackageName());
        }

        /**
         *
         * <p>Title: getLauncherPackages</p>
         * <p>Description: 获取本机的所有Launcher的packageName</p>
         * @return
         */
        private List<String> getLauncherPackages() {

            List<String> packages = new ArrayList<String>();
            final PackageManager mPackageManager = mContext.getPackageManager();
            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
            launcherIntent.addCategory(Intent.CATEGORY_HOME);
            List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(launcherIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo info : resolveInfos) {
                packages.add(info.activityInfo.packageName);
            }
            return packages;
        }

    }

}
