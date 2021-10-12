package com.cgi.op.pyr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Transaction bean */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("transaction")
public class Transaction {

  public static final String SQL_ID = "transaction_id";

  @Id
  @Column(SQL_ID)
  private Long id;

  @Column("transaction_amount")
  private BigDecimal amount;

  @Column("transaction_payment_type")
  private PaymentTypeEnum paymentType;

  @Column("transaction_status")
  private StatusEnum status;

  @Transient private List<OrderLine> orderLineList = new ArrayList<>(0);

  /** PaymentType enum */
  public enum PaymentTypeEnum {
    CREDIT_CARD,
    GIFT_CARD,
    PAYPAL
  }

  /** Status enum */
  public enum StatusEnum {
    NEW,
    AUTHORIZED,
    CAPTURED
  }
}
