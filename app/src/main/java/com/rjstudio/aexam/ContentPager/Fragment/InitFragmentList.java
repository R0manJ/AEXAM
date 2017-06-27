package com.rjstudio.aexam.ContentPager.Fragment;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.rjstudio.aexam.ParserFileClass.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r0man on 2017/6/26.
 */

public class InitFragmentList {
    private List<Subject> subjectList;
    private List<ContentFragment> fragmentList;
    private SharedPreferences sp;

    public InitFragmentList(List<Subject> subjectList,SharedPreferences sp) {
        this.subjectList = subjectList;
        this.fragmentList = getFragmentList(subjectList,0);
        this.sp = sp;
    }

    public List<ContentFragment> getFragmentList(List<Subject> subjectList,int Mode) {
        List<ContentFragment> list = new ArrayList<ContentFragment>();
        ContentFragment contentFragment;
        Subject subject;
        for (int i = 0 ; i < subjectList.size() ;i++)
        {
            subject = subjectList.get(i);
            int Number = subject.getSubjectNumber();
            String Title = subject.getSubjectContent();
            String A = subject.getA();
            String B = subject.getB();
            String C = subject.getC();
            String D = subject.getD();
            String Answer = subject.getAnswer();
            contentFragment = new ContentFragment();
            contentFragment.setSubjectData(Number,Title,A,B,C,D,Answer);
            contentFragment.setEditSP(sp);
            if (Mode == 1)
            {
                contentFragment.setVisible(1);
            }
            else
            {
                contentFragment.setVisible(0);
            }
            list.add(contentFragment);

        }
        return list;
    }


}
