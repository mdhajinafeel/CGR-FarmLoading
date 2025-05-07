package com.codringreen.farmloading.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codringreen.farmloading.R;
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

public class FarmListsActivity extends BaseActivity {

    private RecyclerView rvFarmList;
    private List<FarmDetails> farmDetailsList;
    private AppCompatTextView tvNoFarm;

    private FarmViewModel farmViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_farm_list);
        initComponents();
    }

    private void initComponents() {
        try {
            hideKeyboard(this);

            AppCompatImageView imgBack = findViewById(R.id.imgBack);
            AppCompatTextView txtTitle = findViewById(R.id.txtTitle);
            AppCompatButton btnCreateFarm = findViewById(R.id.btnCreateFarm);
            rvFarmList = findViewById(R.id.rvFarmList);
            tvNoFarm = findViewById(R.id.tvNoFarm);

            txtTitle.setText(getString(R.string.farm_list));
            imgBack.setOnClickListener(v -> finish());

            farmViewModel = new ViewModelProvider(this, viewModelFactory).get(FarmViewModel.class);

            btnCreateFarm.setOnClickListener(v -> startActivity(new Intent(FarmListsActivity.this, CreateFarmActivity.class).putExtra("IsEdit", false)));

            fetchData();

        } catch (Exception e) {
            Log.e("FarmListsActivity", "Error in FarmListsActivity InitComponents", e);
        }
    }

    private void fetchData() {
        farmDetailsList = new ArrayList<>();
        farmDetailsList = farmViewModel.fetchFarmDetails();
        DecimalFormat df = new DecimalFormat("0.000");

        if (!farmDetailsList.isEmpty()) {
            CommonRecyclerViewAdapter<FarmDetails> farmDetailsCommonRecyclerViewAdapter = new CommonRecyclerViewAdapter<FarmDetails>(this, farmDetailsList, R.layout.row_farm_item) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, FarmDetails farmDetails) {
                    if (farmDetails != null) {
                        holder.setViewText(R.id.tvInventoryOrder, farmDetails.getInventoryOrder());
                        holder.setViewText(R.id.tvSupplierName, farmDetails.getSupplierName());
                        holder.setViewText(R.id.tvPurchaseDate, farmDetails.getPurchaseDate());
                        holder.setViewText(R.id.tvPurchaseContract, farmDetails.getMeasurementSystem());
                        holder.setViewText(R.id.tvTotalGrossVolume, df.format(farmDetails.getGrossVolume()));
                        holder.setViewText(R.id.tvTotalPieces, String.valueOf(farmDetails.getTotalPieces()));

                        LinearLayout llRowFarmData = holder.getView(R.id.llRowFarmData);
                        AppCompatImageView imgEdit = holder.getView(R.id.imgEdit);
                        AppCompatImageView imgView = holder.getView(R.id.imgView);
                        if (farmDetails.isClosed()) {
                            holder.setViewText(R.id.tvStatus, getString(R.string.closed));
                            llRowFarmData.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDimGrey1));

                            imgEdit.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorDimGrey1));
                        } else {
                            holder.setViewText(R.id.tvStatus, getString(R.string.opened));
                            llRowFarmData.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));

                            imgEdit.setColorFilter(null);
                            imgEdit.setOnClickListener(v -> startActivity(new Intent(FarmListsActivity.this, FarmDataActivity.class)
                                    .putExtra("FarmDetail", farmDetails)));
                        }

                        imgView.setOnClickListener(v -> startActivity(new Intent(FarmListsActivity.this, CreateFarmActivity.class)
                                .putExtra("IsEdit", true)
                                .putExtra("FarmDetail", farmDetails)));
                    }
                }
            };

            rvFarmList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvFarmList.addItemDecoration(new DividerItemDecoration(this));
            rvFarmList.setAdapter(farmDetailsCommonRecyclerViewAdapter);
            farmDetailsCommonRecyclerViewAdapter.notifyItemRangeInserted(0, farmDetailsList.size());
            rvFarmList.setVisibility(View.VISIBLE);
            tvNoFarm.setVisibility(View.GONE);
        } else {
            tvNoFarm.setVisibility(View.VISIBLE);
            rvFarmList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }
}