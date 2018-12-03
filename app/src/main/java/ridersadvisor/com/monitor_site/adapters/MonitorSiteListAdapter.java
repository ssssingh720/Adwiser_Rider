package ridersadvisor.com.monitor_site.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.monitor_site.activities.MonitorSiteDetailActivity;
import ridersadvisor.com.monitor_site.modal.MonitorSiteDetailVo;

import static ridersadvisor.com.monitor_site.fragment.MonitorSiteListFragment.MONITOR_IMAGE_CAPTURE_STATUS;

/**
 * Created by Sudhir Singh on 24,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MonitorSiteListAdapter extends RecyclerView.Adapter<MonitorSiteListAdapter.MyViewHolder> {

    private Activity mContext;
    private List<MonitorSiteDetailVo> albumList;
    private double myLatitude;
    private double myLongitude;

    public MonitorSiteListAdapter(Activity mContext, List<MonitorSiteDetailVo> albumList,double latitude,double longitude) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.myLatitude=latitude;
        this.myLongitude=longitude;
    }

    @Override
    public MonitorSiteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_site_adapter, parent, false);

        return new MonitorSiteListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MonitorSiteListAdapter.MyViewHolder holder, int position) {
        MonitorSiteDetailVo album = albumList.get(position);

        holder.chk_monitor_site.setVisibility(View.VISIBLE);
        if (album.isImageCaptured()) {
            holder.chk_monitor_site.setChecked(true);
        } else {
            holder.chk_monitor_site.setChecked(false);
        }

        holder.txtTripDistance.setVisibility(View.VISIBLE);
        holder.txtTripDistance.setTag(position);
        holder.txtTripDistance.setText(calculateDistance(myLatitude,myLongitude,album));
        holder.txtTripName.setText(mContext.getResources().getString(R.string.uid) + " : " + album.getSiteCode());
        holder.txtTripLocation.setText(album.getLandmarks() + " , " + album.getSiteType() + " , " + album.getLocality());
        holder.txtMediaOwnerName.setText(album.getMediaOwner());
        holder.txtTripDate.setText(album.getAddedDate());

        holder.imgJury.setTag(position);

//        if (album.getImage() != null && album.getImages().size() > 0) {
        Picasso.get().load(album.getImage()).fit().placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imgJury);
//        }else{
//            Picasso.get().load(R.drawable.placeholder).into(holder.imgJury);
//        }

        holder.lnrSpeakerDetail.setTag(position);
        holder.lnrSpeakerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int positionClicked = (Integer) view.getTag();

                MonitorSiteDetailVo album = albumList.get(positionClicked);
                Intent propertyDetail = new Intent(mContext, MonitorSiteDetailActivity.class);
                propertyDetail.putExtra(FeedParams.PROPERTY_DETAIL, album);
                propertyDetail.putExtra(FeedParams.POSITION_CLICKED, positionClicked);
                propertyDetail.putExtra(FeedParams.IS_OFFLINE_MODE, true);

                mContext.startActivityForResult(propertyDetail, MONITOR_IMAGE_CAPTURE_STATUS);

            }
        });
    }


    private String calculateDistance(double myLatitude,double myLongitude, MonitorSiteDetailVo siteDetail){

        String totalDistance="Not available";
        try {
            float[] result1 = new float[3];
            android.location.Location.distanceBetween(myLatitude, myLongitude, Double.valueOf(siteDetail.getLattitude()), Double.valueOf(siteDetail.getLongitude()), result1);
            Float distance1 = result1[0] / 1000;
            totalDistance=  distance1 +" km";
        }catch(Exception e){

        }


        return totalDistance;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTripName, txtTripLocation, txtMediaOwnerName, txtTripDate,txtTripDistance;
        private ImageView imgJury;
        private LinearLayout lnrSpeakerDetail;
        private CheckBox chk_monitor_site;

        public MyViewHolder(View view) {
            super(view);
            chk_monitor_site = (CheckBox) view.findViewById(R.id.chk_monitor_site);
            txtTripName = (TextView) view.findViewById(R.id.txtTripName);
            txtTripLocation = (TextView) view.findViewById(R.id.txtTripLocation);
            txtMediaOwnerName = (TextView) view.findViewById(R.id.txtMediaOwnerName);
            txtTripDate = (TextView) view.findViewById(R.id.txtTripDate);
            txtTripDistance = (TextView) view.findViewById(R.id.txtTripDistance);
            imgJury = (ImageView) view.findViewById(R.id.imgJury);
            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
        }
    }
}
