package com.cgi.op.pyr.controller;

import com.cgi.op.pyr.exception.AppException;
import com.cgi.op.pyr.model.Transaction;
import com.cgi.op.pyr.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CommonController.ROOT_REQUEST_MAPPING)
public class TransactionController extends CommonController {

  private static final String ENTITY_REQUEST_MAPPING_START = "/transaction/";
  @Autowired private ITransactionService transactionService;

  /**
   * Save
   *
   * @param transaction
   * @return
   * @throws AppException
   */
  @PostMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_SAVE)
  public ResponseEntity<Mono<Transaction>> save(@RequestBody Transaction transaction)
      throws AppException {
    return new ResponseEntity<>(transactionService.save(transaction), HttpStatus.OK);
  }

  /**
   * Find all
   *
   * @return
   */
  @GetMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_FIND_ALL)
  public ResponseEntity<Flux<Transaction>> findAll() {
    return new ResponseEntity<>(transactionService.findAll(), HttpStatus.OK);
  }

  /**
   * Find by id
   *
   * @param id
   * @return
   */
  @GetMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_FIND_BY_ID)
  public ResponseEntity<Mono<Transaction>> findById(
      @RequestParam(value = "id", required = true) long id) {
    return new ResponseEntity<>(transactionService.findById(id), HttpStatus.OK);
  }
}
