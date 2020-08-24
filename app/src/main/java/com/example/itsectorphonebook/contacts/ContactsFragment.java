package com.example.itsectorphonebook.contacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.itsectorphonebook.R;
import com.example.itsectorphonebook.contacts.adapters.ContactCursorAdapter;
import com.example.itsectorphonebook.contacts.data.ContactContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /** Identifier for the contact data loader */
    private final static int CONTACT_LOADER_NORMAL = 0;
    private final static int CONTACT_LOADER_ASC = 1;
    private final static int CONTACT_LOADER_DESC = 2;
    /** Adapter for the ListView */
    ContactCursorAdapter mCursorAdapter;
    Button b1,b2;

    //source https://stackoverflow.com/questions/58070258/how-to-pass-data-from-fragment-to-another-fragment
    public ContactsFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_contacts, container, false);


        if (container != null) {
            container.removeAllViews();
        }

        getActivity().setTitle("Phonebook");
        setHasOptionsMenu(true);

        b1 = (Button) view.findViewById(R.id.btnAscendingOrder);
        b2 = (Button) view.findViewById(R.id.btnDescendingOrder);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                //Send 0 in string value to mark as new contact to be created
                bundle.putString("uri1","0");
                Fragment fragment = new EditorFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.phonebookFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Find the ListView which will be populated with the contact data
        ListView contactListView = (ListView) view.findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = view.findViewById(R.id.empty_view);
        contactListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of contact data in the Cursor.
        // There is no contact data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ContactCursorAdapter(getContext(), null);
        contactListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



                Fragment fragment = new EditorFragment();
                Bundle bundle = new Bundle();

                // Form the content URI that represents the specific contact that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link contactEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.contacts/contacts/2"
                // if the contact with ID 2 was clicked on.

                Uri currentContactUri = ContentUris.withAppendedId(ContactContract.ContactEntry.CONTENT_URI, id);
                String uri_to_string= currentContactUri.toString();
                Log.d("Tag1",uri_to_string );
                Uri mCurrentContactUri = Uri.parse(uri_to_string);
                Log.d("Tag1", String.valueOf(mCurrentContactUri));

                bundle.putString("uri1",uri_to_string); // Put anything what you want

                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.phonebookFragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



                // Create new intent to go to {@link EditorActivity}
                //Intent intent = new Intent(getActivity(), EditorActivity.class);




                // Set the URI on the data field of the intent
                //intent.setData(currentContactUri);

                // Launch the {@link EditorActivity} to display the data for the current contact.
                //startActivity(intent);
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLoaderManager().restartLoader(CONTACT_LOADER_ASC, null, mMyCallback);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(CONTACT_LOADER_DESC, null, mMyCallback);
            }
        });
        // Kick off the loader

        getLoaderManager().initLoader(CONTACT_LOADER_NORMAL, null, mMyCallback);
        return view;


    }

    /**
     * Helper method to insert hardcoded contact data into the database. For debugging purposes only.
     */
    private void insertContact() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's contact attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, "Mila");
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL, "milamaria@gmail.com");
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_GENDER, ContactContract.ContactEntry.GENDER_MALE);
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_PHONENUMBER, 914240810);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link contactEntry#CONTENT_URI} to indicate that we want to insert
        // into the contacts database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.

        Uri newUri = getActivity().getApplicationContext().getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI, values);
    }



    /**
     * Helper method to delete all contacts in the database.
     */
    private void deleteAllContacts() {
        int rowsDeleted = getActivity().getApplicationContext().getContentResolver().delete(ContactContract.ContactEntry.CONTENT_URI, null, null);
        Log.v("ContactsActivity", rowsDeleted + " rows deleted from contact database");
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.menu_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_insert_dummy_data:
                insertContact();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllContacts();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }





    private LoaderManager.LoaderCallbacks<Cursor> mMyCallback = new  LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            // Define a projection that specifies the columns from the table we care about.
            String[] projection = {
                    ContactContract.ContactEntry._ID,
                    ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                    ContactContract.ContactEntry.COLUMN_CONTACT_PHONENUMBER};


            if (id == 1) {
                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(getActivity(),   // Parent activity context
                        ContactContract.ContactEntry.CONTENT_URI,   // Provider content URI to query
                        projection,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        ContactContract.ContactEntry.COLUMN_CONTACT_NAME + " DESC");                  // Default sort order
            }
            if (id == 2) {
                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(getActivity(),   // Parent activity context
                        ContactContract.ContactEntry.CONTENT_URI,   // Provider content URI to query
                        projection,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        ContactContract.ContactEntry.COLUMN_CONTACT_NAME + " ASC");                  // Default sort order
            }
            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(getActivity(),   // Parent activity context
                    ContactContract.ContactEntry.CONTENT_URI,   // Provider content URI to query
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);                  // Default sort order
            //return null;
        }

        @Override
        public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor data) {
            // Update {@link ContactCursorAdapter} with this new cursor containing updated contact data
            mCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
            // Callback called when the data needs to be deleted
            mCursorAdapter.swapCursor(null);
        }

    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Update {@link ContactCursorAdapter} with this new cursor containing updated contact data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }


}
