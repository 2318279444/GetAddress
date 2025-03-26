package com.dxc.getaddress;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationAdressFiexdUtil locationAdressFiexdUtil = new LocationAdressFiexdUtil();
        locationAdressFiexdUtil.getDate(this,true,true,false);
        locationAdressFiexdUtil.setCityCallBack(city -> {
            Toast.makeText(this, city, Toast.LENGTH_SHORT).show();

        });

    }
}