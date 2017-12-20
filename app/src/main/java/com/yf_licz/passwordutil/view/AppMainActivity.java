package com.yf_licz.passwordutil.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yf_licz.passwordutil.AppMainBinding;
import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.SetSafeKeyPopupWindowBinding;
import com.yf_licz.passwordutil.bean.UserKeyBean;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.utils.DialogUtil;
import com.yf_licz.passwordutil.utils.SecurityUtils;
import com.yf_licz.passwordutil.utils.ToastUtils;
import com.yf_licz.passwordutil.view.adapter.AppMainRVAdapter;
import com.yf_licz.passwordutil.viewmodule.AppMainViewModule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/17
 * <p>
 * 后期加入：
 * 密码生成器
 * 点击改变图片
 * safekey缓存逻辑，现在safekey不保存在本地后期可以选择保存在本地
 * 密码核对次数，错误超过多少次做什么
 */

public class AppMainActivity extends AppCompatActivity {
    private static final String TAG = "AppMainActivity";
    private Context mContext = AppMainActivity.this;
    private DialogUtil dialogUtil;
    private AppMainBinding appMainBinding;
    private AppMainViewModule appMainViewModulel;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private AppMainRVAdapter appMainRVAdapter;
    private List<UserKeyBean> userKeyBeanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_main_app_menu, menu);
        return true;
    }


    private void initView() {
        appMainBinding = DataBindingUtil.setContentView(this, R.layout.application_activity_app_main);
        dialogUtil = DialogUtil.getInstance(mContext);
        setMaterialUi();
        setRecyclerView();
        drawerItemClick();

    }


    private void initData() {
        appMainViewModulel = ViewModelProviders.of(this).get(AppMainViewModule.class);
        appMainViewModulel.getUserAllKeyLiveData().observe(this, new Observer<WrapperBean<List<UserKeyBean>>>() {
                    @Override
                    public void onChanged(@Nullable WrapperBean<List<UserKeyBean>> listWrapperBean) {

                        updateView(listWrapperBean);
                    }
                }
        );
    }


    private void setMaterialUi() {
        //toolbar,DrawerLayout
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, appMainBinding.drawerLayout, appMainBinding.toolBar, R.string.open, R.string.close);
        setSupportActionBar(appMainBinding.toolBar);
        //是否显示向左侧箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //向左侧箭头是否可点击
        getSupportActionBar().setHomeButtonEnabled(true);
        //DrawerLayout和navigation ico 联动
        actionBarDrawerToggle.syncState();
        appMainBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //navigation ico点击监听器
        appMainBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appMainBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
                    appMainBinding.drawerLayout.closeDrawer(Gravity.START);
                } else {
                    appMainBinding.drawerLayout.openDrawer(Gravity.START);
                }

            }
        });
        //fab监听器
        appMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("增加一项密码");
                startActivity(new Intent(AppMainActivity.this, AddNewItemActivity.class));

            }
        });
        appMainBinding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_local_main:
                        new RxPermissions(AppMainActivity.this)
                                .requestEach(Manifest.permission.READ_PHONE_STATE)
                                .subscribe(new Consumer<Permission>() {
                                    @Override
                                    public void accept(Permission permission) throws Exception {
                                        if (permission.granted) {
                                            appMainViewModulel.uploadUserKeyDataLocal2Net();
                                            // TODO: 2017/11/29 成功失败提示 
                                            Snackbar.make(appMainBinding.coordinatorLayout, "同步数据-本地覆盖网络", Snackbar.LENGTH_LONG).show();
                                            Log.d(TAG, permission.name + " is granted.");
                                        } else if (permission.shouldShowRequestPermissionRationale) {
                                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                                        } else {
                                            // 用户拒绝了该权限，并且选中『不再询问』
                                            Log.d(TAG, permission.name + " is denied.");
                                        }
                                    }
                                });

                        break;
                    case R.id.item_net_main:
                        appMainViewModulel.uploadUserKeyDataNet2Local();
                        Snackbar.make(appMainBinding.coordinatorLayout, "同步数据-网络覆盖本地", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.item_local_display_type:
                        if (appMainRVAdapter.getDisplayType()) {
                            appMainBinding.toolBar.getMenu().getItem(0).setIcon(R.drawable.eye_hint);
                            appMainRVAdapter.setDisplayType("");
                        } else {
                            showPopupWindow(appMainViewModulel.getSafeKeyMd5());


                        }
                    default:

                }

                return true;
            }
        });
    }

    /**
     * recyclerview初始化
     */
    private void setRecyclerView() {
        appMainBinding.rvUserData.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        appMainRVAdapter = new AppMainRVAdapter(userKeyBeanList);
        appMainBinding.rvUserData.setAdapter(appMainRVAdapter);
    }

    /**
     * 测滑菜单栏点击事件
     */
    private void drawerItemClick() {
        appMainBinding.tvItemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppMainActivity.this, AboutActivity.class));

            }
        });
        appMainBinding.tvItemChangeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showCommonDialog("是否切换账号（退出应用）", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        appMainViewModulel.logOut();
                        finish();
                        startActivity(new Intent(AppMainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
        appMainBinding.tvItemDelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showCommonDialog("是否删除所有数据-远端和本地", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        appMainViewModulel.delAllData();
                        userKeyBeanList.clear();
                        appMainRVAdapter.notifyDataSetChanged();
                        ToastUtils.showShortToast("已经删除本地和远端所有数据");
                    }
                });


            }
        });
        appMainBinding.tvItemUploadSafeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showCommonDialog("是否更改safekey", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/12/3 数据全部解密，然后使用新safekey加密替换本地和服务端数据库
                        // appMainViewModulel.updateSafeKey(SecurityUtils.MD5_Encode("111111"));
                    }
                });

            }
        });
        appMainBinding.tvItemShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showCommonDialog("是否分享本应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/12/20  share
                    }
                });

            }
        });
    }

    private void updateView(WrapperBean<List<UserKeyBean>> listWrapperBean) {
        switch (listWrapperBean.status) {
            case Content: {
                userKeyBeanList.clear();
                userKeyBeanList.addAll(listWrapperBean.data);
                appMainRVAdapter.notifyDataSetChanged();
                break;
            }
            case Empty: {
                userKeyBeanList.clear();
                appMainRVAdapter.notifyDataSetChanged();
                break;
            }
            case Error: {
                ToastUtils.showShortToast(listWrapperBean.error.toString());
                break;
            }
            case Loading: {
                ToastUtils.showShortToast("loading");
                break;
            }
            default:
        }
    }

    private void showPopupWindow(final String safeKeyMd5) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.application_activity_register, null);
        final SetSafeKeyPopupWindowBinding setSafeKeyPopupWindowBinding = SetSafeKeyPopupWindowBinding.inflate(LayoutInflater.from(this));
        View contentView = setSafeKeyPopupWindowBinding.getRoot();
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        setSafeKeyPopupWindowBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (safeKeyMd5.equals(SecurityUtils.MD5_Encode(setSafeKeyPopupWindowBinding.etSafeKey.getText().toString()))) {
                    ToastUtils.showShortToast("safekey核对正确，更改显示模式");
                    appMainBinding.toolBar.getMenu().getItem(0).setIcon(R.drawable.eye_show);
                    appMainRVAdapter.setDisplayType(setSafeKeyPopupWindowBinding.etSafeKey.getText().toString());
                    popupWindow.dismiss();
                } else {
                    popupWindow.dismiss();
                    ToastUtils.showShortToast("safekey核对错误，不予更改显示模式");
                }

            }
        });
        setSafeKeyPopupWindowBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("取消输入safekey");
                popupWindow.dismiss();

            }
        });

    }


}
