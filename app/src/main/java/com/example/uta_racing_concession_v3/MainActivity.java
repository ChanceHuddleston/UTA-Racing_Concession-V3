package com.example.uta_racing_concession_v3;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.design.widget.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uta_racing_concession_v3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private EditText newIDNumInput, newNameInput, startingBalanceInput;
    public DBHandler dbHandler;
    private FloatingActionButton addAccountBtn,rmvAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_add_account, R.id.nav_view_accounts)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cent_button_pressed(View view){
        double total_value = 0.00;
        TextView totalValueText = (TextView) findViewById(R.id.total_value_text);
        total_value = Double.parseDouble(totalValueText.getText().toString());
        if(view.getId() == R.id.cent_25_button){
             total_value = total_value + 0.25;
        }
        if(view.getId() == R.id.cent_50_button){
            total_value = total_value + 0.50;
        }
        if(view.getId() == R.id.cent_75_button){
            total_value = total_value + 0.75;
        }
        if(view.getId() == R.id.cent_100_button){
            total_value = total_value + 1.00;
        }
        String total = Double.toString(total_value);
        totalValueText.setText(total);
    }

    public void clear_button_pressed(View view){
        double total_value = 0.00;
        TextView totalValueText = (TextView) findViewById(R.id.total_value_text);
        String total = Double.toString(total_value);
        if(view.getId() == R.id.clear_value_button){
            totalValueText.setText(total);
        }

    }
}