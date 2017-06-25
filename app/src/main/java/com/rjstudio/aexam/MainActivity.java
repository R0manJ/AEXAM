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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rjstudio.aexam.Adapter.MyPagerAdapter;
import com.rjstudio.aexam.ParserFileClass.PaserToXml;
import com.rjstudio.aexam.ParserFileClass.Subject;
import com.rjstudio.aexam.UsrInfo.UsrDBOpenHelper;

import java.util.List;

public class MainActivity extends Activity {
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

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(subjectList,this,db,usrName);
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
                "清除数据",
                "错题本"
        };
        lv_item.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,item));
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        vp_content.setCurrentItem(0);
                        break;
                    case 1:
                        final EditText et_pager = new EditText(getApplication());
                        AlertDialog.Builder ad = new AlertDialog.Builder(getApplication(),R.style.Theme_AppCompat_DayNight_Dialog_Alert);

                        ad.setTitle("要跳转到?");
                       ad.setView(et_pager);
                        ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pager = Integer.valueOf(et_pager.getText().toString());
                                vp_content.setCurrentItem(pager);
                            }
                        });
                        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        ad.show();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });

    }

}
