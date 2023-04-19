package com.example.dishbishv2;

import static java.time.temporal.ChronoUnit.DAYS;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    private static final String CHANNEL_ID = "DishBish";
    private String output;
    private static final String[] people = {"Max", "Ellisha", "Tupuna", "Nathan", "Rory"};
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextView;

    String[] permissions = new String[] {
            android.Manifest.permission.POST_NOTIFICATIONS
    };

    boolean permission_post_notificaiton=false;
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DishBishNotificationChannel";
            String description = "Channel for DishBish Notificaiton";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyDishBish", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void requestPermissionNotification() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            permission_post_notificaiton=true;
        }
        else {
            requestPermissionLauncherNotification.launch(permissions[0]);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted) {
                    permission_post_notificaiton = true;
                }
                else {
                    permission_post_notificaiton = false;
                    showPermissionDialog("Notification Permission");
                }
            });

    public void showPermissionDialog(String permission_desc) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Cmon man please just one notification :(")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gotoSettings= new Intent();
                        gotoSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        gotoSettings.setData(uri);
                        startActivity(gotoSettings);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        String[] order = {"Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Ellisha", "Rory",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "Max", "Nathan",
                "Max", "Ellisha", "Tupuna", "Nathan", "Rory",
                "No one", "Tupuna"};

        setContentView(R.layout.activity_main);
        TextView total = (TextView) findViewById(R.id.who);
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, people);

        autoCompleteTextView.setAdapter(adapterItems);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!permission_post_notificaiton) {
                    requestPermissionNotification();

                }
                String person = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Hi " + person + " you will get a notification at lunch time when it is your day", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
                intent.putExtra("Name", person);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
                Calendar calNow = Calendar.getInstance();

                Calendar calMidDay = Calendar.getInstance();
                calMidDay.set(Calendar.DATE, calNow.get(Calendar.DATE) +1);
                calMidDay.set(Calendar.HOUR_OF_DAY, 12);
                calMidDay.set(Calendar.MINUTE, 0);
                calMidDay.set(Calendar.SECOND, 0);

                long diff = calMidDay.getTimeInMillis() - calNow.getTimeInMillis();

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        diff, AlarmManager.INTERVAL_DAY,
                        pendingIntent);
            }
        });
        int index = 0;
        LocalDate startDate = LocalDate.of(2022, 11, 14);
        LocalDate currDate = LocalDate.now();
        Duration differenceInTime = Duration.ofDays(DAYS.between(startDate, currDate));
        long differenceInDays = differenceInTime.toDays();
        index = (int) differenceInDays % 21;
        output = order[index];
        output = output + " is DishBish tonight";
        total.setText(output);
    }
}