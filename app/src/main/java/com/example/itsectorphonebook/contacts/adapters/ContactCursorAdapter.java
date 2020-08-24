package com.example.itsectorphonebook.contacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itsectorphonebook.contacts.data.ContactContract.ContactEntry;

import com.example.itsectorphonebook.R;

public class ContactCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ContactCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ContactCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the contact data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current contact can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.phoneNumber);
        ImageView removeImageView = (ImageView) view.findViewById(R.id.removeImage);
        ImageView phoneCallImageView = (ImageView) view.findViewById(R.id.phoneCall);

        // Find the columns of contact attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_NAME);
        int phoneNumberColumnIndex = cursor.getColumnIndex(ContactEntry.COLUMN_CONTACT_PHONENUMBER);

        // Read the contact attributes from the Cursor for the current contact
        String contactName = cursor.getString(nameColumnIndex);
        int contactPhoneNumber = cursor.getInt(phoneNumberColumnIndex);


        // Update the TextViews with the attributes for the current contact
        nameTextView.setText(contactName);
        summaryTextView.setText(String.valueOf(contactPhoneNumber));

        final Context contextListener=context;
        final Cursor cursorListener=cursor;
        final int contactPhoneNumberListener=contactPhoneNumber;
        final int position = cursor.getPosition();


        phoneCallImageView.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View v) {
                  switch (v.getId()) {
                      case R.id.phoneCall:
                          Intent intent = new Intent(Intent.ACTION_DIAL);
                          intent.setData(Uri.parse("tel:"+contactPhoneNumberListener));
                          contextListener.startActivity(intent);

                          break;
                      default:
                          break;
                  }
              }
        });
        removeImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.removeImage:
                        cursorListener.moveToPosition(position);
                        Uri uriDelete = Uri.parse(ContactEntry.CONTENT_URI+"/"+String.valueOf(cursorListener.getLong(cursorListener.getColumnIndex(ContactEntry._ID))));
                        int rowsDeleted = contextListener.getContentResolver().delete(uriDelete, null, null);

                        // Show a toast message depending on whether or not the delete was successful.
                        if (rowsDeleted == 0) {
                            // If no rows were deleted, then there was an error with the delete.
                            Toast.makeText(v.getContext(), "Error deleting", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the delete was successful and we can display a toast.
                            Toast.makeText(v.getContext(), "Deleted...", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                    //i need to use the correct view from BindView here

                }
            }
        });
    }
}