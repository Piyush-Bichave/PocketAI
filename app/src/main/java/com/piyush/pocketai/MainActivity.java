package com.piyush.pocketai;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        sharedPreferences = getSharedPreferences("WebViewPreferences", MODE_PRIVATE);
        BottomNavigationView bottomview = findViewById(R.id.bottomNav);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                 if (itemId == R.id.item2) {
                    String profile = "https://www.linkedin.com/in/piyush-bichave-6b2529223";
                    openUrlInChrome(profile);
                } else if (itemId == R.id.item3) {
                    Toast.makeText(getApplicationContext(), "Coming Soon 🤗", Toast.LENGTH_SHORT).show();
                }else if (itemId == R.id.item4) {
                    openPopup();
                }else if (itemId == R.id.item5) {
                    openPopup1();
                }
                return true;
            }
        });

        if (isConnectedToInternet()) {
            replaceWithFragment(new ChatGpt());

            bottomview.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.bottomItem1) {
                    replaceWithFragment(new BardAI());
                } else if (itemId == R.id.bottomItem2) {
                    replaceWithFragment(new ChatGpt());
                }
                return true;
            });

        } else {
            replaceWithFragment(new NoInternet());
        }
    }

    private void openUrlInChrome(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "LinkedIn is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void replaceWithFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openPopup() {
        // Create a dialog instance
        Dialog popupDialog = new Dialog(this);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.popup_layout);
        // Find views within the popup layout
        TextView popupTitle = popupDialog.findViewById(R.id.imp);
        // Show the popup window
        popupDialog.show();
    }

    private void openPopup1() {
        // Create a dialog instance
        Dialog popupDialog = new Dialog(this);
        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.privacypolicy_popup);
        // Find views within the popup layout
        TextView popupTitle = popupDialog.findViewById(R.id.imp1);
        // Show the popup window
        popupDialog.show();
    }

}