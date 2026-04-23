package com.realestate.repository;

import com.realestate.entity.NegotiationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationMessage, Long> {

    List<NegotiationMessage> findByInquiryId(Long inquiryId);
}