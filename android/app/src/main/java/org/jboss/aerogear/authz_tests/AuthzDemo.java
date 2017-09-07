package org.jboss.aerogear.authz_tests;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.PipeManager;
import org.jboss.aerogear.android.pipe.http.HeaderAndBody;
import org.jboss.aerogear.android.pipe.http.HttpRestProvider;

import java.net.URL;
import java.util.List;

public class AuthzDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authz_demo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthzUtils.connect(this, new Callback() {
            @Override
            public void onSuccess(Object data) {
                PipeManager.getPipe("kc-strings", AuthzDemo.this).read(new Callback<List>() {
                    @Override
                    public void onSuccess(List data) {
                        PipeManager.getPipe("kc-strings", AuthzDemo.this).save(new StringWrapper("test"), new Callback() {
                            @Override
                            public void onSuccess(Object data) {
                                Log.i("Data", data.toString());
                                Toast.makeText(AuthzDemo.this, "Success, response from server : " +data.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                            }
                        });
                        if (data.size() > 0) {
                            Log.i("Data", data.get(0).toString());
                            Toast.makeText(AuthzDemo.this, "Success, response from server : " +data.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.i("Data", "Empty");
                            Toast.makeText(AuthzDemo.this, "Success, Data empty", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        });
        //get current strings
        //check login
        //if logged in, show remove/add
        //else show login
    }

}
