package ru.yandex.praktikum.model;

public class User {
    private String email;
    private String password;
    private String name;
    private String token;
    private User user;

    public User(String email, String password, String name, String token) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.token = token;
    }

    public User(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name=" + name +
                ", token=" + token +
                '}';
    }


}
