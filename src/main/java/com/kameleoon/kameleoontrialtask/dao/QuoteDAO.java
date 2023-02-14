package com.kameleoon.kameleoontrialtask.dao;

import com.kameleoon.kameleoontrialtask.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteDAO extends JpaRepository<Quote, Integer> {
}
