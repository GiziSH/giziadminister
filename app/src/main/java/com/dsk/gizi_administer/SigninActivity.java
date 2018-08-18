package com.dsk.gizi_administer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

public class SigninActivity extends MainActivity {
    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pw = (EditText) findViewById(R.id.et_password);
        et_pw_chk = (EditText) findViewById(R.id.et_password_chk);
        alertBuilder = new AlertDialog.Builder(this);
    }

    public void bt_signinok(View view) {
        //버튼을 눌렀을 때 동작하는 소스
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        if (sPw.equals(sPw_chk)) { // 비밀번호와 비밀번호 체크값을 비교해서
            //패스워드 서로 일치하면 등록
            registDB rdb = new registDB();
            rdb.execute();
        } else {
            //패스워드 불일치하면 실패 메세지 팝업
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
        //finish();
    }

}
