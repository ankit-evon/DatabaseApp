package com.databaseapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    CreateDatabase db;
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText cityEditText;
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView cityTextView;
    private String name = "";
    private String age = "";
    private String city = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new CreateDatabase(MainActivity.this);
        initControls();
        fetchRecord();
    }


    /**
     * Defining widgets controls
     */
    private void initControls(){
        nameEditText = (EditText) findViewById(R.id.NameEditText);
        ageEditText = (EditText) findViewById(R.id.AgeEditText);
        cityEditText = (EditText) findViewById(R.id.CityEditText);
        nameTextView = (TextView) findViewById(R.id.NameTextView);
        ageTextView = (TextView) findViewById(R.id.AgeTextView);
        cityTextView = (TextView) findViewById(R.id.CityTextView);

        findViewById(R.id.SaveButton).setOnClickListener(buttonClick);
        findViewById(R.id.FetchButton).setOnClickListener(buttonClick);
        findViewById(R.id.DeleteButton).setOnClickListener(buttonClick);

        errorDismiss();
    }

    /**
     * Button click listeners
     */
    View.OnClickListener buttonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            hideKeyboard();
            int id = v.getId();
            if(id == R.id.SaveButton){
                saveButtonClick();
            }else if(id == R.id.FetchButton){
                fetchRecord();
            }else if (id == R.id.DeleteButton){
                deleteRecord();
            }

        }
    };

    /**
     * Getting values from edittext and saving them in database.
     */
    private void saveButtonClick(){
        String nameVal = nameEditText.getText().toString();
        String ageVal = ageEditText.getText().toString();
        String cityVal = cityEditText.getText().toString();
        if(nameVal.trim().length()==0){
            nameEditText.setError(getString(R.string.name_error));
        }else if(ageVal.trim().length()==0){
            ageEditText.setError(getString(R.string.age_error));
        }else if(cityVal.trim().length()==0){
            cityEditText.setError(getString(R.string.city_error));
        }else{
            db.open();
            db.createElementsDetail(nameVal, cityVal, ageVal);
            db.close();
            Toast.makeText(MainActivity.this, "Record saved successfully", Toast.LENGTH_SHORT).show();
            nameEditText.setText("");
            ageEditText.setText("");
            cityEditText.setText("");
            nameTextView.setText("");
            ageTextView.setText("");
            cityTextView.setText("");
        }
    }

    /**
     * Fetching record from database.
     */
    private void fetchRecord(){

        db.open();
        Cursor record = db.fetchDetail();
        record.moveToLast();
        int count = record.getCount();
        if(count ==0){
            Toast.makeText(MainActivity.this, "No record to show." , Toast.LENGTH_SHORT).show();
            nameTextView.setText("");
            ageTextView.setText("");
            cityTextView.setText("");
        }else {
            while (count > 0) {
                String name = record.getString(record.getColumnIndex("Name"));
                String age = record.getString(record.getColumnIndex("Age"));
                String city = record.getString(record.getColumnIndex("City"));
                nameTextView.setText(name);
                ageTextView.setText(age);
                cityTextView.setText(city);

                return;
            }
        }
        record.close();
        db.close();

    }

    /**
     * Deleting records based on keys(last entered record will be deleted)
     */
    private void deleteRecord(){
        db.open();
        Cursor record = db.fetchDetail();
        record.moveToLast();
        int count = record.getCount();
        if(count == 0){
            Toast.makeText(MainActivity.this, "No record to delete.", Toast.LENGTH_SHORT).show();
        }else {
            while (count > 0) {
                String key = record.getString(record.getColumnIndex("Key"));
                db.DeleteLastRecord(key);
                Toast.makeText(MainActivity.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                fetchRecord();            // Fetching records after deletion
                return;
            }

        }
        record.close();
        db.close();

    }

    /**
     * Validating blank fields
     */
    private void errorDismiss(){
        nameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                nameEditText.setError(null);
            }
        });

        ageEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                ageEditText.setError(null);
            }
        });

        cityEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                cityEditText.setError(null);
            }
        });
    }

    // Hide keyboard
    public void hideKeyboard() {
        //start with an 'always hidden' command for the activity's window
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //now tell the IMM to hide the keyboard FROM whatever has focus in the activity
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocusedView = getCurrentFocus();
        if(currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
        }
    }
}
