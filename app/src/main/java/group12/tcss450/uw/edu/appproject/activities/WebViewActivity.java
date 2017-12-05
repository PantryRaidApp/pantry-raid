package group12.tcss450.uw.edu.appproject.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import group12.tcss450.uw.edu.appproject.R;

public class WebViewActivity extends AppCompatActivity {
    private android.webkit.WebView mWebView;
    private static String recipeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (android.webkit.WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(recipeUrl);
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.addFavorite);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (MainActivity.addFavorite(recipeUrl))
                    Toast.makeText(getApplicationContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Unable to add to favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }    /**
     * This is a helper method that will set the url to a new recipe.
     * @param theUrl string representing website containing recipe.
     */
    public static void setUrl(String theUrl){
        recipeUrl = theUrl;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
