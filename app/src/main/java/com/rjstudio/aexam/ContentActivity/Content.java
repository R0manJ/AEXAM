package com.rjstudio.aexam.ContentActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private Button bu_delete;
    private ViewPager vp_content_in;
    private FragmentManager fm;
    private TextView tv_pager;
    // private IncorrectFragmentPagerAdapter inAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        incorrectSubject = new ArrayList<>();
        initContentView();
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
        tv_pager = (TextView)findViewById(R.id.tv_content_pager);
        drawerLayout = (DrawerLayout) findViewById(R.id.dw_content);
        TAG = "Content Pager";
        sp = getSharedPreferences("userData", MODE_PRIVATE);
        final SharedPreferences.Editor spe = sp.edit();
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


        //VP part
        fm = getSupportFragmentManager();
        vp_content = (ViewPager) findViewById(R.id.vp_content_fragment);
        vp_content.setOffscreenPageLimit(0);
        InitFragmentList in = new InitFragmentList(subjectList,sp);
        fragmentList = in.getFragmentList(subjectList,0);
       // incorrectSubject = initIncorrect();

        Log.d(TAG, "正题共:"+fragmentList.size()+"道");
        Log.d(TAG, "错题共:"+incorrectSubject.size()+"道");
        //inAdapter = new IncorrectFragmentPagerAdapter(getSupportFragmentManager());
        myContentFragmentAdapter = new MyContentFragmentAdapter(fm,fragmentList);

        //inAdapter.setList(incorrectSubject);
        //myContentFragmentAdapter.setList(fragmentList);

        vp_content.setAdapter(myContentFragmentAdapter);
//        vp_content_in.setAdapter(inAdapter);
        //--

        cb_addIncorrectSubject = (CheckBox)findViewById(R.id.cb_content_check);
        bu_delete = (Button)findViewById(R.id.bu_content_delete);
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
                        incorrectSubject = initIncorrect();
                        Log.d(TAG, "错题共:"+incorrectSubject.size()+"道");
                        if (incorrectSubject.size() == 0){
                            Toast.makeText(Content.this, "目前没有记录~", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        setButtonVisibility(true,false);
                        vp_content.setAdapter(null);
                        FragmentTransaction FT = fm.beginTransaction();
                        for (int i = 0 ; i < fragmentList.size(); i ++)
                        {
                            FT.remove(fragmentList.get(i));
                        }

                        //FT.addToBackStack(null);
                        FT.commit();
                        myContentFragmentAdapter = new MyContentFragmentAdapter(fm,incorrectSubject);
                        vp_content.setAdapter(myContentFragmentAdapter);
                        myContentFragmentAdapter.setMode(1,0);
                        myContentFragmentAdapter.notifyDataSetChanged();

//                        setViewPagerVisibility(false,true);

                        //myContentFragmentAdapter.setAdapterOpen(false);
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.incorrectBookMode));
                      //  IncorrectFragmentPagerAdapter inad = new IncorrectFragmentPagerAdapter(getSupportFragmentManager(),incorrectSubject);
                        break;
                    case 3:
                        //测试模式
                        setButtonVisibility(false,true);
                        myContentFragmentAdapter.setMode(0,0);
                        vp_content.setAdapter(null);
                        FragmentTransaction FT1 = fm.beginTransaction();
                        for (int i = 0 ; i < incorrectSubject.size(); i ++)
                        {
                            FT1.remove(incorrectSubject.get(i));
                        }
                        FT1.commit();
                        myContentFragmentAdapter = new MyContentFragmentAdapter(fm,fragmentList);
                        vp_content.setAdapter(myContentFragmentAdapter);
                        myContentFragmentAdapter.notifyDataSetChanged();

                        //myContentFragmentAdapter = new MyContentFragmentAdapter(getSupportFragmentManager(),fragmentList);
//                        FragmentTransaction ft = inAdapter.getFm().beginTransaction();
//                        for (int i = 0 ; i < incorrectSubject.size();i++)
//                        {
//                            ft.remove(incorrectSubject.get(i));
//
//                        }
////                        for (int i = 0 ; i < fragmentList.size() ; i++)
////                        {
////                            ft.add(fragmentList.get(i),"No."+i);
////                        }
//                        ft.commit();
//                        inAdapter.setList(fragmentList);
//                        inAdapter.notifyDataSetChanged();
                        //错题集的所有fragment都移出
                        //incorrectSubject.removeAll(incorrectSubject);
//                        if (!myContentFragmentAdapter.getAdapterOpen())
//                        {
//                            myContentFragmentAdapter = new MyContentFragmentAdapter(fm,fragmentList);
//                            myContentFragmentAdapter.setAdapterOpen(true);
//                            vp_content.setAdapter(myContentFragmentAdapter);
//
//                        }
//                        else
//                        {
//
//                                Toast.makeText(Content.this, "做题模式", Toast.LENGTH_SHORT).show();
//                                myContentFragmentAdapter.setMode(0,1);
//                                myContentFragmentAdapter.notifyDataSetChanged();
//
//                        }
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.TestMode));
                        drawerLayout.closeDrawers();
                        break;
                    case 4:
                        //背题模式
                        setButtonVisibility(false,true);
//                        setViewPagerVisibility(false,true);
                        FragmentTransaction FT2 = fm.beginTransaction();
                        for (int i = 0 ; i < incorrectSubject.size(); i ++)
                        {
                            FT2.remove(incorrectSubject.get(i));
                        }
                        FT2.commit();
                        myContentFragmentAdapter = new MyContentFragmentAdapter(fm,fragmentList);
                        myContentFragmentAdapter.setMode(1,0);
                        vp_content.setAdapter(myContentFragmentAdapter);
                        myContentFragmentAdapter.notifyDataSetChanged();
//                        if (!myContentFragmentAdapter.getAdapterOpen())
//                        {
//                            myContentFragmentAdapter.setAdapterOpen(true);
//                            myContentFragmentAdapter.notifyDataSetChanged();
//                            vp_content.setAdapter(myContentFragmentAdapter);
//                        }
//                        else
//                        {
//                                Toast.makeText(Content.this, "做题模式", Toast.LENGTH_SHORT).show();
//                                myContentFragmentAdapter.setMode(0,1);
//                                myContentFragmentAdapter.notifyDataSetChanged();
//
//                        }
                        drawerLayout.setBackgroundColor(getResources().getColor(R.color.RememberMode));
                        drawerLayout.closeDrawers();
                        break;
                    case 5:
                        //TODO 待完成,这里是一个关于我们的界面
                        Toast.makeText(Content.this, "磨人的小妖精 ~", Toast.LENGTH_SHORT).show();
//                        Intent toAboutUs = new Intent(getApplicationContext(),AboutUs.class);
//                        startActivity(toAboutUs);
                        break;
                }
            }
        });
        tv_pager.setText(1+" / "+fragmentList.size());

        //设置当前界面
        if (sp.getInt("LastFinish",600) != 600 )
        {
            //TODO 上一次退出时是什么模式
            int LastMode = sp.getInt("LastMode",600);
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
                tv_pager.setText((position+1)+" / "+fragmentList.size());
                bu_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor spe = sp.edit();
                        spe.putBoolean("No."+subjectList.get(vp_content.getCurrentItem()).getSubjectNumber()+"isRecord",false);
                        incorrectSubject.remove(vp_content.getCurrentItem());
                        //TODO 适配器更改
                    }
                });
                //TODO:监听逻辑
                if (sp.getBoolean("No."+subjectList.get(position).getSubjectNumber()+"isRecord",false))
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
    private List<ContentFragment> initIncorrect()
    {
        //InitFragmentList list = new InitFragmentList(subjectList,sp);
        List<ContentFragment> FragmentList = new ArrayList<>();
        for (int i = 0 ; i< subjectList.size();i++)
        {

            if (sp.getBoolean("No."+i+"isRecord",false)) {
                ContentFragment conF = fragmentList.get(i);
                Log.d(TAG, "initIncorrect: "+conF.getSubjectTitle());
                FragmentList.add(conF);
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

    //用于设置按钮显示
    public void setButtonVisibility(boolean DeleteButtonIsShow,boolean CheckBoxIsShow)
    {
        if (DeleteButtonIsShow)
        {
            bu_delete.setVisibility(View.VISIBLE);
            //监听逻辑在页面更变的时候实现

        }
        else
        {
            bu_delete.setVisibility(View.INVISIBLE);
        }

        if (CheckBoxIsShow )
        {
            cb_addIncorrectSubject.setVisibility(View.VISIBLE);
            //监听逻辑在页面更变的时候实现
        }
        else
        {
            cb_addIncorrectSubject.setVisibility(View.INVISIBLE);
        }
    }

    //用于设置两个ViewPager的显示
    public void setViewPagerVisibility(boolean MainViewPagerIsShow,boolean SecondVPIsShow)
    {
        if (MainViewPagerIsShow)
        {
            vp_content.setVisibility(View.VISIBLE);
        }
        else
        {
            vp_content.setVisibility(View.INVISIBLE);
        }

        if (SecondVPIsShow)
        {
            vp_content_in.setVisibility(View.VISIBLE);
        }
        else
        {
            vp_content_in.setVisibility(View.INVISIBLE);
        }
    }

    //private void setNigthMode()
}
