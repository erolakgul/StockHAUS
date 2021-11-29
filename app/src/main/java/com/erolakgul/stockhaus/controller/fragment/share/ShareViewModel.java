package com.erolakgul.stockhaus.controller.fragment.share;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShareViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Paylaşım Sayfası");
    }

    public LiveData<String> getText() {
        return mText;
    }
}