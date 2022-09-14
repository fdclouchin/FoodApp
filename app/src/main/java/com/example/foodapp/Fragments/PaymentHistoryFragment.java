package com.example.foodapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.R;

public class PaymentHistoryFragment extends Fragment implements OnBackPressedFragment {

    private ImageView mBackButton;
    private PaymentHistoryFragment mPaymentHistoryFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        mBackButton = view.findViewById(R.id.back_button);

        mBackButton.setOnClickListener(mOnClickListener);

        if (getActivity() != null) {
            mPaymentHistoryFragment = (PaymentHistoryFragment) getActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.display_fragment);
        }

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_button: {
                    getParentFragmentManager().beginTransaction().remove(mPaymentHistoryFragment).commit();
                }
            }
        }
    };

    @Override
    public boolean onBackPressed() {
        if (mPaymentHistoryFragment != null && mPaymentHistoryFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mPaymentHistoryFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}