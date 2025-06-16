# Design Documentation

## Design Patterns Used

- **Repository Pattern:** Used for data access abstraction.
- **Service Layer Pattern:** Business logic is encapsulated in the service layer.
- **DTO Pattern:** To decouple API layer from entity model.
- **Specification Pattern:** For dynamic querying/search.
- **Optimistic Locking:** Using JPA @Version for concurrent update control.
- **Spring Batch:** For batch data import.

## Class Diagram

```mermaid
classDiagram
    class TransactionRecordController
    class TransactionRecordService
    class TransactionRecordRepository
    class TransactionRecord
    class TransactionRecordDto

    TransactionRecordController --> TransactionRecordService
    TransactionRecordService --> TransactionRecordRepository
    TransactionRecordRepository --> TransactionRecord
    TransactionRecordService --> TransactionRecordDto
```

## Activity Diagram for Update API

```mermaid
flowchart TD
    A[API Request: Update Description] --> B[Find Transaction by ID]
    B --> C{Version Match?}
    C -- Yes --> D[Update Description and Save]
    D --> E[Return Updated Record]
    C -- No --> F[Throw OptimisticLockException]
```