package com.rjstudio.aexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rjstudio.aexam.ParserFileClass.PaserToXml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            paserToXml(getAssets().open("a.txt"));

        }catch (Exception e)
        {

        }

    }

    //xml编码
    public void paserToXml  (InputStream in)
    {
        String TAG = "paserToXml";
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String content;

        //输出文件部分
        BufferedWriter bw;
        File outputFile = new File(getCacheDir()+"\\data\\OutputXml.xml");
        try
        {
            outputFile.createNewFile();
            OutputStream op = new FileOutputStream(outputFile);
            OutputStreamWriter osw = new OutputStreamWriter(op);
            bw = new BufferedWriter(osw);

            //读取文件部分
            try {
                
                while ((content = br.readLine()) != null) {

                    //Log.d(TAG, content);
                    
                    if (content.indexOf("、") != -1)
                    {
                        String split[] = content.split("、");
                        split[0] = "<subject><Number>"+split[0]+"</Number>";
                        bw.write(split[0]);
                        split[1] =  "<Title>"+split[1]+"</Title>";
                        bw.write(split[1]);
                        Log.d(TAG, split[0]+"--"+split[1]);
                    }
                    
                    if (content.split("RJ").length > 1)
                    {
                        String splitContent[] = content.split("RJ");
                        if (splitContent[0].equals("A "))
                        {
                            splitContent[0] = "<item_A>"+splitContent[0]+"</item_A>";
                            splitContent[1] = "<item_A_Content>"+splitContent[1]+"<i/tem_A_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("B "))
                        {
                            splitContent[0] = "<item_B>"+splitContent[0]+"</item_B>";
                            splitContent[1] = "<item_B_Content>"+splitContent[1]+"<i/tem_B_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("C "))
                        {
                            splitContent[0] = "<item_C>"+splitContent[0]+"</item_C>";
                            splitContent[1] = "<item_C_Content>"+splitContent[1]+"<i/tem_C_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        else if (splitContent[0].equals("D "))
                        {
                            splitContent[0] = "<item_D>"+splitContent[0]+"</item_D>";
                            splitContent[1] = "<item_D_Content>"+splitContent[1]+"<i/tem_D_Content>";
                            //Log.d(TAG, splitContent[0]);
                        }
                        bw.write(splitContent[0]);
                        bw.write(splitContent[1]);
                        bw.newLine();
                        Log.d(TAG, splitContent[0]+"-"+splitContent[1]);
                    }

                    if (content.split("答案").length > 1 )
                    {
                        String splitReasult[] ;
                        splitReasult = content.split("答案");
                        splitReasult[1] = "<answer>"+splitReasult[1]+"</answer></subject>";
                        Log.d(TAG, "paserToXml: "+splitReasult[1]);
                        bw.write(splitReasult[1]);
                        bw.newLine();
                    }
                }
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

    public void CreateAndSaveObject(File xmlFile)
    {
        //这个函数用于解析xml并保存
    }
}
