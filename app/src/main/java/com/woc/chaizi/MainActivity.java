package com.woc.chaizi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.woc.chaizi.activity.SettingActivity;
import com.woc.chaizi.db.SQLiteDAOImpl;
import com.woc.chaizi.util.IO;

import java.io.File;

public class MainActivity extends Activity {
    public static   final String SD_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.woc.chaizi/";
    public  static final String DB_NAME="db_data.db";
    private final String KEY="notIsFirstRun";//更新数据库要更新
    private  SharedPreferences sharedPreferences;
    private  SharedPreferences preferences;
    private EditText edit_source,edit_result;
    private ProgressDialog progressDialog;
    private  Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //如果是第一次,或者数据库文件不存在
        if(!sharedPreferences.getBoolean(KEY,false)||!new File(SD_PATH+DB_NAME).exists())
        {
            IO.copyAssetToSD(this,DB_NAME,SD_PATH);
            sharedPreferences.edit().putBoolean(KEY,true).apply();
        }
       edit_result=(EditText)findViewById(R.id.editText2);
        edit_source=(EditText)findViewById(R.id.editText1);
    }

    public void buttonClick(View v)
    {
        String sourceText=edit_source.getText().toString();
        final char[] chs=sourceText.toCharArray();
        progressDialog=ProgressDialog.show(this, "", "拆字中...", false, false);
       thread= new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDAOImpl sqLiteDAO=new SQLiteDAOImpl(MainActivity.this);
                final StringBuilder sb=new StringBuilder();
                int n=Integer.parseInt(preferences.getString("space_count","0"));
                for (int i=0;i<chs.length;i++)
                {
                    String temp=sqLiteDAO.find(String.valueOf(chs[i]));
                    sb.append(temp);
                    for(int j=0;j<n;j++)
                    {
                        sb.append(" ");
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"拆字完成",Toast.LENGTH_SHORT).show();
                        edit_result.setText(sb.toString());
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                    }
                });
            }
        });
        thread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_setting)
        {
            Intent intent=new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
       else if(item.getItemId()==R.id.menu_group)
        {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse("http://jq.qq.com/?_wv=1027&k=2Jzi88M"));
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }
}
