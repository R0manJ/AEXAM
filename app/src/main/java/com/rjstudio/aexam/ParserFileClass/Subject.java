package com.rjstudio.aexam.ParserFileClass;

/**
 * Created by r0man on 2017/6/24.
    用于存放题目,里面包含的数据有:
 题号,题目,选项,选项编号,选项内容,正确答案
 */

public class Subject {
    private int SubjectNumber;
    private String SubjectContent;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;

    public Subject() {
        super();
    }

    public Subject(int SubNum, String SubCon, String A, String B, String C, String D, String answer)
    {
        this.SubjectNumber = SubNum;
        this.SubjectContent = SubCon;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.answer = answer;
    }

    public int getSubjectNumber() {
        return SubjectNumber;
    }

    public void setSubjectNumber(int subjectNumber) {
        SubjectNumber = subjectNumber;
    }

    public String getSubjectContent() {
        return SubjectContent;
    }

    public void setSubjectContent(String subjectContent) {
        SubjectContent = subjectContent;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
