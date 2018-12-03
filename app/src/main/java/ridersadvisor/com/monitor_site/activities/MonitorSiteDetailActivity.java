package ridersadvisor.com.monitor_site.activities;

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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import ridersadvisor.com.R;
import ridersadvisor.com.activities.AppBaseActivity;
import ridersadvisor.com.activities.DetailActivity;
import ridersadvisor.com.adapters.SponsorsAdapter;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.BaseVO;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.monitor_site.adapters.MyItem;
import ridersadvisor.com.monitor_site.modal.MonitorSiteDetailVo;
import ridersadvisor.com.monitor_site.prefs.OfflineMonitorImagePrefs;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.Constants;
import ridersadvisor.com.utils.Fused;
import ridersadvisor.com.utils.GpsActivation;
import ridersadvisor.com.utils.ImageUtility;
import ridersadvisor.com.utils.TrackGPS;
import ridersadvisor.com.utils.Util;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static ridersadvisor.com.utils.Constants.REQUEST_PERMISSION_GPS_SETTING;

/**
 * Created by Sudhir Singh on 25,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MonitorSiteDetailActivity extends AppBaseActivity implements View.OnClickListener
        , OnMapReadyCallback, ClusterManager.OnClusterClickListener<MyItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private static final int PERMISSION_CAMERA_REQUEST_CODE = 2;
    private static final int OPEN_CAMERA_FROM_M_OR_GREATER = 1;
    private static final int OPEN_CAMERA_FROM_L_OR_LOWER = 2;
    private static final int REQUEST_OPEN_CAMERA_SETTING_PAGE = 4;
    private static final int REQUEST_IMAGE_CATURE = 5;

    private Context context;
    private boolean isImageCapture = false;

    private int positionClicked = 0;
    private Uri mUri;
    private String IMAGE_NAME = "";

    private ImageView imgAddSiteImage;
    private ImageView imgMonitorSiteMap;

    //detail
    private LinearLayout lnrDetailView;
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

    private MonitorSiteDetailVo monitorSiteDetail;

    private ProgressDialog pDialog;
    private String mStoredImagePath = "";

    private Fused fused;
    private double myLongitude = 0;
    private double myLatitude = 0;
    private boolean isOffline = false;

    private ClusterManager<MyItem> mClusterManager;
    private GoogleMap monitorSiteMap;
    private SupportMapFragment mapFragment;

    private GridView grdSponsors;
    private SponsorsAdapter chapterVideoAdapter;
    private ArrayList<MySiteImageVo> offlineImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.site_detail);

        context = MonitorSiteDetailActivity.this;

        fused = new Fused(context, 1);
        fused.onStart();
        getLocation();

        grdSponsors = (GridView) findViewById(R.id.grdSponsors);
        imgAddSiteImage = (ImageView) findViewById(R.id.imgAddSiteImage);
        imgMonitorSiteMap = (ImageView) findViewById(R.id.imgMonitorSiteMap);

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
        lnrDetailView = (LinearLayout) findViewById(R.id.lnrDetailView);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.monitorSiteMap);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);


        imgAddSiteImage.setOnClickListener(this);
        imgMonitorSiteMap.setOnClickListener(this);
        lntSiteDetailBack.setOnClickListener(this);

        monitorSiteDetail = (MonitorSiteDetailVo) getIntent().getExtras().get(FeedParams.PROPERTY_DETAIL);
        isOffline = getIntent().getExtras().getBoolean(FeedParams.IS_OFFLINE_MODE);
        positionClicked = getIntent().getExtras().getInt(FeedParams.POSITION_CLICKED);

        offlineImageList = OfflineMonitorImagePrefs.getOfflineImage(context, monitorSiteDetail.getSiteId());

        chapterVideoAdapter = new SponsorsAdapter(context, offlineImageList, isOffline);
        grdSponsors.setAdapter(chapterVideoAdapter);

        grdSponsors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("data", offlineImageList);
                intent.putExtra(FeedParams.IS_OFFLINE_MODE, isOffline);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });

        setData(monitorSiteDetail);

    }

    private void setData(MonitorSiteDetailVo mySiteDetailVo) {

        imgMonitorSiteMap.setVisibility(View.VISIBLE);

        TextView txtPropertyLocation = (TextView) findViewById(R.id.txtPropertyLocation);
        TextView txtPropertyName = (TextView) findViewById(R.id.txtPropertyName);
        TextView txtUID = (TextView) findViewById(R.id.txtUID);

        txtUID.setText(getResources().getString(R.string.uid));
        txtPropertyName.setText(mySiteDetailVo.getSiteCode());
        txtPropertyLocation.setText(mySiteDetailVo.getLocality());

        txtMediaOwnerName.setText(mySiteDetailVo.getMediaOwner());
        txtDtlLandmarks.setText(mySiteDetailVo.getLandmarks());
        txtDtlSiteFacing.setText(mySiteDetailVo.getSiteFacing());
        txtDtlSiteType.setText(mySiteDetailVo.getSiteType());
        txtDtlSiteAngle.setText(mySiteDetailVo.getSiteAngle());
        txtDtlSiteSize.setText(mySiteDetailVo.getSiteSize());
        txtDtlSitePosition.setText(mySiteDetailVo.getSitePosition());
        txtDtlRoadDirection.setText(mySiteDetailVo.getRoadDirection());
        txtDtlIlluminationStatus.setText(mySiteDetailVo.getIlluminationStatus());
        txtDtlLightingType.setText(mySiteDetailVo.getLightingType());
        txtDtlLicense.setText(mySiteDetailVo.getLicense());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgAddSiteImage:

                if(monitorSiteDetail.getRange()==0 || calculateDistance()<=monitorSiteDetail.getRange()) {
                    Intent addImageIntent = new Intent(context, MonitorImageCatureActivity.class);
                    addImageIntent.putExtra(FeedParams.PROPERTY_SITE_ID, monitorSiteDetail);
                    addImageIntent.putExtra(FeedParams.IS_IMAGE_COMPULASARY, "" + false);
                    startActivityForResult(addImageIntent, REQUEST_IMAGE_CATURE);
//                checkCameraPermission();
                }else{
                    showToast(getString(R.string.please_be_in_range));
                }

                break;

            case R.id.imgMonitorSiteMap:

                if (lnrDetailView.getVisibility() == View.VISIBLE) {
                    lnrDetailView.setVisibility(View.GONE);
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    imgMonitorSiteMap.setImageResource(R.drawable.ic_listing);

                } else {
                    lnrDetailView.setVisibility(View.VISIBLE);
                    mapFragment.getView().setVisibility(View.GONE);
                    imgMonitorSiteMap.setImageResource(R.drawable.ic_map);
                }

                break;

            case R.id.lntSiteDetailBack:

                onBackPressed();

                break;
        }
    }

    private float calculateDistance( ){

        float totalDistance=0;
        try {
            float[] result1 = new float[3];
            android.location.Location.distanceBetween(myLatitude, myLongitude, Double.valueOf(monitorSiteDetail.getLattitude()),
                    Double.valueOf(monitorSiteDetail.getLongitude()), result1);
            totalDistance= result1[0];

        }catch(Exception e){

        }


        return totalDistance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        monitorSiteMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }else{
            googleMap.setMyLocationEnabled(true);
        }

        mClusterManager = new ClusterManager<MyItem>(context, googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addMarkers(monitorSiteDetail);
    }

    private void addMarkers(MonitorSiteDetailVo monitorSiteDetail) {
        try {
            double latitude = Double.parseDouble(monitorSiteDetail.getLattitude());
            double longitude = Double.parseDouble(monitorSiteDetail.getLongitude());

            MyItem infoWindowItem = new MyItem(latitude, longitude, monitorSiteDetail.getSiteCode(), monitorSiteDetail.getLandmarks(),
                    monitorSiteDetail.getMediaOwner(), monitorSiteDetail.getImage(), monitorSiteDetail.getLocality());
            mClusterManager.addItem(infoWindowItem);
            monitorSiteMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mClusterManager.cluster();
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
        String firstName = cluster.getItems().iterator().next().getSnippet();
        showToast(firstName);
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
//        showToast(myItem.getTitle());
//        showInfoDialog(myItem);
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

        if (requestCode == REQUEST_IMAGE_CATURE) {
            offlineImageList.clear();
            offlineImageList = OfflineMonitorImagePrefs.getOfflineImage(context, monitorSiteDetail.getSiteId());
            chapterVideoAdapter = new SponsorsAdapter(context, offlineImageList, isOffline);
            grdSponsors.setAdapter(chapterVideoAdapter);
            chapterVideoAdapter.notifyDataSetChanged();
            if(resultCode==RESULT_OK) {
                isImageCapture = true;
            }
        }

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

            params.put(FeedParams.PROPERTY_SITE_ID, monitorSiteDetail.getId());
            params.put(FeedParams.IMAGE, imageString);
            params.put(FeedParams.FILE_NAME, "" + System.currentTimeMillis() + ".jpg");
            params.put(FeedParams.LONGITUDE, monitorSiteDetail.getLongitude());
            params.put(FeedParams.LATTITUDE, monitorSiteDetail.getLattitude());
            placeRequest(APIMethods.SAVE_PROPERTY_IMAGE, BaseVO.class, params, true, null);

        } else {

//            if (!AddSiteDetailPrefs.isSiteExist(context, monitorSiteDetail.getId())) {
//                AddSiteDetailPrefs.addFormData(context, monitorSiteDetail);
//            }
//            MySiteImageVo mySiteImageVo = new MySiteImageVo(monitorSiteDetail.getId(), mStoredImagePath,""+System.currentTimeMillis(),
//                    String.valueOf(latitude), String.valueOf(longitude), "" + System.currentTimeMillis() + ".jpg","");
//            monitorSiteDetail.getImages().add(mySiteImageVo);
//
//            OfflineCaptureImagesPrefs.addOfflineImage(context, mySiteImageVo);

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
//                if (monitorSiteDetail.getImages() != null) {
//
//                    MySiteImageVo mySiteImageVo = new MySiteImageVo();
//                    mySiteImageVo.setImage(resultData.getImage());
//                    monitorSiteDetail.getImages().add(0, mySiteImageVo);
//                    chapterVideoAdapter.notifyDataSetChanged();
//                }

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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, MonitorSiteDetailActivity.this.getApplicationContext().getPackageName() + ".provider", f));
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
        if (isImageCapture) {
            Intent dataIntent = new Intent();
            dataIntent.putExtra(FeedParams.POSITION_CLICKED, positionClicked);
            setResult(RESULT_OK, dataIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        MonitorSiteDetailActivity.this.finish();

        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(MonitorSiteDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MonitorSiteDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
                myLongitude = Double.parseDouble(fused.lon);
                myLatitude = Double.parseDouble(fused.lat);

//                Toast.makeText(context, "Longitude:" + fused.lon + "\nfused.lat:" + fused.lat, Toast.LENGTH_SHORT).show();
            }
//            else {
//                Toast.makeText(context, "Not Able to get location_pointer.please turn on your GPS", Toast.LENGTH_SHORT).show();
//            }
        } else {
            new GpsActivation(MonitorSiteDetailActivity.this).enableGPS();
        }
    }
}
