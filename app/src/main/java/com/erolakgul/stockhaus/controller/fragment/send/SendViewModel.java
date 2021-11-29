package com.erolakgul.stockhaus.controller.fragment.send;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Gönderim Sayfası");
    }

    public LiveData<String> getText() {
        return mText;
    }
}