package com.example.aplikasiuntukuts.data;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aplikasiuntukuts.MainActivity;

import java.util.List;

public class CheeseViewModel extends AndroidViewModel
{
    private CheeseRepository mCheeseRepository;

    private LiveData<List<Cheese>> mAllCheeses;

    public CheeseViewModel (Application app){
        super(app);
        mCheeseRepository = new CheeseRepository(app);
        mAllCheeses = mCheeseRepository.getAllCheese();
    }

    public LiveData<List<Cheese>> getAllCheese(){
        return mAllCheeses;
    }
}
