package com.pnk.trainme_java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomDialog extends Activity {
    private RadioButton unable, enable;
    private EditText editText, perweekText, totalText;
    private TextView length, name;
    private Button send;

    private String message, perweek, total;
    private String price = "-1";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    // 아이디는 나중에는 따로 정보 가져와야함.
    // 이 부분은 트레이너의 관점에서
    // trainerID는 자신의 카카오 아이디가 되고, userID는 자신이 제안서를 보낼 유저의 카카오 아이디
    private String userID, username;
    private String trainerID = "tid001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userId");
        username = intent.getStringExtra("username");

        InitializeView();

        name.setText(username + "  님");

        editText.addTextChangedListener(new TextWatcher() {     // 실시간 글씨 수
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = editText.getText().toString();
                length.setText(input.length() + " / 300");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER) return true;
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unable.isChecked()) {       // 협상 불가능
                    price = "0";
                }
                else if (enable.isChecked()) {price = "1";}      //협상 가능

                perweek = perweekText.getText().toString();
                total = totalText.getText().toString();
                message = editText.getText().toString();

                boolean check = checkInput(price, perweekText, perweek, totalText, total);

                if (check){
                    storeData(price, perweek, total, message, userID);
                    finish();
                }

            }
        });
    }

    public void InitializeView()
    {
        unable = (RadioButton)findViewById(R.id.unableBtn);
        enable = (RadioButton)findViewById(R.id.enableBtn);

        editText = (EditText)findViewById(R.id.editText);
        perweekText = (EditText)findViewById(R.id.perweekText);
        totalText = (EditText)findViewById(R.id.totalText);

        send = (Button)findViewById(R.id.sendBtn);

        length = (TextView)findViewById(R.id.length);
        name = (TextView)findViewById(R.id.usernameText);
    }

    // 문제점은 기존의 내역을 그냥 다 덮어씀
    public void storeData(String price, String perweek, String total, String message, String user){
        String key = databaseReference.child("/Trainer/" + trainerID + "/toUser/").push().getKey();
        Proposal proposal = new Proposal(price, perweek, total, message);
        Map<String, Object> propsalValues = proposal.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Trainer/" + trainerID + "/toUser/" + user + "/", propsalValues);
        childUpdates.put("/User/" + user + "/fromTrainer/" + trainerID + "/", propsalValues);

        databaseReference.updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomDialog.this, "제안서를 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomDialog.this, "제안서 보내기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean checkInput(String price, TextView perweekText, String perweek, TextView totalText, String total){
        int r = 1;
        System.out.println(price);
        if (price.equals("-1")) {       // 라디오 선택 안 함
            Toast.makeText(CustomDialog.this, "가격 협상 가능 여부를 선택하세요", Toast.LENGTH_SHORT).show();
            r = 0;
        }
        else if (perweek.length() <= 0 || total.length() <= 0){      // perweek, total 둘 중 하나 입력 안 했을 때
            Toast.makeText(CustomDialog.this, "추천 횟수를 입력하세요", Toast.LENGTH_SHORT).show();
            r = 0;
        }
        else if (Integer.parseInt(perweek) > 7) {       // 주 n회 인데 7보다 큰 수
            Toast.makeText(CustomDialog.this, "주 추천 횟수는 7회 이하로 입력하세요", Toast.LENGTH_SHORT).show();
            r = 0;
        }
        else if (Integer.parseInt(perweek) > Integer.parseInt(total)){       // 주 횟수보다 전체가 더 작은 회수
            Toast.makeText(CustomDialog.this, "전체 추천 횟수는 주 추천 횟수 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            r = 0;
        }

        if (r == 0)
            return false;
        else
            return true;
    }
}
