package com.rjstudio.aexam.ContentPager.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by r0man on 2017/6/27.
 */

public class IncorrectFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<ContentFragment> list;
    private int AnswerIsShow = 1 ;
    private FragmentManager fm;
    public IncorrectFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public FragmentManager getFm()
    {
        return fm;
    }

    public void setList(List list)
    {
        this.list = list;
    }
    public List getList()
    {
        return list;
    }

    public void setMode(int AnswerMode,int ButtonMode)
    {
        //1.表示显示答案
        //2.表示隐藏答案
        AnswerIsShow = AnswerMode;
    }

    public int getMode()
    {
        return AnswerIsShow;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        ContentFragment fragment = (ContentFragment)super.instantiateItem(container, position);
//        fragment.setVisible(AnswerIsShow);
//        return fragment;
//    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int  position) {
        return list.get(position);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view != object;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
