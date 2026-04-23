package com.realestate.repository;

import com.realestate.entity.SaleInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleInquiryRepository extends JpaRepository<SaleInquiry, Long> {

    List<SaleInquiry> findByCustomerEmail(String email);

    List<SaleInquiry> findByOwnerEmail(String email);
}