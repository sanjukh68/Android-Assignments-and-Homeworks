/*
Assignment No: Homework 2
File Name: SelectPhoto.java
Name: Sanju Kurubara Budi Hall Hiriyanna Gowda
      Sujal T Vijayaraghavan
 */
package com.example.tvs.expensemanagement;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectPhoto extends AppCompatActivity implements View.OnClickListener{
    protected final int GALLERY_CODE = 9000, CAMERA_CODE = 9002;
    protected final String IMAGE_TYPES = "image/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        Button cancel = (Button) findViewById(R.id.cancel);
        Button cameraBtn = (Button) findViewById(R.id.clickCamera2);
        Button galleryBtn = (Button) findViewById(R.id.gallery2);
        ImageView cameraImg = (ImageView) findViewById(R.id.clickCamera1);
        ImageView galleryImg = (ImageView) findViewById(R.id.gallery1);

        cancel.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        cameraImg.setOnClickListener(this);
        galleryImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int clickedElement = v.getId();
        switch(clickedElement) {
            case R.id.cancel:       finish();
                                    break;
            case R.id.gallery1:
            case R.id.gallery2:     Intent intent = new Intent();
                                    intent.setType(IMAGE_TYPES);
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), GALLERY_CODE);
                                    break;
            case R.id.clickCamera1:
            case R.id.clickCamera2: Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, CAMERA_CODE);
                                    break;
            default:                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case GALLERY_CODE:
            case CAMERA_CODE:  if(resultCode==RESULT_OK) {
                            Uri selectedImageUri = data.getData();
                            Intent buffer = new Intent();
                            buffer.putExtra(AddExpense.IMAGE_URL_BUFFER_KEY, selectedImageUri.toString());
                            setResult(RESULT_OK, buffer);
                        }
                        else setResult(RESULT_CANCELED);
                        finish();
                        break;
            default:    break;
        }
    }
}
