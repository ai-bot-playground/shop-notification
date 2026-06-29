Feature: Customer notifications
  shop-notification reacts to terminal order events and notifies the customer.
  Delivery is idempotent because Kafka is at-least-once.

  Scenario Outline: Notify on terminal order events
    When an <event> event arrives for order "O1"
    Then a "<type>" notification is sent for order "O1"

    Examples:
      | event          | type             |
      | OrderConfirmed | purchase success |
      | OrderCancelled | payment failed   |
      | OrderRejected  | out of stock     |

  Scenario: The same event never notifies twice (idempotent)
    When the OrderConfirmed event for order "O1" is delivered twice
    Then exactly one notification is sent for order "O1"

  Scenario: Notification is a leaf and publishes no business events
    When an OrderConfirmed event arrives for order "O1"
    Then no business event is published
