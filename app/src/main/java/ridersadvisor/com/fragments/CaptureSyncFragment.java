package ridersadvisor.com.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stone.vega.library.VegaLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ridersadvisor.com.R;
import ridersadvisor.com.adapters.OfflineDataAdapter;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.AddSiteDetailPrefs;
import ridersadvisor.com.utils.ImageUtility;
import ridersadvisor.com.utils.OfflineCaptureImagesPrefs;

import static ridersadvisor.com.networking.RequestManager.LIVE_SERVER;

/**
 * Created by Sudhir Singh on 16,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CaptureSyncFragment extends BaseFragment implements View.OnClickListener {
    OfflineDataAdapter mySiteAdapter;
    private RecyclerView rclMySite;
    private ImageView imgSiteListView;
    private TextView txtSiteListView;
    private ImageView imgSiteMapView;
    private TextView txtSiteMapView;
    private Context context;
    private RelativeLayout relSiteListView;
    private RelativeLayout relSiteMapView;
    private ProgressDialog pDialog;
    //    private GoogleMap map;
//    private SupportMapFragment mapFragment;
//    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<MySiteDetailVo> mySiteDetailList;
    private int currentPosition = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.my_site_fragment, container, false);

        context = getActivity();

        rclMySite = (RecyclerView) mView.findViewById(R.id.rclMySite);
        rclMySite.setLayoutManager(new VegaLayoutManager());

        relSiteListView = (RelativeLayout) mView.findViewById(R.id.relSiteListView);
        imgSiteListView = (ImageView) mView.findViewById(R.id.imgSiteListView);
        txtSiteListView = (TextView) mView.findViewById(R.id.txtSiteListView);

        relSiteMapView = (RelativeLayout) mView.findViewById(R.id.relSitMapView);
        imgSiteMapView = (ImageView) mView.findViewById(R.id.imgSiteMapView);
        txtSiteMapView = (TextView) mView.findViewById(R.id.txtSiteMapView);

//        relSiteListView.setOnClickListener(this);
//        relSiteMapView.setOnClickListener(this);
        try {

            mySiteDetailList = AddSiteDetailPrefs.loadOfflineSiteData(context);
            if (mySiteDetailList != null && mySiteDetailList.size() > 0) {

                ArrayList<MySiteImageVo> offlineImageList = new ArrayList<>();

                for (int counter = 0; counter < mySiteDetailList.size(); counter++) {

                    String id = mySiteDetailList.get(counter).getId();
                    offlineImageList = OfflineCaptureImagesPrefs.getOfflineImage(context, id);

                    mySiteDetailList.get(counter).setImages(offlineImageList);
                }

                mySiteAdapter = new OfflineDataAdapter(context, mySiteDetailList);
                rclMySite.setAdapter(mySiteAdapter);
            } else {
                showToast("No data to sync.");
            }

        } catch (Exception e) {

        }

        return mView;
    }


    public void sendData() {

        try {
            if (mySiteDetailList != null && mySiteDetailList.size() > 0) {
                MySiteDetailVo mySiteDetailVo = mySiteDetailList.get(0);
                submitData(mySiteDetailVo);
            } else {
                showToast("No data to sync");
            }

        } catch (Exception e) {

        }

    }

    private void submitData(MySiteDetailVo mySiteDetailVo) throws JSONException {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(getResources().getString(R.string.sending_data));
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject params = new JSONObject();
//            formDataObj.put()
        try {
//            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));
            params.put(FeedParams.LOCATION,mySiteDetailVo.getLocality());
            params.put(FeedParams.MEDIA_OWNER_ID, mySiteDetailVo.getMediaOwnerId());
            params.put(FeedParams.LANDMARKS, mySiteDetailVo.getLandmarks());
            params.put(FeedParams.LOCALITY, mySiteDetailVo.getText_location());
            params.put(FeedParams.SITE_FACING_ID, mySiteDetailVo.getSiteFacingId());
            params.put(FeedParams.SITE_TYPE_ID, mySiteDetailVo.getSiteTypeId());
            params.put(FeedParams.SITE_SIZE, mySiteDetailVo.getSiteSize());
            params.put(FeedParams.SITE_ANGEL, mySiteDetailVo.getSiteAngle());
            params.put(FeedParams.ROAD_DIRECTION, mySiteDetailVo.getFacingTraffic());
            params.put(FeedParams.ILLUMINATION_STATUS, mySiteDetailVo.getIlluminationStatus());
            params.put(FeedParams.LIGHTNING_TYPE, mySiteDetailVo.getLightingType());
            params.put(FeedParams.LICENSE, mySiteDetailVo.getLicense());
//            params.put(FeedParams.LOCATION, mySiteDetailVo.getText_location());
            params.put(FeedParams.SIDE_OF_READ, mySiteDetailVo.getSide_of_road());
            params.put(FeedParams.SITE_PINCODE, mySiteDetailVo.getSite_pincode());
            params.put(FeedParams.SITE_POSITION_ID, mySiteDetailVo.getSitePositionId());
            params.put(FeedParams.SITE_POSITION_SECOND, mySiteDetailVo.getSitePositionII());
            params.put(FeedParams.LATTITUDE, "" + mySiteDetailVo.getLattitude());
            params.put(FeedParams.LONGITUDE, "" + mySiteDetailVo.getLongitude());

//            {image, filename, lattitude, longitude},

            JSONArray imageArray = new JSONArray();
            try {
                for (int counter = 0; counter < mySiteDetailList.get(0).getImages().size(); counter++) {

                    JSONObject imageParams = new JSONObject();

//                    Bitmap bitmap = ImageUtility.loadImageFromStorage(album.getImages().get(0).getImage(), mContext);
                    Bitmap bitmap = ImageUtility.loadImageFromStorage(mySiteDetailList.get(0).getImages().get(counter).getImage(), context);
                    String imageString = "";
                    if (bitmap != null) {
                        imageString = ImageUtility.convertBitmapToString(bitmap);
                    }
                    imageParams.put(FeedParams.PROPERTY_SITE_ID, mySiteDetailVo.getId());
                    imageParams.put(FeedParams.IMAGE, imageString);
                    imageParams.put(FeedParams.CAPTURE_TIME, "" + mySiteDetailList.get(0).getImages().get(counter).getCapture_time());
                    imageParams.put(FeedParams.FILE_NAME, "" + mySiteDetailList.get(0).getImages().get(counter).getCapture_time() + ".jpg");
                    imageParams.put(FeedParams.LONGITUDE, "" + mySiteDetailList.get(0).getImages().get(counter).getLongitude());
                    imageParams.put(FeedParams.LATTITUDE, "" + mySiteDetailList.get(0).getImages().get(counter).getLattitude());
                    imageArray.put(counter, imageParams);

                }

            } catch (Exception e) {

            }
            params.put(FeedParams.IMAGE_DATA, imageArray);

        } catch (Exception e) {

        }

        String url = LIVE_SERVER.concat(APIMethods.SAVE_PROPERTY_WITH_IMAGE);
//        String url = "http://adwise.ezbizsoft.in/api/savePropertyWithImage";
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.i("image req", params.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                Log.i("offline succ res", response.toString());
                try {
                    String status = response.getString("error");
                    if (status.equalsIgnoreCase("false")) {
                        try {

                            for (int counter = 0; counter < mySiteDetailList.get(0).getImages().size(); counter++) {
                                String path = mySiteDetailList.get(0).getImages().get(counter).getImage();
                                File f = new File(path);
                                if (f.exists()) {
                                    Log.d("DELETED FILE PATH : ", path);
                                    f.delete();
                                }
                            }

                            if (mySiteDetailList != null && mySiteDetailList.size() > 0) {

                                OfflineCaptureImagesPrefs.removeOfflineImage(context, mySiteDetailList.get(0).getId());
                                AddSiteDetailPrefs.removeOfflineSiteData(context, 0);
                                mySiteDetailList.remove(0);
                                sendData();
                            }
                            mySiteAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            showToast(e.getMessage());
                        }
                    } else {
                        String msg = response.optString("message", getResources().getString(R.string.error_in_sending_data));
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    showToast(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jonu err res", error.toString());
                pDialog.dismiss();
                showToast(getResources().getString(R.string.error_in_sending_data));
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.relSiteListView:

                rclMySite.setVisibility(View.VISIBLE);
                relSiteListView.setBackgroundResource(R.drawable.red_site_list_bg);
                imgSiteListView.setImageResource(R.drawable.list_view_white);
                txtSiteListView.setTextColor(getResources().getColor(R.color.white));

                relSiteMapView.setBackgroundResource(R.drawable.white_site_map_bg);
                imgSiteMapView.setImageResource(R.drawable.map_view_grey);
                txtSiteMapView.setTextColor(getResources().getColor(R.color.stoke_color));

                break;

            case R.id.relSitMapView:

                rclMySite.setVisibility(View.GONE);
//                mapFragment.getView().setVisibility(View.VISIBLE);

                relSiteListView.setBackgroundResource(R.drawable.white_site_list_bg);
                imgSiteListView.setImageResource(R.drawable.list_view_grey);
                txtSiteListView.setTextColor(getResources().getColor(R.color.stoke_color));

                relSiteMapView.setBackgroundResource(R.drawable.red_site_map_bg);
                imgSiteMapView.setImageResource(R.drawable.map_view_white);
                txtSiteMapView.setTextColor(getResources().getColor(R.color.white));

                break;

        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_WITH_IMAGE)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {

                if (mySiteDetailList != null && mySiteDetailList.size() > 0) {

                    OfflineCaptureImagesPrefs.removeOfflineImage(context, mySiteDetailList.get(0).getId());
                    AddSiteDetailPrefs.removeOfflineSiteData(context, 0);
                    mySiteDetailList.remove(0);
                    sendData();
                }
                mySiteAdapter.notifyDataSetChanged();

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

        if (apiMethod.equalsIgnoreCase(APIMethods.SAVE_PROPERTY_WITH_IMAGE)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }


}
