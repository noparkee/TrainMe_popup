package com.pnk.trainme_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button user;
    private Button trainer;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private String userID = "id001";
    private String trainerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.InitializeView();      // initialize btns

        trainer.setOnClickListener(new View.OnClickListener(){      // 트레이너가 유저 프로필에서 보내기 버튼을 눌렀을 때의 팝업창
            @Override
            public void onClick(View v) {
                // 여기서는 선택한 유저의 id를 같이 넘겨줘야함
                // UI같은 곳에서 선택된 유저의 id를 알아올 수 있도록
                userID = "id001";

                databaseReference.child("User").child(userID).child("/profile/name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = snapshot.getValue().toString();

                        Intent intent = new Intent(MainActivity.this, CustomDialog.class);
                        intent.putExtra("userId", userID);
                        intent.putExtra("username", userName);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        // 유저가 자신에게 제안서를 보낸 트레이너 목록 중 한명을 눌렀을 때
        // (실제 코드에서는 눌렀을 때 눌린 트레이너의 정보를 알아야함.)
        // 트레이너의 아이디를 알아야함. 이름을 알면 동명이인 있을 수도
        user.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // --- 선택한 트레이너의 아이디를 알아오는 함수 ---
                // 여기서 trainerID를 찾아냄.
                trainerID = "tid001";

                // 추가 메세지 (가격, 코멘트)
                databaseReference.child("User").child(userID).child("fromTrainer").child(trainerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //System.out.println(snapshot.getValue());
                        final Proposal proposal = snapshot.getValue(Proposal.class);

                        databaseReference.child("Trainer").child(trainerID).child("profile").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String trainerName = snapshot.getValue().toString();
                                Intent intent = new Intent(MainActivity.this, UserPopup.class);

                                intent.putExtra("trainername", trainerName);
                                intent.putExtra("price", proposal.price);
                                intent.putExtra("message", proposal.message);
                                intent.putExtra("perweek", proposal.perweek);
                                intent.putExtra("total", proposal.total);
                                intent.putExtra("trainerId", trainerID);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void InitializeView()
    {
        user = (Button)findViewById(R.id.user);
        trainer = (Button)findViewById(R.id.trainer);
    }
}