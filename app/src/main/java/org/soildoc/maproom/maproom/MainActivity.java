package org.soildoc.maproom.maproom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    Animation animationfadein;
    ImageButton logoImage;
    Button btnLocation, btnMapRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        logoImage = (ImageButton) findViewById(R.id.logo);

        animationfadein = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logoImage.startAnimation(animationfadein);
        logoImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LocationActivity.class));
            }
        });

        /*

        btnLocation = (Button) findViewById(R.id.btnLocation);
        btnMapRoom = (Button) findViewById(R.id.btnMapRoom);

        btnLocation.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), LocationActivity.class));
           }
        });

        btnMapRoom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent appBrowserIntent = new Intent(getApplicationContext(), Web.class);
                startActivity(appBrowserIntent);
            }
        });
*/
    }


}
