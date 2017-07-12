package org.jboss.aerogear.authz_tests;

import android.app.Activity;

import org.jboss.aerogear.android.authorization.AuthorizationManager;
import org.jboss.aerogear.android.authorization.AuthzModule;
import org.jboss.aerogear.android.authorization.oauth2.OAuth2AuthorizationConfiguration;
import org.jboss.aerogear.android.authorization.oauth2.OAuthWebViewDialog;
import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.pipe.PipeManager;
import org.jboss.aerogear.android.pipe.rest.RestfulPipeConfiguration;
import org.jboss.aerogear.android.pipe.rest.gson.GsonResponseParser;

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

            PipeManager.config("kc-strings", RestfulPipeConfiguration.class).module(AuthorizationManager.getModule(MODULE_NAME))
                    .withUrl(new URL(STRING_SERVER_URL + "/secure/string"))
                    .responseParser(new GsonResponseParser(StringWrapper.GSON))
                    .forClass(StringWrapper.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect(final Activity activity, final Callback callback) {
        try {
            final AuthzModule authzModule = AuthorizationManager.getModule(MODULE_NAME);

            authzModule.requestAccess(activity, new Callback<String>() {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(String s) {
                    callback.onSuccess(s);
                }

                @Override
                public void onFailure(Exception e) {
                    if (!e.getMessage().matches(OAuthWebViewDialog.OAuthReceiver.DISMISS_ERROR)) {
                        authzModule.deleteAccount();
                    }
                    callback.onFailure(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void upload(final String string, final Callback<StringWrapper> callback, Activity activity) {
        PipeManager.getPipe("kc-strings", activity).save(string, callback);
    }

    public static boolean isConnected() {
        return AuthorizationManager.getModule(MODULE_NAME).isAuthorized();
    }
}
