package com.pnk.trainme_java;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class UserPopup extends Activity {
    String trainername, trainerId, price, message;

    private TextView trainerText, priceText, commentText;
    private Button moreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_popup);

        InitializeView();

        Intent intent = getIntent();

        trainername = intent.getStringExtra("trainername");
        price = intent.getStringExtra("price");
        message = intent.getStringExtra("message");
        trainerId = intent.getStringExtra("trainerId");

        if (price.equals("0"))
            price = "협의 불가능";

        SetText(trainername, price, message);

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proposal = new Intent(UserPopup.this, SeeProposal.class);
                proposal.putExtra("trainerId", trainerId);

                startActivity(proposal);
                finish();
            }
        });


    }
    public void SetText(String trainername, String price, String message){
        trainerText.setText(trainername);
        priceText.setText(price);
        commentText.setText(message);
    }

    public void InitializeView(){
        trainerText = (TextView)findViewById(R.id.trainerText);
        priceText = (TextView)findViewById(R.id.priceText);
        commentText = (TextView)findViewById(R.id.commentText);
        moreBtn = (Button)findViewById(R.id.moreBtn);
    }
}
