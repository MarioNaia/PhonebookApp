package com.example.itsectorphonebook.contacts;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.itsectorphonebook.R;


public class PhonebookActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_phonebook);


        //Making sure we go always to ContactsFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = new ContactsFragment();
        fragmentTransaction.add(R.id.phonebookFragment, fragment);
        fragmentTransaction.commit();

    }


    /*
    * When we press button back on Android
    * */
    @Override
    public void onBackPressed(){

        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

}
