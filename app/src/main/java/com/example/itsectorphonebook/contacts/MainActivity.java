package com.example.itsectorphonebook.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itsectorphonebook.R;

//Source
//https://www.tutorialspoint.com/android/android_login_screen.htm
public class MainActivity extends AppCompatActivity  {


    Button b1;
    EditText ed1, ed2;
    EditText edt1, edt2,edt3,edt4;
    EditText firstET, secondET;

    private static final String TAG = "MainActivity";

    private ViewPager mViewPager;

    TextView tx1;
    int counter = 3;
    StringBuilder currentCode=new StringBuilder();
    StringBuilder trueCode=new StringBuilder("1121");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        Toast.makeText(this, "Activity Main", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);


        //Making sure we go always to LoginFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.mainFragment, fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed(){

        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

}