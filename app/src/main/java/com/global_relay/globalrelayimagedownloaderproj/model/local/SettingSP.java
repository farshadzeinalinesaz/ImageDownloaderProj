package com.global_relay.globalrelayimagedownloaderproj.model.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingSP
{
    public static final String DATA_LOCAL_PATH_PREFERENCES = "data_local_path";
    public static final String LOCAL_PATH_KEY = "local_path";

    private Context ctx=null;
    private SharedPreferences sp;

    public SettingSP(Context ctx)
    {
        this.ctx=ctx;
        sp=this.ctx.getSharedPreferences(DATA_LOCAL_PATH_PREFERENCES,Context.MODE_PRIVATE);
    }

    public boolean updateDataLocalPath(String localPath)
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString(LOCAL_PATH_KEY,localPath);
        return editor.commit();
    }

    public String getDataLocalPath()
    {
        return sp.getString(LOCAL_PATH_KEY,null);
    }
}
