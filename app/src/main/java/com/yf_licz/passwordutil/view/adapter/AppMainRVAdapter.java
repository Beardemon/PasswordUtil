package com.yf_licz.passwordutil.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.bean.UserKeyBean;
import com.yf_licz.passwordutil.databinding.RVItemBinding;

import java.util.List;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/30
 */

public class AppMainRVAdapter extends RecyclerView.Adapter<AppMainRVAdapter.AppMainViewHolder> {
    private List<UserKeyBean> mData;
    private String mSafekey;
    private boolean mDisplayType;

    public AppMainRVAdapter(List<UserKeyBean> data) {
        mData = data;
    }


    @Override
    public AppMainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RVItemBinding rvItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.application_recyclerview_item_app_main, parent, false);
        AppMainViewHolder appMainViewHolder = new AppMainViewHolder(rvItemBinding.getRoot());
        appMainViewHolder.setBinding(rvItemBinding);
        return appMainViewHolder;
    }

    @Override
    public void onBindViewHolder(AppMainViewHolder holder, int position) {
        if (mData != null && mData.size() > 0) {
            holder.getBinding().setSafekey(mSafekey);
            holder.getBinding().setUserkeybean(mData.get(position));
            holder.getBinding().executePendingBindings();

        }

    }


    @Override
    public int getItemCount() {
        if (mData != null && mData.size() > 0) {
            return mData.size();
        } else {
            return 0;
        }

    }

    /**
     * 修改显示样式，safekey为null或者""则显示密码，提供safekey则显示明文
     * @param safekey
     */
    public void setDisplayType(String safekey) {
        mSafekey = safekey;
        this.notifyDataSetChanged();
    }


    public boolean getDisplayType() {
        if(mSafekey==null||"".equals(mSafekey)){
            return false;
        }else{
            return true;
        }

    }

    public static class AppMainViewHolder extends RecyclerView.ViewHolder {

        private RVItemBinding binding;


        public AppMainViewHolder(View itemView) {
            super(itemView);


        }

        public void setBinding(RVItemBinding binding) {
            this.binding = binding;
        }

        public RVItemBinding getBinding() {
            return this.binding;
        }
    }


}