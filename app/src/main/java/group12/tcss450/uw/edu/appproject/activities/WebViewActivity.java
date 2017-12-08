package group12.tcss450.uw.edu.appproject.activities;

import android.net.http.SslError;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import static group12.tcss450.uw.edu.appproject.activities.MainActivity.getAllFavorites;

import group12.tcss450.uw.edu.appproject.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{
    private android.webkit.WebView mWebView;
    private static String recipeUrl;
    FloatingActionButton myFab;

    private static final String ARG_PARAM = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            recipeUrl = bundle.getString(ARG_PARAM);
        }
        myFab = (FloatingActionButton) findViewById(R.id.addFavorite);
        WebView myWebView = (WebView) findViewById(R.id.webView);

        //Support for JavaScript websites
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Ensures the URL won't open in an external browser
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl(recipeUrl);

        myFab.setAlpha(0.9f);
        myFab.setOnClickListener(this);

        if (MainActivity.isAFavorite(recipeUrl)) {
            myFab.setImageResource(R.drawable.is_favorite);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        String[] favs = getAllFavorites();
        for (String s: favs) {
            Log.d("FAVS", s);
        }

        if (MainActivity.isAFavorite(recipeUrl)) {
            Log.d("isAfavorite", recipeUrl);
            if (!MainActivity.deleteFavorite(recipeUrl))
                Toast.makeText(getApplicationContext(), "Unable to remove from favorites.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Removed from favorites!", Toast.LENGTH_SHORT).show();
        } else {
            if (MainActivity.addFavorite(recipeUrl)) {
                Toast.makeText(getApplicationContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
                myFab.setImageResource(R.drawable.is_favorite);
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to add to favorites.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
