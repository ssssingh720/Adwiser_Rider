package ridersadvisor.com.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ridersadvisor.com.R;
import ridersadvisor.com.adapters.SelectOptionAdapter;
import ridersadvisor.com.models.MediaOwnerVo;

/**
 * Created by Sudhir Singh on 05,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SelectOptionActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_option);

        ArrayList<MediaOwnerVo> mediaOwnerList = (ArrayList<MediaOwnerVo>) getIntent().getExtras().get("LIST_DATA");
        String title = getIntent().getExtras().getString("TITLE");

        TextView txtOptionTitle = (TextView) findViewById(R.id.txtOptionTitle);
        RecyclerView rv_cheque_list = (RecyclerView) findViewById(R.id.rv_cheque_list);
        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
        final SearchView mSvSearchCust = (SearchView) findViewById(R.id.sv_search_customer);

        txtOptionTitle.setText(title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(SelectOptionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_cheque_list.setLayoutManager(layoutManager);
        rv_cheque_list.setItemAnimator(new DefaultItemAnimator());

        final SelectOptionAdapter selectOptionAdapter = new SelectOptionAdapter(SelectOptionActivity.this, mediaOwnerList);
        rv_cheque_list.setAdapter(selectOptionAdapter);

        mSvSearchCust.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchKeyword) {

                if (selectOptionAdapter != null) {
                    String text = searchKeyword.trim().toLowerCase(Locale.getDefault());
                    selectOptionAdapter.filter(text);
                }

//                if (searchKeyword.length() > 2) {
//                    getChequeList(searchKeyword, mFromDate, mToDate);
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (selectOptionAdapter != null) {
//                    String text = newText.trim().toLowerCase(Locale.getDefault());
                    selectOptionAdapter.filter("");
                }

                return false;
            }
        });
        mSvSearchCust.onActionViewExpanded();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSvSearchCust.clearFocus();
            }
        }, 300);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        txtOptionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
        SelectOptionActivity.this.finish();
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);

    }
}
