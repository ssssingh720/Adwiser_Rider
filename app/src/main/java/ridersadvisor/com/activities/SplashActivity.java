package ridersadvisor.com.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ridersadvisor.com.R;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;

/**
 * Created by Sudhir Singh on 19,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SplashActivity extends AppBaseActivity {

    private long _splashTime = 3000;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ImageView imgLogo = (ImageView) (findViewById(R.id.imgLogo));
        performAnimation(imgLogo);
//        Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
//        startActivity(mainIntent);

//        new SplashTask().execute();

    }

    private void performAnimation(final ImageView imgLogo) {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        imgLogo.clearAnimation();
        imgLogo.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID).length() <= 0) {

                    Intent i = new Intent(SplashActivity.this, SignInActivity.class);
//                    if (Build.VERSION.SDK_INT > 20) {
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, imgLogo, getResources().getString(R.string.logo_splash));
//                        startActivity(i, options.toBundle());
//                    } else {
                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                    }
//                    Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
//                    startActivity(mainIntent);

                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, LandingActivity.class);
                    startActivity(mainIntent);
                }

                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

//    private void submitSiteImage() {
//        if (Util.isNetworkOnline(SplashActivity.this)) {
//            pDialog = new ProgressDialog(SplashActivity.this);
//            pDialog.setMessage(getString(R.string.sending_data));
//            pDialog.setCancelable(false);
//            pDialog.show();
//
////userid, usertoken , media_owner_id, landmarks, locality, site_facing_id, site_type_id, site_size,
//// site_angel, road_direction, illumination_status, lighting_type, license, site_position_id, lattitude, longitude
//            HashMap<String, String> params = new HashMap<>();
//            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
//            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));
//
//            Bitmap bitmap = ImageUtility.getBitmapFromUri(SplashActivity.this, mUri);
//            String imageString = ImageUtility.convertBitmapToString(bitmap);
//
//            params.put(FeedParams.PROPERTY_SITE_ID, property_site_id);
//            params.put(FeedParams.IMAGE, imageString);
//            params.put(FeedParams.FILE_NAME, "" + System.currentTimeMillis() + ".jpg");
//            params.put(FeedParams.LONGITUDE, longitude);
//            params.put(FeedParams.LATTITUDE, latitude);
//            placeRequest(APIMethods.SAVE_PROPERTY_IMAGE, BaseVO.class, params, true, null);
//
//        } else {
//
//            OfflineImagesVo offlineImagesVo = new OfflineImagesVo(SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID),
//                    SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN), property_site_id,
//                    mUri, "" + System.currentTimeMillis() + ".jpg", longitude, latitude);
//            OfflineCaptureImagesPrefs.addFormData(context, offlineImagesVo);
//
//            showToast(getString(R.string.upload_image_internet_availalbel));
//        }
//
//    }

}
