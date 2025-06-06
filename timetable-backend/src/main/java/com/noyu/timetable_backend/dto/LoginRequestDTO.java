package com.noyu.timetable_backend.dto;

// ユーザー名とパスワードをリクエストボディとして受け取る
public class LoginRequestDTO {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
