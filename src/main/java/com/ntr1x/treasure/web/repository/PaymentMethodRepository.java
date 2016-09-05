package com.ntr1x.treasure.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntr1x.treasure.web.model.assets.PaymentMethod;
import com.ntr1x.treasure.web.model.purchase.ResourceType;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    
    PaymentMethod findByResType(ResourceType type);
    List<PaymentMethod> findAllByResType(ResourceType type);
}
