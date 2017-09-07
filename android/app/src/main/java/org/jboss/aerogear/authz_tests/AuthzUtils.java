package org.jboss.aerogear.authz_tests;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.authorization.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.PipeManager;
import org.jboss.aerogear.android.pipe.http.HeaderAndBody;
import org.jboss.aerogear.android.pipe.http.HttpException;
import org.jboss.aerogear.android.pipe.http.HttpRestProvider;
import org.jboss.aerogear.android.pipe.module.ModuleFields;
import org.jboss.aerogear.android.pipe.module.PipeModule;
import org.jboss.aerogear.android.pipe.rest.RestfulPipeConfiguration;
import org.jboss.aerogear.android.pipe.rest.gson.GsonResponseParser;

import java.net.URI;
import java.net.URL;

/**
 * Created by summers on 7/7/17.
 */

public final class AuthzUtils {

    private static final String STRING_SERVER_URL = "http://10.0.2.2:8081";
    private static final String AUTHZ_URL = "http://172.17.0.2:8080"+ "/auth";
    private static final String AUTHZ_ENDPOINT = "/realms/authz_demo/protocol/openid-connect/auth";
    private static final String ACCESS_TOKEN_ENDPOINT = "/realms/authz_demo/protocol/openid-connect/token";
    private static final String REFRESH_TOKEN_ENDPOINT = "/realms/authz_demo/protocol/openid-connect/token";
    private static final String AUTHZ_ACCOOUNT_ID = "keycloak-token";
    private static final String AUTHZ_CLIENT_ID = "android";
    private static final String AUTHZ_REDIRECT_URL = "http://oauth2callback";
    private static final String MODULE_NAME = "KeyCloakAuthz";

    private static String bearerToken = "" ;

    private AuthzUtils() {

    }

    static {
        try {
            AuthorizationManager.config(MODULE_NAME, OAuth2AuthorizationConfiguration.class)
                    .setBaseURL(new URL(AUTHZ_URL))
                    .setAuthzEndpoint(AUTHZ_ENDPOINT)
                    .setAccessTokenEndpoint(ACCESS_TOKEN_ENDPOINT)
                    .setRefreshEndpoint(REFRESH_TOKEN_ENDPOINT)
                    .setAccountId(AUTHZ_ACCOOUNT_ID)
                    .setClientId(AUTHZ_CLIENT_ID)
                    .setRedirectURL(AUTHZ_REDIRECT_URL)
                    //.setWithIntent(true)
                    .asModule();

            PipeManager.config("kc-strings", RestfulPipeConfiguration.class)
                    .module(new PipeModule() {
                        @Override
                        public ModuleFields loadModule(URI relativeURI, String httpMethod, byte[] requestBody) {
                            ModuleFields fields = new ModuleFields();
                            fields.addHeader("Authorization", "Bearer " + bearerToken);
                            return fields;
                        }

                        @Override
                        public boolean handleError(HttpException exception) {
                            return false;
                        }
                    })
                    .withUrl(new URL(STRING_SERVER_URL + "/secure/string"))
                    .responseParser(new GsonResponseParser(StringWrapper.GSON))
                    .forClass(StringWrapper.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final AccountManager am = AccountManager.get(activity);
            final Account[] accounts = am.getAccountsByType("org.keycloak.Account");

            if (accounts.length == 0) {
                am.addAccount("org.keycloak.Account", "org.keycloak.Account.token", null, null, activity, null, null);
            } else {

                Account account = accounts[0];
                fetchAccountInfo(activity, account, callback);
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void fetchAccountInfo(final Activity activity, final Account account, final Callback callback) {

        final AccountManager am = AccountManager.get(activity);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                URL accountUrl = null;
                try {
                    accountUrl = new URL("http://10.0.2.2:8080/auth/realms/authz_demo/account");

                    Bundle result = am.getAuthToken(account, "org.keycloak.Account.token", null, null, null, null).getResult();
                    if (result.containsKey(AccountManager.KEY_ERROR_MESSAGE)) {
                        throw new RuntimeException("Herf derf");
                    } else {
                        String token = result.getString(AccountManager.KEY_AUTHTOKEN);
                        if (token == null) {
                            am.addAccount("org.keycloak.Account", "org.keycloak.Account.token", null, null, activity, null, null);
                        } else {
                            bearerToken = token;
                            callback.onSuccess(bearerToken);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                return bearerToken;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        }.execute((Void[]) null);
    }


    @SuppressWarnings("unchecked")
    public static void upload(final String string, final Callback<StringWrapper> callback, Activity activity) {
        PipeManager.getPipe("kc-strings", activity).save(string, callback);
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }
}
