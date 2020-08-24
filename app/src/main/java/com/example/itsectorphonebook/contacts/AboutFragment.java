package com.example.itsectorphonebook.contacts;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.itsectorphonebook.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "AboutFragment";

    private Button btnMainActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button b1,b2,b3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_about, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        b1 = (Button)view.findViewById(R.id.btnLinkedin);
        b2 = (Button)view.findViewById(R.id.btnInstagram);
        b3 = (Button)view.findViewById(R.id.btnFacebook);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Opens linkedin account
                 *
                 */
                //android:text="https://www.linkedin.com/in/marionaia/"
                //android:text="https://www.instagram.com/marionaia/"
                //android:text="https://www.facebook.com/mario.ferreira.754"

                String profile_url = "https://www.linkedin.com/in/marionaia";
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(profile_url));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(profile_url)));
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Opens Instagram account
                 *
                 */


                Uri uri = Uri.parse("https://www.instagram.com/marionaia/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/marionaia")));
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Opens facebook account
                 *
                 */

               String FACEBOOK_URL = "https://www.facebook.com/mario.ferreira.754";
               String FACEBOOK_PAGE_ID = "mario.ferreira.754";

                PackageManager packageManager = getActivity().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);
                    } else { //older versions of fb app
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = "fb://page/" + FACEBOOK_PAGE_ID;
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                }

            }
        });


        return view;
    }
}
