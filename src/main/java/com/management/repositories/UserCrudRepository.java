package com.management.repositories;

import com.management.model.UserAccount;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<UserAccount, Long> {

    public Optional<UserAccount> findByUserName(String userName);

    public List<UserAccount> findAllByRole(String s);


}
