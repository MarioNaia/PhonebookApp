package com.example.itsectorphonebook.contacts.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.itsectorphonebook.contacts.data.ContactContract.ContactEntry;
public class ContactDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ContactDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "phonebook.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ContactDbHelper}.
     *
     * @param context of the app
     */
    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the contacts table
        String SQL_CREATE_CONTACTS_TABLE =  "CREATE TABLE " + ContactEntry.TABLE_NAME + " ("
                + ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ContactEntry.COLUMN_CONTACT_NAME + " TEXT NOT NULL, "
                + ContactEntry.COLUMN_CONTACT_EMAIL + " TEXT, "
                + ContactEntry.COLUMN_CONTACT_GENDER + " INTEGER NOT NULL, "
                + ContactEntry.COLUMN_CONTACT_PHONENUMBER + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}