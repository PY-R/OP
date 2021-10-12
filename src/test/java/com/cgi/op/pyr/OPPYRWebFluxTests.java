package com.cgi.op.pyr;

import com.cgi.op.pyr.config.GlobalErrorWebExceptionHandler;
import com.cgi.op.pyr.exception.AppException;
import com.cgi.op.pyr.model.OrderLine;
import com.cgi.op.pyr.model.Transaction;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Note : Javadoc is the Test method name */
@SpringBootTest
@AutoConfigureWebTestClient
public class OPPYRWebFluxTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(OPPYRWebFluxTests.class);
  private static AtomicInteger createTransactionCount = new AtomicInteger(0);
  @Autowired private WebTestClient webClient;

  /**
   * Create CREDIT_CARD transaction for an amount of 54,80 EUR and an order of 4 'Ski gloves' at 10
   * EUR and 1 'Woolen cap' at 14.80 EUR
   *
   * @return transaction
   */
  private Transaction createTransactionCreditCard(boolean willBeCreated) {
    if (willBeCreated) {
      createTransactionCount.incrementAndGet();
    }
    return Transaction.builder()
        .amount(BigDecimal.valueOf(54.80))
        .paymentType(Transaction.PaymentTypeEnum.CREDIT_CARD)
        .orderLineList(
            Arrays.asList(
                OrderLine.builder()
                    .productName("Ski gloves")
                    .price(BigDecimal.valueOf(10.00))
                    .qty(4)
                    .build(),
                OrderLine.builder()
                    .productName("Woolen cap")
                    .price(BigDecimal.valueOf(14.80))
                    .qty(1)
                    .build()))
        .build();
  }

  /**
   * Create PAYPAL transaction for an amount of 208.00 EUR and an order of 1 bike at 208.00 EUR
   *
   * @return transaction
   */
  private Transaction createTransactionPaypal(boolean willBeCreated) {
    if (willBeCreated) {
      createTransactionCount.incrementAndGet();
    }
    return Transaction.builder()
        .amount(BigDecimal.valueOf(208.00))
        .paymentType(Transaction.PaymentTypeEnum.PAYPAL)
        .orderLineList(
            Arrays.asList(
                OrderLine.builder()
                    .productName("Bike")
                    .price(BigDecimal.valueOf(208.00))
                    .qty(1)
                    .build()))
        .build();
  }

  private boolean isErrorMatchExpected(ObjectNode errorON, int status, String errorMessage) {
    return errorON != null
        && errorON.get(GlobalErrorWebExceptionHandler.STATUS) != null
        && errorON.get(GlobalErrorWebExceptionHandler.STATUS).asInt() == status
        && errorON.get(GlobalErrorWebExceptionHandler.ERROR) != null
        && errorON.get(GlobalErrorWebExceptionHandler.ERROR).textValue().startsWith(errorMessage);
  }

  private Transaction testSaveTransactionOK(
      Transaction tIn, Transaction.StatusEnum expectedStatus) {
    LOGGER.info("Transaction IN : " + tIn);
    Transaction tOut =
        webClient
            .post()
            .uri("/rest/op/transaction/save")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(tIn))
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Transaction.class)
            .returnResult()
            .getResponseBody();
    LOGGER.info("Transaction OUT : " + tOut);
    assertTrue(
        tOut != null
            || tOut.getId() != null
            || expectedStatus.equals(tOut.getStatus())
            || tOut.getOrderLineList().size() == tIn.getOrderLineList().size());
    return tOut;
  }

  @Test
  public void testSaveTransactionCreditCardOKWithoutStatus() {
    testSaveTransactionOK(createTransactionCreditCard(true), Transaction.StatusEnum.NEW);
  }

  @Test
  public void testSaveTransactionPaypalOKWithoutStatus() {
    testSaveTransactionOK(createTransactionPaypal(true), Transaction.StatusEnum.NEW);
  }

  private Transaction testSaveTransactionOKWithNEWStatus(Transaction tIn) {
    tIn.setStatus(Transaction.StatusEnum.NEW);
    return testSaveTransactionOK(tIn, Transaction.StatusEnum.NEW);
  }

  @Test
  public void testSaveTransactionCreditCardOKWithNEWStatus() {
    testSaveTransactionOKWithNEWStatus(createTransactionCreditCard(true));
  }

  @Test
  public void testSaveTransactionPayPalOKWithNEWStatus() {
    testSaveTransactionOKWithNEWStatus(createTransactionPaypal(true));
  }

  private void testSaveTransactionOKIfError(Transaction tIn, int status, String errorMessage) {
    {
      LOGGER.info("Transaction IN : " + tIn);
      ObjectNode errorON =
          webClient
              .post()
              .uri("/rest/op/transaction/save")
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(tIn))
              .exchange()
              .expectStatus()
              .is4xxClientError()
              .expectBody(ObjectNode.class)
              .returnResult()
              .getResponseBody();
      LOGGER.info("Error OUT : " + errorON);
      assertTrue(isErrorMatchExpected(errorON, status, errorMessage));
    }
  }

  @Test
  public void testSaveTransactionCreditCardOKWithCAPTUREDStatus() {
    Transaction tIn = createTransactionCreditCard(false);
    tIn.setStatus(Transaction.StatusEnum.CAPTURED);
    testSaveTransactionOKIfError(tIn, 500, AppException.STATUS_MUST_BE_NEW_TO_ADD_TRANSACTION);
  }

  @Test
  public void testSaveTransactionPaypalOKWithCAPTUREDStatus() {
    Transaction tIn = createTransactionPaypal(false);
    tIn.setStatus(Transaction.StatusEnum.CAPTURED);
    testSaveTransactionOKIfError(tIn, 500, AppException.STATUS_MUST_BE_NEW_TO_ADD_TRANSACTION);
  }

  @Test
  public void testTransactionStatusChangeFromNewToAuthorizedOK() {
    Transaction t =
        testSaveTransactionOK(createTransactionPaypal(true), Transaction.StatusEnum.NEW);
    t.setStatus(Transaction.StatusEnum.AUTHORIZED);
    testSaveTransactionOK(t, Transaction.StatusEnum.AUTHORIZED);
  }

  @Test
  public void testTransactionStatusChangeFromNewToAuthorizedToCapturedOK() {
    Transaction t =
        testSaveTransactionOK(createTransactionPaypal(true), Transaction.StatusEnum.NEW);
    t.setStatus(Transaction.StatusEnum.AUTHORIZED);
    testSaveTransactionOK(t, Transaction.StatusEnum.AUTHORIZED);
    t.setStatus(Transaction.StatusEnum.CAPTURED);
    testSaveTransactionOK(t, Transaction.StatusEnum.CAPTURED);
  }

  @Test
  public void testTransactionStatusChangeFromNewToCapturedKO() {
    Transaction t =
        testSaveTransactionOK(createTransactionPaypal(true), Transaction.StatusEnum.NEW);
    t.setStatus(Transaction.StatusEnum.CAPTURED);
    testSaveTransactionOKIfError(
        t, 500, AppException.STATUS_CAPTURED_IS_AVAILABLE_ONLY_FROM_AUTHORIZED_FOR_TRANSACTION);
  }

  @Test
  public void testTransactionCapturedStatusNotUpdatableKO() {
    Transaction t =
        testSaveTransactionOK(createTransactionPaypal(true), Transaction.StatusEnum.NEW);
    t.setStatus(Transaction.StatusEnum.AUTHORIZED);
    testSaveTransactionOK(t, Transaction.StatusEnum.AUTHORIZED);
    t.setStatus(Transaction.StatusEnum.CAPTURED);
    t = testSaveTransactionOK(t, Transaction.StatusEnum.CAPTURED);
    t.setStatus(Transaction.StatusEnum.AUTHORIZED);
    testSaveTransactionOKIfError(
        t, 500, AppException.STATUS_CAPTURED_CAN_NOT_BE_UPDATED_FOR_TRANSACTION);
  }

  @Test
  public void testTransactionFindAllOK() {
    {
      LOGGER.info("Transaction findAll count expected : " + createTransactionCount.get());
      List<Transaction> tListOut =
          webClient
              .get()
              .uri("/rest/op/transaction/findAll")
              .exchange()
              .expectStatus()
              .isOk()
              .expectBodyList(Transaction.class)
              .returnResult()
              .getResponseBody();
      LOGGER.info("Transaction findAll count returned : " + tListOut.size());
      assertTrue(createTransactionCount.get() == tListOut.size());
    }
  }
}
