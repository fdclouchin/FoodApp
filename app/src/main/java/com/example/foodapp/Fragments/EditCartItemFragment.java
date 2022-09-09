package com.example.foodapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodapp.Interfaces.OnBackPressedFragment;
import com.example.foodapp.R;

public class EditCartItemFragment extends Fragment implements OnBackPressedFragment {
    private EditCartItemFragment mEditCartItemFragment;
    public EditCartItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_cart_item, container, false);

        mEditCartItemFragment = (EditCartItemFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.display_fragment);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        if (mEditCartItemFragment != null && mEditCartItemFragment.isVisible()) {
            getParentFragmentManager().beginTransaction().remove(mEditCartItemFragment).commit();
            return true;
        } else {
            return false;
        }
    }
}