package com.cgi.op.pyr.service.impl;

import com.cgi.op.pyr.model.OrderLine;
import com.cgi.op.pyr.repository.OrderLineRepository;
import com.cgi.op.pyr.service.IOrderLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** {@inheritDoc} */
@Service
public class OrderLineService implements IOrderLineService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderLineService.class);

  @Autowired private OrderLineRepository orderLineRepository;

  /** {@inheritDoc} */
  @Override
  public Mono<OrderLine> findById(long id) {
    return orderLineRepository.findById(id);
  }

  /** {@inheritDoc} */
  @Override
  public Mono<OrderLine> save(OrderLine ol) {
    return orderLineRepository.save(ol);
  }

  /** {@inheritDoc} */
  @Override
  public Flux<OrderLine> findByTransactionId(long id) {
    return orderLineRepository.findByTransactionId(id);
  }

  /** {@inheritDoc} */
  @Override
  public Flux<OrderLine> findAll() {
    return orderLineRepository.findAll();
  }
}
