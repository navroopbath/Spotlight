package com.example.chen.presentationproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditPrezi extends AppCompatActivity {

    private ImageButton editPreziImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_prezi);
        editPreziImage = (ImageButton) findViewById(R.id.editPrezi);
        Intent preziIntent = getIntent();
        editPreziImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Image", Toast.LENGTH_LONG).show();
                Intent goToStats = new Intent(EditPrezi.this, statsPrezi.class);
                EditPrezi.this.startActivity(goToStats);
            }
        });
    }

}
