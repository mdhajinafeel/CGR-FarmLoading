package com.codringreen.farmloading.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.db.entity.FarmCapturedData;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.FarmDataModel;
import com.codringreen.farmloading.utils.CommonUtils;
import com.codringreen.farmloading.viewmodel.FarmViewModel;
import com.codringreen.farmloading.viewmodel.ViewModelFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class FarmDataActivity extends BaseActivity {

    private LinearLayout llFarmDetails2;
    private AppCompatEditText etCircumference, etLength, etPieces;
    private AppCompatTextView tvTotalPieces, tvGrossVolume;
    private AppCompatButton btnCloseFarm;
    private FarmDetails farmDetails;
    private int totalPieces;
    private double totalGrossVolume, totalNetVolume;
    private boolean isEdit = false;
    private FarmCapturedData farmCapturedDataEdit;

    private FarmViewModel farmViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_farm_data);
        initComponents();
    }

    private void initComponents() {
        try {
            hideKeyboard(this);

            AppCompatImageView imgBack = findViewById(R.id.imgBack);
            AppCompatImageView ivList = findViewById(R.id.ivList);
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

            btnCloseFarm = findViewById(R.id.btnCloseFarm);

            farmViewModel = new ViewModelProvider(this, viewModelFactory).get(FarmViewModel.class);

            etCircumference = findViewById(R.id.etCircumference);
            etLength = findViewById(R.id.etLength);
            etPieces = findViewById(R.id.etPieces);

            llFarmDetails2 = findViewById(R.id.llFarmDetails2);

            txtTitle.setText(getString(R.string.farm_data));
            imgBack.setOnClickListener(v -> finish());

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                farmDetails = (FarmDetails) bundle.getSerializable("FarmDetail");

                if (farmDetails != null) {
                    txtSubTitle.setText(farmDetails.getInventoryOrder());
                    txtSubTitle.setVisibility(View.VISIBLE);
                    ivList.setVisibility(View.VISIBLE);

                    tvInventoryOrder.setText(farmDetails.getInventoryOrder());
                    tvSupplierName.setText(farmDetails.getSupplierName());
                    tvPurchaseDate.setText(farmDetails.getPurchaseDate());
                    tvTruckPlateNumber.setText(farmDetails.getTruckPlateNumber());
                    tvPurchaseContract.setText(String.format("%s - %s", farmDetails.getDescription(), farmDetails.getMeasurementSystem()));
                    tvTotalPieces.setText(String.valueOf(farmDetails.getTotalPieces()));
                    Locale currentLocale;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        currentLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
                    } else {
                        currentLocale = Resources.getSystem().getConfiguration().locale;
                    }

                    NumberFormat nf = NumberFormat.getInstance(currentLocale);
                    DecimalFormat df = new DecimalFormat("0.000");
                    tvGrossVolume.setText(df.format(farmDetails.getGrossVolume()));

                    totalPieces = farmDetails.getTotalPieces();
                    totalGrossVolume = Objects.requireNonNull(nf.parse(df.format(farmDetails.getGrossVolume()))).doubleValue(); //Double.parseDouble(df.format(farmDetails.getGrossVolume()));
                    totalNetVolume = Objects.requireNonNull(nf.parse(df.format(farmDetails.getNetVolume()))).doubleValue(); //Double.parseDouble(df.format(farmDetails.getNetVolume()));

                    ivExpand.setOnClickListener(v -> {
                        if (llFarmDetails2.getVisibility() == View.VISIBLE) {
                            llFarmDetails2.setVisibility(View.GONE);
                            ivExpand.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_down));
                        } else {
                            llFarmDetails2.setVisibility(View.VISIBLE);
                            ivExpand.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_up));
                        }
                    });

                    focusEditText();

                    etPieces.setOnEditorActionListener((v, actionId, event) -> {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            boolean isValid = true;

                            if(Double.parseDouble(Objects.requireNonNull(etCircumference.getText()).toString()) <= 0) {
                                isValid = false;
                                Toast.makeText(getApplicationContext(), getString(R.string.girth_check), Toast.LENGTH_SHORT).show();
                            } else if(Double.parseDouble(Objects.requireNonNull(etLength.getText()).toString()) <= 0) {
                                isValid = false;
                                Toast.makeText(getApplicationContext(), getString(R.string.length_check), Toast.LENGTH_SHORT).show();
                            } else if(Integer.parseInt(Objects.requireNonNull(etPieces.getText()).toString()) <= 0) {
                                isValid = false;
                                Toast.makeText(getApplicationContext(), getString(R.string.pieces_check), Toast.LENGTH_SHORT).show();
                            }

                            if(isValid) {
                                FarmDataModel farmDataModel = calculateVolume();
                                if(isEdit) {
                                    FarmCapturedData farmCapturedData = new FarmCapturedData();
                                    farmCapturedData.setInventoryOrder(farmDataModel.getInventoryOrder());
                                    farmCapturedData.setCircumference(farmDataModel.getCircumference());
                                    farmCapturedData.setLength(farmDataModel.getLength());
                                    farmCapturedData.setCircAllowance(farmDataModel.getCircAllowance());
                                    farmCapturedData.setLengthAllowance(farmDataModel.getLengthAllowance());
                                    farmCapturedData.setPieces(farmDataModel.getPieces());
                                    farmCapturedData.setGrossVolume(farmDataModel.getGrossVolume());
                                    farmCapturedData.setNetVolume(farmDataModel.getNetVolume());
                                    farmCapturedData.setFarmId(farmCapturedDataEdit.getFarmId());
                                    farmCapturedData.setFarmDataId(farmCapturedDataEdit.getFarmDataId());
                                    farmCapturedData.setCaptureTimeStamp(farmCapturedDataEdit.getCaptureTimeStamp());
                                    farmCapturedData.setIsSynced(false);

                                    farmViewModel.updateFarmCapturedData(farmDetails, farmCapturedData);

                                    farmDetails = farmViewModel.fetchFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId());
                                    if(farmDetails != null) {
                                        tvTotalPieces.setText(String.valueOf(farmDetails.getTotalPieces()));
                                        tvGrossVolume.setText(df.format(farmDetails.getGrossVolume()));

                                        totalPieces = farmDetails.getTotalPieces();
                                        totalGrossVolume = farmDetails.getGrossVolume();
                                        totalNetVolume = farmDetails.getNetVolume();
                                    }

                                    isEdit = false;

                                } else {
                                    totalPieces = totalPieces + farmDataModel.getPieces();
                                    totalGrossVolume = totalGrossVolume + farmDataModel.getGrossVolume();
                                    totalNetVolume = totalNetVolume + farmDataModel.getNetVolume();

                                    tvTotalPieces.setText(String.valueOf(totalPieces));
                                    tvGrossVolume.setText(df.format(totalGrossVolume));

                                    etCircumference.setText("");
                                    etLength.setText("");
                                    etPieces.setText("");

                                    FarmCapturedData farmCapturedData = new FarmCapturedData();
                                    farmCapturedData.setInventoryOrder(farmDataModel.getInventoryOrder());
                                    farmCapturedData.setCircumference(farmDataModel.getCircumference());
                                    farmCapturedData.setLength(farmDataModel.getLength());
                                    farmCapturedData.setCircAllowance(farmDataModel.getCircAllowance());
                                    farmCapturedData.setLengthAllowance(farmDataModel.getLengthAllowance());
                                    farmCapturedData.setPieces(farmDataModel.getPieces());
                                    farmCapturedData.setGrossVolume(farmDataModel.getGrossVolume());
                                    farmCapturedData.setNetVolume(farmDataModel.getNetVolume());
                                    farmCapturedData.setFarmDataId(0);
                                    farmCapturedData.setFarmId(farmDetails.getFarmId());
                                    farmCapturedData.setIsSynced(false);
                                    farmCapturedData.setCaptureTimeStamp(CommonUtils.getCurrentLocalDateTimeStamp());

                                    farmViewModel.saveOrUpdateFarmData(farmCapturedData, farmDetails.getInventoryOrder(), farmDetails.getSupplierId(),
                                            totalPieces, totalGrossVolume, totalNetVolume);
                                }

                                focusEditText();
                                enabledCloseFarmBtn();
                                return true;
                            }
                        }
                        return false;
                    });

                    ivList.setOnClickListener(v -> {
                        farmDetails = farmViewModel.fetchFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId());
                        Intent intent = new Intent(FarmDataActivity.this, FarmCapturedDataActivity.class)
                                .putExtra("FarmDetail", farmDetails)
                                .putExtra("FarmData", (Serializable) farmViewModel.getFarmCapturedData(farmDetails.getInventoryOrder()));
                        farmDataLauncher.launch(intent);
                    });

                    btnCloseFarm.setOnClickListener(v ->
                            showDialogWithCancel(getString(R.string.are_you_sure_you_want_to_close_the_farm), getString(R.string.confirmation), (dialogInterface, i) -> {

                        //CLOSE FARM
                        int closedFarm = farmViewModel.updateFarmDetailsClosed(true, PreferenceManager.INSTANCE.getUserId(), CommonUtils.convertTimeStampToDate(CommonUtils.getCurrentLocalDateTimeStamp(),
                                "dd/MM/yyyy"), farmDetails.getInventoryOrder());

                        if(closedFarm > 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.farm_closed_successfully), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        dialogInterface.dismiss();
                    }, getString(R.string.text_ok), getString(R.string.text_cancel)));
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } catch (Exception e) {
            Log.e("FarmDataActivity", "Error in FarmDataActivity InitComponents", e);
        }
    }

    private FarmDataModel calculateVolume() {

        FarmDataModel farmDataModel = new FarmDataModel();
        try {

            farmDataModel.setInventoryOrder(farmDetails.getInventoryOrder());
            farmDataModel.setCircAllowance(farmDetails.getCircAllowance());
            farmDataModel.setLengthAllowance(farmDetails.getLengthAllowance());
            farmDataModel.setCircumference(Double.parseDouble(Objects.requireNonNull(etCircumference.getText()).toString()));
            farmDataModel.setLength(Double.parseDouble(Objects.requireNonNull(etLength.getText()).toString()));
            farmDataModel.setPieces(Integer.parseInt(Objects.requireNonNull(etPieces.getText()).toString()));

            double grossVolume = 0;
            double netVolume = 0;

            if (farmDetails.getMeasurementSystem().equalsIgnoreCase("Hoppus")
                    || farmDetails.getMeasurementSystem().equalsIgnoreCase("Fixed Price")) {
                grossVolume = CommonUtils.truncateDecimal((farmDataModel.getCircumference() * farmDataModel.getCircumference() * farmDataModel.getLength()) / 16000000, 3).doubleValue() * farmDataModel.getPieces();
                netVolume = CommonUtils.truncateDecimal(((farmDataModel.getCircumference() - farmDataModel.getCircAllowance()) * (farmDataModel.getCircumference() - farmDataModel.getCircAllowance())
                        * (farmDataModel.getLength() - farmDataModel.getLengthAllowance())) / 16000000, 3).doubleValue() * farmDataModel.getPieces();
            } else if (farmDetails.getMeasurementSystem().equalsIgnoreCase("Geo")) {
                grossVolume = CommonUtils.roundValue((farmDataModel.getCircumference() * farmDataModel.getCircumference() * farmDataModel.getLength() * 0.0796) / 1000000, 3).doubleValue() * farmDataModel.getPieces();
                netVolume = CommonUtils.roundValue(((farmDataModel.getCircumference() - farmDataModel.getCircAllowance()) * (farmDataModel.getCircumference() - farmDataModel.getCircAllowance())
                        * (farmDataModel.getLength() - farmDataModel.getLengthAllowance()) * 0.0796) / 1000000, 3).doubleValue() * farmDataModel.getPieces();
            } else if (farmDetails.getMeasurementSystem().equalsIgnoreCase("Per piece")) {
                grossVolume = CommonUtils.calculatePieceFormula(farmDataModel.getCircumference(), farmDataModel.getLength()) * farmDataModel.getPieces();
                netVolume = CommonUtils.calculatePieceFormula(farmDataModel.getCircumference(), farmDataModel.getLength()) * farmDataModel.getPieces();
            }

            farmDataModel.setGrossVolume(grossVolume);
            farmDataModel.setNetVolume(netVolume);
            return farmDataModel;
        } catch (Exception e) {
            Log.e("FarmDataActivity", "Error in FarmDataActivity calculateVolume", e);
        }
        return farmDataModel;
    }

    private void focusEditText() {
        etCircumference.requestFocus();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etCircumference, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);

        if(!isEdit) {
            etCircumference.setText("");
            etLength.setText("");
            etPieces.setText("");
            isEdit = false;
        }
    }

    private void enabledCloseFarmBtn() {
        if (totalPieces > 0 && totalGrossVolume > 0) {
            btnCloseFarm.setEnabled(true);
        }
    }

    ActivityResultLauncher<Intent> farmDataLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        farmCapturedDataEdit = (FarmCapturedData) data.getSerializableExtra("FarmCapturedData");
                        if(farmCapturedDataEdit != null) {
                            DecimalFormat format = new DecimalFormat("##.##");
                            etCircumference.setText(format.format(farmCapturedDataEdit.getCircumference()));
                            etLength.setText(format.format(farmCapturedDataEdit.getLength()));
                            etPieces.setText(String.valueOf(farmCapturedDataEdit.getPieces()));

                            etCircumference.requestFocus();
                            etCircumference.setSelection(Objects.requireNonNull(etCircumference.getText()).length());

                            isEdit = true;
                        }
                    }
                }
            });

    @Override
    protected void onResume() {
        super.onResume();

        farmDetails = farmViewModel.fetchFarmDetails(farmDetails.getInventoryOrder(), farmDetails.getSupplierId());
        if(farmDetails != null) {
            DecimalFormat df = new DecimalFormat("0.000");
            tvTotalPieces.setText(String.valueOf(farmDetails.getTotalPieces()));
            tvGrossVolume.setText(df.format(farmDetails.getGrossVolume()));

            totalPieces = farmDetails.getTotalPieces();
            totalGrossVolume = farmDetails.getGrossVolume();
            totalNetVolume = farmDetails.getNetVolume();
        }

        focusEditText();
        enabledCloseFarmBtn();
    }
}