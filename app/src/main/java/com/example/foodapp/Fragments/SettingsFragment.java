package com.example.foodapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.R;

public class SettingsFragment extends Fragment implements OnBackPressedFragment {
    private SettingsFragment mSettingsFragment;
    private LinearLayout  mPaymentHistory;
    private ImageView mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mPaymentHistory = view.findViewById(R.id.payment_history_layout);
        mBackButton = view.findViewById(R.id.back_button);

        mPaymentHistory.setOnClickListener(mOnClickListener);
        mBackButton.setOnClickListener(mOnClickListener);
        if (getActivity() != null) {
            mSettingsFragment = (SettingsFragment) getActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.display_fragment);
        }

        return view;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.payment_history_layout: {
                    PaymentHistoryFragment paymentHistoryFragment = new PaymentHistoryFragment();
                    displayPaymentFragment(paymentHistoryFragment);
                    break;
                }
                case R.id.back_button: {
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(mSettingsFragment).commit();
                    }
                    break;
                }
            }
        }
    };

    private void displayPaymentFragment(Fragment fragment) {
        if (getActivity() == null) {
            return;
        }
       getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.display_fragment, fragment, "PaymentHistoryFragmentTag")
                .commit();
    }

    @Override
    public boolean onBackPressed() {
        if (mSettingsFragment != null && mSettingsFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mSettingsFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}