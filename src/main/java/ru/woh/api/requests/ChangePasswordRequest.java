package ru.woh.api.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {
    protected String password;
    protected String password2;

    public Boolean isValid() {
        return Objects.equals(this.password, this.password2)
            && this.password != null
            && !this.password.isEmpty();
    }
}
