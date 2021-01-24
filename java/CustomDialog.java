package com.pnk.trainme_java;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomDialog extends Activity {
    private RadioButton unable, enable;
    private EditText editPrice, editText;
    private TextView length;
    private Button send;

    private String price;
    private String message;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    // 아이디는 나중에는 따로 정보 가져와야함.
    // 이 부분은 트레이너의 관점에서
    // trainerID는 자신의 카카오 아이디가 되고, userID는 자신이 제안서를 보낼 유저의 카카오 아이디
    private String userID = "id001";
    private String trainerID = "tid001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        InitializeView();

        enable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editPrice.setEnabled(true);
            }
        });

        unable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editPrice.setEnabled(false);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
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
                    message = editText.getText().toString();
                    storeData(price, message, userID);
                    finish();
                }

                if (enable.isChecked()){        // 협상 가능
                    price = editPrice.getText().toString();
                    if (price.length() == 0 || price.equals("0"))        // 가격 미입력시
                        Toast.makeText(CustomDialog.this, "가격을 입력하세요!", Toast.LENGTH_SHORT).show();
                    else {
                        message = editText.getText().toString();
                        storeData(price, message, userID);
                        finish();
                    }
                }
                System.out.println(price);
                System.out.println(message);
            }
        });
    }

    public void InitializeView()
    {
        unable = (RadioButton)findViewById(R.id.unableBtn);
        enable = (RadioButton)findViewById(R.id.enableBtn);
        editPrice = (EditText)findViewById(R.id.editPrice);
        editText = (EditText)findViewById(R.id.editText);
        send = (Button)findViewById(R.id.sendBtn);
        length = (TextView)findViewById(R.id.length);
    }

    // 문제점은 기존의 내역을 그냥 다 덮어씀
    public void storeData(String price, String message, String user){
        String key = databaseReference.child("/Trainer/" + trainerID + "/toUser/").push().getKey();
        Proposal proposal = new Proposal(price, message);
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
}
