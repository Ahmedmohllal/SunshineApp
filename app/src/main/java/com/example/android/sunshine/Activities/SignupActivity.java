package com.example.android.sunshine.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.sunshine.Network.RetrofitAuthSinglton;
import com.example.android.sunshine.R;
import com.example.android.sunshine.data.SharedPrefManager;
import com.example.android.sunshine.utilities.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText fnameEditText, lnameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneEditText;
    Button signup_btn;

    String firstName, lastName, email, password, Cpassword, mobile, Gplus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fnameEditText = (EditText) findViewById(R.id.FNAME);
        lnameEditText = (EditText) findViewById(R.id.LNAME);
        emailEditText = (EditText) findViewById(R.id.EMAIL);
        passwordEditText = (EditText) findViewById(R.id.PASSWROD);
        confirmPasswordEditText = (EditText) findViewById(R.id.CPASSWROD);
        phoneEditText = (EditText) findViewById(R.id.PHONE);

        signup_btn = (Button) findViewById(R.id.SIGNUP);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = fnameEditText.getText().toString();
                lastName = lnameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                mobile = phoneEditText.getText().toString();
                Gplus = "0";

                SignUp(firstName, lastName, email, password, mobile, Gplus);
            }
        });
    }


    private void SignUp(final String firstName, final String lastName, final String email, String password, final String mobile, String Gplus) {

        Call<ResponseBody> signupResponseCall = RetrofitAuthSinglton
                .getInstance()
                .getAPI()
                .register(firstName, lastName, email, mobile, password, Gplus);

        signupResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("registration_success")) {

                        UserModel userModel = new UserModel(email, firstName, lastName, mobile);
                        SharedPrefManager.getInstance(SignupActivity.this)
                                .saveUser(userModel);
                        Toast.makeText(SignupActivity.this, "Message is : " + message, Toast.LENGTH_LONG).show();
                        goToMainActivity();

                    } else {
                        //Toast.makeText(SignupActivity.this, "Message is : " + message, Toast.LENGTH_LONG).show();

                    }
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Error is : " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.v("SignupActivity", t.getMessage());
            }
        });

    }

    private void goToMainActivity() {
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(SignupActivity.this).checkReg()) {
            goToMainActivity();
        }
    }
}
