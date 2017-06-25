package com.rjstudio.aexam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rjstudio.aexam.UsrInfo.UsrDBOpenHelper;

public class FirstIn extends AppCompatActivity {

    SharedPreferences sp;
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
        sp = this.getSharedPreferences("usrData",MODE_PRIVATE);
        et_usr.setText(readUsr().toString());
        et_usr.setSelection(readUsr().length());
        bu_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_usr.getText().toString().equals(""))
                {
                    Toast.makeText(FirstIn.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                String recordContent = et_usr.getText().toString();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                //存放了一个用户名字
                recordUsr(recordContent);
                intent.putExtra("UsrName",recordContent);
                startActivity(intent);


            }
        });

    }

    public void recordUsr(String name)
    {

        SharedPreferences.Editor spe = sp.edit();
        spe.putString("Username",name);
        spe.commit();
    }

    public String readUsr()
    {
        return sp.getString("Username","");

    }
}
