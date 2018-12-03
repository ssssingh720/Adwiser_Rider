package ridersadvisor.com.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.stone.vega.library.VegaLayoutManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.adapters.MySiteAdapter;
import ridersadvisor.com.manager.SharedPrefManager;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.models.MySiteVo;
import ridersadvisor.com.models.ResponseError;
import ridersadvisor.com.utils.APIMethods;
import ridersadvisor.com.utils.Util;

/**
 * Created by Sudhir Singh on 27,March,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CaptureSiteListFragment extends BaseFragment implements View.OnClickListener
//        ,        OnMapReadyCallback, ClusterManager.OnClusterClickListener<MyItem>,
//        ClusterManager.OnClusterInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>,
//        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>
{

    List<MySiteDetailVo> mySiteDetailList;
    MySiteAdapter mySiteAdapter;
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

    public static synchronized void saveSiteData(Context context, Object object, String binFileName) {
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

    public static synchronized Object readSaveSiteData(Context context, String binFileName) {
        Object obj = new Object();
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

        if (Util.isNetworkOnline(context)) {

            try {
                MySiteVo listDat = (MySiteVo) readSaveSiteData(context, FeedParams.SAVED_CAPTURE_SITE_FRAGMENT);
                if (listDat != null && listDat.getData() != null) {
                    mySiteDetailList = listDat.getData();
                    mySiteAdapter = new MySiteAdapter(context, mySiteDetailList);
                    rclMySite.setAdapter(mySiteAdapter);
                }
            }catch (Exception e){

            }

            getMySiteList();

        } else {

            try {
                MySiteVo listDat = (MySiteVo) readSaveSiteData(context, FeedParams.SAVED_CAPTURE_SITE_FRAGMENT);
                if (listDat != null && listDat.getData() != null) {
                    mySiteDetailList = listDat.getData();
                    if (mySiteAdapter == null) {
                        mySiteAdapter = new MySiteAdapter(context, mySiteDetailList);
                        rclMySite.setAdapter(mySiteAdapter);
                    } else {
                        mySiteAdapter.notifyDataSetChanged();
                    }

                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            } catch (Exception e) {
                showToast(getResources().getString(R.string.no_internet));
            }
        }

//        mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        mapFragment.getView().setVisibility(View.GONE);

        return mView;

    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        map = googleMap;
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//
//        mClusterManager = new ClusterManager<MyItem>(context, googleMap);
//        googleMap.setOnCameraIdleListener(mClusterManager);
//        googleMap.setOnMarkerClickListener(mClusterManager);
//
//        googleMap.setOnCameraIdleListener(mClusterManager);
//        googleMap.setOnMarkerClickListener(mClusterManager);
//        googleMap.setOnInfoWindowClickListener(mClusterManager);
//
//        mClusterManager.setOnClusterClickListener(this);
//        mClusterManager.setOnClusterInfoWindowClickListener(this);
//        mClusterManager.setOnClusterItemClickListener(this);
//        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
//
//    }

//    private void addMarkers(List<MySiteDetailVo> mySiteDetailList) {
//
//        for (int counter = 0; counter < mySiteDetailList.size(); counter++) {
//
//            MySiteDetailVo mySiteDetailVo = mySiteDetailList.get(counter);
//            try {
//                double latitude = Double.parseDouble(mySiteDetailVo.getLattitude());
//                double longitude = Double.parseDouble(mySiteDetailVo.getLongitude());
//
//                MyItem infoWindowItem = new MyItem(latitude, longitude, mySiteDetailVo.getSiteCode(), mySiteDetailVo.getLandmarks(),
//                        mySiteDetailVo.getMediaOwner(), mySiteDetailVo.getImages(), mySiteDetailVo.getLocality());
//                mClusterManager.addItem(infoWindowItem);
//
//                if (counter == 0) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 5));
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
////        mClusterManager.cluster();
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.relSiteListView:

//                Drawable myDrawable;
//                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
//                    myDrawable = context.getResources().getDrawable(R.drawable.red_site_list_bg, context.getTheme());
//                } else {
//                    myDrawable = context.getResources().getDrawable(R.drawable.red_site_list_bg);
//                }
                rclMySite.setVisibility(View.VISIBLE);
//                mapFragment.getView().setVisibility(View.GONE);
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

    private void getMySiteList() {

        if (Util.isNetworkOnline(context)) {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(getString(R.string.fetching_data));
            pDialog.setCancelable(false);
            pDialog.show();
//userid, usertoken , media_owner_id, landmarks, locality, site_facing_id, site_type_id, site_size,
// site_angel, road_direction, illumination_status, lighting_type, license, site_position_id, lattitude, longitude
            HashMap<String, String> params = new HashMap<>();
            params.put(FeedParams.USERID, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERID));
            params.put(FeedParams.USERTOKEN, SharedPrefManager.getInstance().getSharedDataString(FeedParams.USERTOKEN));

            placeRequest(APIMethods.GET_PROPERTY_SITE_LISTING, MySiteVo.class, params, true, null);

        } else {
            showToast(R.string.no_internet);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_PROPERTY_SITE_LISTING)) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {

                MySiteVo resultData = (MySiteVo) response;

                mySiteDetailList = resultData.getData();
                if (mySiteAdapter == null) {
                    mySiteAdapter = new MySiteAdapter(context, mySiteDetailList);
                    rclMySite.setAdapter(mySiteAdapter);
                } else {
                    mySiteAdapter.notifyDataSetChanged();
                }

                saveSiteData(context, resultData, FeedParams.SAVED_CAPTURE_SITE_FRAGMENT);
//                addMarkers(mySiteDetailList);

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

        if (apiMethod.equalsIgnoreCase(APIMethods.GET_PROPERTY_SITE_LISTING)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }

//    @Override
//    public boolean onClusterClick(Cluster<MyItem> cluster) {
//        return false;
//    }
//
//    @Override
//    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
//        String firstName = cluster.getItems().iterator().next().getSnippet();
//        showToast(firstName);
//    }
//
//    @Override
//    public boolean onClusterItemClick(MyItem myItem) {
//        return false;
//    }
//
//    @Override
//    public void onClusterItemInfoWindowClick(MyItem myItem) {
//        showToast(myItem.getTitle());
//        showInfoDialog(myItem);
//    }


//    private void showInfoDialog(MyItem myItem) {
//
//        final Dialog infoDialog = new Dialog(context);
//        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        infoDialog.setCancelable(false);
//
//        infoDialog.setContentView(R.layout.my_site_info_dialog);
//
//        TextView txtDlgTitle = (TextView) infoDialog.findViewById(R.id.txtDlgTitle);
//        TextView txtDlgTLocation = (TextView) infoDialog.findViewById(R.id.txtDlgLocation);
//        TextView txtDlgDesc = (TextView) infoDialog.findViewById(R.id.txtDlgDesc);
//        ImageView imgCloseDialog = (ImageView) infoDialog.findViewById(R.id.imgCloseDialog);
//        ImageView imgSiteMap = (ImageView) infoDialog.findViewById(R.id.imgSiteMap);
//
//        if (myItem.getImages() != null && myItem.getImages().size() > 0) {
//            Picasso.get().load(myItem.getImages().get(0).getImage()).fit().placeholder(R.drawable.placeholder)
//                    .error(R.drawable.placeholder)
//                    .into(imgSiteMap);
//        } else {
//            Picasso.get().load(R.drawable.placeholder).into(imgSiteMap);
//        }
//
//        txtDlgTitle.setText(myItem.getTitle());
//        txtDlgTLocation.setText(myItem.getLandmarks());
//        txtDlgDesc.setText(myItem.getMediaOwner());
//
//        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                infoDialog.dismiss();
//            }
//        });
//
//        infoDialog.show();
//    }
}


