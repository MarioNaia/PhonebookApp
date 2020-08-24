package com.example.itsectorphonebook.contacts;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.itsectorphonebook.R;
import com.example.itsectorphonebook.contacts.data.ContactContract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorFragment newInstance(String param1, String param2) {
        EditorFragment fragment = new EditorFragment();
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


    /** Identifier for the contact data loader */
    private static final int EXISTING_CONTACT_LOADER = 0;

    /** Content URI for the existing contact (null if it's a new contact) */
    private Uri mCurrentContactUri;

    /** EditText field to enter the contacts's name */
    private EditText mNameEditText;

    /** EditText field to enter the contacts's email */
    private EditText mEmailEditText;

    /** EditText field to enter the contacts's phonenumber */
    private EditText mPhoneNumberEditText;

    /** EditText field to enter the contacts's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the contact. The possible valid values are in the contactContract.java file:
     * {@link ContactContract.ContactEntry#GENDER_UNKNOWN}, {@link ContactContract.ContactEntry#GENDER_MALE}, or
     * {@link ContactContract.ContactEntry#GENDER_FEMALE}.
     */
    private int mGender = ContactContract.ContactEntry.GENDER_UNKNOWN;

    /** Boolean flag that keeps track of whether the contact has been edited (true) or not (false) */
    private boolean mContactHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mcontactHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mContactHasChanged = true;
            return false;
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_editor, container, false);
        if (container != null) {
            container.removeAllViews();
        }
        setHasOptionsMenu(true);
        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new contact or editing an existing one.
        //Intent intent = getIntent();



        Bundle bundle = this.getArguments();



        // handle your code here.
        String uriContact=bundle.getString("uri1");


        // If bundle has string 0 means is new contact
        if (uriContact == "0") {
            // This is a new contact, so change the app bar to say "Add a contact"
            getActivity().setTitle(getString(R.string.editor_activity_title_new_contact));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a contact that hasn't been created yet.)
            getActivity().invalidateOptionsMenu();
        } else {
            mCurrentContactUri = Uri.parse(uriContact);
            // Otherwise this is an existing contact, so change app bar to say "Edit contact"
            getActivity().setTitle(getString(R.string.editor_activity_title_edit_contact));

            // Initialize a loader to read the contact data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_CONTACT_LOADER, null, this);

        }



        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) view.findViewById(R.id.edit_contact_name);
        mEmailEditText = (EditText) view.findViewById(R.id.edit_contact_email);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.edit_contact_phonenumber);
        mGenderSpinner = (Spinner) view.findViewById(R.id.spinner_gender);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mPhoneNumberEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);




        setupSpinner();

        return view;


    }


    /**
     * Setup the dropdown spinner that allows the user to select the gender of the contact.
     */
    private void setupSpinner() {

        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = ContactContract.ContactEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = ContactContract.ContactEntry.GENDER_FEMALE;
                    } else {
                        mGender = ContactContract.ContactEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = ContactContract.ContactEntry.GENDER_UNKNOWN;
            }
        });


    }

    /**
     * Get user input from editor and save contact into database.
     */
    private void saveContact() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mEmailEditText.getText().toString().trim();
        String weightString = mPhoneNumberEditText.getText().toString().trim();

        // Check if this is supposed to be a new contact
        // and check if all the fields in the editor are blank
        if (mCurrentContactUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
                TextUtils.isEmpty(weightString) && mGender == ContactContract.ContactEntry.GENDER_UNKNOWN) {
            // Since no fields were modified, we can return early without creating a new contact.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and contact attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_NAME, nameString);
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL, breedString);
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_GENDER, mGender);
        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int weight = 0;
        if (!TextUtils.isEmpty(weightString)) {
            weight = Integer.parseInt(weightString);
        }
        values.put(ContactContract.ContactEntry.COLUMN_CONTACT_PHONENUMBER, weight);

        // Determine if this is a new or existing contact by checking if mCurrentcontactUri is null or not
        if (mCurrentContactUri == null) {
            // This is a NEW contact, so insert a new contact into the provider,
            // returning the content URI for the new contact.

            Uri newUri = getActivity().getApplicationContext().getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(getActivity(), getString(R.string.editor_insert_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(getActivity(), getString(R.string.editor_insert_contact_successful),
                       Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING contact, so update the contact with content URI: mCurrentcontactUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentcontactUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getActivity().getApplicationContext().getContentResolver().update(mCurrentContactUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(getActivity(), getString(R.string.editor_update_contact_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(getActivity(), getString(R.string.editor_update_contact_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("TAG2", "onCreateOptionsMenu");
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        // Cleans the former fragment menus
        menu.clear();
        menu.clear();
        inflater.inflate(R.menu.menu_editor, menu);
        super.onCreateOptionsMenu(menu, inflater);






    }


    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        // If this is a new contact, hide the "Delete" menu item.
        if (mCurrentContactUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save contact to database
                saveContact();
                // Exit activity
                getActivity().onBackPressed();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the contact hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mContactHasChanged) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(getActivity());
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // Since the editor shows all contact attributes, define a projection that contains
        // all columns from the contact table
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_CONTACT_NAME,
                ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL,
                ContactContract.ContactEntry.COLUMN_CONTACT_GENDER,
                ContactContract.ContactEntry.COLUMN_CONTACT_PHONENUMBER };



        Log.d("TAG2", "String é    "+String.valueOf(mCurrentContactUri));
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                mCurrentContactUri,         // Query the content URI for the current contact
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order


    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of contact attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_NAME);
            int emailColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_EMAIL);
            int genderColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_GENDER);
            int phonenumberColumnIndex = cursor.getColumnIndex(ContactContract.ContactEntry.COLUMN_CONTACT_PHONENUMBER);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(phonenumberColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mEmailEditText.setText(email);
            mPhoneNumberEditText.setText(Integer.toString(weight));

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (gender) {
                case ContactContract.ContactEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case ContactContract.ContactEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mEmailEditText.setText("");
        mPhoneNumberEditText.setText("");
        mGenderSpinner.setSelection(0); // Select "Unknown" gender
    }


    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the contact.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this contact.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the contact.
                deleteContact();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the contact.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the contact in the database.
     */
    private void deleteContact() {

        // Only perform the delete if this is an existing contact.
        if (mCurrentContactUri != null) {

            // Call the ContentResolver to delete the contact at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentcontactUri
            // content URI already identifies the contact that we want.
            int rowsDeleted = getActivity().getApplicationContext().getContentResolver().delete(mCurrentContactUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(getActivity(), getString(R.string.editor_delete_contact_failed),
                       Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(getActivity(), getString(R.string.editor_delete_contact_successful),
                       Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        //finish();
        getActivity().onBackPressed();
    }


}
