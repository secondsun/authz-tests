package org.jboss.aerogear.authz_tests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.PipeManager;

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
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("ERROR", e.getMessage(), e);
                            }
                        });
                        if (data.size() > 0) {
                            Log.i("Data", data.get(0).toString());
                        } else {
                            Log.i("Data", "Empty");
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
