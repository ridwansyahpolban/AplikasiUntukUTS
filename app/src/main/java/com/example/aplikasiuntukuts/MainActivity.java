/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.aplikasiuntukuts;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiuntukuts.data.Cheese;
import com.example.aplikasiuntukuts.data.CheeseViewModel;
import com.example.aplikasiuntukuts.provider.SampleContentProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * Not very relevant to Room. This just shows data from {@link SampleContentProvider}.
 *
 * <p>Since the data is exposed through the ContentProvider, other apps can read and write the
 * content in a similar manner to this.</p>
 */
public class MainActivity extends AppCompatActivity {

    private static final int LOADER_CHEESES = 1;
    private CheeseAdapter mCheeseAdapter;
    private CheeseViewModel cheeseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mCheeseAdapter = new CheeseAdapter();
        list.setAdapter(mCheeseAdapter);

        cheeseViewModel = new ViewModelProvider(this).get(CheeseViewModel.class);
        cheeseViewModel.getAllCheese().observe(this, new Observer<List<Cheese>>(){
            @Override
            public void onChanged(List<Cheese> cheeses) {mCheeseAdapter.setCheeses(cheeses);}
        });
//        LoaderManager.getInstance(this).initLoader(LOADER_CHEESES, null, mLoaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            SampleContentProvider.URI_CHEESE,
                            new String[]{Cheese.COLUMN_NAME},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
                    ArrayList<Cheese> data = new ArrayList<>();

                    do {
                        // Read the values of a row in the table using the indexes acquired above
                        final long id = cursor.getLong(cursor.getColumnIndex(Cheese.COLUMN_ID));
                        final String name = cursor.getString(cursor.getColumnIndex(Cheese.COLUMN_NAME));

                        data.add(new Cheese(id, name));

                    } while (cursor.moveToNext());

                    mCheeseAdapter.setCheeses(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    mCheeseAdapter.setCheeses(null);
                }

            };

    private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

        private List<Cheese> mCheeses;

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mCheeses.get(position) != null) {
                Cheese cheese = mCheeses.get(position);
                holder.mText.setText(cheese.getName());
            }
        }

        @Override
        public int getItemCount() {
            return mCheeses == null ? 0 : mCheeses.size();
        }


        void setCheeses(List<Cheese> cheeses) {
            mCheeses = cheeses;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = itemView.findViewById(android.R.id.text1);
            }

        }

    }

}
