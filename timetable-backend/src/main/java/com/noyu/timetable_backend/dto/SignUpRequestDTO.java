package com.noyu.timetable_backend.dto;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequestDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    public SignUpRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;

        SignUpRequestDTO signUpRequestDTO = (SignUpRequestDTO) o;

        return Objects.equals(username, signUpRequestDTO.username) &&
                Objects.equals(email, signUpRequestDTO.email) &&
                Objects.equals(password, signUpRequestDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    @Override
    public String toString() {
        return "SignUpRequestDTO{" +
                "username = " + username + '\'' +
                ", email = " + email + '\'' +
                ", password = '[PROTECTED]'" +
                '}';
    }
}
