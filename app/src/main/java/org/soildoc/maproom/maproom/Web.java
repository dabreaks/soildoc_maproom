package org.soildoc.maproom.maproom;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class Web extends AppCompatActivity {

    private static final String MAIN_WEBPAGE = "http://iridl.ldeo.columbia.edu/org.org.soildoc.maproom.org.org.soildoc.maproom/";

    Bundle extras;
    WebView webViewer;
    TextView url_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        extras = getIntent().getExtras();

        webViewer = (WebView) findViewById(R.id.webView);
        //webViewer.getSettings().setJavaScriptEnabled(true);
        url_bar = (TextView) findViewById(R.id.url);
    }

    @Override
    protected void onStart () {
        super.onStart();
        String url;

        if (extras != null) {
            url = extras.getString("url");
        } else {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("No GPS coordinates were included. Heading to the org.org.soildoc.maproom.org.org.soildoc.maproom main page")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            myAlert.show();
            url = MAIN_WEBPAGE;
        }

        webViewer.loadUrl(url);
        url_bar.setText(url);

    }

}
