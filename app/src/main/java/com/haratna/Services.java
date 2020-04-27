package com.haratna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Services extends AppCompatActivity {

    LinearLayout Delivery_LL;
    LinearLayout Store_LL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Delivery_LL = findViewById(R.id.Delivery_LL);
        Store_LL = findViewById(R.id.Store_LL);
        Delivery_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Services.this,AroundStores.class));
            }
        });
        Store_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Services.this,AvailableStores.class));
            }
        });
    }
}
