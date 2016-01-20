package galmapp.scoutappv2;

import android.app.Application;
import android.content.Context;

/**
 * Created by Arnaud on 10-Dec-15.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;



    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }


}




