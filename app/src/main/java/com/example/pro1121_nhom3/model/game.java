package com.example.pro1121_nhom3.model;

public class game {

    private String magame;
    private String tengame;
    private String nph;
    private float giaban;
    private String img;
    private loaigame loaigame;
    private String mota;
    private int likecount;
    private long sellcount;
    private String ngayph;

    public game() {
    }

    public game(String tengame, String nph, float giaban, String img, String ngayph, com.example.pro1121_nhom3.model.loaigame loaigame, String mota) {
        this.tengame = tengame;
        this.nph = nph;
        this.giaban = giaban;
        this.ngayph = ngayph;
        this.img = img;
        this.loaigame = loaigame;
        this.mota = mota;
    }

    public String getMagame() {
        return magame;
    }

    public void setMagame(String magame) {
        this.magame = magame;
    }

    public String getTengame() {
        return tengame;
    }

    public void setTengame(String tengame) {
        this.tengame = tengame;
    }

    public String getNph() {
        return nph;
    }

    public void setNph(String nph) {
        this.nph = nph;
    }

    public float getGiaban() {
        return giaban;
    }

    public void setGiaban(float giaban) {
        this.giaban = giaban;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public com.example.pro1121_nhom3.model.loaigame getLoaigame() {
        return loaigame;
    }

    public void setLoaigame(com.example.pro1121_nhom3.model.loaigame loaigame) {
        this.loaigame = loaigame;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public long getSellcount() {
        return sellcount;
    }

    public void setSellcount(long sellcount) {
        this.sellcount = sellcount;
    }

    public String getNgayph() {
        return ngayph;
    }

    public void setNgayph(String ngayph) {
        this.ngayph = ngayph;
    }

}