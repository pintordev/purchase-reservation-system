package com.pintor.purchase_module.domain.member_module.auth.repository;

import com.pintor.purchase_module.domain.member_module.auth.entity.MailToken;
import org.springframework.data.repository.CrudRepository;

public interface MailTokenRepository extends CrudRepository<MailToken, String> {
}
