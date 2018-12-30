package ru.pschsch.pschschapps.studyingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.*;


public class MainActivity extends AppCompatActivity {
    public static final List<String> buttons = Arrays.asList(
            "AndroidStructure",
            "Activity Information",
            "Intents Information",
            "Task and Back Stack Information",
            "Fragment Information");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
