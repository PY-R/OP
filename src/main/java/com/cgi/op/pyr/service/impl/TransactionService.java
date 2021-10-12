package com.cgi.op.pyr.service.impl;

import com.cgi.op.pyr.exception.AppException;
import com.cgi.op.pyr.model.OrderLine;
import com.cgi.op.pyr.model.Transaction;
import com.cgi.op.pyr.repository.OrderLineRepository;
import com.cgi.op.pyr.repository.TransactionRepository;
import com.cgi.op.pyr.service.ITransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

/** {@inheritDoc} */
@Service
public class TransactionService implements ITransactionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

  @Autowired private TransactionRepository transactionRepository;
  @Autowired private OrderLineRepository orderLineRepository;

  /** {@inheritDoc} */
  @Override
  public Mono<Transaction> findById(long id) {
    AtomicReference<Transaction> art = new AtomicReference<>();
    return transactionRepository
        .findById(id)
        .zipWith(
            orderLineRepository.findByTransactionId(id).collectList(),
            (t, ol) -> {
              if (art.get() == null) {
                art.set(t);
              }
              art.get().getOrderLineList().addAll(ol);
              return art.get();
            });
  }

  /** {@inheritDoc} */
  @Override
  public Flux<Transaction> findAll() {
    return transactionRepository.findAll();
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Mono<Transaction> save(Transaction tIn) throws AppException {
    if (tIn != null) {
      Mono<Transaction> monoT = null;

      if (tIn.getId() == null) {
        checkTransaction(null, tIn);
        monoT = Mono.empty();

      } else {
        monoT =
            transactionRepository
                .findById(tIn.getId())
                .map(
                    tOld -> {
                      try {
                        checkTransaction(tOld, tIn);
                      } catch (AppException e) {
                        throw new RuntimeException(e);
                      }
                      return tOld;
                    });
      }
      return monoT
          .then(transactionRepository.save(tIn))
          .map(
              t -> {
                for (OrderLine ol : t.getOrderLineList()) {
                  ol.setTransactionId(t.getId());
                }
                return t;
              })
          .thenMany(Flux.fromIterable(tIn.getOrderLineList()).flatMap(orderLineRepository::save))
          .then(Mono.just(tIn));
    } else {
      return Mono.empty();
    }
  }

  /**
   * Checked the transaction
   *
   * @param tOld
   * @param t
   * @return
   * @throws AppException
   */
  private Transaction checkTransaction(Transaction tOld, Transaction t) throws AppException {
    if (tOld == null) {
      if (t.getStatus() == null) {
        t.setStatus(Transaction.StatusEnum.NEW);
      }
      if (!Transaction.StatusEnum.NEW.equals(t.getStatus())) {
        throw new AppException(AppException.STATUS_MUST_BE_NEW_TO_ADD_TRANSACTION + t);
      }
    }
    /** if transaction equals AUTHORIZED then transaction can to be CAPTURED */
    if (Transaction.StatusEnum.CAPTURED.equals(t.getStatus())
        && !Transaction.StatusEnum.AUTHORIZED.equals(tOld.getStatus())
        && !Transaction.StatusEnum.CAPTURED.equals(tOld.getStatus())) {

      throw new AppException(
          AppException.STATUS_CAPTURED_IS_AVAILABLE_ONLY_FROM_AUTHORIZED_FOR_TRANSACTION
              + tOld.getId());
    }
    /** if transaction current status equals 'CAPTURED' then it is not editable */
    if (tOld != null
        && Transaction.StatusEnum.CAPTURED.equals(tOld.getStatus())
        && !Transaction.StatusEnum.CAPTURED.equals(t.getStatus())) {
      throw new AppException(
          AppException.STATUS_CAPTURED_CAN_NOT_BE_UPDATED_FOR_TRANSACTION + tOld.getId());
    }

    return t;
  }
}
