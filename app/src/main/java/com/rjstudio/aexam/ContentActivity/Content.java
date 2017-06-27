package com.rjstudio.aexam.ContentActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.aexam.ContentPager.Fragment.ContentFragment;
import com.rjstudio.aexam.ContentPager.Fragment.IncorrectFragmentPagerAdapter;
import com.rjstudio.aexam.ContentPager.Fragment.InitFragmentList;
import com.rjstudio.aexam.ContentPager.Fragment.MyContentFragmentAdapter;
import com.rjstudio.aexam.ParserFileClass.PaserToXml;
import com.rjstudio.aexam.ParserFileClass.Subject;
import com.rjstudio.aexam.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Content extends AppCompatActivity {

    private List<Subject> subjectList;
    private String TAG;
    private SharedPreferences sp;
    private ViewPager vp_content;
    private List<ContentFragment> incorrectSubject;
    private ImageView imageView_check;
    private List<ContentFragment> fragmentList;
    private MyContentFragmentAdapter myContentFragmentAdapter;
    private DrawerLayout drawerLayout;
    private CheckBox cb_addIncorrectSubject;
    private PaserToXml px;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initContentView();

        incorrectSubject = new ArrayList<>();
        cb_addIncorrectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+vp_content.getCurrentItem());
                SharedPreferences.Editor spe = sp.edit();
                if( sp.getBoolean("No."+subjectList.get(vp_content.getCurrentItem()).getSubjectNumber()+"isRecord",false))
                {
                    spe.putBoolean("No."+subjectList.get(vp_content.getCurrentItem()).getSubjectNumber()+"isRecord",false);
                    Toast.makeText(Content.this, "- - 取消收藏到错题集啦.", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    spe.putBoolean("No."+subjectList.get(vp_content.getCurrentItem()).getSubjectNumber()+"isRecord",true);
                    Toast.makeText(Content.this, "^ ^ 收藏到错题集啦.", Toast.LENGTH_SHORT).show();
                }
                spe.commit();
            }
        });
        operatIncorrectSubject();
    }

    private void initContentView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.dw_content);
        TAG = "Content Pager";
        sp = getSharedPreferences("userData", MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        Toast.makeText(this, "加载数据ing", Toast.LENGTH_SHORT).show();
        File file = new File(getCacheDir(),"OutputXml.xml");
        subjectList = new ArrayList<>();
        px = new PaserToXml(this);
        if (!file.exists()) {
            //读取文件并解析文件
            try {
                Log.d(TAG, "初始数据不存在,进行解析");
                px = new PaserToXml(getAssets().open("a.txt"), this);
                subjectList = px.CreateAndSaveObject(file);
                Log.d(TAG, "initContentView: Error "+subjectList.size());
            } catch (Exception e) {
                Log.d(TAG, "Can not open file.");
            }
        }
        else
        {
            Log.d(TAG, "初始数据存在,进行解析");
            subjectList = px.CreateAndSaveObject(file);
        }
        imageView_check = (ImageView)findViewById(R.id.im_content_operateButton);
        vp_content = (ViewPager) findViewById(R.id.vp_content_fragment);
        cb_addIncorrectSubject = (CheckBox)findViewById(R.id.cb_content_check);

        final FragmentManager fm = getSupportFragmentManager();
        Log.d(TAG, "initContentView: Error"+subjectList.size());
        final InitFragmentList initFragmentList = new InitFragmentList(subjectList, sp);
        fragmentList = initFragmentList.getFragmentList(subjectList,0);
        myContentFragmentAdapter = new MyContentFragmentAdapter(fm, fragmentList);
        vp_content.setAdapter(myContentFragmentAdapter);

        String item[] = {
                "首页",
                "跳转到",
                "错题本",
                "做题模式",
                "背题模式",
                "关于我们"
        };
        ListView lv_menu = (ListView)findViewById(R.id.lv_content_menu);
        lv_menu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,item));
        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        AlertDialog.Builder ad = new AlertDialog.Builder(Content.this,R.style.Theme_AppCompat_Dialog_Alert);
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
                        drawerLayout.closeDrawers();
                        break;
                    case 2:
                        //错题本
                        cb_addIncorrectSubject.setChecked(false);
                        incorrectSubject = initIncorrect();
                        myContentFragmentAdapter.setAdapterOpen(false);
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.incorrectNormal));
                        vp_content.setAdapter(new IncorrectFragmentPagerAdapter(fm,incorrectSubject));
                        //vp_content.setAdapter(myContentFragmentAdapter);
                        //待开发
                        Toast.makeText(getApplicationContext(), "Sorry , 我们正在玩命开发中 >_<!", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        cb_addIncorrectSubject.setChecked(true);
                        if (!myContentFragmentAdapter.getAdapterOpen())
                        {
                            myContentFragmentAdapter.setAdapterOpen(true);
                            vp_content.setAdapter(myContentFragmentAdapter);
                        }
                        else
                        {

                                Toast.makeText(Content.this, "做题模式", Toast.LENGTH_SHORT).show();
                                myContentFragmentAdapter.setMode(0,1);
                                myContentFragmentAdapter.notifyDataSetChanged();

                        }
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        drawerLayout.closeDrawers();
                        break;
                    case 4:
                        cb_addIncorrectSubject.setChecked(true);
                        if (!myContentFragmentAdapter.getAdapterOpen())
                        {
                            myContentFragmentAdapter.setAdapterOpen(true);
                            vp_content.setAdapter(myContentFragmentAdapter);
                        }
                        else
                        {
                                Toast.makeText(Content.this, "做题模式", Toast.LENGTH_SHORT).show();
                                myContentFragmentAdapter.setMode(0,1);
                                myContentFragmentAdapter.notifyDataSetChanged();

                        }
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        drawerLayout.closeDrawers();
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        if (myContentFragmentAdapter.getList().size() == fragmentList.size())
                        {
                            Toast.makeText(Content.this, "背题模式", Toast.LENGTH_SHORT).show();
                            myContentFragmentAdapter.setMode(1,1);
                            myContentFragmentAdapter.notifyDataSetChanged();
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case 5:
                        Toast.makeText(Content.this, "磨人的小妖精 ~", Toast.LENGTH_SHORT).show();
//                        Intent toAboutUs = new Intent(getApplicationContext(),AboutUs.class);
//                        startActivity(toAboutUs);
                        break;
                }
            }
        });

        //设置当前界面
        if (sp.getInt("LastFinish",99) !=99 )
        {
            int LastMode = sp.getInt("LastMode",99);
            myContentFragmentAdapter.setMode(LastMode,0);
            vp_content.setCurrentItem(sp.getInt("LastFinish",99));
        }
        TextView tv_userName = (TextView)findViewById(R.id.tv_content_userName);
        tv_userName.setText(getIntent().getStringExtra("UsrName"));


        //设置页面滑动监听,用于更改Checkbox
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //TODO:监听逻辑
                if (sp.getBoolean("No."+position+"isRecord",false))
                {
                    cb_addIncorrectSubject.setChecked(true);
                }
                else
                {
                    cb_addIncorrectSubject.setChecked(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt("LastFinish",vp_content.getCurrentItem());
        spe.putInt("LastMode",myContentFragmentAdapter.getMode());
        spe.commit();
    }


    private void operatIncorrectSubject()
    {

        imageView_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    //初始化错题本集合
    //xml - > 解析错题 - >封装到集合
    private List initIncorrect()
    {
        List<ContentFragment> FragmentList = new ArrayList<>();
        for (int i = 0 ; i< subjectList.size();i++)
        {

            if (sp.getBoolean("No."+i+"isRecord",false))
            {
                FragmentList.add(fragmentList.get(i));
                Log.d(TAG, "Fragment"+i);
            }
        }
        return FragmentList;
    }

    private void RemeberMode()
    {
        //错题记录
    }

    private void TestMode()
    {
        //错题记录
    }

}
