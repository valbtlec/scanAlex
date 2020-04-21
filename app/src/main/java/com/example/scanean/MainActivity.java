package com.example.scanean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static int CAMERA_PERMISSION_CODE = 26;
    Button Scan;
    TextView EANBase,DesignationBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Scan = (Button) findViewById(R.id.Scan);
        EANBase = (TextView) findViewById(R.id.EAN);
        DesignationBase = (TextView) findViewById(R.id.Designation);

        final String EAN = getIntent().getStringExtra("EAN");
        final String Designation = getIntent().getStringExtra("Designation");

        if(EAN != null ){
            EANBase.append(" "+EAN);
            DesignationBase.append(" "+Designation);
        }


        requestCameraPermission();

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ScanCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }


        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == CAMERA_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                db.addPermissionCode(String.valueOf(requestCode));
                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //  Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

}
