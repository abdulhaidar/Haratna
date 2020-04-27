package com.haratna.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.haratna.R;

public class Payment extends AppCompatActivity {

    Button Done_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Done_btn = findViewById(R.id.Done_btn);
        Done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Payment.this,DeliveryComplete.class));
            }
        });
    }
}
