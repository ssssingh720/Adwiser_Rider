package ridersadvisor.com.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ridersadvisor.com.R;
import ridersadvisor.com.activities.LandingActivity;
import ridersadvisor.com.models.FeedParams;
import ridersadvisor.com.models.MediaOwnerVo;

/**
 * Created by Sudhir Singh on 05,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SelectOptionAdapter extends RecyclerView.Adapter<SelectOptionAdapter.MyViewHolder> {

    private ArrayList<MediaOwnerVo> mediaOwnerList;
    private ArrayList<MediaOwnerVo> searchMediaOwnerList;
    private Activity mContext;

    public SelectOptionAdapter(Activity pContext, ArrayList<MediaOwnerVo> mediaOwnerList) {
        this.mContext = pContext;
        this.mediaOwnerList = mediaOwnerList;
        searchMediaOwnerList = new ArrayList<>();
        searchMediaOwnerList.addAll(mediaOwnerList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_option_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txtOption.setText(mediaOwnerList.get(position).getLabel());

        if (mediaOwnerList.get(position).isSelected()) {
            holder.rdOption.setChecked(true);
        } else {
            holder.rdOption.setChecked(false);
        }

        holder.lnrOption.setTag(position);
        holder.lnrOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionClicked = (Integer) v.getTag();
                if (mediaOwnerList.get(positionClicked).isSelected()) {
                    mediaOwnerList.get(positionClicked).setSelected(false);
                } else {
                    mediaOwnerList.get(positionClicked).setSelected(true);
                }
                notifyDataSetChanged();

                Intent mediaOwnerIntent = new Intent(mContext, LandingActivity.class);
                mediaOwnerIntent.putExtra(FeedParams.MEDIA_OWNER_ID, mediaOwnerList.get(positionClicked));
                mContext.setResult(Activity.RESULT_OK, mediaOwnerIntent);
                mContext.finish();
                mContext.overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mediaOwnerList != null ? mediaOwnerList.size() : 0;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
//        if (searchList.size() == 0) {
//            searchList.addAll(customerList);
//        }
        mediaOwnerList.clear();
        if (charText.length() == 0) {
            mediaOwnerList.addAll(searchMediaOwnerList);
        } else {
            for (MediaOwnerVo wp : searchMediaOwnerList) {
                if (wp.getLabel().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mediaOwnerList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOption;
        LinearLayout lnrOption;
        RadioButton rdOption;
//        CardView cvContainer;

        MyViewHolder(View view) {
            super(view);
            txtOption = (TextView) view.findViewById(R.id.txtOption);
//            cvContainer = (CardView) view.findViewById(R.id.cv_cheque_container);
            lnrOption = (LinearLayout) view.findViewById(R.id.lnrOption);
            rdOption = (RadioButton) view.findViewById(R.id.rdOption);
        }
    }
}
