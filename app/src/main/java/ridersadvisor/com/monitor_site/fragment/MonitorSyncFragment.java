package ridersadvisor.com.monitor_site.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import java.util.ArrayList;

import ridersadvisor.com.R;
import ridersadvisor.com.fragments.BaseFragment;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.monitor_site.adapters.OfflineMonitorDataAdapter;
import ridersadvisor.com.monitor_site.prefs.OfflineMonitorImagePrefs;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.ImageUtility;

import static ridersadvisor.com.networking.RequestManager.LIVE_SERVER;

/**
 * Created by Sudhir Singh on 24,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MonitorSyncFragment extends BaseFragment {
    OfflineMonitorDataAdapter mySiteAdapter;
    private RecyclerView rclMySite;
    private ImageView imgSiteListView;
    private TextView txtSiteListView;
    private ImageView imgSiteMapView;
    private TextView txtSiteMapView;
    private Context context;
    private RelativeLayout relSiteListView;
    private RelativeLayout relSiteMapView;
    private ProgressDialog pDialog;
    //    private ArrayList<MySiteImageVo> mySiteDetailList;
    private ArrayList<MySiteImageVo> offlineSiteImages;

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

        try {
            offlineSiteImages = OfflineMonitorImagePrefs.loadOfflineImages(context);
            if (offlineSiteImages != null && offlineSiteImages.size() > 0) {

//                    String id = mySiteDetailList.get(counter).getId();
//                    offlineImageList = OfflineCaptureImagesPrefs.getOfflineImage(context, id);
//                    mySiteDetailList.get(counter).setImages(offlineImageList);

                mySiteAdapter = new OfflineMonitorDataAdapter(context, offlineSiteImages);
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
            if (offlineSiteImages != null && offlineSiteImages.size() > 0) {

                pDialog = new ProgressDialog(context);
                pDialog.setMessage(getResources().getString(R.string.sending_data));
                pDialog.setCancelable(false);
                pDialog.show();

                MySiteImageVo mySiteImageVo = offlineSiteImages.get(0);
                new GetImageToSync().execute(mySiteImageVo);

            } else {
                showToast("No data to sync");
            }

        } catch (Exception e) {

        }

    }

    private void submitData(JSONObject jsonObject) throws JSONException {


        String url = LIVE_SERVER.concat(APIMethods.SAVE_MONITOR_SITE_IMAGE);
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.i("image req", jsonObject.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                Log.i("offline succ res", response.toString());
                try {
                    String status = response.getString("error");
                    if (status.equalsIgnoreCase("false")) {
                        try {

                            String path = offlineSiteImages.get(0).getImage();
                            File f = new File(path);
                            if (f.exists()) {
                                Log.d("DELETED FILE PATH : ", path);
                                f.delete();
                            }


                            if (offlineSiteImages != null && offlineSiteImages.size() > 0) {

                                OfflineMonitorImagePrefs.removeOfflineImage(context, offlineSiteImages.get(0).getSiteId());
//                                AddSiteDetailPrefs.removeOfflineSiteData(context, 0);
                                offlineSiteImages.remove(0);
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

    private class GetImageToSync extends AsyncTask<MySiteImageVo, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(MySiteImageVo... mySiteImageVo) {


            JSONObject params = new JSONObject();
            try {
                params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
                params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));
                params.put(FeedParams.SITE_ID, "" + mySiteImageVo[0].getSiteId());
                params.put(FeedParams.TYPE, "" + mySiteImageVo[0].getType());
                JSONArray imageArray = new JSONArray();
                try {

                    JSONObject imageParams = new JSONObject();
                    Bitmap bitmap = ImageUtility.loadImageFromStorage(mySiteImageVo[0].getImage(), context);
                    String imageString = "";
                    if (bitmap != null) {
                        imageString = ImageUtility.convertBitmapToString(bitmap);
                    }
                    imageParams.put(FeedParams.PROPERTY_SITE_ID, mySiteImageVo[0].getId());
                    imageParams.put(FeedParams.ALLOCATION_ID, mySiteImageVo[0].getAllocation_id());
                    imageParams.put(FeedParams.IMAGE, imageString);
                    imageParams.put(FeedParams.REMARKS, mySiteImageVo[0].getRemarks());
                    imageParams.put(FeedParams.CAPTURE_TIME, "" + mySiteImageVo[0].getCapture_time());
                    imageParams.put(FeedParams.FILE_NAME, "" + mySiteImageVo[0].getCapture_time() + ".jpg");
                    imageParams.put(FeedParams.LONGITUDE, "" + mySiteImageVo[0].getLongitude());
                    imageParams.put(FeedParams.LATTITUDE, "" + mySiteImageVo[0].getLattitude());
                    imageArray.put(0, imageParams);


                } catch (Exception e) {

                }

                params.put(FeedParams.IMAGE_DATA, imageArray);

            } catch (Exception e) {

            }

            return params;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                submitData(jsonObject);
            } catch (Exception e) {

            }
        }
    }
}
