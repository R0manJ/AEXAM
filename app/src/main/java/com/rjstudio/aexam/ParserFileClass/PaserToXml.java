package com.rjstudio.aexam.ParserFileClass;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by r0man on 2017/6/24.
 */

public class PaserToXml {

    InputStream in;
    BufferedReader br ;

    public PaserToXml(InputStream in) {
        this.in = in;
        InputStreamReader isr = new InputStreamReader(in);
        this.br = new BufferedReader(isr);

        ChangeToXml(br);
    }

    public void ChangeToXml(BufferedReader br)
    {
        String TAG = "ChangeToXml";
        try {
            String content ;
            while((content = br.readLine())!=null)
            {

                // Log.d(TAG, content.indexOf("、")+"");
                String split[] = content.split("、");
                if (split.length>=1)
                {
                    //Log.d(TAG, "ChangeToXml: "+split[0]+"--"+split[1]);

                }




            }


        }
        catch (Exception e)
        {
            Log.d(TAG,"Error");
        }

    }
}
