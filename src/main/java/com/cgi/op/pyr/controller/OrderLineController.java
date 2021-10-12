package com.cgi.op.pyr.controller;

import com.cgi.op.pyr.model.OrderLine;
import com.cgi.op.pyr.service.IOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CommonController.ROOT_REQUEST_MAPPING)
public class OrderLineController extends CommonController {

  private static final String ENTITY_REQUEST_MAPPING_START = "/orderLine/";
  private static final String ACTION_FIND_BY_TRANSACTION_ID = "findByTransactionId";

  @Autowired private IOrderLineService orderLineService;

  /**
   * Save
   *
   * @param orderLine
   * @return
   */
  @PostMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_SAVE)
  public ResponseEntity<Mono<OrderLine>> save(@RequestBody OrderLine orderLine) {
    return new ResponseEntity<>(orderLineService.save(orderLine), HttpStatus.OK);
  }

  /**
   * Find all
   *
   * @return
   */
  @GetMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_FIND_ALL)
  public ResponseEntity<Flux<OrderLine>> findOrderLinesAll() {
    return new ResponseEntity<>(orderLineService.findAll(), HttpStatus.OK);
  }

  /**
   * Find by id
   *
   * @param id
   * @return
   */
  @GetMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_FIND_BY_ID)
  public ResponseEntity<Mono<OrderLine>> findById(
      @RequestParam(value = "id", required = true) long id) {
    return new ResponseEntity<>(orderLineService.findById(id), HttpStatus.OK);
  }

  /**
   * Find by Transaction id
   *
   * @param id
   * @return
   */
  @GetMapping(path = ENTITY_REQUEST_MAPPING_START + ACTION_FIND_BY_TRANSACTION_ID)
  public ResponseEntity<Flux<OrderLine>> findOrderLinesByTransactionId(
      @RequestParam(value = "id", required = true) long id) {
    return new ResponseEntity<>(orderLineService.findByTransactionId(id), HttpStatus.OK);
  }
}
