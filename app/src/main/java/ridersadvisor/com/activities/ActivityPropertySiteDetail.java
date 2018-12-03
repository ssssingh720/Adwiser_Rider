package ridersadvisor.com.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import ridersadvisor.com.R;
import ridersadvisor.com.adapters.SponsorsAdapter;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.BaseVO;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.monitor_site.activities.MonitorImageCatureActivity;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.AddSiteDetailPrefs;
import ridersadvisor.com.utils.Constants;
import ridersadvisor.com.utils.Fused;
import ridersadvisor.com.utils.GpsActivation;
import ridersadvisor.com.utils.ImageUtility;
import ridersadvisor.com.utils.OfflineCaptureImagesPrefs;
import ridersadvisor.com.utils.TrackGPS;
import ridersadvisor.com.utils.Util;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static ridersadvisor.com.utils.Constants.REQUEST_PERMISSION_GPS_SETTING;

/**
 * Created by Sudhir Singh on 28,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ActivityPropertySiteDetail extends AppBaseActivity implements View.OnClickListener {

    private static final int PERMISSION_CAMERA_REQUEST_CODE = 2;
    private static final int OPEN_CAMERA_FROM_M_OR_GREATER = 1;
    private static final int OPEN_CAMERA_FROM_L_OR_LOWER = 2;
    private static final int REQUEST_OPEN_CAMERA_SETTING_PAGE = 4;
    private static final int REQUEST_IMAGE_CATURE = 5;

    private Context context;

    private Uri mUri;
    private String IMAGE_NAME = "";

    private GridView grdSponsors;
    private ImageView imgAddSiteImage;

    //detail
    private TextView txtMediaOwnerName;
    private TextView txtDtlLandmarks;
    private TextView txtDtlSiteFacing;
    private TextView txtDtlSiteType;
    private TextView txtDtlSiteAngle;
    private TextView txtDtlSiteSize;
    private TextView txtDtlSitePosition;
    private TextView txtDtlRoadDirection;
    private TextView txtDtlIlluminationStatus;
    private TextView txtDtlLightingType;
    private TextView txtDtlLicense;

    private MySiteDetailVo mySiteDetailVo;
    private SponsorsAdapter chapterVideoAdapter;

    private ProgressDialog pDialog;
    private String mStoredImagePath = "";

    private Fused fused;
    private double longitude = 0;
    private double latitude = 0;
    private boolean isOffline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.site_detail);

        context = ActivityPropertySiteDetail.this;

        fused = new Fused(context, 1);
        fused.onStart();
        getLocation();

        grdSponsors = (GridView) findViewById(R.id.grdSponsors);

        imgAddSiteImage = (ImageView) findViewById(R.id.imgAddSiteImage);

        txtMediaOwnerName = (TextView) findViewById(R.id.txtMediaOwnerName);
        txtDtlLandmarks = (TextView) findViewById(R.id.txtDtlLandmarks);
        txtDtlSiteFacing = (TextView) findViewById(R.id.txtDtlSiteFacing);
        txtDtlSiteType = (TextView) findViewById(R.id.txtDtlSiteType);
        txtDtlSiteAngle = (TextView) findViewById(R.id.txtDtlSiteAngle);
        txtDtlSiteSize = (TextView) findViewById(R.id.txtDtlSiteSize);
        txtDtlSitePosition = (TextView) findViewById(R.id.txtDtlSitePosition);
        txtDtlRoadDirection = (TextView) findViewById(R.id.txtDtlRoadDirection);
        txtDtlIlluminationStatus = (TextView) findViewById(R.id.txtDtlIlluminationStatus);
        txtDtlLightingType = (TextView) findViewById(R.id.txtDtlLightingType);
        txtDtlLicense = (TextView) findViewById(R.id.txtDtlLicense);

        LinearLayout lntSiteDetailBack = (LinearLayout) findViewById(R.id.lntSiteDetailBack);

        imgAddSiteImage.setOnClickListener(this);
        lntSiteDetailBack.setOnClickListener(this);

        mySiteDetailVo = (MySiteDetailVo) getIntent().getExtras().get(FeedParams.PROPERTY_DETAIL);
        isOffline = getIntent().getExtras().getBoolean(FeedParams.IS_OFFLINE_MODE);

//        PropertyDetailAdapter adapter = new PropertyDetailAdapter(context, mySiteDetailVo.getImages());

        chapterVideoAdapter = new SponsorsAdapter(context, mySiteDetailVo.getImages(), isOffline);
        grdSponsors.setAdapter(chapterVideoAdapter);
//        rclPropertyDtl.setAdapter(adapter);

        setData(mySiteDetailVo);

//        final ArrayList<MySiteImageVo> images = mySiteDetailVo.getImages();
        grdSponsors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("data", mySiteDetailVo.getImages());
                intent.putExtra(FeedParams.IS_OFFLINE_MODE, isOffline);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });

    }

    private void setData(MySiteDetailVo mySiteDetailVo) {

        TableRow tblPropertyName=(TableRow)findViewById(R.id.tblPropertyName);
        TableRow tblLocality=(TableRow)findViewById(R.id.tblLocality);
        TableRow tblLandmarks=(TableRow)findViewById(R.id.tblLandmarks);
        TableRow tblSiteFacing=(TableRow)findViewById(R.id.tblSiteFacing);
        TableRow tblSiteType=(TableRow)findViewById(R.id.tblSiteType);
        TableRow tblSiteAngle=(TableRow)findViewById(R.id.tblSiteAngle);
        TableRow tblSiteSize=(TableRow)findViewById(R.id.tblSiteSize);
        TableRow tblSitePosition=(TableRow)findViewById(R.id.tblSitePosition);
        TableRow tblFacingTraffic=(TableRow)findViewById(R.id.tblFacingTraffic);
        TableRow tblIlluminationStatus=(TableRow)findViewById(R.id.tblIlluminationStatus);
        TableRow tblLightingType=(TableRow)findViewById(R.id.tblLightingType);
        TableRow tblLicense=(TableRow)findViewById(R.id.tblLicense);
        TableRow tblMedOwner=(TableRow)findViewById(R.id.tblMedOwner);



        TextView txtPropertyLocation = (TextView) findViewById(R.id.txtPropertyLocation);
        TextView txtPropertyName = (TextView) findViewById(R.id.txtPropertyName);

        setVisibility(mySiteDetailVo.getSiteCode(),txtPropertyName,tblPropertyName);
        setVisibility(mySiteDetailVo.getLocality(),txtPropertyLocation,tblLocality);
        setVisibility(mySiteDetailVo.getMediaOwner(),txtMediaOwnerName,tblMedOwner);
        setVisibility(mySiteDetailVo.getLandmarks(),txtDtlLandmarks,tblLandmarks);
        setVisibility(mySiteDetailVo.getSiteFacing(),txtDtlSiteFacing,tblSiteFacing);
        setVisibility(mySiteDetailVo.getSiteType(),txtDtlSiteType,tblSiteType);
        setVisibility(mySiteDetailVo.getSiteAngle(),txtDtlSiteAngle,tblSiteAngle);
        setVisibility(mySiteDetailVo.getSiteSize(),txtDtlSiteSize,tblSiteSize);
        setVisibility(mySiteDetailVo.getSitePosition(),txtDtlSitePosition,tblSitePosition);
        setVisibility(mySiteDetailVo.getFacingTraffic(),txtDtlRoadDirection,tblFacingTraffic);
        setVisibility(mySiteDetailVo.getIlluminationStatus(),txtDtlIlluminationStatus,tblIlluminationStatus);
        setVisibility(mySiteDetailVo.getLightingType(),txtDtlLightingType,tblLightingType);
        setVisibility(mySiteDetailVo.getLicense(),txtDtlLicense,tblLicense);

//        txtPropertyLocation.setText(mySiteDetailVo.getLocality());

//        txtMediaOwnerName.setText(mySiteDetailVo.getMediaOwner());
//        txtDtlLandmarks.setText(mySiteDetailVo.getLandmarks());
//        txtDtlSiteFacing.setText(mySiteDetailVo.getSiteFacing());
//        txtDtlSiteType.setText(mySiteDetailVo.getSiteType());
//        txtDtlSiteAngle.setText(mySiteDetailVo.getSiteAngle());
//        txtDtlSiteSize.setText(mySiteDetailVo.getSiteSize());
//        txtDtlSitePosition.setText(mySiteDetailVo.getSitePosition());
//        txtDtlRoadDirection.setText(mySiteDetailVo.getFacingTraffic());
//        txtDtlIlluminationStatus.setText(mySiteDetailVo.getIlluminationStatus());
//        txtDtlLightingType.setText(mySiteDetailVo.getLightingType());
//        txtDtlLicense.setText(mySiteDetailVo.getLicense());

    }

    private void setVisibility(String text,TextView textView,View view){
        if(text!=null && text.length()>0) {
            textView.setText(text);
        }else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgAddSiteImage:

                Intent addImageIntent = new Intent(context, OfflineCaptureSiteImage.class);
                addImageIntent.putExtra(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo);
                addImageIntent.putExtra(FeedParams.IS_IMAGE_COMPULASARY, "" + false);
                startActivityForResult(addImageIntent, REQUEST_IMAGE_CATURE);

//                checkCameraPermission();

                break;

            case R.id.lntSiteDetailBack:

                onBackPressed();

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String reqPermission[], int[] reqPermissionResult) {
        switch (requestCode) {
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (reqPermissionResult.length == 3 && reqPermissionResult[0] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[1] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[2] == PackageManager.PERMISSION_GRANTED) {
                    captureImageFromCamera();
                } else {
                    showToast(getString(R.string.camera_permission_decline));
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLocation();
        mStoredImagePath = "";
        if (requestCode == OPEN_CAMERA_FROM_L_OR_LOWER) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = ImageUtility.getBitmapFromUri(this, mUri);
                if (bitmap != null) {
                    checkAndSubmitData(bitmap);

                }
            }
        } else if (requestCode == OPEN_CAMERA_FROM_M_OR_GREATER) {
            if (resultCode == RESULT_OK) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals(IMAGE_NAME)) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    mUri = getImageUri(BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions));
                    Bitmap bitmap = ImageUtility.getBitmapFromUri(this, mUri);
                    if (bitmap != null) {
                        checkAndSubmitData(bitmap);
//                        mIvCard.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkAndSubmitData(Bitmap bitmap) {

        mStoredImagePath = ImageUtility.insertBitmapToAppFolder(bitmap, this);
        submitSiteImage();

    }

    private void submitSiteImage() {
        if (Util.isNetworkOnline(context) && !isOffline) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.sending_data));
            pDialog.setCancelable(false);
            pDialog.show();

            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));

            Bitmap bitmap = ImageUtility.getBitmapFromUri(context, mUri);
            String imageString = ImageUtility.convertBitmapToString(bitmap);

            params.put(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo.getId());
            params.put(FeedParams.IMAGE, imageString);
            params.put(FeedParams.FILE_NAME, "" + System.currentTimeMillis() + ".jpg");
            params.put(FeedParams.LONGITUDE, mySiteDetailVo.getLongitude());
            params.put(FeedParams.LATTITUDE, mySiteDetailVo.getLattitude());
            placeRequest(APIMethods.SAVE_PROPERTY_IMAGE, BaseVO.class, params, true, null);

        } else {

            if (!AddSiteDetailPrefs.isSiteExist(context, mySiteDetailVo.getId())) {
                AddSiteDetailPrefs.addFormData(context, mySiteDetailVo);
            }
            MySiteImageVo mySiteImageVo = new MySiteImageVo(mySiteDetailVo.getId(),"allocation_id", mStoredImagePath,""+System.currentTimeMillis(),
                    String.valueOf(latitude), String.valueOf(longitude), "" + System.currentTimeMillis() + ".jpg","",true,false,"Not available");
            mySiteDetailVo.getImages().add(mySiteImageVo);
            chapterVideoAdapter.notifyDataSetChanged();

            OfflineCaptureImagesPrefs.addOfflineImage(context, mySiteImageVo);

            showToast(getString(R.string.upload_image_internet_availalbel));
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_IMAGE)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                BaseVO resultData = (BaseVO) response;
                String message = resultData.getMessage();
                mUri = null;
                showToast(message);
                if (mySiteDetailVo.getImages() != null) {

                    MySiteImageVo mySiteImageVo = new MySiteImageVo();
                    mySiteImageVo.setImage(resultData.getImage());
                    mySiteDetailVo.getImages().add(0, mySiteImageVo);
                    chapterVideoAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_IMAGE)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(this, Manifest.permission.CAMERA) && checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                captureImageFromCamera();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);
                } else {
                    openSettingForCamraAndExternal();
                }
            }
        } else {
            captureImageFromCamera();
        }
    }

    private boolean checkPermission(Activity activity, String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        return isGranted;
    }

    private void openSettingForCamraAndExternal() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Camera and External Permission Required ");
        alertDialog.setMessage("Show Camera and External settings?");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_OPEN_CAMERA_SETTING_PAGE);
                showToast(getString(R.string.go_to_permission));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showToast(getString(R.string.permission_required));
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void captureImageFromCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMAGE_NAME = "ebiz_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), IMAGE_NAME);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, ActivityPropertySiteDetail.this.getApplicationContext().getPackageName() + ".provider", f));
            startActivityForResult(intent, OPEN_CAMERA_FROM_M_OR_GREATER);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, "ebiz_" + String.valueOf(System.currentTimeMillis()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intent, OPEN_CAMERA_FROM_L_OR_LOWER);
        }
    }

    private Uri getOutputMediaFileUri(int type, String path) {
        // return Uri.fromFile(getOutputMediaFile(type, path));
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), path));
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ActivityPropertySiteDetail.this.finish();
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(ActivityPropertySiteDetail.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ActivityPropertySiteDetail.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.ACCESS_FINE_LOCATION_PERMISSION_CONSTANT);
                } else {
                    openSettingForGPS();
                }
            }
        } else {
            getUpdatedLocation();
        }
    }

    private void openSettingForGPS() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is disabled");
        alertDialog.setMessage("Show location_pointer settings?");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_PERMISSION_GPS_SETTING);

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                Utils.stopProgress(progressDialog);
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void getUpdatedLocation() {
        TrackGPS gps = new TrackGPS(context);

        if (gps.canGetLocation()) {
            if (fused.lon != null && fused.lat != null && fused.lon != "0.0" && fused.lat != "0.0") {
                longitude = Double.parseDouble(fused.lon);
                latitude = Double.parseDouble(fused.lat);

//                Toast.makeText(context, "Longitude:" + fused.lon + "\nfused.lat:" + fused.lat, Toast.LENGTH_SHORT).show();
            }
//            else {
//                Toast.makeText(context, "Not Able to get location_pointer.please turn on your GPS", Toast.LENGTH_SHORT).show();
//            }
        } else {
            new GpsActivation(ActivityPropertySiteDetail.this).enableGPS();
        }
    }
}
