package com.advse.universitybazaar.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.advse.universitybazaar.bean.Student;
import com.advse.universitybazaar.R;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Button register;
    private Button loginButton;
    private EditText mavID;
    private EditText password;
    private Boolean loginSuccess;
    private static final int SIGNUP_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        SharedPreferences pref = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
        String currentPref =  pref.getString("mavID",null);
        if(currentPref != null) {
            Intent intent = new Intent(getApplicationContext(),UserHome.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login);
        mavID = (EditText) findViewById(R.id.mavID);
        password = (EditText) findViewById(R.id.password);

        //When user clicks on register button
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent registrationActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(registrationActivity,SIGNUP_REQUEST);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(requestCode == SIGNUP_REQUEST) {
            if(resultCode == RESULT_OK) {
                Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_Login),"User account successfully created",Snackbar.LENGTH_SHORT);
                snack.getView().setBackgroundColor(Color.parseColor("#298E10"));
                snack.show();
            } else if(resultCode == RESULT_CANCELED) {
                //Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void login() {

        mavID.clearFocus();
        password.clearFocus();

        if(!validate()) {
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String mID = mavID.getText().toString();
        final String pass = password.getText().toString();
        loginSuccess = false;

        db = FirebaseDatabase.getInstance().getReference("Users/" + mID);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                System.out.print(dataSnapshot);
                System.out.println("Datasnapshot captured");
                if(student != null && !("".equals(mID)) && !("".equals(pass)) && student.getPassword().equals(pass)) {
                    loginSuccess = true;
                    SharedPreferences.Editor preferences = getApplicationContext().getSharedPreferences("LOGIN_PREF",MODE_PRIVATE).edit();
                    preferences.putString("mavID",student.getMavID());
                    preferences.putString("name",student.getName());
                    preferences.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButton.setEnabled(true);
                if(loginSuccess) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                    Intent UserHome = new Intent(getApplicationContext(), UserHome.class);
                    startActivity(UserHome);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                    mavID.setText("");
                    password.setText("");
                }
            }
        },2000);
    }

    public Boolean validate() {
        boolean valid = true;

        final String mID = mavID.getText().toString();
        final String pass = password.getText().toString();

        if (mID.isEmpty() && pass.isEmpty()) {
            valid = false;
            makeErrorSnack("Please enter all fields");
            mavID.requestFocus();
        } else {
            if(mID.isEmpty()) {
                valid = false;
                makeErrorSnack("Please enter all fields");
                mavID.setError("Cannot be empty");
                mavID.requestFocus();
            } else if (pass.isEmpty()) {
                valid = false;
                makeErrorSnack("Please enter all fields");
                password.setError("Cannot be empty");
                password.requestFocus();
            }
        }

        return valid;
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void makeErrorSnack(String snackText) {
        Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_Login),snackText,Snackbar.LENGTH_SHORT);
        snack.getView().setBackgroundColor(Color.parseColor("#B21919"));
        snack.show();
    }
}
