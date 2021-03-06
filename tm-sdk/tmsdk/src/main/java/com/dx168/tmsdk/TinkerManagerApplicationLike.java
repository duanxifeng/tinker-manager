package com.dx168.tmsdk;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.dx168.tmsdk.tinker.SampleTinkerLog;
import com.dx168.tmsdk.tinker.SampleTinkerManager;

/**
 * Created by jianjun.lin on 2016/10/25.
 */
public class TinkerManagerApplicationLike extends DefaultApplicationLike {

    public static Application application;

    private static final String TAG = TinkerManagerApplicationLike.class.getSimpleName();

    public TinkerManagerApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                                        long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
                                        Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleTinkerManager.setTinkerApplicationLike(this);
        SampleTinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        SampleTinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new SampleTinkerLog());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        SampleTinkerManager.installTinker(this);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

}
