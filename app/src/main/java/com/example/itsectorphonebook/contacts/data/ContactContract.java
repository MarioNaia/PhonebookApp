package com.example.itsectorphonebook.contacts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Phonebook app.
 */

//source: udacity database android basics degree
public final class ContactContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ContactContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.itsectorphonebook.contacts";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * content://com.example.android.contacts/contacts/ is a valid path
     */
    public static final String PATH_CONTACTS = "contacts";

    /**
     * Inner class that defines constant values for the contacts database table.
     * Each entry in the table represents a single contact.
     */
    public static final class ContactEntry implements BaseColumns {

        /** The content URI to access the contact data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CONTACTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of contacts.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single contact.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACTS;

        /** Name of database table for contacts */
        public final static String TABLE_NAME = "contacts";

        /**
         * Unique ID number for the contact (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the contact.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CONTACT_NAME ="name";

        /**
         * PhoneNumber of the contact.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CONTACT_PHONENUMBER = "number";

        /**
         * Gender of the contact.
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_CONTACT_GENDER = "gender";

        /**
         * Email of the contact.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_CONTACT_EMAIL = "email";


        /**
         * Possible values for the gender of the contact.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         */
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }

}
