package com.shameme.chobbish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView chobbishView;
    String currentURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declaring all the elements of the splash screen
        final ConstraintLayout SplashView = findViewById(R.id.SplashPanel);
        final TextView SplashTitle = findViewById(R.id.SplashTitle);
        final TextView SplashSubTitle = findViewById(R.id.SplashSubTitle);
        final TextView MadeWithLove = findViewById(R.id.MadeWithLove);
        final Animation ZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);

        //by this point the SplashTitle (CHOBBISH) is visible on screen

        //two instances of the ZoomOut Animation, Because One Object to animate two TextView made the animation misbehave
        Animation ZoomOut1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomout);
        SplashSubTitle.startAnimation(ZoomOut1);
        Animation SlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidedown);
        MadeWithLove.startAnimation(SlideDown);

        //by this point the SubTitle Texts have Animated (Zoomed out) on the screen
        //the user can see the Title and SubTitles

        //Loads the webpage while the splashscreen is still visible. and animation not finished yet.
        //this will help  take the user straight to the content as soon as the animations are complete.
        chobbishView = (WebView) findViewById(R.id.chobbishview);
        //navigate thru pages within app using the web client.
        chobbishView.setWebViewClient(new WebClient());
        //javascript is necessary for the site to function well
        chobbishView.getSettings().setJavaScriptEnabled(true);
        //Will start loading cached data but also look forward to updating the cache.
        chobbishView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //this is our site
        chobbishView.loadUrl("https://sites.google.com/view/24csedu/");

        //this thread waits for 5 seconds till the webpage loads in the background
        //after 5 seconds, the whole splashScreen (SplashPanel) will Animate (Zoom out) out of the screen and...
        //...and finally the SplashScreen Disappears (View.GONE)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashView.startAnimation(ZoomIn);
                SplashView.setVisibility(View.GONE);
            }
        },5000);

    }

    //the webClient class, allows the app to navigate thru pages within the site inside the WebView
    //constrained to load only pages from our site
    //any external links will open thru external default apps
    private class WebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView chobbishView, String request) {
            currentURL = request;
            //allowing to load only if requested url is from our site.
            if(currentURL.contains("sites.google.com/view/24csedu"))
                chobbishView.loadUrl(request);
            //handling external links that's not a page of our site.
            else startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(request)));
            return true;
        }
    }

    //by default, pressing the backButton exits the app, we no want dis.
    //if the webView can go back, it does. otherwise in the HomePage press back twice within a second to exit
    boolean backPressedTwice = false;
    @Override
    public void onBackPressed() {

        if(chobbishView.canGoBack()){
            chobbishView.goBack();
        }
        else{
            if(backPressedTwice){
                super.onBackPressed();
                return;
            }
            this.backPressedTwice = true;
            Toast.makeText(this, "Press Back Button Twice to Exit App", Toast.LENGTH_SHORT).show();
            //if back is not pressed within the next 1000ms, The app doesn't count double press
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedTwice = false;
                }
            },1000);
        }
    }
}