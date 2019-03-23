package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login;

public class Login {
    private String id;
    private String username;
    private String token;

    public Login(String id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == null) return false;
        if (o == null) return false;
        if (!(o instanceof Login)) return false;

        Login login = (Login) o;

        return getId().equals(login.getId()) &&
                getUsername().equals(login.getUsername()) &&
                getToken().equals(login.getToken());
    }

    @Override
    public int hashCode() {
        int hashCode;
        hashCode = (id != null ? id.hashCode() : 0);
        hashCode = 31 * hashCode + (username != null ? username.hashCode() : 0);
        hashCode = 31 * hashCode + (token != null ? token.hashCode() : 0);
        return hashCode;
    }
}
