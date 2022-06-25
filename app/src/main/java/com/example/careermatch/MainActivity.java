package com.example.careermatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.careermatch.fragments.HomeFragment;
import com.example.careermatch.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager()  ;
    FrameLayout flContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        flContainer = findViewById(R.id.flContainer);

        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_compose:
                        Toast.makeText(MainActivity.this,"Roadmap",Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_home);
    }
    //TODO 1) add filter home view, add search, add log out button

}