package com.pintor.member_module.domain.auth.repository;

import com.pintor.member_module.domain.auth.entity.LoginToken;
import org.springframework.data.repository.CrudRepository;

public interface LoginTokenRepository extends CrudRepository<LoginToken, String> {
}
