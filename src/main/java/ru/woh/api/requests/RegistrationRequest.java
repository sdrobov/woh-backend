package ru.woh.api.requests;

public class RegistrationRequest extends LoginRequest {
    protected String name = "";

    public RegistrationRequest() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
