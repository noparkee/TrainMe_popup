package com.pnk.trainme_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeeProposal extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private String trainerID;
    private TextView trainerName, sexageText, exerciseText1, exerciseText2, exerciseText3,  programText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_proposal);

        InitializeView();

        Intent intent = getIntent();
        trainerID = intent.getStringExtra("trainerId");
        System.out.println(trainerID);

        SetText(trainerID);

    }

    public void SetText(String trainerID){
        // 트레이너의 프로필
        databaseReference.child("Trainer").child(trainerID).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());
                TrainerProfile tf = snapshot.getValue(TrainerProfile.class);
                String[] array = tf.exercise.split(",");

                SetExcersize(array);

                trainerName.setText(tf.name);
                sexageText.setText(tf.sex + " / " + tf.age);
                programText.setText(tf.program);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void SetExcersize(String[] array){
        // 지금은 그냥 텍스트뷰로 했는데 나중에 리스트뷰나 그런걸로 하면 좋을 듯
        int length = array.length;

        if (length == 0) {
            exerciseText1.setText(" ");
            exerciseText2.setText(" ");
            exerciseText3.setText(" ");
        }
        else if (length == 1) {
            exerciseText1.setText(array[0]);
            exerciseText2.setText(" ");
            exerciseText3.setText(" ");
        }
        else if (length == 2) {
            exerciseText1.setText(array[0]);
            exerciseText2.setText(array[1]);
            exerciseText3.setText(" ");
        }
        else{
            exerciseText1.setText(array[0]);
            exerciseText2.setText(array[1]);
            exerciseText3.setText(array[2]);
        }
    }

    public void InitializeView(){
        trainerName = (TextView)findViewById(R.id.trainerName);
        sexageText = (TextView)findViewById(R.id.sexageText);
        exerciseText1 = (TextView)findViewById(R.id.exerciseText1);
        exerciseText2 = (TextView)findViewById(R.id.exerciseText2);
        exerciseText3 = (TextView)findViewById(R.id.exerciseText3);
        programText = (TextView) findViewById(R.id.programText);
        programText.setMovementMethod(new ScrollingMovementMethod());
    }

}