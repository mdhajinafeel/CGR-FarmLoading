package com.codringreen.farmloading.view.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.db.entity.FarmDetails;
import com.codringreen.farmloading.db.entity.InventoryNumbers;
import com.codringreen.farmloading.db.entity.PurchaseContract;
import com.codringreen.farmloading.db.entity.SupplierProductTypes;
import com.codringreen.farmloading.db.entity.SupplierProducts;
import com.codringreen.farmloading.db.entity.Suppliers;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.utils.CommonUtils;
import com.codringreen.farmloading.utils.CustomProgress;
import com.codringreen.farmloading.utils.DividerItemDecoration;
import com.codringreen.farmloading.view.adapter.CommonRecyclerViewAdapter;
import com.codringreen.farmloading.view.adapter.ViewHolder;
import com.codringreen.farmloading.viewmodel.FarmViewModel;
import com.codringreen.farmloading.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class CreateFarmActivity extends BaseActivity {

    private AppCompatTextView tvSupplierName, tvWoodSpecies, tvWoodType, tvPurchaseContract, tvPurchaseDate, tvNoDataFound;
    private AppCompatEditText etInventoryNumber, etTruckPlateNumber;
    private AppCompatButton btnSubmit;
    private List<Suppliers> suppliersList, suppliersArrayList;
    private List<SupplierProducts> suppliersProductsList, suppliersProductsArrayList;
    private List<SupplierProductTypes> suppliersProductTypesList, suppliersProductTypesArrayList;
    private List<PurchaseContract> purchaseContractList, purchaseContractArrayList;
    private CommonRecyclerViewAdapter<Suppliers> suppliersCommonRecyclerViewAdapter;
    private CommonRecyclerViewAdapter<SupplierProducts> supplierProductsCommonRecyclerViewAdapter;
    private CommonRecyclerViewAdapter<SupplierProductTypes> supplierProductTypesCommonRecyclerViewAdapter;
    private CommonRecyclerViewAdapter<PurchaseContract> purchaseContractCommonRecyclerViewAdapter;
    private final Calendar calendar = Calendar.getInstance();

    private FarmViewModel farmViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_farm);
        initComponents();
    }

    private void initComponents() {
        try {
            hideKeyboard(this);

            AppCompatImageView imgBack = findViewById(R.id.imgBack);
            AppCompatTextView txtTitle = findViewById(R.id.txtTitle);

            tvSupplierName = findViewById(R.id.tvSupplierName);
            tvWoodSpecies = findViewById(R.id.tvWoodSpecies);
            tvWoodType = findViewById(R.id.tvWoodType);
            tvPurchaseContract = findViewById(R.id.tvPurchaseContract);
            tvPurchaseDate = findViewById(R.id.tvPurchaseDate);
            etInventoryNumber = findViewById(R.id.etInventoryNumber);
            etTruckPlateNumber = findViewById(R.id.etTruckPlateNumber);
            btnSubmit = findViewById(R.id.btnSubmit);

            farmViewModel = new ViewModelProvider(this, viewModelFactory).get(FarmViewModel.class);

            txtTitle.setText(getString(R.string.create_farm));
            imgBack.setOnClickListener(v -> finish());

            farmViewModel.getError().observe(this, s ->
                    showDialog(s, farmViewModel.getErrorTitle(), null, getString(R.string.text_ok)));


            farmViewModel.getFarmCreateStatus().observe(this, aBoolean -> {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), R.string.data_download_success, Toast.LENGTH_SHORT).show();
                }
            });

            farmViewModel.getProgressBar().observe(this, aBoolean -> {
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

            btnSubmit.setOnClickListener(v -> saveFarmDetails());

            tvPurchaseDate.setText(CommonUtils.convertTimeStampToDate(CommonUtils.getCurrentLocalDateTimeStamp(), "dd/MM/yyyy"));
            suppliersList = new ArrayList<>();
            suppliersList = farmViewModel.fetchSupplierList();

            tvSupplierName.setOnClickListener(v -> showDataDialog("Supplier"));
            tvWoodSpecies.setOnClickListener(v -> showDataDialog("Product"));
            tvWoodType.setOnClickListener(v -> showDataDialog("ProductType"));
            tvPurchaseContract.setOnClickListener(v -> showDataDialog("PurchaseContract"));
            //tvPurchaseDate.setOnClickListener(v -> showDatePickerDialog());

            etInventoryNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etTruckPlateNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            PreferenceManager.INSTANCE.setLastTempReceptionId("");
        } catch (Exception e) {
            Log.e("CreateFarmActivity", "Error in CreateFarmActivity InitComponents", e);
        }
    }

    private void showDataDialog(String type) {
        Dialog dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setContentView(R.layout.list_dialog);

        AppCompatTextView dialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        tvNoDataFound = dialog.findViewById(R.id.tvNoDataFound);
        AppCompatImageView closeDialog = dialog.findViewById(R.id.imgClose);
        AppCompatEditText etSearch = dialog.findViewById(R.id.etSearch);
        RecyclerView rvList = dialog.findViewById(R.id.rvList);
        closeDialog.setOnClickListener(v -> dialog.dismiss());

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new DividerItemDecoration(this));

        if (type.equalsIgnoreCase("Supplier")) {
            suppliersArrayList = new ArrayList<>();
            dialogTitle.setText(getString(R.string.supplier_name));

            suppliersCommonRecyclerViewAdapter = new CommonRecyclerViewAdapter<Suppliers>
                    (this, suppliersList, R.layout.row_dialog_list) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, Suppliers supplier) {
                    if (supplier != null) {
                        if (supplier.isSelected()) {
                            holder.setViewVisibility(R.id.ivItemSelected, View.VISIBLE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_bold));
                        } else {
                            holder.setViewVisibility(R.id.ivItemSelected, View.GONE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_medium));
                        }
                        holder.setViewText(R.id.tvName, supplier.getSupplierName());
                    }
                }
            };

            rvList.setAdapter(suppliersCommonRecyclerViewAdapter);
            suppliersCommonRecyclerViewAdapter.setOnItemClickListener((view, position) -> {

                Suppliers suppliers;
                if (!suppliersArrayList.isEmpty()) {
                    suppliers = suppliersArrayList.get(position);
                } else {
                    suppliers = suppliersList.get(position);
                }

                tvSupplierName.setText(suppliers.getSupplierName());
                farmViewModel.setSelectedSupplier(suppliersList, suppliersArrayList, suppliers, position);
                farmViewModel.selectedSuppliersProducts = null;
                farmViewModel.selectedSuppliersProductTypes = null;
                farmViewModel.selectedPurchaseContract = null;

                suppliersProductsList = new ArrayList<>();
                suppliersProductTypesList = new ArrayList<>();
                purchaseContractList = new ArrayList<>();

                suppliersProductsList = farmViewModel.fetchSupplierProducts(suppliers.getSupplierId());

                tvPurchaseContract.setText("");
                tvWoodType.setText("");
                tvWoodSpecies.setText("");

                validateFields();
                dialog.dismiss();
            });
        } else if (type.equalsIgnoreCase("Product")) {
            suppliersProductsArrayList = new ArrayList<>();
            dialogTitle.setText(getString(R.string.wood_species));

            supplierProductsCommonRecyclerViewAdapter = new CommonRecyclerViewAdapter<SupplierProducts>
                    (this, suppliersProductsList, R.layout.row_dialog_list) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, SupplierProducts supplierProduct) {
                    if (supplierProduct != null) {
                        if (supplierProduct.isSelected()) {
                            holder.setViewVisibility(R.id.ivItemSelected, View.VISIBLE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_bold));
                        } else {
                            holder.setViewVisibility(R.id.ivItemSelected, View.GONE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_medium));
                        }
                        holder.setViewText(R.id.tvName, supplierProduct.getProductName());
                    }
                }
            };

            rvList.setAdapter(supplierProductsCommonRecyclerViewAdapter);
            supplierProductsCommonRecyclerViewAdapter.setOnItemClickListener((view, position) -> {

                SupplierProducts supplierProducts;
                if(!suppliersProductsArrayList.isEmpty()) {
                    supplierProducts = suppliersProductsArrayList.get(position);
                } else {
                    supplierProducts = suppliersProductsList.get(position);
                }

                tvWoodSpecies.setText(supplierProducts.getProductName());
                farmViewModel.setSelectedSupplierProducts(suppliersProductsList, suppliersProductsArrayList, supplierProducts, position);
                farmViewModel.selectedSuppliersProductTypes = null;
                farmViewModel.selectedPurchaseContract = null;

                suppliersProductTypesList = new ArrayList<>();
                purchaseContractList = new ArrayList<>();

                suppliersProductTypesList = farmViewModel.fetchSuppliersProductsType(supplierProducts.getSupplierId(),
                        supplierProducts.getProductId());

                tvPurchaseContract.setText("");
                tvWoodType.setText("");

                validateFields();
                dialog.dismiss();
            });
        } else if (type.equalsIgnoreCase("ProductType")) {
            suppliersProductTypesArrayList = new ArrayList<>();
            dialogTitle.setText(getString(R.string.wood_type));

            supplierProductTypesCommonRecyclerViewAdapter = new CommonRecyclerViewAdapter<SupplierProductTypes>
                    (this, suppliersProductTypesList, R.layout.row_dialog_list) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, SupplierProductTypes supplierProductType) {
                    if (supplierProductType != null) {
                        if (supplierProductType.isSelected()) {
                            holder.setViewVisibility(R.id.ivItemSelected, View.VISIBLE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_bold));
                        } else {
                            holder.setViewVisibility(R.id.ivItemSelected, View.GONE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_medium));
                        }
                        holder.setViewText(R.id.tvName, supplierProductType.getProductTypeName());
                    }
                }
            };

            rvList.setAdapter(supplierProductTypesCommonRecyclerViewAdapter);
            supplierProductTypesCommonRecyclerViewAdapter.setOnItemClickListener((view, position) -> {

                SupplierProductTypes supplierProductTypes;
                if(!suppliersProductTypesArrayList.isEmpty()) {
                    supplierProductTypes = suppliersProductTypesArrayList.get(position);
                } else {
                    supplierProductTypes = suppliersProductTypesList.get(position);
                }

                tvWoodType.setText(supplierProductTypes.getProductTypeName());
                farmViewModel.setSelectedSuppliersProductTypes(suppliersProductTypesList, suppliersProductTypesArrayList, supplierProductTypes, position);
                farmViewModel.selectedPurchaseContract = null;

                purchaseContractList = new ArrayList<>();

                List<Integer> productTypesList = new ArrayList<>();
                if (farmViewModel.selectedSuppliersProductTypes.getProductTypeId() == 2 || farmViewModel.selectedSuppliersProductTypes.getProductTypeId() == 4) {
                    productTypesList.add(2);
                    productTypesList.add(4);
                } else {
                    productTypesList.add(1);
                    productTypesList.add(3);
                }

                purchaseContractList = farmViewModel.fetchPurchaseContractList(supplierProductTypes.getSupplierId(),
                        supplierProductTypes.getProductId(), productTypesList);

                tvPurchaseContract.setText("");

                if(purchaseContractList.size() == 1) {
                    PurchaseContract purchaseContract = purchaseContractList.get(0);
                    farmViewModel.setSelectedPurchaseContract(purchaseContractList, purchaseContractList, purchaseContract, 0);

                    if(purchaseContract.getDescription() != null && !Objects.equals(purchaseContract.getDescription(), "")) {
                        tvPurchaseContract.setText(String.format("%s - %s", purchaseContract.getDescription(), purchaseContract.getPurchaseUnit()));
                    } else {
                        tvPurchaseContract.setText(String.format("%s", purchaseContract.getPurchaseUnit()));
                    }
                }

                validateFields();
                dialog.dismiss();
            });
        } else if (type.equalsIgnoreCase("PurchaseContract")) {
            purchaseContractArrayList = new ArrayList<>();
            dialogTitle.setText(getString(R.string.purchase_contract));

            purchaseContractCommonRecyclerViewAdapter = new CommonRecyclerViewAdapter<PurchaseContract>
                    (this, purchaseContractList, R.layout.row_dialog_list) {
                @Override
                public void onPostBindViewHolder(ViewHolder holder, PurchaseContract purchaseContract) {
                    if (purchaseContract != null) {
                        if (purchaseContract.isSelected()) {
                            holder.setViewVisibility(R.id.ivItemSelected, View.VISIBLE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_bold));
                        } else {
                            holder.setViewVisibility(R.id.ivItemSelected, View.GONE);
                            holder.setViewTypeface(R.id.tvName, ResourcesCompat.getFont(
                                    holder.itemView.getContext(), R.font.montserrat_medium));
                        }

                        if (purchaseContract.getDescription() != null && !purchaseContract.getDescription().isEmpty()) {
                            holder.setViewText(R.id.tvName, purchaseContract.getContractCode() + " - " + purchaseContract.getPurchaseUnit() + " - " + purchaseContract.getDescription());
                        } else {
                            holder.setViewText(R.id.tvName, purchaseContract.getContractCode() + " - " + purchaseContract.getPurchaseUnit());
                        }
                    }
                }
            };

            rvList.setAdapter(purchaseContractCommonRecyclerViewAdapter);
            purchaseContractCommonRecyclerViewAdapter.setOnItemClickListener((view, position) -> {

                PurchaseContract purchaseContract;
                if(!purchaseContractArrayList.isEmpty()) {
                    purchaseContract = purchaseContractArrayList.get(position);
                } else {
                    purchaseContract = purchaseContractList.get(position);
                }

                if(purchaseContract.getDescription() != null && !Objects.equals(purchaseContract.getDescription(), "")) {
                    tvPurchaseContract.setText(String.format("%s - %s", purchaseContract.getDescription(), purchaseContract.getPurchaseUnit()));
                } else {
                    tvPurchaseContract.setText(String.format("%s", purchaseContract.getPurchaseUnit()));
                }


                farmViewModel.setSelectedPurchaseContract(purchaseContractList, purchaseContractArrayList, purchaseContract, position);

                validateFields();
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.show();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString(), type);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void filter(String searchText, String type) {
        try {
            if (type.equalsIgnoreCase("Supplier")) {
                suppliersArrayList = new ArrayList<>();
                for (Suppliers suppliers : suppliersList) {
                    if (suppliers.getSupplierName().toLowerCase().contains(searchText.toLowerCase())
                            || suppliers.getSupplierCode().toLowerCase().contains(searchText.toLowerCase())) {
                        suppliersArrayList.add(suppliers);
                    }
                }
                if (suppliersArrayList.isEmpty()) {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    tvNoDataFound.setVisibility(View.GONE);
                }
                suppliersCommonRecyclerViewAdapter.filterList(suppliersArrayList);
            } else if (type.equalsIgnoreCase("Product")) {
                suppliersProductsArrayList = new ArrayList<>();
                for (SupplierProducts supplierProduct : suppliersProductsList) {
                    if (supplierProduct.getProductName().toLowerCase().contains(searchText.toLowerCase())) {
                        suppliersProductsArrayList.add(supplierProduct);
                    }
                }
                if (suppliersProductsArrayList.isEmpty()) {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    tvNoDataFound.setVisibility(View.GONE);
                }
                supplierProductsCommonRecyclerViewAdapter.filterList(suppliersProductsArrayList);
            } else if (type.equalsIgnoreCase("ProductType")) {
                suppliersProductTypesArrayList = new ArrayList<>();
                for (SupplierProductTypes supplierProductType : suppliersProductTypesList) {
                    if (supplierProductType.getProductTypeName().toLowerCase().contains(searchText.toLowerCase())) {
                        suppliersProductTypesArrayList.add(supplierProductType);
                    }
                }
                if (suppliersProductTypesArrayList.isEmpty()) {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    tvNoDataFound.setVisibility(View.GONE);
                }
                supplierProductTypesCommonRecyclerViewAdapter.filterList(suppliersProductTypesArrayList);
            } else if (type.equalsIgnoreCase("PurchaseContract")) {
                purchaseContractArrayList = new ArrayList<>();
                for (PurchaseContract purchaseContract : purchaseContractList) {
                    if (purchaseContract.getContractCode().toLowerCase().contains(searchText.toLowerCase())
                            || purchaseContract.getPurchaseUnit().toLowerCase().contains(searchText.toLowerCase())
                            || purchaseContract.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                        purchaseContractArrayList.add(purchaseContract);
                    }
                }
                if (purchaseContractArrayList.isEmpty()) {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    tvNoDataFound.setVisibility(View.GONE);
                }
                purchaseContractCommonRecyclerViewAdapter.filterList(purchaseContractArrayList);
            }
        } catch (Exception e) {
            Log.e("CreateFarmActivity", "Error in CreateFarmActivity filter", e);
        }
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    tvPurchaseDate.setText(sdf.format(calendar.getTime()));

                }, year, month, day);

        datePickerDialog.show();
    }

    private void validateFields() {
        try {
            if (farmViewModel.selectedSupplier != null && farmViewModel.selectedSupplier.getSupplierId() > 0) {
                if (farmViewModel.selectedSuppliersProducts != null && farmViewModel.selectedSuppliersProducts.getProductId() > 0) {
                    if (farmViewModel.selectedSuppliersProductTypes != null && farmViewModel.selectedSuppliersProductTypes.getProductTypeId() > 0) {
                        if (farmViewModel.selectedPurchaseContract != null && farmViewModel.selectedPurchaseContract.getContractId() > 0) {
                            if (Objects.requireNonNull(etInventoryNumber.getText()).length() > 0) {
                                btnSubmit.setEnabled(Objects.requireNonNull(etTruckPlateNumber.getText()).length() > 0);
                            } else {
                                btnSubmit.setEnabled(false);
                            }
                        } else {
                            btnSubmit.setEnabled(false);
                        }
                    } else {
                        btnSubmit.setEnabled(false);
                    }
                } else {
                    btnSubmit.setEnabled(false);
                }
            } else {
                btnSubmit.setEnabled(false);
            }
        } catch (Exception e) {
            Log.e("CreateFarmActivity", "Error in CreateFarmActivity validateFields", e);
        }
    }

    private void saveFarmDetails() {
        try {

            int countInventoryNumber = farmViewModel.getInventoryCount(Objects.requireNonNull(etInventoryNumber.getText()).toString(),
                    farmViewModel.selectedSupplier.getSupplierId());

            if(countInventoryNumber > 0) {
                showDialog(getString(R.string.inventory_number_already_exists), getString(R.string.information),
                        (dialog, which) -> dialog.dismiss(), getString(R.string.text_ok));
            } else {
                FarmDetails farmDetail = new FarmDetails();
                farmDetail.setFarmId(0);
                farmDetail.setSupplierId(farmViewModel.selectedSupplier.getSupplierId());
                farmDetail.setProductId(farmViewModel.selectedSuppliersProducts.getProductId());
                farmDetail.setProductTypeId(farmViewModel.selectedSuppliersProductTypes.getProductTypeId());
                farmDetail.setInventoryOrder(Objects.requireNonNull(etInventoryNumber.getText()).toString());
                farmDetail.setPurchaseContractId(farmViewModel.selectedPurchaseContract.getContractId());
                farmDetail.setPurchaseDate(Objects.requireNonNull(tvPurchaseDate.getText()).toString());
                farmDetail.setTruckPlateNumber(Objects.requireNonNull(etTruckPlateNumber.getText()).toString());
                farmDetail.setSupplierName(farmViewModel.selectedSupplier.getSupplierName());
                farmDetail.setProductName(farmViewModel.selectedSuppliersProducts.getProductName());
                farmDetail.setMeasurementSystem(farmViewModel.selectedPurchaseContract.getPurchaseUnit());
                farmDetail.setCircAllowance(farmViewModel.selectedPurchaseContract.getCircAllowance());
                farmDetail.setLengthAllowance(farmViewModel.selectedPurchaseContract.getLengthAllowance());
                farmDetail.setDescription(farmViewModel.selectedPurchaseContract.getDescription());
                farmDetail.setTotalPieces(0);
                farmDetail.setGrossVolume(0);
                farmDetail.setNetVolume(0);
                farmDetail.setPurchaseUnitId(farmViewModel.selectedPurchaseContract.getPurchaseUnitId());

                String tempFarmId = "F_" + CommonUtils.getCurrentLocalDateTimeStamp();
                if (!Objects.equals(PreferenceManager.INSTANCE.getLastTempReceptionId(), "")) {
                    farmDetail.setTempFarmId(PreferenceManager.INSTANCE.getLastTempReceptionId());
                } else {
                    farmDetail.setTempFarmId(tempFarmId);
                }

                if (!Objects.equals(PreferenceManager.INSTANCE.getLastTempReceptionId(), "")) {
                    Toast.makeText(getApplicationContext(), R.string.farm_created, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    PreferenceManager.INSTANCE.setLastTempReceptionId(tempFarmId);
                    if (farmViewModel.saveFarmDetails(farmDetail) > 0) {

                        InventoryNumbers inventoryNumber = new InventoryNumbers();
                        inventoryNumber.setInventoryNumber(Objects.requireNonNull(etInventoryNumber.getText()).toString());
                        inventoryNumber.setSupplierId(farmViewModel.selectedSupplier.getSupplierId());

                        farmViewModel.saveInventoryNumbers(inventoryNumber);

                        Toast.makeText(getApplicationContext(), R.string.farm_created, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CreateFarmActivity", "Error in CreateFarmActivity saveFarmDetails", e);
        }
    }
}