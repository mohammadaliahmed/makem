package com.appsinventiv.makemoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.IOException;

public class Redeam extends AppCompatActivity {
    EditText name, phonenumber;
    Button redeem;

    DatabaseReference mDatabase;
    TextView earned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeam);
        phonenumber = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        redeem = (Button) findViewById(R.id.redeem);
        earned = (TextView) findViewById(R.id.textView2);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        Intent intent = getIntent();
        double ear = Double.parseDouble(intent.getStringExtra("earning"));

        earned.setText("You earned:  Rs " + String.format("%.2f", ear));
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isConnected()) {
                        String user = name.getText().toString();
                        String phone = phonenumber.getText().toString();

                        if (name.getText().length() == 0) {
                            name.setError("It cannot be null");
                        }
                        if (phonenumber.getText().length() == 0) {
                            phonenumber.setError("It cannot be null");
                        } else {

                            mDatabase.push().setValue(new Users(user, phone)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Redeam.this, "Request successfully submitted\nYou will be updated within 2-5 days", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }
                    }
                    else {
                        Toast.makeText(Redeam.this, "No internet connection\nPlease connect to internet", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }
}
