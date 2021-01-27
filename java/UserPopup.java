package com.pnk.trainme_java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



public class UserPopup extends Activity {
    String trainername, trainerId, price, message, perweek, total;

    private TextView trainerText, priceText, commentText, numText;
    private Button moreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_popup);

        InitializeView();

        Intent intent = getIntent();

        trainername = intent.getStringExtra("trainername");
        trainerId = intent.getStringExtra("trainerId");

        price = intent.getStringExtra("price");
        message = intent.getStringExtra("message");
        perweek = intent.getStringExtra("perweek");
        total = intent.getStringExtra("total");

        if (price.equals("0"))
            price = "협의 불가능";
        else if (price.equals("1"))
            price = "협의 가능";

        SetText(trainername, price, perweek, total, message);

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
    public void SetText(String trainername, String price, String perweek, String total, String message){
        trainerText.setText(trainername + " 님");
        priceText.setText(price);
        numText.setText("주 " + perweek + "회 / 총" + total + "회");

        commentText.setText(message);
    }

    public void InitializeView(){
        trainerText = (TextView)findViewById(R.id.trainerText);
        priceText = (TextView)findViewById(R.id.priceText);
        commentText = (TextView)findViewById(R.id.commentText);
        numText = (TextView)findViewById(R.id.numText);

        moreBtn = (Button)findViewById(R.id.moreBtn);
    }
}
