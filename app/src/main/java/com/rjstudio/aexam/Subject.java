package com.rjstudio.aexam;

/**
 * Created by r0man on 2017/6/24.
    用于存放题目,里面包含的数据有:
 题号,题目,选项,选项编号,选项内容,正确答案
 */

public class Subject {
    private int SubjectNumber;
    private int SubjectContent;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;

    public Subject(int SubNum,int SubCon,String A,String B,String C,String D,String answer)
    {
        this.SubjectNumber = SubNum;
        this.SubjectContent = SubCon;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.answer = answer;
    }
}
