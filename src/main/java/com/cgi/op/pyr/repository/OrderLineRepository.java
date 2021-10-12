package com.cgi.op.pyr.repository;

import com.cgi.op.pyr.model.OrderLine;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static com.cgi.op.pyr.model.OrderLine.ORDER_LINE_TABLE_NAME;
import static com.cgi.op.pyr.model.OrderLine.ORDER_LINE_TRANSACTION_ID;

/** OrderLine Reactive Crud Repository */
@Repository
public interface OrderLineRepository extends ReactiveCrudRepository<OrderLine, Long> {

  @Query(
      "SELECT * FROM " + ORDER_LINE_TABLE_NAME + " WHERE " + ORDER_LINE_TRANSACTION_ID + " = :tId")
  Flux<OrderLine> findByTransactionId(long tId);
}
