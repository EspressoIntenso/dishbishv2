package com.example.dishbishv2;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.Duration;
import java.time.LocalDate;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String person = (String) intent.getExtras().get("Name");
        String[] order = {"Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Ellisha", "Rory",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Max", "Nathan",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "No one", "Tupuna"};
        int index = 0;
        LocalDate startDate = LocalDate.of(2022, 11, 14);
        LocalDate currDate = LocalDate.now();
        Duration differenceInTime = Duration.ofDays(DAYS.between(startDate, currDate));
        long differenceInDays = differenceInTime.toDays();
        index = (int) differenceInDays % 21;
        String output = order[index];
        if (person.equals(output)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyDishBish")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("It's You!")
                    .setContentText("Test")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(200, builder.build());
        }
    }
}
