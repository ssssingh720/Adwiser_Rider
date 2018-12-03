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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ridersadvisor.com.R;
import ridersadvisor.com.models.BaseVO;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
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
 * Created by Sudhir Singh on 27,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ActivitySiteImageCapture extends AppBaseActivity implements View.OnClickListener {

    private static final int PERMISSION_CAMERA_REQUEST_CODE = 2;
    private static final int PERMISSION_STORAGE_REQUEST_CODE = 3;
    private static final int REQUEST_OPEN_CAMERA_SETTING_PAGE = 4;
    private static final int REQUEST_OPEN_GALLERY_SETTING_PAGE = 5;
    private static final int OPEN_CAMERA_FROM_M_OR_GREATER = 1;
    private static final int OPEN_CAMERA_FROM_L_OR_LOWER = 2;
    private static final int OPEN_GALLARY_FROM_M_OR_GREATER = 3;
    private static final int OPEN_GALLARY_FROM_L_OR_LOWER = 4;
    String mStoredImagePath = "";
    //    private Button mBtnCamera;
//    private Button mBtnGalery;
    private ImageView mIvCard;
    private Uri mUri;
    private String IMAGE_NAME = "";
    private Context context;
    private ProgressDialog pDialog;
    private boolean isOneImageUploaded = false;
    private boolean isImageCompulsary = true;

    //    private String latitude = "";
//    private String longitude = "";
//    private String property_site_id = "";
    private MySiteDetailVo mySiteDetailVo;

    private Fused fused;
    private double longitude = 0;
    private double latitude = 0;

    private RelativeLayout relImageCaptured;
    private ImageView imgCapturedImage;
    private EditText edtImgRemarks;
    private ImageView imgSendImage;

    public static boolean checkPermission(Activity activity, String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        return isGranted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cheque_image_capture);
        context = ActivitySiteImageCapture.this;

        fused = new Fused(context, 1);
        fused.onStart();

        mySiteDetailVo = (MySiteDetailVo) getIntent().getExtras().get(FeedParams.PROPERTY_SITE_ID);
        isImageCompulsary = getIntent().getExtras().getBoolean(FeedParams.IS_IMAGE_COMPULASARY);

        AddSiteDetailPrefs.addFormData(context, mySiteDetailVo);

        findViews();
    }

    private void findViews() {
        LinearLayout lntSiteImgBack = (LinearLayout) findViewById(R.id.lntSiteImgBack);
        mIvCard = (ImageView) findViewById(R.id.iv_card);
        TextView txtSelectImage = (TextView) findViewById(R.id.txtSelectImage);
        relImageCaptured = (RelativeLayout) findViewById(R.id.relImageCaptured);
        imgCapturedImage = (ImageView) findViewById(R.id.imgCapturedImage);
        edtImgRemarks = (EditText) findViewById(R.id.edtImgRemarks);
        imgSendImage = (ImageView) findViewById(R.id.imgSendImage);

        relImageCaptured.setVisibility(View.GONE);

        lntSiteImgBack.setOnClickListener(this);
        mIvCard.setOnClickListener(this);
        txtSelectImage.setOnClickListener(this);
        imgSendImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_card:
            case R.id.txtSelectImage:
                checkCameraPermission();
                break;

            case R.id.btn_galery:
                checkGalleryPermission();
                break;

            case R.id.btn_done:
                if (checkValidity()) {
                    submitSiteImage();
                } else {
                    displayMessageSnakBar(getString(R.string.please_capture_image));
                }
                break;

            case R.id.btn_Cancel:

                onBackPressed();

                break;

            case R.id.imgSendImage:

                submitSiteImage();

                break;

            case R.id.lntSiteImgBack:

                onBackPressed();

                break;

        }
    }

    private void submitSiteImage() {

        MySiteImageVo mySiteImageVo = new MySiteImageVo(mySiteDetailVo.getId(), "allocation_id",mStoredImagePath, "" + System.currentTimeMillis(),
                String.valueOf(latitude), String.valueOf(longitude),
                "" + System.currentTimeMillis() + ".jpg", edtImgRemarks.getText().toString().trim(),false,false,"Not available");
        OfflineCaptureImagesPrefs.addOfflineImage(context, mySiteImageVo);
        showToast(getString(R.string.successfully_saved));

        relImageCaptured.setVisibility(View.GONE);
        edtImgRemarks.setText("");

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_IMAGE)) {
            isOneImageUploaded = true;
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                BaseVO resultData = (BaseVO) response;
                String message = resultData.getMessage();
                mUri = null;
                showToast(message);
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

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getImageFromGallery();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE_REQUEST_CODE);
                } else {
                    openSettingForExternalStorage();
                }
            }
        } else {
            getImageFromGallery();
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
                displayMessageSnakBar(getString(R.string.go_to_permission));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                displayMessageSnakBar(getString(R.string.permission_required));
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void openSettingForExternalStorage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.storage_permission_required);
        alertDialog.setMessage(R.string.show_external_setting);

        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_OPEN_GALLERY_SETTING_PAGE);
                displayMessageSnakBar(getString(R.string.grant_storage));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                displayMessageSnakBar(getString(R.string.permission_required));
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private boolean checkValidity() {
        return mUri != null;
    }

    private void captureImageFromCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMAGE_NAME = "ebiz_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), IMAGE_NAME);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, ActivitySiteImageCapture.this.getApplicationContext().getPackageName() + ".provider", f));
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

    public void getImageFromGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intentGalleryM = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGalleryM, "Select Image From Gallery"), OPEN_GALLARY_FROM_M_OR_GREATER);
        } else {
            Intent intentGalleryL = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGalleryL, "Select Image From Gallery"), OPEN_GALLARY_FROM_L_OR_LOWER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mStoredImagePath = "";
        getLocation();
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == OPEN_GALLARY_FROM_M_OR_GREATER) {
            if (data != null) {
                mUri = data.getData();
                Bitmap bitmap = ImageUtility.getBitmapFromUri(this, mUri);
                if (bitmap != null) {
                    checkAndSubmitData(bitmap);
                }
            }
        } else if (requestCode == OPEN_GALLARY_FROM_L_OR_LOWER) {
            if (data != null) {
                mUri = data.getData();
                Bitmap bitmap = ImageUtility.getBitmapFromUri(this, mUri);
                if (bitmap != null) {
                    checkAndSubmitData(bitmap);
                }
            }
        }
    }

    private void checkAndSubmitData(Bitmap bitmap) {
        mStoredImagePath = ImageUtility.insertBitmapToAppFolder(bitmap, this);
        relImageCaptured.setVisibility(View.VISIBLE);
        imgCapturedImage.setImageBitmap(ImageUtility.loadImageFromStorage(mStoredImagePath, context));

    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String reqPermission[], int[] reqPermissionResult) {
        switch (requestCode) {
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (reqPermissionResult.length == 3 && reqPermissionResult[0] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[1] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[2] == PackageManager.PERMISSION_GRANTED) {
                    captureImageFromCamera();
                } else {
                    displayMessageSnakBar(getString(R.string.camera_permission_decline));
                }
                break;
            case PERMISSION_STORAGE_REQUEST_CODE:
                if (reqPermissionResult.length == 2 && reqPermissionResult[0] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[1] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                } else {
                    displayMessageSnakBar(getString(R.string.gallery_permission_cancel));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void displayMessageSnakBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.lnrImageCapture), message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {

        if (isImageCompulsary) {
            if (isOneImageUploaded) {
                ActivitySiteImageCapture.this.finish();
                overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
            } else {
                displayMessageSnakBar(getString(R.string.please_add_one_image));
            }
        } else {
            ActivitySiteImageCapture.this.finish();
            overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
        }
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(ActivitySiteImageCapture.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ActivitySiteImageCapture.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
            }
        } else {
            new GpsActivation(ActivitySiteImageCapture.this).enableGPS();
        }
    }
}
