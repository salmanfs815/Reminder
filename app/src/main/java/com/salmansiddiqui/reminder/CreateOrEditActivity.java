package com.salmansiddiqui.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class CreateOrEditActivity extends AppCompatActivity implements View.OnClickListener {


    private ReminderDBHelper dbHelper;
    EditText taskEditText;
    EditText completeEditText;
    EditText remindEditText;
    EditText timeEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int reminderID;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reminderID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_edit);
        taskEditText = (EditText) findViewById(R.id.editTextTask);
        completeEditText = (EditText) findViewById(R.id.editTextComplete);
        remindEditText = (EditText) findViewById(R.id.editTextRemind);
        timeEditText = (EditText) findViewById(R.id.editTextTime);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ReminderDBHelper(this);

        if (reminderID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getReminder(Integer.toString(reminderID));
            rs.moveToFirst();
            String reminderTask = rs.getString(rs.getColumnIndex(ReminderDBHelper.REMINDER_COLUMN_TASK));
            String reminderComplete = rs.getString(rs.getColumnIndex(ReminderDBHelper.REMINDER_COLUMN_COMPLETE));
            String reminderRemind = rs.getString(rs.getColumnIndex(ReminderDBHelper.REMINDER_COLUMN_REMIND));
            String reminderTime = rs.getString(rs.getColumnIndex(ReminderDBHelper.REMINDER_COLUMN_TIME));

            if (!rs.isClosed()) {
                rs.close();
            }

            taskEditText.setText(reminderTask);
            taskEditText.setFocusable(false);
            taskEditText.setClickable(false);

            completeEditText.setText((CharSequence) reminderComplete);
            completeEditText.setFocusable(false);
            completeEditText.setClickable(false);

            remindEditText.setText((CharSequence) (reminderTime + ""));
            remindEditText.setFocusable(false);
            remindEditText.setClickable(false);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistPerson();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                taskEditText.setEnabled(true);
                taskEditText.setFocusableInTouchMode(true);
                taskEditText.setClickable(true);

                completeEditText.setEnabled(true);
                completeEditText.setFocusableInTouchMode(true);
                completeEditText.setClickable(true);

                remindEditText.setEnabled(true);
                remindEditText.setFocusableInTouchMode(true);
                remindEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteReminder)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteReminder(Integer.toString(reminderID));
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Person?");
                d.show();
                return;
        }
    }

    public void persistPerson() {
        if (reminderID > 0) {
            if (dbHelper.updateReminder(Integer.toString(reminderID), taskEditText.getText().toString(),
                    completeEditText.getText().toString(), remindEditText.getText().toString(),
                    timeEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (dbHelper.insertReminder(taskEditText.getText().toString(),
                    completeEditText.getText().toString(), remindEditText.getText().toString(),
                    timeEditText.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateOrEdit Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}