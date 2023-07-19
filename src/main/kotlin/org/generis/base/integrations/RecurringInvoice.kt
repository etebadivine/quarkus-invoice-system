package org.generis.base.integrations

import jakarta.inject.Singleton
import org.generis.business.subscription.repo.Subscription
import java.time.format.DateTimeFormatter


@Singleton
class RecurringInvoice {
    fun generateInvoiceHtml(subscription: Subscription): String {

        val nextInvoiceDate = subscription.nextInvoiceDate

        val dueDate = nextInvoiceDate?.plusDays(5)

        val formattedDueDate = dueDate?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

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
            <p>Dear customer ${subscription.customerId?.name ?: subscription.company?.name ?: "valued customer"}, here is a breakdown of your subscription</p>
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
                <td>${subscription.customerId?.country?.currencyCode ?: subscription.company?.country?.currencyCode }${subscription.totalAmount}</td>
              </tr>
            </tfoot>
          </table>
          <div class="invoice-total">
            <p>Payment Due: ${formattedDueDate}</p>
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
              <td>${subscription.customerId?.country?.currencyCode ?: subscription.company?.country?.currencyCode }${item.productId?.unitPrice}</td>
              <td>${subscription.customerId?.country?.currencyCode ?: subscription.company?.country?.currencyCode}${item.totalAmount}</td>
            </tr>
        """
        }
        return tableRows
    }

    fun generateWelcomeMessage(subscription: Subscription): String{

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Welcome Card</title>
        <style>
                body {
                    font-family: "Lucida Sans", sans-serif;
                    margin: 0;
                    padding: 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-color: #f0f0f0;
                }

                    .card {
                        max-width: 600px;
                        width: 100%;
                        background-color: #fff;
                        border-radius: 10px;
                        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                        padding: 20px;
                        margin: 20px;
                    }

                    .header {
                        text-align: center;
                        margin-bottom: 20px;
                    }

                    .header h2 {
                    font-family: "Lucida Sans";
                    color:  #ff5722;
                    margin: 0;
                    }

            .content {
            font-family: "Lucida Sans";
                color: #444;
            }

            .signature {
                font-family: "Lucida Sans";
                text-align: right;
                margin-top: 40px;
                color: #7c7c7c;
            }
        </style>
        </head>
        <body>
        <div class="card">
        <div class="header">
        <h2>Welcome to Nsano</h2>
        </div>
        <div class="content">
        <p>Dear ${subscription.customerId?.name ?: subscription.company?.name ?: "valued customer"},</p>
        <p>
                We hope this message finds you well. We are thrilled to welcome you as
        our valued customer here at Nsano. Thank you for subscribing to our
        Service. We are excited to have you on board!
        </p>
        <p>
                As a subscriber, you now have access to a wide range of features that
        will enhance your business. Our team has worked tirelessly to create a
        seamless and enjoyable experience for all our customers, and we are
        confident that you will find great value in using our Products.
        </p>
        <p>Here are a few key details to get you started:</p>
        <ul>
        <li>Subscription ID: ${subscription.subscriptionNumber}</li>
        <li>Subscription Start Date: ${subscription.startDate}</li>
        <li>Subscription End Date: ${subscription.endDate}</li>
        <li>Subscription Plan: ${subscription.recurringPeriod} days</li>
        </ul>
        <p>
                If you have any questions or need assistance at any point during your
        subscription, please don't hesitate to reach out to our dedicated
        customer support team. We are here to help you make the most of your
        subscription and provide prompt solutions to any queries you may have.
        </p>
        <p>
                Stay tuned for regular updates, new features, and exciting offers that
        we have in store for our valued subscribers. We are committed to
                continuously enhancing our Service to meet and exceed your
        expectations.
        </p>
        <p>
                Thank you once again for choosing Nsano. We appreciate your trust in
                us, and we look forward to serving you with excellence.
        </p>
        </div>
        <div class="signature">
        <p>Best regards,</p>
        <p>Customer Support Team</p>
        <p>Nsano</p>
        <p>Email: nsano@gmail.com</p>
        <p>Phone: +233 54 3308 462</p>
        <p>Website: www.nsano.com</p>
        </div>
        </div>
        </body>
        </html>
        """.trimIndent()
    }
}