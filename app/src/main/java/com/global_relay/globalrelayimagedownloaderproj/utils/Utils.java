package com.global_relay.globalrelayimagedownloaderproj.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class Utils
{
    private static Utils utils=new Utils();
    private Context context;

    private Utils() {}

    public static Utils getInstance(Context context)
    {
        if (utils.context==null)
        {
            utils.context=context;
        }
        return utils;
    }

    public boolean writeToFile(String path,byte[] data)
    {
        try
        {
            File file=new File(path);
            if(!file.getParentFile().exists())
            {
                file.mkdirs();
            }
            FileOutputStream outputStream=new FileOutputStream(file);
            outputStream.write(data,0,data.length);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
