package com.erolakgul.stockhaus.controller.fragment.material;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MaterialViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public MaterialViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Malzeme Sayfası");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
