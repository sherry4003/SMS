package com.example.drago.schedule_sms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    Button schedulebtn, autobtn, historybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //define buttons
        schedulebtn = (Button)findViewById(R.id.schedulebtn);
        autobtn = (Button)findViewById(R.id.autobtn);
        historybtn = (Button)findViewById(R.id.historybtn);
        //define onclick listener
        schedulebtn.setOnClickListener(this);
        autobtn.setOnClickListener(this);
        historybtn.setOnClickListener(this);

    }

    public void onClick (View view){
        switch (view.getId()){
            case R.id.schedulebtn:
                Intent scheduleIntent = new Intent (MenuActivity.this, ScheduleSetActivity.class);
                startActivity(scheduleIntent);
                break;
            case R.id.autobtn:
                Intent autoIntent = new Intent (MenuActivity.this, AutoSetActivity.class);
                startActivity(autoIntent);
                break;
            case R.id.historybtn:
                Intent hisIntent = new Intent (MenuActivity.this, HistoryViewActivity.class);
                startActivity(hisIntent);
                break;
        }
    }


}
