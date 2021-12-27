package com.scet.saloonspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminDashBoardActivity extends AppCompatActivity {

    FloatingActionButton floatingAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_dash_board);
        getInit();
        setEvents();

    }

    private void setEvents() {
        floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashBoardActivity.this,AddSaloonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getInit() {
        floatingAdd = findViewById(R.id.floatingAdd);
    }
}
