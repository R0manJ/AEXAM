package com.rjstudio.aexam;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rjstudio.aexam.Adapter.MyPagerAdapter;
import com.rjstudio.aexam.ParserFileClass.PaserToXml;
import com.rjstudio.aexam.ParserFileClass.Subject;
import com.rjstudio.aexam.UsrInfo.UsrDBOpenHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private String usrName;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialization();


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
        ViewPager vp_content = (ViewPager)findViewById(R.id.vp_content);

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



    }

    //设置ViewPager适配器


    public class MyFPagerAdapter extends FragmentPagerAdapter
    {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FragmentActivity fm = new FragmentActivity();
            return fm;
        }

        public MyFPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }


    public class MyAdapter extends BaseAdapter{

        private List<Subject> dataList;

        public MyAdapter(List<Subject> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHoler viewHolder ;
            if (convertView == null)
            {
                convertView =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.vp_item_layout,null);

                TextView tv_title = (TextView) convertView.findViewById(R.id.tv_subject);
                RadioGroup rg_item = (RadioGroup)convertView.findViewById(R.id.rg_itemAnswer);
                RadioButton rb_1 = (RadioButton)convertView.findViewById(R.id.rb_1);
                RadioButton rb_2 = (RadioButton)convertView.findViewById(R.id.rb_2);
                RadioButton rb_3 = (RadioButton)convertView.findViewById(R.id.rb_3);
                RadioButton rb_4 = (RadioButton)convertView.findViewById(R.id.rb_4);
                TextView tv_answer = (TextView)convertView.findViewById(R.id.tv_answer);
                viewHolder = new ViewHoler(tv_title,rg_item,rb_1,rb_2,rb_3,rb_4,tv_answer);
                convertView.setTag(viewHolder);

            }
            else
            {
                viewHolder = (ViewHoler)convertView.getTag();
            }

            Subject subject = dataList.get(position);
            viewHolder.tv_title.setText(subject.getSubjectContent());
          //  viewHolder.rg_item.setOnCheckedChangeListener();
            viewHolder.rb_1.setText(subject.getA());
            viewHolder.rb_2.setText(subject.getB());
            viewHolder.rb_3.setText(subject.getC());
            viewHolder.rb_4.setText(subject.getD());
            viewHolder.tv_answer.setText(subject.getAnswer());
            return convertView;
        }

        class ViewHoler{
            private TextView tv_title;
            private RadioGroup rg_item;
            private RadioButton rb_1;
            private RadioButton rb_2;
            private RadioButton rb_3;
            private RadioButton rb_4;
            private TextView tv_answer;

            public ViewHoler(TextView tv,RadioGroup rg,RadioButton r1,RadioButton r2,RadioButton r3,RadioButton r4,TextView tva) {
                tv_title = tv;
                rg_item = rg;
                rb_1 = r1;
                rb_2 = r2;
                rb_3 = r3;
                rb_4 = r4;
                tv_answer = tva;
            }
        }
    }
}
