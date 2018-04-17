package com.advse.universitybazaar.register;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.advse.universitybazaar.bean.Student;
import com.advse.universitybazaar.R;
import com.google.firebase.database.*;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Button b1;
    private EditText t1;
    private EditText t2;
    private EditText t3;
    private EditText t4;
    private TextInputLayout t1l;
    private TextInputLayout t2l;
    private TextInputLayout t3l;
    private TextInputLayout t4l;

    private String err = "";
    private boolean succsess;

    private static final String coDomain = "mavs.uta.edu";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + Pattern.quote(coDomain) + "$";
    private static final String PW_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%]).{8,20})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        t1 = (EditText) findViewById(R.id.t1);
        t2 = (EditText) findViewById(R.id.t2);
        t3 = (EditText) findViewById(R.id.t3);
        t4 = (EditText) findViewById(R.id.t4);
        t2l = (TextInputLayout) findViewById(R.id.t2l);
        t3l = (TextInputLayout) findViewById(R.id.t3l);
        t4l = (TextInputLayout) findViewById(R.id.t4l);

        t2l.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t2l.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        t3l.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t3l.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        t4l.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                t4l.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        succsess = false;

        b1 = (Button) findViewById(R.id.b1);
        //When reserve button is clicked
        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                doRegister();
            }
        });
    }

    public void doRegister(){

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(t3.getText().toString().trim());

        Pattern pattern2 = Pattern.compile(PW_PATTERN);
        Matcher matcher2 = pattern2.matcher(t4.getText().toString().trim());

        if(t1.getText().toString().trim().length() <= 0
                || t2.getText().toString().trim().length() <= 0
                || t3.getText().toString().trim().length() <= 0
                || t4.getText().toString().trim().length() <= 0){

            makeErrorSnack("Please enter all fields");
        }

        if(t2.getText().toString().trim().length() != 10)
            t2l.setError("MaverickID should be a 10 digit number");

        if(!matcher.matches())
            t3l.setError("Enter a valid UTA student email");

        if(!matcher2.matches())
            t4l.setError("Password must contain 8 characters, at least 1 digit, 1 alphabet and 1 special character from @#$%");

        //If no validation issues
        else{
            db = FirebaseDatabase.getInstance().getReference("Users");
            System.out.println("Connection created");

            db.child(t2.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);
                    if(student == null) {
                        sendEmail();
                        Student newStudent = new Student(t2.getText().toString().trim(),
                                t1.getText().toString().trim(),
                                t4.getText().toString().trim(),
                                t3.getText().toString().trim());

                        db.child(t2.getText().toString().trim()).setValue(newStudent);
                        setResult(RESULT_OK,null);
                        finish();
                    } else {
                        makeErrorSnack("A user with same MaverickID already exists in system");
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





        }

    }

    public void sendEmail(){
        //Toast.makeText(getApplicationContext(),"Email function called",Toast.LENGTH_LONG).show();
        new EmailActivity(this,t3.getText().toString().trim()).execute(t3.getText().toString().trim());

    }

    public void makeErrorSnack(String snackText) {
        Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_Registration),snackText,Snackbar.LENGTH_SHORT);
        snack.getView().setBackgroundColor(Color.parseColor("#B21919"));
        snack.show();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}