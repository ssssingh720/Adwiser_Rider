package ridersadvisor.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ridersadvisor.com.R;
import ridersadvisor.com.activities.ActivityPropertySiteDetail;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MySiteDetailVo;
import ridersadvisor.com.utils.ImageUtility;

/**
 * Created by Sudhir Singh on 16,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class OfflineDataAdapter extends RecyclerView.Adapter<OfflineDataAdapter.MyViewHolder> {

    private Context mContext;
    private List<MySiteDetailVo> albumList;

    public OfflineDataAdapter(Context mContext, List<MySiteDetailVo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public OfflineDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_site_adapter, parent, false);

        return new OfflineDataAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfflineDataAdapter.MyViewHolder holder, int position) {
        MySiteDetailVo album = albumList.get(position);

        holder.txtTripName.setText(album.getLocality());
        holder.txtTripLocation.setText(album.getLandmarks() + " , " + album.getSiteType() + " , " + album.getLocality());
        holder.txtMediaOwnerName.setText(album.getMediaOwner());
        holder.txtTripDate.setText(album.getAddedDate());

        holder.imgJury.setTag(position);

        if (album.getImages() != null && album.getImages().size() > 0) {

            Bitmap bitmap = ImageUtility.loadImageFromStorage(album.getImages().get(0).getImage(), mContext);
            holder.imgJury.setImageBitmap(bitmap);
//            Picasso.get().load(album.getImages().get(0).getImage()).fit().placeholder(R.drawable.placeholder)
//                    .error(R.drawable.placeholder)
//                    .into(holder.imgJury);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.imgJury);
        }

        holder.lnrSpeakerDetail.setTag(position);
        holder.lnrSpeakerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int positionClicked = (Integer) view.getTag();

                MySiteDetailVo album = albumList.get(positionClicked);
                Intent propertyDetail = new Intent(mContext, ActivityPropertySiteDetail.class);
                propertyDetail.putExtra(FeedParams.PROPERTY_DETAIL, album);
                propertyDetail.putExtra(FeedParams.IS_OFFLINE_MODE, true);
                mContext.startActivity(propertyDetail);

//                showPopupMenu(holder.overflow);
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
        private LinearLayout lnrSpeakerDetail;

        public MyViewHolder(View view) {
            super(view);
            txtTripName = (TextView) view.findViewById(R.id.txtTripName);
            txtTripLocation = (TextView) view.findViewById(R.id.txtTripLocation);
            txtMediaOwnerName = (TextView) view.findViewById(R.id.txtMediaOwnerName);
            txtTripDate = (TextView) view.findViewById(R.id.txtTripDate);
            imgJury = (ImageView) view.findViewById(R.id.imgJury);
            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
        }
    }
}
