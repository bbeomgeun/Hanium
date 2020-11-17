package com.example.hanium_login_trail2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Cognito {
    private static final String TAG = "Cognito";
    private String poolID = "ap-northeast-2_XrucmlaJK";
    private String clientID = "1qe319ebpnshj3lhpb59rkh9tl";
    private String clientSecret = "1tkl3h8ea00oj3h6l55tu91lqa8q562squh8ngtfcpv7v2dqlg3";
    private Regions awsRegion = Regions.AP_NORTHEAST_2;         // Place your Region
    // ############################################################# End of Information about Cognito Pool
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
    private Context appContext;

    private String userPassword;                        // Used for Login

    Login loginActivity = (Login)Login.LoginActivity; // Login에서 전역변수로 선언 후 그 내용을 담은 객체 생성.

    public Cognito(Context context){
        appContext = context;
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }

    public void signUpInBackground(String userId, String password){
        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
        //userPool.signUp(userId, password, this.userAttributes, null, signUpCallback);
    }

    SignUpHandler signUpCallback = new SignUpHandler() {


        @Override
        //This method is called successfully registering a new user. Confirming the user may be required to activate the users account.
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            Toast.makeText(appContext,"Sign-up Success", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + signUpResult);
        }

        @Override
        //This method is called when user registration has failed. Probe exception for cause of the failure.
        public void onFailure(Exception exception) {
            Toast.makeText(appContext,"Sign-up failed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + exception);
        }
    };

    public void confirmUser(String userId, String code){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code,false, confirmationCallback);
        //cognitoUser.confirmSignUp(code,false, confirmationCallback);
    }
    // Callback handler for confirmSignUp API
    GenericHandler confirmationCallback = new GenericHandler() {

        @Override
        public void onSuccess() {
            // User was successfully confirmed
            Toast.makeText(appContext,"User Confirmed", Toast.LENGTH_LONG).show();
            Log.i(TAG, "User Confirmation success");
        }

        @Override
        public void onFailure(Exception exception) {
            // User confirmation failed. Check exception for the cause.
            Toast.makeText(appContext,"User Confirmation failed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Confirmation failed: " + exception);
        }
    };

    public void addAttribute(String key, String value){
        userAttributes.addAttribute(key, value);
    }

    public void userLogin(String userId, String password){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(authenticationHandler);
    }
    // Callback handler for the sign-in process
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        //This method is called to deliver valid tokens, when valid tokens were locally available (cached) or after successful completion of the authentication process.
        // The newDevice will is an instance of CognitoDevice for this device, and this parameter will be not null during these cases:
        // 1- If the user pool allows devices to be remembered and this is is a new device, that is first time authentication on this device.
        // 2- When the cached device key is lost and, hence, the service identifies this as a new device.
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Toast.makeText(appContext,"Authentication success", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Authentication success"); // 인증완료

            Intent i = new Intent(appContext, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(i);
            loginActivity.finish();
            //finish안해주면 activity가 쌓인다. 뒤로가기 시 로그인액티비티가 살아있음.
        }

        @Override
        //Call out to the dev to get the credentials for a user.
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            // Allow the sign-in to continue
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-in failed, check exception for the cause
            Toast.makeText(appContext,"Authentication Failure", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Authentication Failure");
        }
    };

    public String getPoolID() {
        return poolID;
    }

    public void setPoolID(String poolID) {
        this.poolID = poolID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Regions getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(Regions awsRegion) {
        this.awsRegion = awsRegion;
    }

}
