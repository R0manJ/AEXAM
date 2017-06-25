package com.rjstudio.aexam.ParserFileClass;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by r0man on 2017/6/24.
 */

public class PaserToXml {

    private InputStream in;
    private BufferedReader br ;
    private Context context;
    private List<Subject> subjectList;

    public PaserToXml(InputStream in, Context context) {
        this.in = in;
        this.context = context;
        InputStreamReader isr = new InputStreamReader(in);

        paserToXml(in);


    }

    //InputStream ->  xml编码
    public void paserToXml  (InputStream in)
    {
        String TAG = "paserToXml";
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String content;

        //输出文件部分
        BufferedWriter bw;

        File outputFile = new File(context.getCacheDir()+"OutputXml.xml");
        try
        {

            if (outputFile.exists())
            {

                Log.d(TAG, "paserToXml: 删除原文件");
                outputFile.delete();
            }
            outputFile.createNewFile();
           // Log.d(TAG, "创建文件放在: "+outputFile.getPath());
            OutputStream op = new FileOutputStream(outputFile);
            OutputStreamWriter osw = new OutputStreamWriter(op);
            bw = new BufferedWriter(osw);

            //读取文件部分
            try {
                bw.write("<Document>");
                bw.newLine();
                while ((content = br.readLine()) != null) {

                    //Log.d(TAG, content);

                    if (content.indexOf("、") != -1)
                    {
                        String split[] = content.split("、");
                        split[0] = "<subject>\n\r<Number>"+split[0]+"</Number>";
                        bw.write(split[0]);
                        bw.newLine();
                        split[1] =  "<Title>"+split[1]+"</Title>";
                        bw.write(split[1]);
                        bw.newLine();
                        //Log.d(TAG, split[0]+"--"+split[1]);
                    }

                    if (content.split("rJ").length > 1)
                    {
                        String splitContent[] = content.split("rJ");
                        if (splitContent[0].equals("A "))
                        {
                            splitContent[0] = "<item_A>"+splitContent[0]+"</item_A>";
                            splitContent[1] = "<item_A_Content>"+splitContent[1]+"</item_A_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("B "))
                        {
                            splitContent[0] = "<item_B>"+splitContent[0]+"</item_B>";
                            splitContent[1] = "<item_B_Content>"+splitContent[1]+"</item_B_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("C "))
                        {
                            splitContent[0] = "<item_C>"+splitContent[0]+"</item_C>";
                            splitContent[1] = "<item_C_Content>"+splitContent[1]+"</item_C_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("D "))
                        {
                            splitContent[0] = "<item_D>"+splitContent[0]+"</item_D>";
                            splitContent[1] = "<item_D_Content>"+splitContent[1]+"</item_D_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        bw.write(splitContent[0]);
                        bw.newLine();
                        bw.write(splitContent[1]);
                        bw.newLine();
                        // Log.d(TAG, splitContent[0]+splitContent[1]);
                    }

                    if (content.split("答案").length > 1 )
                    {
                        String splitReasult[] ;
                        splitReasult = content.split("答案");
                        splitReasult[1] = "<answer>"+splitReasult[1]+"</answer></subject>";
                        //Log.d(TAG, "paserToXml: "+splitReasult[1]);
                        bw.write(splitReasult[1]);
                        bw.newLine();
                    }
                }
                bw.write("</Document>");
                //写入完毕
                //关闭输出流/输出流
                bw.close();
                br.close();
            }
            catch (Exception e)
            {
                Log.d(TAG, "读取失败");
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "创建文件失败");
        }


    }


    //解析XML -> List
    public List<Subject> CreateAndSaveObject()
    {
        List<Subject> subjectList = new ArrayList<Subject>();
        //这个函数用于解析xml并保存对象
        try
        {
            int recordeNumber = 1;

            String TAG  = "XML解析";
            File xmlFile = new File(context.getCacheDir()+"OutputXml.xml");
            InputStream in = new FileInputStream(xmlFile);

            //Log.d(TAG, "读取完毕");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullPaser = factory.newPullParser();
            xmlPullPaser.setInput(in,"utf-8");

            int eventType = xmlPullPaser.getEventType();

            Subject subject = null;
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                String nodeName = xmlPullPaser.getName();
                // Log.d(TAG, "解析的节点:"+nodeName);
                String SubjectContent = "x";
                String A = "xx";
                String B = null;
                String C = null;
                String D = null;
                String answer = null;


                // Log.d(TAG, "CreateAndSaveObject: "+xmlPullPaser.nextText());
                switch(eventType) {
                    case XmlPullParser.START_TAG:
                    {
                        if ("subject".equals(nodeName))
                        {
                            subject = new Subject();
                            subject.setSubjectNumber(recordeNumber++);
                        }
                        else if ("Title".equals(nodeName)) {
                            SubjectContent = xmlPullPaser.nextText();
                            subject.setSubjectContent(SubjectContent);
                        } else if ("item_A_Content".equals(nodeName)) {
                            A = xmlPullPaser.nextText();
                            subject.setA(A);
                            Log.d(TAG, A);
                        } else if ("item_B_Content".equals(nodeName)) {
                            B = xmlPullPaser.nextText();
                            subject.setB(B);
                            Log.d(TAG, B);
                        } else if ("item_C_Content".equals(nodeName)) {
                            C = xmlPullPaser.nextText();
                            subject.setC(C);
                            Log.d(TAG, C);
                        } else if ("item_D_Content".equals(nodeName)) {
                            D = xmlPullPaser.nextText();
                            subject.setD(D);
                            Log.d(TAG, D);

                        } else if ("answer".equals(nodeName)) {
                            answer = xmlPullPaser.nextText();
                            subject.setAnswer(answer);
                            Log.d(TAG, answer);
                        }
                        break;
                        //完成解析某一个节点
                    }

                    case XmlPullParser.END_TAG:
                    {
                        // Log.d(TAG, "结束标签");
                        if ("subject".equals(nodeName))
                        {

                            //Toast.makeText(this, "解析了一个节点", Toast.LENGTH_SHORT).show();
                            //创建一个题目类
                            //将其添加到集合中
                            subjectList.add(subject);
                            Log.d(TAG, "添加了"+subjectList.size()+"个记录");
                        }
                        else
                            break;
                    }
                    default:
                        break;

                }
                eventType = xmlPullPaser.next();

            }
            //Toast.makeText(this,"这个题目集合的长度为:"+ subjectList.size(), Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            //Log.d("Test subject", subjectList.get(0).getSubjectNumber()+"--"+subjectList.get(0).getSubjectContent()+"--"+subjectList.get(0).getA()+subjectList.get(0).getB()+subjectList.get(0).getC()+subjectList.get(0).getD());
            //Toast.makeText(this,"这个题目集合的长度为:"+ subjectList.size(), Toast.LENGTH_SHORT).show();
            return subjectList;
//            Log.d("出错题",subjectList.get(subjectList.size()-1).getSubjectNumber()
//                    +"--"+subjectList.get(subjectList.size()-1).getSubjectContent()
//                    +"--"+subjectList.get(subjectList.size()-1).getA()
//                    +"--"+subjectList.get(subjectList.size()-1).getB()
//                    +"--"+subjectList.get(subjectList.size()-1).getC()
//                    +"--"+subjectList.get(subjectList.size()-1).getD()
//                    +"--"+subjectList.get(subjectList.size()-1).getAnswer());
//
//            Log.d("出错了", "这个集合共有: "+subjectList.size());
//            Toast.makeText(this, "读取XML文件失败", Toast.LENGTH_SHORT).show();
        }
        return subjectList;
    }

    //用于遍历输出解析的集合
    public void CheckList()
    {
        for (int i =0;i<subjectList.size();i++)
        {
            Log.d(i+"出错题",subjectList.get(i).getSubjectNumber()
                    +"--"+subjectList.get(i).getSubjectContent()
                    +"--"+subjectList.get(i).getA()
                    +"--"+subjectList.get(i).getB()
                    +"--"+subjectList.get(i).getC()
                    +"--"+subjectList.get(i).getD()
                    +"--"+subjectList.get(i).getAnswer());
        }
    }
}
