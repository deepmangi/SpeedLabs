@Task-2
Feature: E2E scenario for sauce labs checkout

  Scenario: navigate to inventory page, and purchase a product
    Given Navigate to Inventory page in sauce labs with viewport "Desktop"
    Then Add Sauce Labs Backpack to cart
    Then Navigate to card and complete the purchase
