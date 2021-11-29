package com.erolakgul.stockhaus.controller.fragment.inventory.items;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erolakgul.stockhaus.R;

public class InventoryItemsFragment extends Fragment {

    private InventoryItemsViewModel mViewModel;

    public static InventoryItemsFragment newInstance() {
        return new InventoryItemsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_items_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InventoryItemsViewModel.class);
        // TODO: Use the ViewModel
    }

}
