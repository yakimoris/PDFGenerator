package ru.biophotonics.msu.pdfgenerator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "pdfCreator";


    private final static int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 1;

    private static final String fileName = "test";
    private static final String Message = "This is message!";
    private PdfGenerator mPdfGenerator;

    private void checkWriteStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                try {
                    mPdfGenerator = new PdfGenerator(fileName,Message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if(shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(MainActivity.this,
                            "app needs to be able to save pdfs", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
    }


    private void checkReadStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {


            } else {
                if(shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(MainActivity.this,
                            "app needs to be able to save pdfs", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case (REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT):{
                if (grantResults.length != 1 || grantResults[0]
                        != PackageManager.PERMISSION_GRANTED) {
                    showToast("Permission not granted!");
                    showToast("Exiting ...");

                    try {
                        wait(10000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else{
                    try {
                        mPdfGenerator = new PdfGenerator(fileName,Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return;
            }

            case (REQUEST_READ_EXTERNAL_STORAGE_PERMISSION_RESULT):{
                if (grantResults.length != 1 || grantResults[0]
                        != PackageManager.PERMISSION_GRANTED) {
                    showToast("Permission not granted!");
                    showToast("Exiting ...");

                    try {
                        wait(10000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkWriteStoragePermission();
//        checkReadStoragePermission();

        mPdfGenerator.generatePDF();
    }
}
