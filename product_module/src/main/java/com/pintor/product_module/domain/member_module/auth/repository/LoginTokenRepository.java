package com.pintor.product_module.domain.member_module.auth.repository;

import com.pintor.product_module.domain.member_module.auth.entity.LoginToken;
import org.springframework.data.repository.CrudRepository;

public interface LoginTokenRepository extends CrudRepository<LoginToken, String> {
}
