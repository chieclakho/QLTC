package com.clk.quanlichitieu;

public interface OnMainCallBack {
    void showFragment(String tag, Object data, boolean isBacked);

    void backToPrevious();

    void runOnUi(Runnable runnable);
}
