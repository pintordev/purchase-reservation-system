package com.pintor.member_module.domain.auth.repository;

import com.pintor.member_module.domain.auth.entity.MailToken;
import org.springframework.data.repository.CrudRepository;

public interface MailTokenRepository extends CrudRepository<MailToken, String> {
}
