package com.example.dishbishv2;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.time.Duration;
import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] order = {"Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Ellisha", "Rory",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Max", "Nathan",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "No one", "Tupuna"};

        setContentView(R.layout.activity_main);
        TextView total = (TextView) findViewById(R.id.who);

        int index = 0;
        LocalDate startDate = LocalDate.of(2022, 11, 14);
        LocalDate currDate = LocalDate.now();
        Duration differenceInTime = Duration.ofDays(DAYS.between(startDate, currDate));
        long differenceInDays = differenceInTime.toDays();
        index = (int)differenceInDays  % 21;
        String output = order[index];
        System.out.println(output);
        total.setText(output);
    }
}