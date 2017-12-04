package group12.tcss450.uw.edu.appproject.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;

import group12.tcss450.uw.edu.appproject.R;

public class WebView extends AppCompatActivity {
    private android.webkit.WebView mWebView;
    private static String recipeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (android.webkit.WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(recipeUrl);
    }    /**
     * This is a helper method that will set the url to a new recipe.
     * @param theUrl string representing website containing recipe.
     */
    public static void setUrl(String theUrl){
        recipeUrl = theUrl;
    }


}
