package org.generis.config

import jakarta.inject.Singleton
import org.generis.entity.Subscription


@Singleton
class RecurringMail {

    fun generateInvoiceHtml(subscription: Subscription): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("<html>")
        stringBuilder.append("<head>")
        stringBuilder.append("<title>Invoice</title>")
        stringBuilder.append("</head>")
        stringBuilder.append("<body>")

        stringBuilder.append("<h3>Dear Customer ${subscription?.customerId?.name}, here is a breakdown of your monthly invoice</h3>")
        stringBuilder.append("<h3>Invoice Items:</h3>")

        stringBuilder.append("<ul>")
        for (item in subscription.items ) {
            stringBuilder.append("<li>")
            stringBuilder.append("<b>Product:</b> ${item?.productId?.productName}<br/>")
            stringBuilder.append("<b>Quantity:</b> ${item.quantity}<br/>")
            stringBuilder.append("<b>Total Amount:</b> ${item.totalAmount}<br/>")
            stringBuilder.append("</li>")
        }
        stringBuilder.append("</ul>")

        stringBuilder.append("</body>")
        stringBuilder.append("</html>")

        return stringBuilder.toString()
    }

}