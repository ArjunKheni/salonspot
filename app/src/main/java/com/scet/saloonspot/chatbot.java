package com.scet.saloonspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;

import com.scet.saloonspot.R;

public class chatbot extends Activity {

    WebView web;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        web = (WebView) findViewById(R.id.webView1);
        web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("https://web-chat.global.assistant.watson.cloud.ibm.com/preview.html?region=au-syd&integrationID=1e44e74e-6ed6-4c62-ac6e-69d609fd241e&serviceInstanceID=94517b44-59a6-49bb-b002-df6ee80b334d3");
        web.setWebViewClient(new myWebClient());
        web.setWebChromeClient(new WebChromeClient());
        setContentView(web);
    }



    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);


        }
    }

    //flip screen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(web.canGoBack()){
                        web.goBack();
                    }
                    else
                    {
                        backButtonHandler();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                chatbot.this);


        // Setting Dialog Title
        // Setting Dialog Message

        alertDialog.setTitle("Do you Want to Exit Saloonbot");

        // I've included a simple dialog icon in my project named "dialog_icon", which's image file is copied and pasted in all "drawable" folders of "res" folders of the project. You can include any dialog image of your wish and rename it to dialog_icon.


        alertDialog.setMessage("Exit Now?");

        // Setting Icon to Dialog
        // Setting Positive "Yes" Button

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        // Setting Negative "NO" Button

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

}