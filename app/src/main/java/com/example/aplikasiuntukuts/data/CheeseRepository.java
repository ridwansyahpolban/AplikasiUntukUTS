package com.example.aplikasiuntukuts.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CheeseRepository {
    private CheeseDao mCheeseDao;
    private LiveData<List<Cheese>> mAllCheeses;

    public CheeseRepository(Application app){
        SampleDatabase db = SampleDatabase.getInstance(app);
        mCheeseDao = db.cheese();
        mAllCheeses = mCheeseDao.getAllCheese();
    }

    LiveData<List<Cheese>> getAllCheese(){
        return mAllCheeses;
    }
}
