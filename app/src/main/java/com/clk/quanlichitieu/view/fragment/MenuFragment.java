package com.clk.quanlichitieu.view.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.clk.quanlichitieu.R;
import com.clk.quanlichitieu.dao.entities.KhoanChi;
import com.clk.quanlichitieu.dao.entities.KhoanThu;
import com.clk.quanlichitieu.databinding.FragmentMenuBinding;
import com.clk.quanlichitieu.view.dialog.UpDateMoneyDialog;
import com.clk.quanlichitieu.view.fragment.chi.ChiFragment;
import com.clk.quanlichitieu.view.fragment.thongke.ThongkeFragment;
import com.clk.quanlichitieu.view.fragment.thu.ThuFragment;
import com.clk.quanlichitieu.viewmodel.MenuViewMOdel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;


public class MenuFragment extends BaseFragment<FragmentMenuBinding, MenuViewMOdel> {
    public static final String TAG = MenuFragment.class.getName();
    private long sumMoneyThu;
    private long sumMoneyChi;
    private long number;
    private String userId;
    private String money;
    private CollectionReference userRef;
    private HashMap<String, Object> userData;
    private boolean isDataMoneyLoaded;
    private boolean isDataThuLoaded;
    private boolean isDataChiLoaded;
    private AlertDialog dialog;
    private FirebaseUser user;

    @Override
    protected Class<MenuViewMOdel> initClassModel() {
        return MenuViewMOdel.class;
    }

    @Override
    protected void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progressbar);
        dialog = builder.create();
        binding.menu.ivMenu.setOnClickListener(this);
        binding.menuDrawer.tbKhoanChi.setOnClickListener(this);
        binding.menuDrawer.tbExit.setOnClickListener(this);
        binding.menuDrawer.tbKhoanThu.setOnClickListener(this);
        binding.menuDrawer.tbThongKe.setOnClickListener(this);
        binding.menuDrawer.tbLogout.setOnClickListener(this);
        binding.menuDrawer.tbExit.setOnClickListener(this);
        binding.menuDrawer.tbMyProfile.setOnClickListener(this);
        binding.menuDrawer.tbChangePassword.setOnClickListener(this);
        binding.menuDrawer.tbProfile.setOnClickListener(this);
        binding.tvNgay.setOnClickListener(this);
        binding.tvTuan.setOnClickListener(this);
        binding.tvThang.setOnClickListener(this);
        binding.tvNam.setOnClickListener(this);
        binding.tvChonNgay.setOnClickListener(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) userId = user.getUid();
        showUserInfomation();
        userRef = db.collection(userId);
        dialog.show();
        initMoney();
        loadDataThu();
        loadDataChi();
    }

    private void loadDataChi() {
        userRef.whereEqualTo("loai", "LOAIKHOANCHI").get().addOnCompleteListener(task -> {
            QuerySnapshot snapshot = task.getResult();
            sumMoneyChi = 0;
            for (DocumentSnapshot doc : snapshot) {
                KhoanChi khoanChi = doc.toObject(KhoanChi.class);
                assert khoanChi != null;
                sumMoneyChi += khoanChi.soTien;
            }
            isDataChiLoaded = true;
            checkDataLoaded();
        });
    }

    private void loadDataThu() {
        userRef.whereEqualTo("loai", "LOAIKHOANTHU").get().addOnCompleteListener(task -> {
            QuerySnapshot snapshot = task.getResult();
            sumMoneyThu = 0;
            for (DocumentSnapshot doc : snapshot) {
                KhoanThu khoanThu = doc.toObject(KhoanThu.class);
                assert khoanThu != null;
                sumMoneyThu += khoanThu.soTien;
            }
            isDataThuLoaded = true;
            checkDataLoaded();
        });
    }

    private void initMoney() {
        userRef.document(userId).get().addOnSuccessListener(documentSnapshot -> {
            money = documentSnapshot.getString("money");
            if (money != null) {
                number = Long.parseLong(money);
                isDataMoneyLoaded = true;
                checkDataLoaded();
            } else {
                UpDateMoneyDialog dialog = new UpDateMoneyDialog(context, (key, data) -> {
                    userData = new HashMap<>();
                    userData.put("money", data);
                    userRef.document(userId).set(userData);
                    number = Long.parseLong((String) data);
                    isDataMoneyLoaded = true;
                    checkDataLoaded();
                });
                dialog.show();
            }
        });
    }

    private void checkDataLoaded() {
        if (isDataThuLoaded && isDataChiLoaded && isDataMoneyLoaded) {
            setUpPieChart();
        }
    }

    private void setUpPieChart() {
        dialog.dismiss();
        int a, b;
        if ((sumMoneyThu + sumMoneyChi) == 0) {
            binding.piechart.addPieSlice(new PieModel("Tổng thu", 100, Color.parseColor("#FF1100")));
            return;
        }
        double tongMoney = sumMoneyThu + sumMoneyChi;
        double moneyThu = sumMoneyThu * 100;
        double moneyChi = sumMoneyChi * 100;
        a = (int) (moneyThu / tongMoney);
        b = (int) (moneyChi / tongMoney);
        binding.tvTongChi.setText(String.format("~ %s%%", b));
        binding.tvTongThu.setText(String.format("~ %s%%", a));
        long soDu = number - sumMoneyChi + sumMoneyThu;
        binding.tvNumberMoney.setText(String.format("Số tiền ban đầu: %s", initMoney(number)));
        binding.tvSoDu.setText(String.format("Số dư: %s", initMoney(soDu)));
        binding.menuDrawer.tvMoney.setText(String.format("Số dư: %s", initMoney(soDu)));
        binding.tvTongThu1.setText(String.format("Tổng thu: %s", initMoney(sumMoneyThu)));
        binding.tvTongChi1.setText(String.format("Tổng chi: %s", initMoney(sumMoneyChi)));
        binding.piechart.addPieSlice(new PieModel("Tổng thu", a, Color.parseColor("#FFA726")));
        binding.piechart.addPieSlice(new PieModel("Tổng chi", b, Color.parseColor("#66BB6A")));
        binding.piechart.startAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        isDataMoneyLoaded = false;
        isDataThuLoaded = false;
        isDataChiLoaded = false;
    }

    private String initMoney(long money) {
        try {
            Locale locale = new Locale("vi", "VN");
            Currency currency = Currency.getInstance("VND");
            DecimalFormatSymbols df = DecimalFormatSymbols.getInstance(locale);
            df.setCurrency(currency);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            numberFormat.setCurrency(currency);
            return numberFormat.format(money);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showUserInfomation() {
        if (user == null) return;
        if (user.getDisplayName() == null) {
            binding.menuDrawer.tvName.setVisibility(View.GONE);
        } else {
            binding.menuDrawer.tvName.setVisibility(View.VISIBLE);
            binding.menuDrawer.tvName.setText(user.getDisplayName());
        }
        binding.menuDrawer.tvEmail.setText(user.getEmail());
        Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avt).into(binding.menuDrawer.ivAvt);
    }

    @Override
    protected void clickView(View v) {
        if (v.getId() == R.id.iv_menu) {
            binding.drawer.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tb_khoan_chi) {
            binding.drawer.close();
            callBack.showFragment(ChiFragment.TAG, null, true);
        } else if (v.getId() == R.id.tb_khoan_thu) {
            binding.drawer.close();
            callBack.showFragment(ThuFragment.TAG, null, true);
        } else if (v.getId() == R.id.tb_thong_ke) {
            binding.drawer.close();
            callBack.showFragment(ThongkeFragment.TAG, null, true);
        } else if (v.getId() == R.id.tb_logout) {
            FirebaseAuth.getInstance().signOut();
            callBack.showFragment(LoginFragment.TAG, null, false);
        } else if (v.getId() == R.id.tb_exit) {
            binding.drawer.close();
            callBack.backToPrevious();
        } else if (v.getId() == R.id.tb_my_profile || v.getId() == R.id.tb_profile) {
            binding.drawer.close();
            callBack.showFragment(UpdateProfileFrg.TAG, null, true);
        } else if (v.getId() == R.id.tb_change_password) {
            binding.drawer.close();
            callBack.showFragment(ChangePassword.TAG, null, true);
        }
    }

    @Override
    protected FragmentMenuBinding initViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMenuBinding.inflate(inflater, container, false);
    }
}
