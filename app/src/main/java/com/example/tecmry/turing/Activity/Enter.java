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
import com.avos.avoscloud.LogInCallback;
import com.example.tecmry.turing.R;

public class Enter extends AppCompatActivity {
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);
        if(AVUser.getCurrentUser()!=null){
            startActivity(new Intent(Enter.this,MainActivity.class));
            Enter.this.finish();
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("登录");
        mUsernameView = (AutoCompleteTextView)findViewById(R.id.username);
        mPasswordView = (EditText)findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login||id== EditorInfo.IME_NULL){
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //Button mButton=(Button)findViewById(R.id.action_logout);
        Button mUserNameLoginButton = (Button)findViewById(R.id.username_login_button);
        mUserNameLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mUserNameRegisterButton = (Button)findViewById(R.id.username_register_button);
        mUserNameRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Enter.this,register.class));
                Enter.this.finish();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    private void attemptLogin(){
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        final  String username = mUsernameView.getText().toString();
        final  String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(!TextUtils.isEmpty(password) && !isPasswordValid(password)){
            mPasswordView.setError("密码大于4位");
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)){
            mUsernameView.setError("这个是必填项");
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        }else {
            showProgress(true);
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if(e == null){
                        Enter.this.finish();
                        startActivity(new Intent(Enter.this,MainActivity.class));
                    }else {
                        showProgress(false);
                        Toast.makeText(Enter.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean isPasswordValid(String password){
        return password.length() >= 4 ;
    }
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
