package com.rjstudio.aexam;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rjstudio.aexam.Adapter.MyPagerAdapter;
import com.rjstudio.aexam.ParserFileClass.PaserToXml;
import com.rjstudio.aexam.ParserFileClass.Subject;
import com.rjstudio.aexam.UsrInfo.UsrDBOpenHelper;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private String usrName;
    private SQLiteDatabase db;
    private ViewPager vp_content;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        initialization();


    }

    //退出时记录上一次完成页面


    @Override
    protected void onStop() {
        super.onStop();
        sp = this.getSharedPreferences("usrData",MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("LastFinish",vp_content.getCurrentItem());
        spe.commit();
    }

    String TAG = "Error";
    //初始化布局
    //一个ViewPager
    List<Subject> subjectList;
    public void initialization()
    {
        intent = getIntent();
        usrName = intent.getStringExtra("UsrName");
        UsrDBOpenHelper dbOpenHelper = new UsrDBOpenHelper(this,usrName+"DB",null,1);
        db = dbOpenHelper.getWritableDatabase();
        vp_content = (ViewPager)findViewById(R.id.vp_content);
        sp = this.getSharedPreferences("usrData",MODE_PRIVATE);
        File file = new File("OutputXml.xml");
        if (!file.exists())
        {
            //读取文件并解析文件
            try
            {
                PaserToXml px = new PaserToXml(getAssets().open("a.txt"),this);
                subjectList = px.CreateAndSaveObject();
            }
            catch (Exception e)
            {
                Log.d(TAG, "Can not open file.");
            }
        }


        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(subjectList,this,db,usrName,0);
        vp_content.setAdapter(myPagerAdapter);
        //设置当前界面
        if (sp.getInt("LastFinish",99) !=99 )
        {
            vp_content.setCurrentItem(sp.getInt("LastFinish",99));
        }

        //左侧菜单逻辑
        TextView tv_userName = (TextView)findViewById(R.id.tv_left_userName);
        ListView lv_item = (ListView)findViewById(R.id.lv_left_drawer);
        tv_userName.setText(usrName);
        String item[] = {
                "首页",
                "跳转到",
                "错题本",
                "做题模式",
                "背题模式",
                "关于我们"
        };

        lv_item.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,item));
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TranslateAnimation translateAnimation = new TranslateAnimation(0,10,0,0);
                translateAnimation.setDuration(1000);
                switch (position)
                {
                    case 0:
                        vp_content.setCurrentItem(0);
                        break;
                    case 1:
                        final EditText et_pager = new EditText(getApplication());
                        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this,R.style.Theme_AppCompat_Dialog_Alert);
                        //这里有疑问??AlertDialog
                        ad.setTitle("要跳转到第几题?");
                        ad.setView(et_pager);
                        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pager = Integer.valueOf(et_pager.getText().toString())-1;
                                vp_content.setCurrentItem(pager);
                            }
                        });
                        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad.create().show();
                        break;
                    case 2:
                        vp_content.setAnimation(translateAnimation);
                        translateAnimation.start();
                        MyPagerAdapter myPagerAdapterZ = new MyPagerAdapter(subjectList,getApplicationContext(),db,usrName,2);
                        vp_content.setAdapter(myPagerAdapterZ);
                        break;
                    case 3:
                        vp_content.setAnimation(translateAnimation);
                        translateAnimation.start();
                        MyPagerAdapter myPagerAdapterX = new MyPagerAdapter(subjectList,getApplicationContext(),db,usrName,0);
                        vp_content.setAdapter(myPagerAdapterX);
                        break;
                    case 4:
                        vp_content.setAnimation(translateAnimation);
                        translateAnimation.start();
                        MyPagerAdapter myPagerAdapterY = new MyPagerAdapter(subjectList,getApplicationContext(),db,usrName,1);
                        vp_content.setAdapter(myPagerAdapterY);
                        break;
                    case 5:
                        Intent toAboutUs = new Intent(getApplicationContext(),AboutUs.class);
                        startActivity(toAboutUs);
                        break;
                }
            }
        });

    }

}
