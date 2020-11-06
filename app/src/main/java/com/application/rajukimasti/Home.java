package com.application.rajukimasti;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import pl.droidsonroids.gif.GifImageView;

public class Home extends AppCompatActivity {


    private final static int HOME_ID = 1;
    private final static int LOGIN_ID = 2;
    private final static int YOUTUBE_ID = 3;
    private final static int CONTACT_ID = 4;


    MeowBottomNavigation navigation;

    Animation topAnim, bottomAnim;
    ImageView img;
    GifImageView gifImageView;

    private static final int SPLASH_TIMER = 4500;
    Button retryButton;
    RelativeLayout noInternetLayout;
    ConstraintLayout splashLayout, webViewLayout;

    WebView webView1, webView2, webView3, webView4;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    ProgressBar progressBarWeb1;
    SwipeRefreshLayout RefreshLayout;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    public String url1 = "https://www.rajukimasti.com";
    public String url2 = "https://www.rajukimasti.com/login";
    public String url3 = "https://www.rajukimasti.com/contactus";
    public String url4 = "https://www.youtube.com/channel/UCkeHNitFPxR3el1xmqGi2ug";


    @Override
    protected void onStart() {
        super.onStart();

        if (webView1.getUrl() == null) {
            webView1.loadUrl(url1);
        }

        RefreshLayout.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener =
                new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (webView1.getVisibility() == View.VISIBLE) {
                            if (webView1.getScrollY() == 0)
                                RefreshLayout.setEnabled(true);
                            else
                                RefreshLayout.setEnabled(false);
                        } else if (webView2.getVisibility() == View.VISIBLE) {
                            if (webView2.getScrollY() == 0)
                                RefreshLayout.setEnabled(true);
                            else
                                RefreshLayout.setEnabled(false);
                        } else if (webView3.getVisibility() == View.VISIBLE) {
                            if (webView3.getScrollY() == 0)
                                RefreshLayout.setEnabled(true);
                            else
                                RefreshLayout.setEnabled(false);
                        } else if (webView4.getVisibility() == View.VISIBLE) {
                            if (webView4.getScrollY() == 0)
                                RefreshLayout.setEnabled(true);
                            else
                                RefreshLayout.setEnabled(false);
                        }


                    }
                });
    }


    @Override
    protected void onStop() {
        RefreshLayout.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inItViews();


        int DELAY = 3500;
        Handler h1 = new Handler();
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView2.loadUrl(url2);
                webView3.loadUrl(url3);
                webView4.loadUrl(url4);
            }
        }, DELAY);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                onStartConnection();
                /*splashLayout.setVisibility(View.GONE);
                 */
                webView1.setVisibility(View.VISIBLE);
                webView2.loadUrl(url2);
                webView3.loadUrl(url3);
                webView4.loadUrl(url4);
            }
        }, SPLASH_TIMER);


        //Retry Button when internet Conncetion is lost
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Checking Internet Connection...", Toast.LENGTH_SHORT).show();

                checkConnection();
            }
        });

        RefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);


        //Swipe to Refresh Action
        RefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connection();
                if (webView1.getVisibility() == View.VISIBLE) {
                    webView1.reload();
                } else if (webView2.getVisibility() == View.VISIBLE) {
                    webView2.reload();
                } else if (webView3.getVisibility() == View.VISIBLE) {
                    webView3.reload();
                } else if (webView4.getVisibility() == View.VISIBLE) {
                    webView4.reload();
                }

            }
        });


        navigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });


        navigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });



// Action when navigation buttons are pressed.....
        navigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case HOME_ID:
                        connection();
                        webView1.setVisibility(View.VISIBLE);
                        webView2.setVisibility(View.GONE);
                        webView3.setVisibility(View.GONE);
                        webView4.setVisibility(View.GONE);

                        break;

                    case LOGIN_ID:
                        connection();
                        webView2.setVisibility(View.VISIBLE);
                        webView1.setVisibility(View.GONE);
                        webView3.setVisibility(View.GONE);
                        webView4.setVisibility(View.GONE);
                        break;

                    case YOUTUBE_ID:
                        connection();
                        webView3.setVisibility(View.VISIBLE);
                        webView2.setVisibility(View.GONE);
                        webView1.setVisibility(View.GONE);
                        webView4.setVisibility(View.GONE);
                        break;

                    case CONTACT_ID:
                        connection();
                        webView4.setVisibility(View.VISIBLE);
                        webView2.setVisibility(View.GONE);
                        webView3.setVisibility(View.GONE);
                        webView1.setVisibility(View.GONE);
                        break;
                }
            }
        });

        webView1.setWebViewClient(new WebViewClient() {

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          RefreshLayout.setRefreshing(false);
                                          super.onPageFinished(view, url);
                                      }
                                  }
        );

        webView2.setWebViewClient(new WebViewClient() {

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          RefreshLayout.setRefreshing(false);
                                          super.onPageFinished(view, url);
                                      }
                                  }
        );

        webView3.setWebViewClient(new WebViewClient() {

                                      @Override
                                      public void onPageFinished(WebView view, String url) {

                                          RefreshLayout.setRefreshing(false);
                                          super.onPageFinished(view, url);
                                      }
                                  }
        );

        webView4.setWebViewClient(new WebViewClient() {

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          RefreshLayout.setRefreshing(false);
                                          super.onPageFinished(view, url);
                                      }
                                  }
        );


        webView1.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (webViewLayout.getVisibility() == View.VISIBLE) {

                    connection();
                    progressBarWeb1.setVisibility(View.VISIBLE);

                }

                progressBarWeb1.setProgress(newProgress);

                if (newProgress == 100) {
                    progressBarWeb1.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }


            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]>
                    filePathCallback, FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String
                    acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

        });

        webView2.setWebChromeClient(new WebChromeClient() {
                                        @Override

                                        public void onProgressChanged(WebView view, int newProgress) {

                                            if (webViewLayout.getVisibility() == View.VISIBLE) {

                                                connection();
                                                progressBarWeb1.setVisibility(View.VISIBLE);

                                            }
                                            progressBarWeb1.setVisibility(View.VISIBLE);
                                            progressBarWeb1.setProgress(newProgress);
                                            if (newProgress == 100) {
                                                progressBarWeb1.setVisibility(View.GONE);
                                            }
                                            super.onProgressChanged(view, newProgress);
                                        }


                                    }
        );

        webView3.setWebChromeClient(new WebChromeClient() {
                                        @Override
                                        public void onProgressChanged(WebView view, int newProgress) {

                                            if (webViewLayout.getVisibility() == View.VISIBLE) {

                                                connection();
                                                progressBarWeb1.setVisibility(View.VISIBLE);

                                            }
                                            progressBarWeb1.setVisibility(View.VISIBLE);
                                            progressBarWeb1.setProgress(newProgress);
                                            if (newProgress == 100) {
                                                progressBarWeb1.setVisibility(View.GONE);
                                            }
                                            super.onProgressChanged(view, newProgress);
                                        }


                                    }
        );

        webView4.setWebChromeClient(new WebChromeClient() {
                                        @Override
                                        public void onProgressChanged(WebView view, int newProgress) {


                                            if (webViewLayout.getVisibility() == View.VISIBLE) {

                                                connection();
                                                progressBarWeb1.setVisibility(View.VISIBLE);

                                            }
                                            progressBarWeb1.setVisibility(View.VISIBLE);
                                            progressBarWeb1.setProgress(newProgress);
                                            if (newProgress == 100) {
                                                progressBarWeb1.setVisibility(View.GONE);
                                            }
                                            super.onProgressChanged(view, newProgress);
                                        }


                                    }
        );


    }


    // initialize views
    public void inItViews()
    {
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_up_anim);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_down_anim);

        splashLayout = findViewById(R.id.splashLayout);

        img = findViewById(R.id.logoImg);
        gifImageView = findViewById(R.id.gifImageView);

        gifImageView.setAnimation(bottomAnim);
        img.setAnimation(topAnim);


        navigation = findViewById(R.id.bottomNavigation);
        navigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        navigation.add(new MeowBottomNavigation.Model(2, R.drawable.login));
        navigation.add(new MeowBottomNavigation.Model(3, R.drawable.contact));
        navigation.add(new MeowBottomNavigation.Model(4, R.drawable.yotube));


        RefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBarWeb1 = findViewById(R.id.progressbar1);
        getSupportActionBar().hide();


        retryButton = findViewById(R.id.retry_Btn);
        noInternetLayout = findViewById(R.id.no_Connection_layout);
        webViewLayout = findViewById(R.id.webViewLayout);
        webView1 = findViewById(R.id.myWebView1);
        webView2 = findViewById(R.id.myWebView2);
        webView3 = findViewById(R.id.myWebView3);
        webView4 = findViewById(R.id.myWebView4);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setJavaScriptEnabled(true);
        webView3.getSettings().setJavaScriptEnabled(true);
        webView4.getSettings().setJavaScriptEnabled(true);
    }



    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != Home.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }


    }


    private class xWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (webView1.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView1.canGoBack()) {
                webView1.goBack();
                return true;
            }

        } else if (webView2.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView2.canGoBack()) {
                webView2.goBack();
                return true;
            }

        } else if (webView3.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView3.canGoBack()) {
                webView3.goBack();
                return true;
            }

        } else if (webView4.getVisibility() == View.VISIBLE) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView4.canGoBack()) {
                webView4.goBack();
                return true;
            }

        } else {

            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }


    boolean doubleBackToExitPressedOnce = false;



// Action on back button Pressed
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void onStartConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {

            if (splashLayout.getVisibility() == View.VISIBLE) {
                splashLayout.setVisibility(View.GONE);
                webViewLayout.setVisibility(View.VISIBLE);
            }

        } else {
            checkConnection();
        }
    }

    //Wifi and mobile network connection checking........
    public void connection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {

            // my code

        } else {
            checkConnection();
        }
    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (wifi.isConnected() || mobile.isConnected()) {
            String w1 = webView1.getUrl();
            String w2 = webView2.getUrl();
            String w3 = webView3.getUrl();
            String w4 = webView4.getUrl();
            webView1.loadUrl(w1);
            webView2.loadUrl(w2);
            webView3.loadUrl(w3);
            webView4.loadUrl(w4);
            int DELAY = 2000;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    splashLayout.setVisibility(View.GONE);
                    noInternetLayout.setVisibility(View.GONE);
                    webViewLayout.setVisibility(View.VISIBLE);
                }
            }, DELAY);


        } else {
            splashLayout.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
            webViewLayout.setVisibility(View.GONE);
        }


    }


}







