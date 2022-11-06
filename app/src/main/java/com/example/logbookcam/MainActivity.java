package com.example.logbookcam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    EditText textURL;
    Button buttonShowPicture;
    ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textURL = findViewById(R.id.textURL);
        buttonShowPicture = findViewById(R.id.buttonShowPicture);
        picture = findViewById(R.id.picture);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        buttonShowPicture.setOnClickListener(v -> {
            final String[]  options = {"Take photo","View a picture from URL"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(options,(dialog,item)->{
                if (options[item]=="Take photo") {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityIfNeeded(intent, 100);
                }else if(options[item]=="View a picture from URL"){
                    try {
                        String url = textURL.getText().toString();
                        Picasso.get().load(url).into(picture);
                    }catch (Exception e) {
                        Toast.makeText(this, "URL invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(captureImage);
        }
    }
}