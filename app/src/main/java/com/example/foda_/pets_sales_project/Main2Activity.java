package com.example.foda_.pets_sales_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.pet);
        toolbar.setTitle("\t \t P.e.t.s");
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.myframe,new Main2ActivityFragment()).commit();

    }

}
