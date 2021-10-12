package com.cgi.op.pyr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/** OrderLine bean */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(OrderLine.ORDER_LINE_TABLE_NAME)
public class OrderLine {

  public static final String ORDER_LINE_TABLE_NAME = "order_line";
  public static final String ORDER_LINE_TRANSACTION_ID = "order_line_transaction_id";

  @Id
  @Column("order_line_id")
  private Long id;

  @Column(ORDER_LINE_TRANSACTION_ID)
  private Long transactionId;

  @Column("order_line_product_name")
  private String productName;

  @Column("order_line_qty")
  private int qty;

  @Column("order_line_price")
  private BigDecimal price;
}
