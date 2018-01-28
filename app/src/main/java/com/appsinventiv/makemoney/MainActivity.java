package com.appsinventiv.makemoney;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    Button button;
    TextView textView;
    double count;
    Button cashout;
    private SharedPreferences userPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,

        };
        if (!hasPermissions(this, PERMISSIONS)) {

            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("This app needs to store your profile\nPlease allow access to your device storage");
            builder.setTitle("Alert!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
            });
            AlertDialog dialog = builder.create();
                        dialog.show();
        }
        else{
//            Toast.makeText(this, "has permissions", Toast.LENGTH_SHORT).show();
//            startService(new Intent(this, BackgroundService.class));
        }
        button =(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        cashout=(Button)findViewById(R.id.cash);


        cashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<=100){
                    Toast.makeText(MainActivity.this, "Cannot Redeem now\nMinimum balanc should be Rs 100", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent i =new Intent(MainActivity.this,Redeam.class);
                    i.putExtra("earning",""+count);
                    startActivity(i);
                    SharedPreferences.Editor ed = userPref.edit();
                    count=0;
                    ed.putString("count", ""+count);
                    ed.commit();
                    finish();
                }
            }
        });

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        count = Double.parseDouble(userPref.getString("count", "0"));
        textView.setText("Rs "+String.format("%.2f", count));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=count+0.01;
                textView.setText("Rs "+String.format("%.2f", count));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor ed = userPref.edit();
        ed.putString("count", ""+count);
        ed.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                startService(new Intent(this, BackgroundService.class));
            }
        }

    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
//        Toast.makeText(context, "has", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, BackgroundService.class));
        return true;
    }
}
