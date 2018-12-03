package ridersadvisor.com.monitor_site.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.models.MySiteImageVo;
import ridersadvisor.com.utils.ImageUtility;

/**
 * Created by Sudhir Singh on 25,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class OfflineMonitorDataAdapter extends RecyclerView.Adapter<OfflineMonitorDataAdapter.MyViewHolder> {

    private Context mContext;
    private List<MySiteImageVo> albumList;

    public OfflineMonitorDataAdapter(Context mContext, List<MySiteImageVo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public OfflineMonitorDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_site_adapter, parent, false);

        return new OfflineMonitorDataAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfflineMonitorDataAdapter.MyViewHolder holder, int position) {
        MySiteImageVo album = albumList.get(position);


        holder.txtTripName.setText(mContext.getResources().getString(R.string.site_id)+" : "+album.getSiteId());
        holder.txtTripLocation.setText("Remarks : " + album.getRemarks());
        holder.lnrOfflineImage.setVisibility(View.GONE);
        holder.txtTripDate.setVisibility(View.GONE);

        holder.imgJury.setTag(position);
        Bitmap bitmap = ImageUtility.loadImageFromStorage(album.getImage(), mContext);
        holder.imgJury.setImageBitmap(bitmap);


        holder.lnrSpeakerDetail.setTag(position);
        holder.lnrSpeakerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int positionClicked = (Integer) view.getTag();
//
//                MySiteDetailVo album = albumList.get(positionClicked);
//                Intent propertyDetail = new Intent(mContext, ActivityPropertySiteDetail.class);
//                propertyDetail.putExtra(FeedParams.PROPERTY_DETAIL, album);
//                propertyDetail.putExtra(FeedParams.IS_OFFLINE_MODE, true);
//                mContext.startActivity(propertyDetail);

            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTripName, txtTripLocation, txtMediaOwnerName, txtTripDate;
        private ImageView imgJury;
        private LinearLayout lnrOfflineImage;
        private LinearLayout lnrSpeakerDetail;

        public MyViewHolder(View view) {
            super(view);
            txtTripName = (TextView) view.findViewById(R.id.txtTripName);
            txtTripLocation = (TextView) view.findViewById(R.id.txtTripLocation);
            txtMediaOwnerName = (TextView) view.findViewById(R.id.txtMediaOwnerName);
            txtTripDate = (TextView) view.findViewById(R.id.txtTripDate);
            imgJury = (ImageView) view.findViewById(R.id.imgJury);
            lnrOfflineImage = (LinearLayout) view.findViewById(R.id.lnrOfflineImage);
            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
        }
    }
}
