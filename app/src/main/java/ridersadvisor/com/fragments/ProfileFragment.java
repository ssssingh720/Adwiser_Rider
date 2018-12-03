package ridersadvisor.com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ridersadvisor.com.R;

/**
 * Created by Sudhir Singh on 13,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private TextInputLayout txtInputName;
    private EditText edtInputName;
    private TextInputLayout txtInputEmail;
    private EditText edtInputEmail;
    private TextInputLayout txtInputMobile;
    private EditText edtInputMobile;
    private TextInputLayout txtInputCity;
    private EditText edtInputCity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.profile_fragment, container, false);

        return mView;
    }

    @Override
    public void onClick(View v) {

    }
}
