package com.example.tecmry.turing.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.tecmry.turing.R;

public class register extends AppCompatActivity {
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsernameView=(AutoCompleteTextView)findViewById(R.id.username);
        mPasswordView=(EditText)findViewById(R.id.password);
        mEmailView = (EditText)findViewById(R.id.email);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id==R.id.register || id== EditorInfo.IME_NULL){
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        Button mUsernameSignIn = (Button)findViewById(R.id.username_register_button);
        mUsernameSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView=findViewById(R.id.register_progress);
    }
    private void attemptRegister(){
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mEmailView.setError(null);
        String username=mUsernameView.getText().toString();
        String passwords = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(!TextUtils.isEmpty(passwords)&&!isPasswordValid(passwords)){
            mPasswordView.setError("密码大于四位");
            focusView = mPasswordView;
            cancel = true;
        }

        if(TextUtils.isEmpty(username)){
            mUsernameView.setError("这个是必填项");
            focusView = mUsernameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)){
            mEmailView.setError("忘记填邮箱了噢");
            focusView = mEmailView;
            cancel = true;
        }
        if(cancel)
        {
            focusView.requestFocus();
        }else {
            showProgress(true);
            AVUser user = new AVUser();
            user.setUsername(username);
            user.setPassword(passwords);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if(e == null){
                        startActivity(new Intent(register.this,MainActivity.class));
                        register.this.finish();
                    }else {
                        showProgress(false);
                        Toast.makeText(register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean isPasswordValid(String passwords){
        return passwords.length() > 4;
    }
    private void showProgress(final boolean show){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB_MR2){
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mRegisterFormView.setVisibility(show?View.VISIBLE:View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}