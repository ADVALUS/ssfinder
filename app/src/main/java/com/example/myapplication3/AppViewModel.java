package com.example.myapplication3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppViewModel extends ViewModel {
    private MutableLiveData<String> resultTextLiveData = new MutableLiveData<>();

    public LiveData<String> getResultTextLiveData() {
        return resultTextLiveData;
    }

    public void setResultText(String resultText) {
        resultTextLiveData.setValue(resultText);
    }
}
