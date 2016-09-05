package com.ntr1x.treasure.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.security.SecurityPhoneCode;

public interface SecurityPhoneCodeRepository extends JpaRepository<SecurityPhoneCode, Long> {

    SecurityPhoneCode findByPhoneAndCode(long phone, int code);
}
