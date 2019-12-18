package com.web.product.repository;

import com.web.product.entity.Provider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends CrudRepository<Provider, Long> {
}
