package com.rjstudio.aexam.ContentPager.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by r0man on 2017/6/26.
 */

public class MyContentFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private List<ContentFragment> list;
    private int AnswerIsShow = 0 ;
    private int ButtonIsShow = 0;
    private int isChecked = 0;
    private Boolean isOpen = false;

    public void setAdapterOpen (Boolean values)
    {
        isOpen = values;
    }

    public Boolean getAdapterOpen()
    {
        return isOpen;
    }

    public MyContentFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyContentFragmentAdapter(FragmentManager fm, List list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    public void setList(List list)
    {
        this.list = list;
    }

    public List getList()
    {
        return list;
    }


    public void setIsChecked(int Mode)
    {
        isChecked = Mode;
    }


    public void setMode(int AnswerMode,int ButtonMode)
    {
        //1.表示显示答案
        //2.表示隐藏答案
        AnswerIsShow = AnswerMode;
        ButtonIsShow = ButtonMode;
    }

    public int getMode()
    {
        return AnswerIsShow;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ContentFragment contentFragment = (ContentFragment)super.instantiateItem(container,position);
        contentFragment.setVisible(AnswerIsShow);
        contentFragment.setChecked(isChecked);
        return contentFragment;
        //TODO :这里需要总结,CSDN的一篇文章非常好.
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int  position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
