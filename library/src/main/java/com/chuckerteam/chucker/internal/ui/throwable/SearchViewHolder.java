package com.chuckerteam.chucker.internal.ui.throwable;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chuckerteam.chucker.api.ExceptionCollector;
import com.chuckerteam.chucker.databinding.ChuckerSerachLayoutBinding;
import com.chuckerteam.chucker.internal.data.entity.RecordedThrowableTuple;
import com.chuckerteam.chucker.internal.ui.MainViewModel;

import java.util.List;

public class SearchViewHolder {

    public View rootView ;
    ChuckerSerachLayoutBinding serachLayoutBinding;
    MainViewModel viewModel;
    AppCompatActivity activity;
    ThrowableAdapter adapter;

    public SearchViewHolder(Context context, ViewGroup parent) {
        serachLayoutBinding = ChuckerSerachLayoutBinding.inflate(LayoutInflater.from(context));
        rootView = serachLayoutBinding.getRoot();
        if(context instanceof AppCompatActivity){
            activity = (AppCompatActivity) context;
        }else {
            activity = (AppCompatActivity) ExceptionCollector.top.get();
        }
        viewModel = new ViewModelProvider(activity).get(MainViewModel.class);

        initEvent();
        adapter = new ThrowableAdapter(new ThrowableAdapter.ThrowableClickListListener() {
            @Override
            public void onThrowableClick(long throwableId, int position) {
                ThrowableActivity.Companion.start(activity,throwableId);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        serachLayoutBinding.recyclerview.setLayoutManager(manager);
        serachLayoutBinding.recyclerview.setAdapter(adapter);

    }

    private void initEvent() {
        serachLayoutBinding.input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.getSearchResults(s.toString())
                        .observe(activity, new Observer<List<RecordedThrowableTuple>>() {
                            @Override
                            public void onChanged(List<RecordedThrowableTuple> tuples) {
                                update(tuples);
                            }
                        });
            }
        });
    }

    private void update(List<RecordedThrowableTuple> tuples) {
        adapter.setData(tuples);
        Log.d("dd",tuples.size()+"-sise");
    }
}
