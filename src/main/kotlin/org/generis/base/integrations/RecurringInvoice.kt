package org.generis.base.integrations

import jakarta.inject.Singleton
import org.generis.business.subscription.repo.Subscription


@Singleton
class RecurringInvoice {
    fun generateInvoiceHtml(subscription: Subscription): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("<html>")
        stringBuilder.append("<head>")
        stringBuilder.append("<title>Invoice</title>")
        stringBuilder.append("<style>")
        stringBuilder.append("body { font-family: 'Geneva', Tahoma, Geneva, Verdana, sans-serif; padding: 20px; background-color: #e6f7ff; }")
        stringBuilder.append(".invoice-card { max-width: 450px; margin: 0 auto; background-color: #E3F6F5; border-radius: 5px; border-color: #10375C; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }")
        stringBuilder.append(".invoice-header { background-color: #10375C; color: #ffffff; padding: 20px; border-top-left-radius: 5px; border-top-right-radius: 5px; }")
        stringBuilder.append(".invoice-title { margin: 0; font-size: 18px; font-weight: bold; }")
        stringBuilder.append(".invoice-body { padding: 20px; }")
        stringBuilder.append(".invoice-items { list-style-type: none; padding-left: 1px; }")
        stringBuilder.append(".invoice-item { margin-bottom: 20px; }")
        stringBuilder.append(".invoice-item-product { font-weight: bold; }")
        stringBuilder.append(".invoice-item-details { margin-top: 5px; color: #666; }")
        stringBuilder.append(".invoice-item-amount { font-weight: bold; }")
        stringBuilder.append(".invoice-total-amount { font-weight: bold; padding-left: 1px;  margin-top: 4px; font-family: 'Tahoma' }")
        stringBuilder.append("</style>")
        stringBuilder.append("</head>")
        stringBuilder.append("<body>")

        stringBuilder.append("<div class=\"invoice-card\">")
        stringBuilder.append("<div class=\"invoice-header\">")
        stringBuilder.append("<h3 class=\"invoice-title\">Dear Customer ${subscription?.customerId?.name},</h3>")
        stringBuilder.append("<h3 class=\"invoice-title\">Here is a breakdown of your subscription:</h3>")
        stringBuilder.append("</div>")

        stringBuilder.append("<div class=\"invoice-body\">")

        stringBuilder.append("<h3 class=\"invoice-title\">Items:</h3>")
        stringBuilder.append("<ul class=\"invoice-items\">")
        for (item in subscription.items) {
            stringBuilder.append("<li class=\"invoice-item\">")
            stringBuilder.append("<div class=\"invoice-item-product\">Product :  ${item?.productId?.productName}</div>")
            stringBuilder.append("<div class=\"invoice-item-details\">")
            stringBuilder.append("<div>Quantity: ${item.quantity}</div>")
            stringBuilder.append("<div class=\"invoice-item-amount\">Price: ${subscription.customerId?.currency?.currencyCode}${item.productId?.unitPrice}</div>")
            stringBuilder.append("<div class=\"invoice-item-amount\">SubTotal: ${subscription.customerId?.currency?.currencyCode}${item.totalAmount}</div>")
            stringBuilder.append("</div>")
            stringBuilder.append("</li>")
        }
        stringBuilder.append("</ul>")
        stringBuilder.append("<h3 class=\"invoice-total-amount\">Total Amount: ${subscription.customerId?.currency?.currencyCode}  ${subscription.totalAmount}</h3>")
        stringBuilder.append("</div>")

        stringBuilder.append("</div>")

        stringBuilder.append("</body>")
        stringBuilder.append("</html>")

        return stringBuilder.toString()
    }
}