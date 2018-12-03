package ridersadvisor.com.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.activities.ActivitySiteImageCapture;
import ridersadvisor.com.activities.SelectOptionActivity;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.BaseVO;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MasterDataVo;
import ridersadvisor.com.models.MediaOwnerVo;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.Constants;
import ridersadvisor.com.utils.Fused;
import ridersadvisor.com.utils.GpsActivation;
import ridersadvisor.com.utils.TrackGPS;
import ridersadvisor.com.utils.Util;

import static ridersadvisor.com.utils.Constants.REQUEST_PERMISSION_GPS_SETTING;

/**
 * Created by Sudhir Singh on 22,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AddSiteFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_MEDIA_OWNER = 1001;
    public static final int REQUEST_SITE_FACING = 1002;
    public static final int REQUEST_SITE_TYPE = 1003;
    public static final int REQUEST_LIGHT_TYPE = 1004;
    public static final int REQUEST_SITE_POSITION = 1005;
    private MySiteDetailVo mySiteDetailVo;
    private double longitude = 0;
    private double latitude = 0;
    private Activity context;
    private ScrollView mSvContainer;

    private TextInputLayout txtTextLocation;
    private EditText edtTextLocation;

    private TextInputLayout txtInputSitePositionII;
    private EditText edtSitePositionII;

    private TextInputLayout txtPincode;
    private EditText edtPincode;


    //    private TextInputLayout txtSpinner_media_owner;
//    private EditText edtSpinner_media_owner;
    private TextInputLayout txtInputLandmarks;
    private EditText edtInputLandmarks;
    private TextInputLayout txtInputLocality;
    private EditText edtInputLocality;
    //    private Spinner spnSiteFacing;
//    private Spinner spnSiteType;
    private TextInputLayout txtSiteType;
    private EditText edtSiteType;
    private TextInputLayout txtInputSiteAngle;
    private EditText edtInputSiteAngle;
    private TextInputLayout txtInputSiteSize;
    private EditText edtInputSiteSize;
    private TextInputLayout txtInputFacingTraffic;
    private EditText edtFacingTraffic;
    private RadioGroup rdgIllumintaioStatus;
    private RadioButton rdbYes;
    private RadioButton rdbNo;
    private RadioButton rdbLed;
    private RadioGroup rdgSideOfRoad;
    private RadioButton rdbLeft;
    private RadioButton rdbRight;

    private TextInputLayout txtInputLightType;
    private EditText edtInputLightType;

    private TextInputLayout txtInputSitePosition;
    private EditText edtInputSitePosition;
    //    private Spinner spnSitePosition;
    private TextInputLayout txtInputLicense;
    private EditText edtInputLicense;
    private TextView txtNext;
    private ProgressDialog pDialog;
    //    private String mediaOwnerId = "";
    private String localityId = "";
    private String siteTypeId = "";
    private String lightingTypeId = "";
    private String sitePositionId = "";
    private Fused fused;
    private MasterDataVo masterDataVo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_add_site, container, false);

        context = getActivity();

        fused = new Fused(context, 1);
        fused.onStart();

        findViews(mView);
        setTextWatcher();
        getLocation();

        return mView;
    }

    private void findViews(View rootView) {

        mSvContainer = (ScrollView) rootView.findViewById(R.id.mSvContainer);
        txtTextLocation = (TextInputLayout) rootView.findViewById(R.id.txtInputTextLocation);
        edtTextLocation = (EditText) rootView.findViewById(R.id.edtTextLocation);
        txtInputLandmarks = (TextInputLayout) rootView.findViewById(R.id.txtInputLandmarks);
        edtInputLandmarks = (EditText) rootView.findViewById(R.id.edtInputLandmarks);
        txtInputLocality = (TextInputLayout) rootView.findViewById(R.id.txtInputLocality);
        edtInputLocality = (EditText) rootView.findViewById(R.id.edtInputLocality);
        txtSiteType = (TextInputLayout) rootView.findViewById(R.id.txtSiteType);
        edtSiteType = (EditText) rootView.findViewById(R.id.edtSiteType);
        txtInputSiteAngle = (TextInputLayout) rootView.findViewById(R.id.txtInputSiteAngle);
        edtInputSiteAngle = (EditText) rootView.findViewById(R.id.edtInputSiteAngle);
        txtInputSiteSize = (TextInputLayout) rootView.findViewById(R.id.txtInputSiteSize);
        edtInputSiteSize = (EditText) rootView.findViewById(R.id.edtInputSiteSize);
        txtInputFacingTraffic = (TextInputLayout) rootView.findViewById(R.id.txtInputFacingTraffic);
        edtFacingTraffic = (EditText) rootView.findViewById(R.id.edtFacingTraffic);
        rdgIllumintaioStatus = (RadioGroup) rootView.findViewById(R.id.rdgIllumintaioStatus);
        rdbYes = (RadioButton) rootView.findViewById(R.id.rdbYes);
        rdbNo = (RadioButton) rootView.findViewById(R.id.rdbNo);
        rdbLed = (RadioButton) rootView.findViewById(R.id.rdbLed);
        rdgSideOfRoad = (RadioGroup) rootView.findViewById(R.id.rdgSideOfRoad);
        rdbLeft = (RadioButton) rootView.findViewById(R.id.rdbLeft);
        rdbRight = (RadioButton) rootView.findViewById(R.id.rdbRight);
        txtInputLightType = (TextInputLayout) rootView.findViewById(R.id.txtInputLightType);
        edtInputLightType = (EditText) rootView.findViewById(R.id.edtInputLightType);
        txtPincode = (TextInputLayout) rootView.findViewById(R.id.txtPincode);
        edtPincode = (EditText) rootView.findViewById(R.id.edtPincode);
        txtInputSitePosition = (TextInputLayout) rootView.findViewById(R.id.txtInputSitePosition);
        edtInputSitePosition = (EditText) rootView.findViewById(R.id.edtSitePosition);
        txtInputSitePositionII = (TextInputLayout) rootView.findViewById(R.id.txtInputSitePositionII);
        edtSitePositionII = (EditText) rootView.findViewById(R.id.edtSitePositionII);

//        spnSitePosition = (Spinner) rootView.findViewById(R.id.spnSitePosition);
        txtInputLicense = (TextInputLayout) rootView.findViewById(R.id.txtInputLicense);
        edtInputLicense = (EditText) rootView.findViewById(R.id.edtInputLicense);
        txtNext = (TextView) rootView.findViewById(R.id.txtNext);

        txtNext.setOnClickListener(this);
        edtInputLocality.setOnClickListener(this);
        edtSiteType.setOnClickListener(this);
        edtInputLightType.setOnClickListener(this);
        edtInputSitePosition.setOnClickListener(this);

        try {
            masterDataVo = (MasterDataVo) readMasterData(context, FeedParams.MASTER_DATA);
            if (masterDataVo != null) {
                setMasterData();
            }
        } catch (Exception e) {

        }

        if (masterDataVo == null) {
            fetchMasterData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == txtNext) {

            if (isAllDataEntered()) {
//                if (Util.isNetworkOnline(context)) {
//                    submitSiteData(mySiteDetailVo);
//                } else {
                Intent addImageIntent = new Intent(context, ActivitySiteImageCapture.class);
                addImageIntent.putExtra(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo);
                addImageIntent.putExtra(FeedParams.IS_IMAGE_COMPULASARY, "" + true);
                startActivity(addImageIntent);

                clearData();

//                    showToast(getString(R.string.no_internet));
//                }
            }

        }

//        else if (v == edtSpinner_media_owner) {
//            if (masterDataVo != null) {
//                ArrayList<MediaOwnerVo> mediaOwneList = masterDataVo.getMediaOwner();
//                Intent selectOption = new Intent(context, SelectOptionActivity.class);
//                selectOption.putExtra("LIST_DATA", mediaOwneList);
//                selectOption.putExtra("TITLE", getResources().getString(R.string.media_owner));
//                startActivityForResult(selectOption, REQUEST_MEDIA_OWNER);
//                context.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
//            }
//        }
        else if (v == edtInputLocality) {
            if (masterDataVo != null) {
                ArrayList<MediaOwnerVo> mediaOwneList = masterDataVo.getLocation();
                Intent selectOption = new Intent(context, SelectOptionActivity.class);
                selectOption.putExtra("LIST_DATA", mediaOwneList);
                selectOption.putExtra("TITLE", getResources().getString(R.string.locality));
                startActivityForResult(selectOption, REQUEST_SITE_FACING);
                context.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        }
        else if (v == edtSiteType) {
            if (masterDataVo != null) {
                ArrayList<MediaOwnerVo> mediaOwneList = masterDataVo.getSiteType();
                Intent selectOption = new Intent(context, SelectOptionActivity.class);
                selectOption.putExtra("LIST_DATA", mediaOwneList);
                selectOption.putExtra("TITLE", getResources().getString(R.string.site_type));
                startActivityForResult(selectOption, REQUEST_SITE_TYPE);
                context.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        } else if (v == edtInputLightType) {
            if (masterDataVo != null) {
                ArrayList<MediaOwnerVo> mediaOwneList = masterDataVo.getLightingType();
                Intent selectOption = new Intent(context, SelectOptionActivity.class);
                selectOption.putExtra("LIST_DATA", mediaOwneList);
                selectOption.putExtra("TITLE", getResources().getString(R.string.lighting_type));
                startActivityForResult(selectOption, REQUEST_LIGHT_TYPE);
                context.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        } else if (v == edtInputSitePosition) {
            if (masterDataVo != null) {
                ArrayList<MediaOwnerVo> mediaOwneList = masterDataVo.getSitePosition();
                Intent selectOption = new Intent(context, SelectOptionActivity.class);
                selectOption.putExtra("LIST_DATA", mediaOwneList);
                selectOption.putExtra("TITLE", getResources().getString(R.string.site_position));
                startActivityForResult(selectOption, REQUEST_SITE_POSITION);
                context.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == context.RESULT_OK) {

            MediaOwnerVo mediaOwnerVo = (MediaOwnerVo) data.getExtras().get(FeedParams.MEDIA_OWNER_ID);

            switch (requestCode) {

                case REQUEST_MEDIA_OWNER:

//                    edtSpinner_media_owner.setText(mediaOwnerVo.getLabel());
//                    mediaOwnerId = mediaOwnerVo.getId();

                    break;

                case REQUEST_SITE_FACING:

                    edtInputLocality.setText(mediaOwnerVo.getLabel());
                    localityId = mediaOwnerVo.getId();

                    break;

                case REQUEST_SITE_TYPE:

                    edtSiteType.setText(mediaOwnerVo.getLabel());
                    siteTypeId = mediaOwnerVo.getId();

                    break;
                case REQUEST_LIGHT_TYPE:

                    edtInputLightType.setText(mediaOwnerVo.getLabel());
                    lightingTypeId = mediaOwnerVo.getId();

                    break;
                case REQUEST_SITE_POSITION:

                    edtInputSitePosition.setText(mediaOwnerVo.getLabel());
                    sitePositionId = mediaOwnerVo.getId();

                    break;
            }
        }
    }

    private void fetchMasterData() {
        if (Util.isNetworkOnline(context)) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.fetching_data));
            pDialog.setCancelable(false);
            pDialog.show();


            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));
            placeRequest(APIMethods.GET_MASTER_DATA, MasterDataVo.class, params, true, null);

        } else {
//            showToast(R.string.no_internet);
        }

    }



    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_MASTER_DATA)) {

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            masterDataVo = (MasterDataVo) response;

            setMasterData();
            saveMasterData(context, masterDataVo, FeedParams.MASTER_DATA);

        } else if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_SITE)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                BaseVO resultData = (BaseVO) response;
//                Gson gson = new Gson();
//                String json = gson.toJson(resultData);
//                JSONObject responseObj = new JSONObject(new String(json));
                //JSONObject result = responseObj.getJSONObject("result");
//                String message = responseObj.getString("message");
                String message = resultData.getMessage();
                showToast(message);

                clearData();

                mySiteDetailVo.setId(resultData.getProperty_site_id());
                mySiteDetailVo.setStatus("Y");
                Intent addImageIntent = new Intent(context, ActivitySiteImageCapture.class);
                addImageIntent.putExtra(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo);
//                addImageIntent.putExtra(FeedParams.LATTITUDE, "" + latitude);
//                addImageIntent.putExtra(FeedParams.LONGITUDE, "" + longitude);
                addImageIntent.putExtra(FeedParams.IS_IMAGE_COMPULASARY, "" + true);
                startActivity(addImageIntent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setMasterData() {

        MediaOwnerVo mediaOwnerVo = new MediaOwnerVo();
        mediaOwnerVo.setId("-1");
        mediaOwnerVo.setLabel("Unknown");

        masterDataVo.getMediaOwner().add(0, mediaOwnerVo);
        List<MediaOwnerVo> mediaOwnerVos = masterDataVo.getMediaOwner();

        masterDataVo.getSiteFacing().add(0, mediaOwnerVo);
        List<MediaOwnerVo> siteFacingVos = masterDataVo.getSiteFacing();

        masterDataVo.getSiteType().add(0, mediaOwnerVo);
        List<MediaOwnerVo> siteTypeVos = masterDataVo.getSiteType();

        masterDataVo.getLightingType().add(0, mediaOwnerVo);
        List<MediaOwnerVo> lightingTypeVos = masterDataVo.getLightingType();

        masterDataVo.getSitePosition().add(0, mediaOwnerVo);
        List<MediaOwnerVo> sitePositionVos = masterDataVo.getSitePosition();

        masterDataVo.getLocation().add(0, mediaOwnerVo);
        List<MediaOwnerVo> siteLocationVo = masterDataVo.getLocation();



//        List<IlluminationStatusVo> illuminationStatusVos = masterDataVo.getIlluminationStatusVos();

//        if (illuminationStatusVos != null) {
//            if (illuminationStatusVos.size() > 1) {
        rdbYes.setText(getResources().getString(R.string.lit));
        rdbNo.setText(getResources().getString(R.string.non_lit));
        rdbLed.setText(getResources().getString(R.string.led));
//            }
//        }
    }

    private synchronized void saveMasterData(Context context, Object object, String binFileName) {
        try {
            String tempPath = context.getFilesDir() + "/" + binFileName + ".bin";
            File file = new File(tempPath);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized Object readMasterData(Context context, String binFileName) {
        Object obj = null;
        try {
            String tempPath = context.getFilesDir() + "/" + binFileName + ".bin";
            File file = new File(tempPath);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                obj = ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_MASTER_DATA)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_SITE)) {
            try {

                Intent addImageIntent = new Intent(context, ActivitySiteImageCapture.class);
                addImageIntent.putExtra(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo);
                addImageIntent.putExtra(FeedParams.IS_IMAGE_COMPULASARY, "" + true);
                startActivity(addImageIntent);


                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                // JSONObject result = errorObject.getJSONObject("result");
//                String message = errorObject.getString("message");
//                showToast(message);


            } catch (JSONException e) {
                e.printStackTrace();
                showToast(getString(R.string.error_in_saving_data));
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getString(R.string.error_in_saving_data));
            }
        }
    }

    private void clearData() {


        localityId ="";
        siteTypeId="";
        sitePositionId="";
        lightingTypeId="";

        edtTextLocation.setText("");
        edtInputLocality.setText("");
        edtSiteType.setText("");
        edtInputSitePosition.setText("");
        edtSitePositionII.setText("");
        edtInputLandmarks.setText("");
        edtInputSiteAngle.setText("");
        edtFacingTraffic.setText("");
        edtPincode.setText("");
        edtInputLightType.setText("");
        edtInputSiteSize.setText("");
        edtInputLicense.setText("");

        edtTextLocation.requestFocus();
        focusOnView(edtTextLocation);

    }

    private void setTextWatcher() {

        edtTextLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLocality.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        edtSiteFacing.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                txtSiteFacing.setErrorEnabled(false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        edtSiteType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtSiteType.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtInputLandmarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLandmarks.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        edtInputLocality.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                txtInputLocality.setErrorEnabled(false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        edtInputSiteAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSiteAngle.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtInputSiteSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSiteSize.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtFacingTraffic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputFacingTraffic.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtInputLightType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLightType.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtPincode.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtInputSitePosition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSitePosition.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSitePositionII.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSitePositionII.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtInputLicense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputLicense.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean isAllDataEntered() {

        if (edtTextLocation.getText().toString().length() <= 0) {
            txtTextLocation.setErrorEnabled(true);
            txtTextLocation.setError("Enter location.");
            edtTextLocation.requestFocus();
            focusOnView(edtTextLocation);
            return false;
        }

        String illluminationId = "Yes";
        int selectedId = rdgIllumintaioStatus.getCheckedRadioButtonId();
        if (selectedId == rdbYes.getId()) {
            illluminationId = "lit";
        } else if (selectedId == rdbNo.getId()) {
            illluminationId = "Non-lit";
        } else if (selectedId == rdbLed.getId()) {
            illluminationId = "LED";
        }

        String sideOfRoadId = "Left";
        int selectedSideId = rdgSideOfRoad.getCheckedRadioButtonId();
        if (selectedSideId == rdbLeft.getId()) {
            sideOfRoadId = "Left";
        } else if (selectedSideId == rdbRight.getId()) {
            sideOfRoadId = "Right";
        }

        if (latitude == 0 || longitude == 0) {
            getLocation();
        }

        ArrayList<MySiteImageVo> images = new ArrayList<>();
        mySiteDetailVo = new MySiteDetailVo(edtTextLocation.getText().toString().trim(),
                "id" + System.currentTimeMillis(), "", "", "",
                edtInputLandmarks.getText().toString().trim(), localityId,
                edtTextLocation.getText().toString().trim(),
                String.valueOf(latitude), String.valueOf(longitude), "", siteTypeId,
                edtInputSiteSize.getText().toString().trim(),
                edtInputSiteAngle.getText().toString().trim(), edtFacingTraffic.getText().toString().trim(),
                illluminationId, lightingTypeId, edtInputLicense.getText().toString().trim(), sitePositionId,
                "N", "", "", "", "",
                "", "",
                "", "",
                edtSiteType.getText().toString(), edtInputSitePosition.getText().toString(),
                edtSitePositionII.getText().toString(), sideOfRoadId, edtPincode.getText().toString(),
                images);

//        else if (mediaOwnerId.length() <= 0) {
////            showToast("Select media owner.");
//            txtSpinner_media_owner.setErrorEnabled(true);
//            txtSpinner_media_owner.setError("Select media owner.");
//            edtSpinner_media_owner.requestFocus();
//            focusOnView(edtSpinner_media_owner);
//            return false;
//        } else if (edtInputLandmarks.getText().toString().length() <= 0) {
//            txtInputLandmarks.setErrorEnabled(true);
//            txtInputLandmarks.setError("Enter landmark.");
//            edtInputLandmarks.requestFocus();
//            focusOnView(edtInputLandmarks);
//            return false;
//        } else if (edtInputLocality.getText().toString().length() <= 0) {
//            txtInputLocality.setErrorEnabled(true);
//            txtInputLocality.setError("Enter localityId.");
//            edtInputLocality.requestFocus();
//            focusOnView(edtInputLocality);
//            return false;
//        } else if (siteFacingId.length() <= 0) {
//            txtSiteFacing.setErrorEnabled(true);
//            txtSiteFacing.setError("Select site facing.");
//            edtSiteFacing.requestFocus();
//            focusOnView(edtSiteFacing);
//            return false;
//        } else if (siteTypeId.length() <= 0) {
//            showToast("Select site type.");
//            txtSiteType.setErrorEnabled(true);
//            txtSiteType.setError("Select site type.");
//            edtSiteType.requestFocus();
//            focusOnView(edtSiteType);
//            return false;
//        } else if (edtInputSiteAngle.getText().toString().length() <= 0) {
//            txtInputSiteAngle.setErrorEnabled(true);
//            txtInputSiteAngle.setError("Enter site angle.");
//            edtInputSiteAngle.requestFocus();
//            focusOnView(edtInputSiteAngle);
//            return false;
//        } else if (edtInputSiteSize.getText().toString().length() <= 0) {
//            txtInputSiteSize.setErrorEnabled(true);
//            txtInputSiteSize.setError("Enter site size.");
//            edtInputSiteSize.requestFocus();
//            focusOnView(edtInputSiteSize);
//            return false;
//        } else if (edtInputRoadDirection.getText().toString().length() <= 0) {
//            txtInputRoadDirection.setErrorEnabled(true);
//            txtInputRoadDirection.setError("Enter road direction.");
//            edtInputRoadDirection.requestFocus();
//            focusOnView(edtInputRoadDirection);
//            return false;
//        } else if (lightingTypeId.length() <= 0) {
//            showToast("Select lighting type.");
//            txtInputLightType.setErrorEnabled(true);
//            txtInputLightType.setError("Select lighting type.");
//            edtInputLightType.requestFocus();
//            focusOnView(edtInputLightType);
//            return false;
//        } else if (sitePositionId.length() <= 0) {
//            txtInputSitePosition.setErrorEnabled(true);
//            txtInputSitePosition.setError("Select site position.");
//            edtInputSitePosition.requestFocus();
//            focusOnView(edtInputSitePosition);
//            return false;
//        } else if (edtInputLicense.getText().toString().length() <= 0) {
//            txtInputLicense.setErrorEnabled(true);
//            txtInputLicense.setError("Enter license.");
//            edtInputLicense.requestFocus();
////            focusOnView(edtInputLicense);
//            return false;
//        }


        return true;
    }

    private void focusOnView(final View view) {
        mSvContainer.post(new Runnable() {
            @Override
            public void run() {
                mSvContainer.scrollTo(0, view.getTop());
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.ACCESS_FINE_LOCATION_PERMISSION_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUpdatedLocation();
                } else {
                    showToast(getResources().getString(R.string.location_permission_decline));
                }
                break;
        }
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Util.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                callLocationManager();
                getUpdatedLocation();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
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
            new GpsActivation(context).enableGPS();
        }
    }


}
