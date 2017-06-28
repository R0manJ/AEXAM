package com.rjstudio.aexam.ContentPager.Fragment;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rjstudio.aexam.R;

import static android.content.ContentValues.TAG;

/**
 * Created by r0man on 2017/6/26.
 */

public class ContentFragment extends Fragment {

    private int number;
    private String subjectTitle;
    private String contentA;
    private String contentB;
    private String contentC;
    private String contentD;
    private String answer;
    private TextView tv_title;
    private TextView tv_answer;
    private RadioGroup rg_item;
    private RadioButton rb_a;
    private RadioButton rb_b;
    private RadioButton rb_c;
    private RadioButton rb_d;
    private String usrAnswer;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private View view;
    private String isCorrect;
    private CheckBox ck_check;
    private int isShow = 0;
    private int isCheck = 0;

    public ContentFragment() {
        super();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.vp_item_layout,null);
        initView();
        operateLogic();
        setViewData();
        setAnswerMode(isShow);
        Log.d(TAG, "onCreateView: "+number+subjectTitle);
        return view;
    }

    public void setChecked(int i)
    {
        isCheck = i;
    }
    public void setVisible(int i)
    {
        //1,表示True
        //2,表示False
        isShow = i;
    }


    public void initView()
    {
        tv_title = (TextView)view.findViewById(R.id.tv_subject);
        tv_answer = (TextView)view.findViewById(R.id.tv_answer);
        rg_item = (RadioGroup)view.findViewById(R.id.rg_itemAnswer);
        rb_a = (RadioButton)view.findViewById(R.id.rb_1);
        rb_b = (RadioButton)view.findViewById(R.id.rb_2);
        rb_c = (RadioButton)view.findViewById(R.id.rb_3);
        rb_d = (RadioButton)view.findViewById(R.id.rb_4);

      //  Toast.makeText(getContext(), "InitView successful", Toast.LENGTH_SHORT).show();
    }


    private void setAnswerMode(int i)
    {
//        if (isChecked == 1)
//        {
//            ck_check.setChecked(true);
//        }
//        else
//        {
//            ck_check.setChecked(false);
//        }
        if(i == 1)
        {

            tv_answer.setVisibility(View.VISIBLE);

        }
        else if (i == 0)
        {
            tv_answer.setVisibility(View.INVISIBLE);
        }
    }

    private void operateLogic()
    {
        usrAnswer = null;
        isCorrect = null;
        spe = sp.edit();
        //之前存放的数据
        int i = number;
        String uAnswer = sp.getString(i+"-userAnswer","");
        switch (uAnswer)
        {
            case "A":
                rb_a.setChecked(true);
                break;
            case "B":
                rb_b.setChecked(true);
                break;
            case "C":
                rb_c.setChecked(true);
                break;
            case "D":
                rb_d.setChecked(true);
                break;
        }

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

                spe.putString(number+"-"+"titleNumber",number+"");
                spe.putString(number+"-"+"userAnswer",isCorrect);
                String correctAnswer = answer;
                Log.d(TAG, "onCheckedChanged: +____+"+correctAnswer+"-"+isCorrect+"-"+answer);
                if (isCorrect.equals(correctAnswer))
                {
                    Toast.makeText(getContext(), "(≧▽≦) 贼6,答对了", Toast.LENGTH_SHORT).show();
                    isCorrect = "1";
                }
                else
                {
                    Toast.makeText(getContext(), "辣鸡 ಥ_ಥ", Toast.LENGTH_SHORT).show();
                    isCorrect = "0";
                }
                spe.putString((number)+"-"+"isCorrect",isCorrect);
                spe.commit();

                AlphaAnimation an = new AlphaAnimation(0f,1f);
                an.setDuration(1000);
                tv_answer.setAnimation(an);
                an.start();
                tv_answer.setVisibility(View.VISIBLE);

                //db.execSQL("insert usr_info (USERNAME,SubjectNumber,isCorrect) values ('"+usrName+"',"+number+","+Integer.valueOf(isCorrect)+")");
            }
        });



    }
    public String getSubjectTitle()
    {
        return subjectTitle;
    }

    public void setSubjectData(int number,String subjectTitle,String contentA,String contentB,String contentC,String contentD,String answer)
    {
        this.number = number;
        this.subjectTitle = subjectTitle;
        this.contentA = contentA;
        this.contentB = contentB;
        this.contentC = contentC;
        this.contentD = contentD;
        this.answer = answer;
    }

    public void setEditSP(SharedPreferences sp)
    {
        this.sp = sp;
    }

    public SharedPreferences getEditSP()
    {
        return sp;
    }
    private void setViewData()
    {
        tv_title.setText((number+1)+"-"+subjectTitle);
        tv_answer.setText("正确答案: "+answer);
        rb_a.setText("A. "+contentA);
        rb_b.setText("B. "+contentB);
        rb_c.setText("C. "+contentC);
        rb_d.setText("D. "+contentD);
    }
}
