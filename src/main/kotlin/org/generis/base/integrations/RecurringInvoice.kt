package org.generis.base.integrations

import jakarta.inject.Singleton
import org.generis.business.subscription.repo.Subscription


@Singleton
class RecurringInvoice {
    fun generateInvoiceHtml(subscription: Subscription): String {
        return """
    <!DOCTYPE html>
    <html lang="en">
      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Invoice</title>
        <style>
          body {
            font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande", "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
          }

          .invoice {
            width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #cccccc;
          }

          .invoice-header {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            background-color: #2b3a67;
            font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande", "Lucida Sans Unicode", Geneva, Verdana, sans-serif
          }

          .invoice-header h1 {
            font-size: 24px;
            margin: 0;
            color: #fff;
          }

          .invoice-details {
            margin-bottom: 20px;
          }

          .invoice-details p {
            margin: 0;
             font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
            "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
          }

          .invoice-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
            "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
          }

          .invoice-table th,
          .invoice-table td {
            padding: 8px;
            border: 1px solid #ccc;
          }

          .invoice-table th {
            background-color: #2b3a67;
            color: #fff;
          }

          .invoice-total {
            text-align: right;
          }
          
          .invoice-total p {
            margin: 0;
          }
        </style>
      </head>
      <body>
        <div class="invoice">
          <div class="invoice-header">
            <div>
              <h4 style="color: #fff; padding-left: 20px;">nsano</h4>
            </div>
            <div>
              <h1 style=" padding-left: 365px; padding-top: 9px">INVOICE</h1>
            </div>
          </div>
          <div class="invoice-details">
            <p>Invoice Number: ${subscription.subscriptionNumber}</p>
            <p>Dear customer ${subscription.customerId?.name}, here is a breakdown of your subscription</p>
          </div>
          <table class="invoice-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Sub Total</th>
              </tr>
            </thead>
            <tbody>
              ${generateTableRows(subscription)}
            </tbody>
            <tfoot>
              <tr>
                <td colspan="3">Total Amount</td>
                <td>${subscription.customerId?.currency?.currencyCode}${subscription.totalAmount}</td>
              </tr>
            </tfoot>
          </table>
          <div class="invoice-total">
            <p>Payment Due: ${subscription.nextInvoiceDate}</p>
          </div>
        </div>
      </body>
    </html>
    """.trimIndent()
    }

    private fun generateTableRows(subscription: Subscription): String {
        var tableRows = ""
        for (item in subscription.items) {
            tableRows += """
            <tr>
              <td>${item.productId?.productName}</td>
              <td>${item.quantity}</td>
              <td>${subscription.customerId?.currency?.currencyCode}${item.productId?.unitPrice}</td>
              <td>${subscription.customerId?.currency?.currencyCode}${item.totalAmount}</td>
            </tr>
        """
        }
        return tableRows
    }
}