#  Feb 11th HW 17

## 1. Unit Testing

### Definition
Unit Testing verifies a single unit of code (usually a method or class) in isolation.

### Purpose
To ensure that business logic works correctly.

### Example
Testing a discount calculation method:

```java
public double calculateDiscount(double price, double rate) {
    return price * rate;
}
```

## 2. Functional Testing

### Definition
Functional Testing verifies whether a feature works according to requirements.

### Purpose
To ensure system behavior matches business requirements.

### Example
- Add item to cart
- Apply coupon
- Verify total price is correct

## 3. Integration Testing

### Definition
Integration Testing verifies interaction between multiple components.

### Purpose
To ensure modules work together correctly.

### Example
- Cart Service calls Pricing Service
- Pricing Service retrieves data from Database
- Order is saved correctly

## 4. Regression Testing

### Definition
Regression Testing re-runs existing tests after code changes.

### Purpose
To ensure new changes do not break existing features.

### Example
- Re-test credit card payment
- Re-test coupon logic
- Re-test order confirmation

## 5. Smoke Testing

### Definition
Smoke Testing is a basic system health check.

### Purpose
To verify the system is stable enough for deeper testing.

### Example
After deployment:
- Can user log in?
- Can homepage load?
- Can order be submitted?

## 6. Performance Testing

### Definition
Performance Testing evaluates system speed and responsiveness under expected load.

### Purpose
To measure response time and system behavior under normal usage.

### Example
- 1,000 users checkout simultaneously
- Response time must be under 2 seconds

## 7. Stress Testing

### Definition
Stress Testing pushes the system beyond normal capacity.

### Purpose
To determine system breaking point and recovery ability.

### Example
- 50,000 users checkout simultaneously
- Does the system crash?
- Does it recover automatically?

## 8. A/B Testing

### Definition
A/B Testing compares two versions of a feature using real users.

### Purpose
To determine which version performs better.

### Example
- Version A: Blue "Pay Now" button
- Version B: Green "Pay Now" button

## 9. End-to-End Testing

### Definition
End-to-End Testing verifies the complete workflow from start to finish.

### Purpose
To simulate real user behavior.

### Example
- User logs in
- Searches product
- Adds to cart
- Makes payment
- Receives confirmation email

## 10. User Acceptance Testing

### Definition
User Acceptance Testing is performed by business users or clients.

### Purpose
To confirm the system meets business requirements.

### Example
- Tax rules are correct
- Invoice format is correct
- Business policies are implemented correctly

## 11. Development Environment

### Definition
Environment used by developers to write and test code.

### Example
- Runs on local machines
- Uses mock/test data
- Debugging enabled
- Unstable and frequently changed

## 12. QA Environment

### Definition
Environment used for formal testing.

### Example
- Separate test database
- Stable compared to development
- Used for functional and regression testing

## 13. Pre-Production / Staging Environment

### Definition
Environment that closely mirrors production.

### Example
- Same configuration as production
- Final verification before release
- Used for performance and smoke testing

## 14. Production Environment

### Definition
Live environment used by real customers.

### Example
- Real users
- Real data
- Highest stability requirement
- Monitoring and logging enabled
