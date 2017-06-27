package com.rjstudio.aexam.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.aexam.R;
import com.rjstudio.aexam.ParserFileClass.Subject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2017/6/25.
 */

public class MyPagerAdapter extends PagerAdapter {


        private Context context;
        private List<Subject> dataList;
        private List viewList;
        private SQLiteDatabase db;
        private String usrName;
        private String isCorrect;
        private SharedPreferences sp;
        private SharedPreferences.Editor spe;
        private RadioButton rb_1;
        private RadioButton rb_2;
        private RadioButton rb_3;
        private RadioButton rb_4;
        //用于存放错题的View集合
        private List<View> incorrectList;
    //参数一dataList表示的是所有题目数据的集合
    //参数二表示的是上下文
    //参数三表示传入数据库操作对象
    //参数四表示传入用户名
    //参数五表示显示模式,1表示答题模式,0表示做题模式,2表示错题模式
    public MyPagerAdapter(List<Subject> dataList, Context context, SQLiteDatabase db, String usrname ,int showMode) {
        this.dataList = dataList;
        this.context = context;
        this.db = db;
        this.usrName = usrname;
        this.sp = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
        this.spe = sp.edit();
        incorrectList = new ArrayList<View>();

        if (showMode == 2)
        {
            viewList = getIncorrectViewList();
        }
        else if (showMode == 1)
        {
            viewList = initializationViewMode(dataList);
        }
        else if (showMode == 0)
        {
            viewList = initializationView(dataList);
        }
    }

    public List<View> initializationViewMode(List<Subject> dataList)
    {
        List<View> mViewList =  new ArrayList<View>();
        View view =  null;
        for (int i = 0; i < dataList.size() ; i++)
        {

            view =  LayoutInflater.from(context).inflate(R.layout.vp_item_layout,null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_subject);
            rb_1 = (RadioButton)view.findViewById(R.id.rb_1);
            rb_2 = (RadioButton)view.findViewById(R.id.rb_2);
            rb_3 = (RadioButton)view.findViewById(R.id.rb_3);
            rb_4 = (RadioButton)view.findViewById(R.id.rb_4);


            final TextView tv_answer = (TextView)view.findViewById(R.id.tv_answer);
            final Subject subject = dataList.get(i);
            tv_title.setText((subject.getSubjectNumber()+1)+"-"+subject.getSubjectContent());
            rb_1.setText("A.  "+subject.getA());
            rb_2.setText("B.  "+subject.getB());
            rb_3.setText("C.  "+subject.getC());
            rb_4.setText("D.  "+subject.getD());

            String uAnswer = subject.getAnswer();
            String content[] = uAnswer.split("：");
            switch (content[0])
            {
                case "A":
                    rb_1.setChecked(true);
                    break;
                case "B":
                    rb_2.setChecked(true);
                    break;
                case "C":
                    rb_3.setChecked(true);
                    break;
                case "D":
                    rb_4.setChecked(true);
                    break;

            }

            tv_answer.setText("正确答案"+subject.getAnswer());
            mViewList.add(view);
        }

            return mViewList;
        }

        public List<View> initializationView(List<Subject> dataList)
        {
            final List<View> mViewList =  new ArrayList<View>();
            View view =  null;
            for (int i = 0; i < dataList.size() ; i++)
            {
                isCorrect = null;
                view =  LayoutInflater.from(context).inflate(R.layout.vp_item_layout,null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_subject);
                RadioGroup rg_item = (RadioGroup)view.findViewById(R.id.rg_itemAnswer);
                rb_1 = (RadioButton)view.findViewById(R.id.rb_1);
                rb_2 = (RadioButton)view.findViewById(R.id.rb_2);
                rb_3 = (RadioButton)view.findViewById(R.id.rb_3);
                rb_4 = (RadioButton)view.findViewById(R.id.rb_4);

                //两个按钮用于操作本题目
                Button bu_question = (Button)view.findViewById(R.id.bu_main_question);
                Button bu_check = (Button)view.findViewById(R.id.bu_item_check);
                bu_check.setVisibility(View.VISIBLE);
                //如果之前存有数据,这一段逻辑时这样的
                //读取之前的数据
                String uAnswer = sp.getString(i+"-usrAnswer","");
                switch (uAnswer)
                {
                    case "A":
                        rb_1.setChecked(true);
                        break;
                    case "B":
                        rb_2.setChecked(true);
                        break;
                    case "C":
                        rb_3.setChecked(true);
                        break;
                    case "D":
                        rb_4.setChecked(true);
                        break;

                }

                final TextView tv_answer = (TextView)view.findViewById(R.id.tv_answer);
                final Subject subject = dataList.get(i);
                tv_title.setText((subject.getSubjectNumber()+1)+"-"+subject.getSubjectContent());
                rg_item.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                        switch (checkedId)
                        {
                            case R.id.rb_1:
                                isCorrect = "A";
                                break;
                            case R.id.rb_2:
                                isCorrect = "B";
                                break;
                            case R.id.rb_3:
                                isCorrect = "C";
                                break;
                            case R.id.rb_4:
                                isCorrect = "D";
                                break;

                        }

                        spe.putString((subject.getSubjectNumber())+"-"+"titleNumber",subject.getSubjectNumber()+"");
                        spe.putString((subject.getSubjectNumber())+"-"+"usrAnswer",isCorrect);
                        String content[] = subject.getAnswer().split("：");
                        String correctAnswer = content[1];
                        Log.d(TAG, "onCheckedChanged: +____+"+correctAnswer+"-"+isCorrect+"-"+correctAnswer);
                        if (isCorrect.equals(correctAnswer))
                        {
                            Toast.makeText(context, "(≧▽≦) 贼6,答对了", Toast.LENGTH_SHORT).show();
                            isCorrect = "1";
                        }
                        else
                        {
                            Toast.makeText(context, "辣鸡 ಥ_ಥ", Toast.LENGTH_SHORT).show();
                            isCorrect = "0";
                        }
                        spe.putString((subject.getSubjectNumber())+"-"+"isCorrect",isCorrect);
                        spe.commit();

                        AlphaAnimation an = new AlphaAnimation(0f,1f);
                        an.setDuration(1000);
                        tv_answer.setAnimation(an);
                        an.start();
                        tv_answer.setVisibility(View.VISIBLE);
                        ContentValues cv = new ContentValues();
                        cv.put("USERNAME",usrName);
                        cv.put("SubjectNumber",subject.getSubjectNumber());
                        cv.put("isCorrect",isCorrect);
                        db.insert("usr_info",null,cv);
                        //db.execSQL("insert usr_info (USERNAME,SubjectNumber,isCorrect) values ('"+usrName+"',"+subject.getSubjectNumber()+","+Integer.valueOf(isCorrect)+")");
                    }
                });



                rb_1.setText("A.  "+subject.getA());
                rb_2.setText("B.  "+subject.getB());
                rb_3.setText("C.  "+subject.getC());
                rb_4.setText("D.  "+subject.getD());
                tv_answer.setText("正确答案"+subject.getAnswer());
                tv_answer.setVisibility(View.INVISIBLE);
                mViewList.add(view);
                //检查之前有没有存放在错题集里面的题目
                if (sp.getBoolean(i+"isAdd",false))
                {
                    incorrectList.add(view);
                }

                //点击添加到错题,一是向xml记录要添加这一题,二是向集合添加这个View,方便返回到错题界面
                bu_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否重复提交了
                        if (sp.getBoolean(subject.getSubjectNumber()+"isAdd",false))
                        {
                            Toast.makeText(context, "老铁,您已经添加了这道题目了", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //向xml文件写入数据,并提交
                        spe.putBoolean(subject.getSubjectNumber()+"isAdd",true);
                        spe.commit();
                        //添加集合
                        incorrectList.add(mViewList.get(subject.getSubjectNumber()));
                        Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return mViewList;
        }

        //获取错题集的函数
        public List<View> getIncorrectViewList()
        {
            List<Subject> incoreectSubjectList = new ArrayList<>();
            for (int i = 0 ;i < dataList.size() ; i++)
            {
                if (sp.getBoolean(i+"isAdd",false))
                {
                    incoreectSubjectList.add(dataList.get(i));
                    incorrectList = initializationViewMode(incoreectSubjectList);
                }

            }
            return incorrectList;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //  container.addView((View)viewList.get(position));
            // return viewList.get(position);
//            View view =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.vp_item_layout,null);
//
//            TextView tv_title = (TextView) view.findViewById(R.id.tv_subject);
//            RadioGroup rg_item = (RadioGroup)view.findViewById(R.id.rg_itemAnswer);
//            RadioButton rb_1 = (RadioButton)view.findViewById(R.id.rb_1);
//            RadioButton rb_2 = (RadioButton)view.findViewById(R.id.rb_2);
//            RadioButton rb_3 = (RadioButton)view.findViewById(R.id.rb_3);
//            RadioButton rb_4 = (RadioButton)view.findViewById(R.id.rb_4);
//            TextView tv_answer = (TextView)view.findViewById(R.id.tv_answer);
//
//            Subject subject = dataList.get(position);
//            tv_title.setText(subject.getSubjectContent());
//            //    rg_item.setOnCheckedChangeListener();
//            rb_1.setText(subject.getA());
//            rb_2.setText(subject.getB());
//            rb_3.setText(subject.getC());
//            rb_4.setText(subject.getD());
//            tv_answer.setText(subject.getAnswer());
            container.addView((View)viewList.get(position));
            return  viewList.get(position);


        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)viewList.get(position));
        }

}
