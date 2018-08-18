package com.dsk.gizi_administer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = (EditText) findViewById(R.id.et_id);
        et_pw = (EditText) findViewById(R.id.et_password);
        et_pw_chk = (EditText) findViewById(R.id.et_password_chk);

        // signin 버튼을 눌렀을 때 생기는 일
        Button bt_signin = (Button) findViewById(R.id.bt_signin);
        bt_signin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SigninActivity 실행
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

    }
    public class registDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://192.168.35.51/gizi_signin.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /* 서버에서 응답 */
            Log.e("RECV DATA",data);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            if(data.equals("1"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 등록되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else if(data.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }

    }


    public class loginDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            Log.e("POST",param);

            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://192.168.35.51/gizi_login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA",data);



                /*if(data.equals("1"))
                {
                    Log.e("RESULT","성공적으로 처리되었습니다!");
                }
                else if(data.equals("0")) {
                    Log.e("RESULT", "아이디와 비밀번호가 일치하지 않습니다."); //
                    alertBuilder
                            .setTitle("알림")
                            .setMessage("아이디와 비밀번호가 일치하지 않습니다")
                            .setCancelable(true)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();
                                }
                            });
                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /* 서버에서 응답 */
            Log.e("RECV DATA",data);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            if(data.equals("1"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");
                Log.e("RESULT", "로그인 성공!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("로그인 성공!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
                /*Intent intent = new Intent(MainActivity.this, ComplainListActivity.class);
                startActivity(intent);*/
            }
            else if(data.equals("0")) {
                Log.e("RESULT", "아이디와 비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("아이디와 비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else {
                Log.e("RESULT", "존재하지 않는 아이디입니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("존재하지 않는 아이디입니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }

    }

    public void bt_signin(View view) {

    }

    public void bt_login(View view) {
        try {
            sId = et_id.getText().toString();
            sPw = et_pw.getText().toString();
        } catch(NullPointerException e) {
            Log.e("err", e.getMessage());
        }
        loginDB IDB = new loginDB();
        IDB.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



