package ru.woh.api.requests;

import java.util.Objects;

public class ChangePasswordRequest {
    protected String password;
    protected String password2;

    public ChangePasswordRequest() {
    }

    public Boolean isValid() {
        return Objects.equals(this.password, this.password2)
            && this.password != null
            && !this.password.isEmpty();
    }

    public String getPassword() {
        return this.password;
    }

    public String getPassword2() {
        return this.password2;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
