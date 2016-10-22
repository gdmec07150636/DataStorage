package com.rynfar.datastorege;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.jar.Manifest;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private Button btnSPW,btnSPR,btnROMW,btnROMR,btnSDW,btnSDR;
    private EditText edtView1,edtView2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
    }

    private void init() {
        btnSPW = (Button) findViewById(R.id.spWrite);
        btnSPR = (Button) findViewById(R.id.spRead);
        btnROMW = (Button) findViewById(R.id.romWrite);
        btnROMR = (Button) findViewById(R.id.romRead);
        btnSDW = (Button) findViewById(R.id.sdWrite);
        btnSDR = (Button) findViewById(R.id.sdRead);

        btnSPW.setOnClickListener(new MyListener());
        btnSPR.setOnClickListener(new MyListener());
        btnROMW.setOnClickListener(new MyListener());
        btnROMR.setOnClickListener(new MyListener());
        btnSDW.setOnClickListener(new MyListener());
        btnSDR.setOnClickListener(new MyListener());

        edtView1 = (EditText) findViewById(R.id.edt1);
        edtView2 = (EditText) findViewById(R.id.edt2);

        textView = (TextView) findViewById(R.id.textView);
    }


    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.spWrite:
                    spWrite();
                    break;
                case R.id.spRead:
                    spRead();
                    break;
                case R.id.romWrite:
                    romWrite();
                    break;
                case R.id.romRead:
                    romRead();
                    break;
                case R.id.sdWrite:
                    sdWrite();
                    break;
                case R.id.sdRead:
                    sdRead();
                    break;
            }
        }
    }

    private void romRead() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(openFileInput("user.txt")));
            StringBuffer sb = new StringBuffer();
            String s = "";
            while((s=bfr.readLine())!=null) {
                sb.append(s);
            }
            bfr.close();
            textView.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void romWrite() {
        String account = edtView1.getText().toString();
        String password = edtView2.getText().toString();
        try{
            FileOutputStream fos = openFileOutput("user.txt",MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bfw = new BufferedWriter(osw);
            bfw.write(account+":"+password);
            bfw.flush();
            fos.close();
            Toast.makeText(MainActivity.this,"写入成功",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spRead() {
        SharedPreferences user = getSharedPreferences("user",MODE_PRIVATE);
        String account = user.getString("account","没有值");
        String password = user.getString("password","没有值");
        textView.setText("账户："+account+"\n"+"密码"+password);

    }

    private void spWrite(){
        SharedPreferences user = getSharedPreferences("user",MODE_APPEND);
        SharedPreferences.Editor editor = user.edit();
        editor.putString("account",edtView1.getText().toString());
        editor.putString("password",edtView2.getText().toString());
        editor.commit();
        Toast.makeText(this,"SP写入成功",Toast.LENGTH_SHORT).show();
    }

    private void sdWrite() {
        String str = edtView1.getText().toString()+":"+edtView2.getText().toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += "/user.txt";
        File file = new File(path);

        try {
            if(!file.exists()){
                 file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
            fos.flush();
            fos.close();
            Toast.makeText(this,"SP写入成功",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sdRead() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += "/user.txt";
        //Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        File file = new File(path);
        int length = (int) file.length();
        byte[] buffer = new byte[length];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer,0,length);
            fis.close();
            textView.setText(new String(buffer));
            Toast.makeText(this,"SP读取成功",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
