package com.codringreen.farmloading.view.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codringreen.farmloading.BuildConfig;
import com.codringreen.farmloading.R;
import com.codringreen.farmloading.constants.NavigationType;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.FarmDetailDashboardModel;
import com.codringreen.farmloading.model.MenuModel;
import com.codringreen.farmloading.utils.CircularImageView;
import com.codringreen.farmloading.utils.CommonUtils;
import com.codringreen.farmloading.utils.CustomProgress;
import com.codringreen.farmloading.view.adapter.NavigationAdapters;
import com.codringreen.farmloading.viewmodel.DashboardViewModel;
import com.codringreen.farmloading.viewmodel.ViewModelFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class DashboardActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private CircularImageView imgProfile;
    private ListView lstMenu;
    private List<MenuModel> menuModels;
    private Toolbar toolBar;
    private AppCompatTextView tvName, tvSupplierCount, tvVolume, tvPieces, tvRecentPieces, tvRecentSupplierCount, tvRecentVolume, tvICACount, tvRecentICACount;
    private boolean exitCode = false;

    private DashboardViewModel dashboardViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dashboard);
        initComponents();
    }

    private void initComponents() {
        try {
            hideKeyboard(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                }
            }

            drawerLayout = findViewById(R.id.drawerLayout);
            toolBar = findViewById(R.id.toolbar);
            lstMenu = findViewById(R.id.lstMenu);
            tvName = findViewById(R.id.tvName);

            tvSupplierCount = findViewById(R.id.tvSupplierCount);
            tvVolume = findViewById(R.id.tvVolume);
            tvPieces = findViewById(R.id.tvPieces);
            tvRecentSupplierCount = findViewById(R.id.tvRecentSupplierCount);
            tvRecentVolume = findViewById(R.id.tvRecentVolume);
            tvRecentPieces = findViewById(R.id.tvRecentPieces);
            tvICACount = findViewById(R.id.tvICACount);
            tvRecentICACount = findViewById(R.id.tvRecentICACount);

            AppCompatTextView tvLogout = findViewById(R.id.tvLogout);
            AppCompatTextView tvVersion = findViewById(R.id.tvVersion);
            AppCompatImageView imgClose = findViewById(R.id.imgClose);
            imgProfile = findViewById(R.id.imgProfile);
            CardView cvFarmLists = findViewById(R.id.cvFarmLists);
            CardView cvSync = findViewById(R.id.cvSync);

            Bundle extras = getIntent().getExtras();

            dashboardViewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);

            setMenus();
            setUserProfile();
            setDashboardDetails();

            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            tvVersion.setText(String.format("%s: %s.%s", getString(R.string.version), packageInfo.versionName, packageInfo.versionCode));
            tvVersion.setPaintFlags(tvVersion.getPaintFlags() | 8);

            imgClose.setOnClickListener(v -> closeDrawers());
            tvLogout.setOnClickListener(v -> showConfirmation());
            lstMenu.setOnItemClickListener(this);

            if (extras != null && extras.containsKey("DownloadMaster") && extras.getString("DownloadMaster") != null && Objects.requireNonNull(extras.getString("DownloadMaster")).equalsIgnoreCase("Yes")) {
                dashboardViewModel.getMasterDownload();
            }

            dashboardViewModel.getError().observe(this, s ->
                    showDialog(s, dashboardViewModel.getErrorTitle(), null, getString(R.string.text_ok)));

            dashboardViewModel.getDownloadState().observe(this, aBoolean -> {
                if(aBoolean) {
                    Toast.makeText(getApplicationContext(), R.string.data_download_success, Toast.LENGTH_SHORT).show();
                }
                setDashboardDetails();
            });

            dashboardViewModel.getSyncStatus().observe(this, aBoolean -> {
                if(aBoolean) {
                    Toast.makeText(getApplicationContext(), R.string.data_synced_successfully, Toast.LENGTH_SHORT).show();
                }
                setDashboardDetails();
            });

            dashboardViewModel.getProgressBar().observe(this, aBoolean -> {
                if (aBoolean) {
                    if (getApplicationContext() != null) {
                        CustomProgress.getInstance().showProgress(this);
                    } else {
                        CustomProgress.getInstance().dismissProgress();
                    }
                } else {
                    CustomProgress.getInstance().dismissProgress();
                }
            });

            getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawers();
                        return;
                    }
                    if (exitCode) {
                        System.exit(0);
                    }
                    exitCode = true;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> exitCode = false,10000);
                }
            });

            cvFarmLists.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, FarmListsActivity.class)));

            cvSync.setOnClickListener(v -> dashboardViewModel.syncFarmData());
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity InitComponents", e);
        }
    }

    private void setMenus() {
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setupDrawerToggle();
            lstMenu.setAdapter(new NavigationAdapters(this, R.layout.row_menu_item, getMenuListItems()));
        }
    }

    private void setUserProfile() {
        try {
            tvName.setText(PreferenceManager.INSTANCE.getName());
            Glide.with(this).load(PreferenceManager.INSTANCE.getPhoto())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_default_user).error(R.drawable.ic_default_user)).into(imgProfile);
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity setUserProfile", e);
        }
    }

    private void setDashboardDetails() {
        try {
            FarmDetailDashboardModel farmDetailDashboardModel = dashboardViewModel.fetchTodayDashboardData(CommonUtils.convertTimeStampToDate(CommonUtils.getCurrentLocalDateTimeStamp(), "dd/MM/yyy"));
            if(farmDetailDashboardModel != null) {
                DecimalFormat df = new DecimalFormat("0.000");
                tvSupplierCount.setText(String.valueOf(farmDetailDashboardModel.getSupplierCount()));
                tvICACount.setText(String.valueOf(farmDetailDashboardModel.getTotalICA()));
                tvVolume.setText(df.format(farmDetailDashboardModel.getGrossVolume()));
                tvPieces.setText(String.valueOf(farmDetailDashboardModel.getTotalPieces()));
            } else {
                tvSupplierCount.setText("0");
                tvICACount.setText("0");
                tvVolume.setText("0");
                tvPieces.setText("0");
            }

            FarmDetailDashboardModel farmDetailDashboardRecent = dashboardViewModel.fetchRecentDashboardData(CommonUtils.getDateByType("fifth"), CommonUtils.getDateByType("today"));
            if(farmDetailDashboardRecent != null) {
                DecimalFormat df = new DecimalFormat("0.000");
                tvRecentSupplierCount.setText(String.valueOf(farmDetailDashboardRecent.getSupplierCount()));
                tvRecentICACount.setText(String.valueOf(farmDetailDashboardRecent.getTotalICA()));
                tvRecentVolume.setText(df.format(farmDetailDashboardRecent.getGrossVolume()));
                tvRecentPieces.setText(String.valueOf(farmDetailDashboardRecent.getTotalPieces()));
            } else {
                tvRecentSupplierCount.setText("0");
                tvRecentICACount.setText("0");
                tvRecentVolume.setText("0");
                tvRecentPieces.setText("0");
            }
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity setDashboardDetails", e);
        }
    }

    public void setupDrawerToggle() {
        try {
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
            actionBarDrawerToggle.syncState();
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.notificationBarColor));
            actionBarDrawerToggle.setToolbarNavigationClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity setupDrawerToggle", e);
        }
    }

    private List<MenuModel> getMenuListItems() {
        menuModels = new ArrayList<>();
        menuModels.add(new MenuModel(R.drawable.ic_dashboard, getString(R.string.dashboard), NavigationType.HOME));
        menuModels.add(new MenuModel(R.drawable.ic_farm_list, getString(R.string.farm_list), NavigationType.FARM));
        menuModels.add(new MenuModel(R.drawable.ic_sync, getString(R.string.synchronization), NavigationType.SYNCHRONIZATION));
        menuModels.add(new MenuModel(R.drawable.ic_database, getString(R.string.export_database), NavigationType.EXPORT_DATABASE));
        return menuModels;
    }

    private void closeDrawers() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new Bundle().putString("Title", menuModels.get(position).getMenuName());
        closeDrawers();

        NavigationType selectedType = menuModels.get(position).getNavigationType();
        switch (selectedType) {
            case FARM:
                startActivity(new Intent(DashboardActivity.this, FarmListsActivity.class));
                break;
            case SYNCHRONIZATION:
                dashboardViewModel.syncFarmData();
                break;
            case EXPORT_DATABASE:
                if (checkPermission()) {
                    exportDB();
                } else {
                    requestPermission();
                }
                break;
            default:
                break;
        }
    }

    private void showConfirmation() {
        try {
            closeDrawers();
            showDialogWithCancel(getString(R.string.logout_confirmation), getString(R.string.confirmation), (dialogInterface, i) -> {
                dialogInterface.dismiss();
                PreferenceManager.INSTANCE.clearLoginDetails();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }, getString(R.string.text_ok), getString(R.string.text_cancel));
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity showConfirmation", e);
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0
                && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    private void exportDB() {
        if (Build.VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent();
            intent.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
            startActivity(intent);
        }
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File dataDirectory = Environment.getDataDirectory();
        String str = "cgr_farmloading" + ("_" + CommonUtils.convertTimeStampToDate(CommonUtils.getCurrentLocalDateTimeStamp(), "dd_MM_yyyy_HH_mm_ss_S")) + ".db";
        File file = new File(dataDirectory, "/data/" + BuildConfig.APPLICATION_ID +"/databases/cgr_farmloadingv1.db");
        File file2 = new File(externalStorageDirectory + "/Codrin Green", str);
        File file3 = new File(externalStorageDirectory + "/Codrin Green");
        if (!file3.exists()) {
            if (!file3.mkdir()) {
                Log.e("DashboardActivity", "Failed to create directory: " + file3.getAbsolutePath());
                return;
            }
        }
        if (!file2.exists()) {
            try {
                if (!file2.createNewFile()) {
                    Log.e("DashboardActivity", "Failed to create file: " + file2.getAbsolutePath());
                    return;
                }
            } catch (IOException e) {
                Log.e("DashboardActivity", "Error in DashboardActivity exportDB", e);
            }
        }
        try (
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(file2);
                FileChannel sourceChannel = fis.getChannel();
                FileChannel destinationChannel = fos.getChannel()
        ) {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            Toast.makeText(this, getString(R.string.export_database), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity exportDB", e);
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                manageAllFilesAccessPermissionLauncher.launch(intent);
            } catch (Exception unused) {
                Intent intent2 = new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                manageAllFilesAccessPermissionLauncher.launch(intent2);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 101);
        }
    }

    private final ActivityResultLauncher<Intent> manageAllFilesAccessPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (Build.VERSION.SDK_INT >= 30 && Environment.isExternalStorageManager()) {
                            Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                        }
                    });

    @Override
    protected void onResume() {
        super.onResume();
        setDashboardDetails();
    }
}