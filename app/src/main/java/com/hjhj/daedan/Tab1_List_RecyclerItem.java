package com.hjhj.daedan;

public class Tab1_List_RecyclerItem {
    //고유id, 닉네임, 카테고리명, 사진url, 제목, 메세지, 타임렝스...
    int imgUrl;
    public String imgurl;
    public String category;
    public String nickname;
    public String school;
    public String title;
    public String duration;
    public String msg;

    public Tab1_List_RecyclerItem() {
    }



    public Tab1_List_RecyclerItem(int imgUrl,String category, String nickname, String school, String title, String msg,String duration) {
        this.imgUrl = imgUrl;
        this.category = category;
        this.nickname = nickname;
        this.school = school;
        this.msg = msg;
        this.title = title;
        this.duration = duration;
    }
    public Tab1_List_RecyclerItem(String category, String nickname, String school, String title, String msg,String duration) {
        this.category = category;
        this.nickname = nickname;
        this.school = school;
        this.msg = msg;
        this.title = title;
        this.duration = duration;
    }




}
