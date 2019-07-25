package com.management.repositories;
import com.management.model.UserAccount;

import org.springframework.data.repository.CrudRepository;

public interface UserCrudRepository extends CrudRepository<UserAccount, Long>{
	public UserAccount findByUserName(String userName);
}
