package ridersadvisor.com.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ridersadvisor.com.R;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.BaseVO;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.models.UserProfileVo;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.Util;

/**
 * Created by Sudhir Singh on 20,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SignInActivity extends AppBaseActivity implements View.OnClickListener {

    private EditText _usernameEditText, _passwordEditText;
    private TextView txtForgotPassword;

    private Dialog forgotPasswordPopUp;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_login);

        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        final Button _signinButton = (Button) findViewById(R.id.btn_signin);
        _usernameEditText = (EditText) findViewById(R.id.username_edittxt);
        _passwordEditText = (EditText) findViewById(R.id.password_edittxt);
        final TextInputLayout input_username = (TextInputLayout) findViewById(R.id.input_username);
        final TextInputLayout input_password = (TextInputLayout) findViewById(R.id.input_password);

//        _usernameEditText.setText("sudhir@ezbizsoft.com");
//        _passwordEditText.setText("12345678");

        txtForgotPassword.setOnClickListener(this);

        _usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                input_username.setError(null);
                input_username.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_password.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        _passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (_usernameEditText.getText().length() <= 0) {
                        input_username.setError(getResources().getString(R.string.provide_username));
                        _usernameEditText.requestFocus();
                    } else if (_passwordEditText.getText().length() <= 0) {
                        input_password.setError(getResources().getString(R.string.provide_password));
                        _passwordEditText.requestFocus();
                    } else {
//                    SalesUtils.openAlertDialog(context, getResources().getString(R.string.internet_title), getResources().getString(R.string.internet_description));
                        performLogin(_usernameEditText, _passwordEditText);
                    }
                }
                return false;
            }
        });

        _signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_usernameEditText.getText().length() <= 0) {
                    input_username.setError(getResources().getString(R.string.provide_username));
                    _usernameEditText.requestFocus();
                } else if (_passwordEditText.getText().length() <= 0) {
                    input_password.setError(getResources().getString(R.string.provide_password));
                    _passwordEditText.requestFocus();
                } else {
                    performLogin(_usernameEditText, _passwordEditText);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtForgotPassword:
                displayForgotPasswordPopUp();
                break;

        }
    }

    private void performLogin(EditText _usernameEditText, EditText _passwordEditText) {

        if (Util.isNetworkOnline(SignInActivity.this)) {

            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage(getString(R.string.checking_credentials));
            pDialog.setCancelable(false);
            pDialog.show();

            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.EMAIL, _usernameEditText.getText().toString().trim());
            params.put(FeedParams.PASSWORD, _passwordEditText.getText().toString().trim());
            placeRequest(APIMethods.LOGIN_API, UserProfileVo.class, params, true, null);

        } else {
            showToast(R.string.no_internet);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN_API)) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            UserProfileVo userProfileVo = (UserProfileVo) response;
            SharedPrefManager.getInstance().setSharedData(FeedParams.USERID, userProfileVo.getUserId());
            SharedPrefManager.getInstance().setSharedData(FeedParams.NAME, userProfileVo.getName());
            SharedPrefManager.getInstance().setSharedData(FeedParams.EMAIL, userProfileVo.getUserEmail());
            SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ACCESS, userProfileVo.getAccess());
            SharedPrefManager.getInstance().setSharedData(FeedParams.USERTOKEN, userProfileVo.getToken());
//            SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ACCESS, "capture");

            Intent landingIntent = new Intent(SignInActivity.this, LandingActivity.class);
//            landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(landingIntent);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                BaseVO resultData = (BaseVO) response;
                Gson gson = new Gson();
                String json = gson.toJson(resultData);
                JSONObject responseObj = new JSONObject(new String(json));
                //JSONObject result = responseObj.getJSONObject("result");
                String message = responseObj.getString("message");
                showToast(message);
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getString(R.string.password_sent));
            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN_API)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
            try {
                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                // JSONObject result = errorObject.getJSONObject("result");
                String message = errorObject.getString("message");
                showToast(message);

            } catch (JSONException e) {
                e.printStackTrace();
                showToast(getString(R.string.could_not_recover_password));
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getString(R.string.could_not_recover_password));
            }
        }

    }


//    private boolean isValidData() {
//
//        if (edtUserName.getText().toString().trim().length() <= 0) {
//            edtUserName.setError(getString(R.string.provide_username));
//            return false;
//        } else if (edtPassword.getText().toString().trim().length() <= 0) {
//            edtPassword.setError(getString(R.string.provide_password));
//            return false;
//        }
//
//        return true;
//`
//    }

    private void displayForgotPasswordPopUp() {
        forgotPasswordPopUp = new Dialog(SignInActivity.this);
        forgotPasswordPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordPopUp.setContentView(R.layout.popup_forgot_password);
        forgotPasswordPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotPasswordPopUp.getWindow().setGravity(Gravity.CENTER);
        forgotPasswordPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText emailField = (EditText) forgotPasswordPopUp.findViewById(R.id.forgot_password_email);
        Button resetPasswordBtn = (Button) forgotPasswordPopUp.findViewById(R.id.reset_password_button);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                if (email.length() > 0 && Util.isValidEmail(email)) {
                    if (Util.isNetworkOnline(SignInActivity.this)) {
                        recoverPassword(email);
                        hidePopupDialog();
                    } else {
                        showToast(getResources().getString(R.string.no_internet));
                    }
                } else {
                    showToast(getResources().getString(R.string.email_to_get_password));
                }
            }
        });
        forgotPasswordPopUp.show();
    }

    private void recoverPassword(String email) {

        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage(getString(R.string.sending_password));
        pDialog.setCancelable(false);
        pDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.EMAIL, email);
        placeRequest(APIMethods.RESET_PASSWORD, BaseVO.class, params, true, null);
    }

    private void hidePopupDialog() {
        if (forgotPasswordPopUp.isShowing()) {
            forgotPasswordPopUp.dismiss();
            forgotPasswordPopUp.cancel();
        }
    }
}
