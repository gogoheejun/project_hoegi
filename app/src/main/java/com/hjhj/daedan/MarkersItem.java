package com.hjhj.daedan;
//글쓴이아이디, 카테고리, 등록유지시간, 업로드시간, 제목,내용, 이미지url,위치 올려야 함.
public class MarkersItem {
    public String school;
    public String nickname;
    public String userid;
    public String category;
    public String uploadTime;
    public String timeLength;
    public String title;
    public String message;
    public String imgUrl;
    public String lat;
    public String lon;

    public MarkersItem() {
    }

    public MarkersItem(String school, String nickname, String userid, String category, String uploadTime, String timeLength, String title, String message, String imgUrl, String lat, String lon) {
        this.school = school;
        this.nickname = nickname;
        this.userid = userid;
        this.category = category;
        this.uploadTime = uploadTime;
        this.timeLength = timeLength;
        this.title = title;
        this.message = message;
        this.imgUrl = imgUrl;
        this.lat = lat;
        this.lon = lon;
    }
    //이미지 선택 안했을때
    public MarkersItem(String school, String nickname, String userid, String category, String uploadTime, String timeLength, String title, String message, String lat, String lon) {
        this.school = school;
        this.nickname = nickname;
        this.userid = userid;
        this.category = category;
        this.uploadTime = uploadTime;
        this.timeLength = timeLength;
        this.title = title;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
    }
}
