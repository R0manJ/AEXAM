package com.rjstudio.aexam;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.rjstudio.aexam.ContentPager.Fragment.ContentFragment;
import com.rjstudio.aexam.ContentPager.Fragment.MyContentFragmentAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        try {
            String path = null ;
            File file = getApplicationContext().getFileStreamPath("vp_item_layout.xml");
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader ur = new BufferedReader(isr);
            String content = null;
            while((content = ur.readLine())!=null)
            {
                Log.d("Test",content);
            }
            Toast.makeText(getApplicationContext(),path, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {

        }
    }


}
