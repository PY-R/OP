package com.cgi.op.pyr.service;

import com.cgi.op.pyr.model.OrderLine;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** OrderLine service */
public interface IOrderLineService {

  /**
   * Find by id
   *
   * @param id
   * @return
   */
  Mono<OrderLine> findById(long id);

  /**
   * Find all
   *
   * @return
   */
  Flux<OrderLine> findAll();

  /**
   * Find by Transaction id
   *
   * @param id
   * @return
   */
  Flux<OrderLine> findByTransactionId(long id);

  /**
   * Save
   *
   * @param ol
   * @return
   */
  Mono<OrderLine> save(OrderLine ol);
}
