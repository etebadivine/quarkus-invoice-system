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
        stringBuilder.append("body { font-family: Arial, sans-serif; padding: 20px; }")
        stringBuilder.append("h3 { color: #333; }")
        stringBuilder.append("ul { padding-left: 20px; }")
        stringBuilder.append("li { margin-bottom: 10px; }")
        stringBuilder.append("</style>")
        stringBuilder.append("</head>")
        stringBuilder.append("<body>")

        stringBuilder.append("<h3>Dear Customer ${subscription?.customerId?.name}, here is a breakdown of your monthly invoice :</h3>")
        stringBuilder.append("<h3>Invoice Items:</h3>")

        stringBuilder.append("<ul>")
        for (item in subscription.items) {
            stringBuilder.append("<li style=\"list-style-type:none;\">")
            stringBuilder.append("<b>Product:</b> ${item?.productId?.productName}<br/>")
            stringBuilder.append("<b>Quantity:</b> ${item.quantity}<br/>")
            stringBuilder.append("<b>Total Amount:</b> ${subscription.customerId?.currency?.currencyName} ${item.totalAmount}<br/>")
            stringBuilder.append("</li>")
        }
        stringBuilder.append("</ul>")

        stringBuilder.append("</body>")
        stringBuilder.append("</html>")

        return stringBuilder.toString()
    }

}