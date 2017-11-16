package com.woc.chaizi.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.woc.chaizi.db.SQLiteDAOImpl;
import com.woc.chaizi.entity.Words;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyw on 2016/4/10.
 */
public class IO {

    /**
     * 拷贝文件到内存卡
     * @param path
     */
    public static void copyAssetToSD(Context context,String fileName,String path)
    {
        File f=new File(path);
        if(!f.exists())
        {
            f.mkdirs();
        }
        f=new File(path+"db_data.db");
        AssetManager assetManager=context.getAssets();
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
         bufferedInputStream=new  BufferedInputStream(assetManager.open(fileName));
          bufferedOutputStream= new BufferedOutputStream(new FileOutputStream(f));
            byte[] temp=new byte[1024];
            while((bufferedInputStream.read(temp))!=-1)
            {
                bufferedOutputStream.write(temp);
                bufferedOutputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(bufferedInputStream!=null)
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bufferedOutputStream!=null)
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 写内容到数据库
     */
    public static void writeToDB(Context context)
    {
        SQLiteDAOImpl op=new SQLiteDAOImpl(context);
        BufferedReader bufferedReader=null;
        try {
            bufferedReader=new BufferedReader(new FileReader("/sdcard/chaizi.txt"));
            String line=null;
            while((line=bufferedReader.readLine())!=null)
            {
                Words words=  new Words();
                words.setSource(line.substring(0, 1));
                words.setChai(line.substring(3, line.length()));
                op.add(words);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
