package com.zoombank.wallet_api;

import com.zoombank.wallet_api.accounts.Account;
import com.zoombank.wallet_api.accounts.AccountsRepository;
import com.zoombank.wallet_api.customers.Customer;
import com.zoombank.wallet_api.customers.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DetailsService implements UserDetailsService {

    @Autowired
    AccountsRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Account> result = users.findById(username);
        if (!result.isPresent()){
            throw new UsernameNotFoundException(username + " was not found");
        }
        Account user = result.get();

        return new org.springframework.security.core.userdetails.User(
                user.getAccountId(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLES")
        );
    }
}
