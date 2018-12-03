package ridersadvisor.com.activities;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.HashMap;

import ridersadvisor.com.fragments.BaseFragment;
import ridersadvisor.com.manager.PageManager;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.networking.RequestManager;
import ridersadvisor.com.networking.ServerCallback;


/**
 * Created by srajendrakumar on 11/09/15.
 */
public class AppBaseActivity extends AppCompatActivity implements ServerCallback {

    public static final int MAX_PAGE_BUFFER = 3;
    protected PageManager pageManager = new PageManager(this,
            MAX_PAGE_BUFFER);
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Toast mToast;
    //private AnalyticsTracker mAnalyticsTracker=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(this);
        //mAnalyticsTracker= new AnalyticsTracker(this);
//        int theme = prefManager.getSharedDataInt(OutlookConstants.theme);
//        if (theme != 0)
//            setTheme(theme);

        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
//    }


    /**
     * Place GET API requet with params
     *
     * @param methodName
     * @param clazz
     * @param params
     */
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params) {
        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, params, false, null);
    }

    /**
     * Place simple GET API Request
     *
     * @param methodName
     * @param clazz
     */
    public void placeRequest(String methodName, Class clazz) {
        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, null, false, null);
    }


    /**
     * Place API request with isPOST boolean param
     *
     * @param methodName
     * @param clazz
     * @param params
     * @param isPOST
     */
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params, boolean isPOST, String action) {

        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, params, isPOST, action);
    }

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mRegistrationBroadcastReceiver!=null) {
//            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                    new IntentFilter(OutlookConstants.REGISTRATION_COMPLETE));
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleAnalytics.getInstance(this).reportActivityStart(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        GoogleAnalytics.getInstance(this).reportActivityStop(this);
//
//    }


    /**
     * method to get GCM token*/
//    public void registerGCM() {
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                SharedPreferences sharedPreferences =
//                        PreferenceManager.getDefaultSharedPreferences(context);
//                boolean sentToken = sharedPreferences
//                        .getBoolean(OutlookConstants.SENT_TOKEN_TO_SERVER, false);
//                if (sentToken) {
//                    Log.d("LogActivity", getString(R.string.gcm_send_message));
//                } else {
//                    Log.d("LOgActivity", getString(R.string.token_error_message));
//                }
//            }
//        };
//
//        if (Util.checkPlayServices(AppBaseActivity.this)) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
//        }
//    }

    /**
     * Method to show the Toast message
     *
     * @param stringID
     */
    public void showToast(int stringID) {
        String str = getString(stringID);
        mToast.setText(str);
        mToast.show();
    }

    /**
     * Method to show the Toast message
     *
     * @param str
     */
    public void showToast(String str) {
        mToast.setText(str);
        mToast.show();
    }

    /*
    * Method called to save a tag Object to the previous Fragment in the stack.
    * This is used to communicate to the previous stack
    *
    * @param tag An Object saved to the previous fragment in the stack
    */
    public void setTagToPreviousState(Object tag) {
        pageManager.setTagToPreviousState(tag);
    }

    /**
     * This method is called when the fragment that we want to add as the next
     * in the stack
     *
     * @param fragment
     */
    public void showNextScreen(BaseFragment fragment) {
        pageManager.pushScreen(fragment);

    }

    /*
     * This method is called when the fragment that we want to add has to be
     * added as the root fragment, thus clearing the stack
     *
     * @param fragment
     */
    public void showAsRootScreen(BaseFragment fragment) {
        pageManager.pushAsRootScreen(fragment);
    }

    /**
     * @return of type
     * @author Hari K J
     * @since Dec 6, 2013
     */
    public Object getTagFromPreviousState() {
        return pageManager.getTagFromPreviousState();
    }


}

