package com.rjstudio.aexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rjstudio.aexam.UsrInfo.UsrDBOpenHelper;

public class FirstIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_in);
        initialization();
    }

    private void initialization()
    {
        final EditText et_usr = (EditText)findViewById(R.id.et_usrname);
        Button bu_login = (Button)findViewById(R.id.bu_login);

        bu_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recordContent = et_usr.getText().toString();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //存放了一个用户名字
                intent.putExtra("UsrName",recordContent);
                startActivity(intent);
            }
        });

    }
}
