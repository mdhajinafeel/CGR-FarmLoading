package com.codringreen.farmloading.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.utils.DividerItemDecoration;
import com.codringreen.farmloading.view.adapter.CommonRecyclerViewAdapter;
import com.codringreen.farmloading.view.adapter.ViewHolder;
import com.codringreen.farmloading.viewmodel.FarmViewModel;
import com.codringreen.farmloading.viewmodel.ViewModelFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FarmCapturedDataActivity extends BaseActivity{

    private FarmDetails farmDetails;
    private LinearLayout llFarmDetails2;
    private List<FarmCapturedData> farmCapturedDataList;
    private FarmViewModel farmViewModel;
    private AppCompatTextView tvNoFarmData, tvTotalPieces, tvGrossVolume;
    private RecyclerView rvFarmDataList;
    private CommonRecyclerViewAdapter<FarmCapturedData> farmDetailsCommonRecyclerViewAdapter;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_farm_data_captured);
        initComponents();
    }

    private void initComponents() {
        try {
            hideKeyboard(this);

            AppCompatImageView imgBack = findViewById(R.id.imgBack);
            AppCompatTextView txtTitle = findViewById(R.id.txtTitle);
            AppCompatTextView txtSubTitle = findViewById(R.id.txtSubTitle);

            AppCompatImageView ivExpand = findViewById(R.id.ivExpand);
            AppCompatTextView tvInventoryOrder = findViewById(R.id.tvInventoryOrder);
            AppCompatTextView tvSupplierName = findViewById(R.id.tvSupplierName);
            AppCompatTextView tvPurchaseDate = findViewById(R.id.tvPurchaseDate);
            AppCompatTextView tvTruckPlateNumber = findViewById(R.id.tvTruckPlateNumber);
            AppCompatTextView tvPurchaseContract = findViewById(R.id.tvPurchaseContract);
            tvTotalPieces = findViewById(R.id.tvTotalPieces);
            tvGrossVolume = findViewById(R.id.tvGrossVolume);
            rvFarmDataList = findViewById(R.id.rvFarmDataList);
            tvNoFarmData = findViewById(R.id.tvNoFarmData);

            txtTitle.setText(R.string.farm_captured_data);
            imgBack.setOnClickListener(v -> finish());

            llFarmDetails2 = findViewById(R.id.llFarmDetails2);

            farmViewModel = new ViewModelProvider(this, viewModelFactory).get(FarmViewModel.class);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                farmDetails = (FarmDetails) bundle.getSerializable("FarmDetail");

                if(farmDetails != null) {
                    txtSubTitle.setText(farmDetails.getInventoryOrder());
                    txtSubTitle.setVisibility(View.VISIBLE);

                    tvInventoryOrder.setText(farmDetails.getInventoryOrder());
                    tvSupplierName.setText(farmDetails.getSupplierName());
                    tvPurchaseDate.setText(farmDetails.getPurchaseDate());
                    tvTruckPlateNumber.setText(farmDetails.getTruckPlateNumber());
                    tvPurchaseContract.setText(String.format("%s - %s", farmDetails.getDescription(), farmDetails.getMeasurementSystem()));
                    tvTotalPieces.setText(String.valueOf(farmDetails.getTotalPieces()));
                    DecimalFormat df = new DecimalFormat("0.000");
                    tvGrossVolume.setText(df.format(farmDetails.getGrossVolume()));

                    ivExpand.setOnClickListener(v -> {
                        if (llFarmDetails2.getVisibility() == View.VISIBLE) {
                            llFarmDetails2.setVisibility(View.GONE);
                            ivExpand.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_down));
                        } else {
                            llFarmDetails2.setVisibility(View.VISIBLE);
                            ivExpand.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_up));
                        }
                    });

                    fetchData();

                } else {
                    finish();
                }

            } else {
                finish();
            }

        } catch (Exception e) {
            Log.e("FarmCapturedActivity", "Error in FarmCapturedDataActivity InitComponents", e);
        }
    }

    private void fetchData() {
        farmCapturedDataList = new ArrayList<>();
        farmCapturedDataList = farmViewModel.getFarmCapturedData(farmDetails.getInventoryOrder());
        DecimalFormat df = new DecimalFormat("0.000");
        DecimalFormat df1 = new DecimalFormat("##.##");

        if(!farmCapturedDataList.isEmpty()) {
            farmDetailsCommonRecyclerViewAdapter =
                    new CommonRecyclerViewAdapter<FarmCapturedData>(this, farmCapturedDataList, R.layout.row_dialog_farm_data_item) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, FarmCapturedData farmCapturedData) {
                    if (farmCapturedData != null) {
                        holder.setViewText(R.id.tvCircumference, df1.format(farmCapturedData.getCircumference()));
                        holder.setViewText(R.id.tvLength, df1.format(farmCapturedData.getLength()));
                        holder.setViewText(R.id.tvGrossVolume, df.format(farmCapturedData.getGrossVolume()));
                        holder.setViewText(R.id.tvPieces, String.valueOf(farmCapturedData.getPieces()));

                        holder.getView(R.id.imgEdit).setOnClickListener(v -> {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("FarmCapturedData", farmCapturedData);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        });

                        holder.getView(R.id.imgDelete).setOnClickListener(v -> showConfirmation(farmCapturedData));
                    }
                }
            };

            rvFarmDataList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvFarmDataList.addItemDecoration(new DividerItemDecoration(this));
            rvFarmDataList.setAdapter(farmDetailsCommonRecyclerViewAdapter);
            farmDetailsCommonRecyclerViewAdapter.notifyItemRangeInserted(0, farmCapturedDataList.size());
            rvFarmDataList.setVisibility(View.VISIBLE);
            tvNoFarmData.setVisibility(View.GONE);
        } else {
            tvNoFarmData.setVisibility(View.VISIBLE);
            rvFarmDataList.setVisibility(View.GONE);
        }
    }

    private void showConfirmation(FarmCapturedData farmCapturedData) {
        try {
            if(farmCapturedData != null) {
                showDialogWithCancel(getString(R.string.delete_confirmation), getString(R.string.confirmation), (dialogInterface, i) -> {

                    if(farmViewModel.deleteFarmCapturedData(farmDetails,  farmCapturedData) > 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.data_deleted_successfully), Toast.LENGTH_SHORT).show();

                        farmDetails = farmViewModel.fetchFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId());
                        if(farmDetails != null) {

                            farmCapturedDataList = farmViewModel.getFarmCapturedData(farmDetails.getInventoryOrder());
                            farmDetailsCommonRecyclerViewAdapter.updateData(farmCapturedDataList);

                            DecimalFormat df = new DecimalFormat("0.000");
                            tvTotalPieces.setText(String.valueOf(farmDetails.getTotalPieces()));
                            tvGrossVolume.setText(df.format(farmDetails.getGrossVolume()));

                            if(!farmCapturedDataList.isEmpty()) {
                                tvNoFarmData.setVisibility(View.GONE);
                                rvFarmDataList.setVisibility(View.VISIBLE);
                            } else {
                                tvNoFarmData.setVisibility(View.VISIBLE);
                                rvFarmDataList.setVisibility(View.GONE);
                            }
                        }
                    }

                    dialogInterface.dismiss();
                }, getString(R.string.text_ok), getString(R.string.text_cancel));
            }
        } catch (Exception e) {
            Log.e("DashboardActivity", "Error in DashboardActivity showConfirmation", e);
        }
    }
}