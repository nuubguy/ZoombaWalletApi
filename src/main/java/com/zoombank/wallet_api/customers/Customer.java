package com.zoombank.wallet_api.customers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoombank.wallet_api.accounts.Account;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represent that use the banking service
 */
@Entity
public class Customer {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String info;

    @Id
    private String customerId;

    @Column
    @NotNull
    private boolean disabled = false;

    @JsonIgnore
    @Column
    private String password;

    public Customer(){

    }

    public Customer(String name, String info){

        this.name = name;
        this.info = info;
        this.setPassword("P@ssw0rd");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
