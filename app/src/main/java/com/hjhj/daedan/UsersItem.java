package com.hjhj.daedan;

public class UsersItem {
    // 프로필URL, 닉네임, userid, email
    public String profile;
    public String nickname;
    public String userid;
    public String email;
    public String token;

    public UsersItem() {
    }

    public UsersItem(String profile, String nickname, String userid, String email, String token) {
        this.profile = profile;
        this.nickname = nickname;
        this.userid = userid;
        this.email = email;
        this.token = token;
    }
}
