package com.cgi.op.pyr.repository;

import com.cgi.op.pyr.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/** Transaction Reactive Crud Repository */
@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {}
