package com.example.tp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp.ApiService;
import com.example.tp.R;
import com.example.tp.adapter.HaoAdapter;
import com.example.tp.bean.AsscoBean;
import com.example.tp.bean.HaoBean;
import com.example.tp.bean.HotspotBean;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HaoFragment extends Fragment {
    private RecyclerView mRv;
    private ArrayList<HaoBean.DataBean.ExpTopBean.ListBean> listBeans;
    private HaoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.hao, null);
        initView(inflate);
        initData();
        return inflate;
    }

    private void initData() {
       new Retrofit.Builder().baseUrl(ApiService.oUrl)
               .addConverterFactory(GsonConverterFactory.create())
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .build()
               .create(ApiService.class)
               .getHao()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<HaoBean>() {
                   @Override
                   public void onSubscribe(Disposable d) {

                   }

                   @Override
                   public void onNext(HaoBean haoBean) {
                        listBeans.addAll(haoBean.getData().getExpTop().getList());
                        adapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onError(Throwable e) {

                   }

                   @Override
                   public void onComplete() {

                   }
               });


    }

    private void initView(@NonNull final View itemView) {
        mRv = (RecyclerView) itemView.findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        listBeans = new ArrayList<>();
        adapter = new HaoAdapter(getContext(), listBeans);
        mRv.setAdapter(adapter);
    }
}
