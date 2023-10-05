package com.clk.quanlichitieu.Model;

public class IntroModel {
  private final String tilte;
    private final String loinoi;
    private final int hinh;

    public IntroModel(String tilte, String loinoi, int hinh) {
        this.tilte = tilte;
        this.loinoi = loinoi;
        this.hinh = hinh;
    }

    public String getTilte() {
        return tilte;
    }

    public String getLoinoi() {
        return loinoi;
    }

    public int getHinh() {
        return hinh;
    }

}
