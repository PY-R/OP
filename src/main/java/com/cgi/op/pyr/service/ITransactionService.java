package com.cgi.op.pyr.service;

import com.cgi.op.pyr.exception.AppException;
import com.cgi.op.pyr.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Transaction service */
public interface ITransactionService {
  /**
   * Find by id
   *
   * @param id
   * @return
   */
  Mono<Transaction> findById(long id);

  /**
   * Find all
   *
   * @return
   */
  Flux<Transaction> findAll();

  /**
   * Save
   *
   * @param t
   * @return
   * @throws AppException
   */
  Mono<Transaction> save(Transaction t) throws AppException;
}
