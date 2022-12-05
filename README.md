# Курсовая работа "Перевод денег с карты на карту"
## Общая схема работы программы
![схема работы программы](src/main/resources/Sxema%20SendMoney.jpg)
## Описание программы
### Входные данные
от клиента приходит два HTTP-запроса:
- /transfer с деталями транзакции
- /confirmOperation с кодом подтверждения транзакции
### Уровень контроллеров
`GetDataFromClientController`
Принимает запросы, парсит в соответствующие классы, вызывает методы у сервисов.
### Уровень сервисов
`checkTransferService`
- oбрабатывает информацию по ветке `/transfer`
- проверяет данные карт на соответствие в хранилище `CardsRepository`
- проверяет возможность проведения операции
- блокирует деньги со счета отправителя, если все проверки прошли успешно
- выкидывает 400 и 500 ошибку, если проверки не пройдены
- отправляет данные о транзакции в `TransactionRepository`
- возвращает класс `OperationId` в контроллер с номером операции

`checkConfirmOperation`
- oбрабатывает информацию по ветке `/confirmOperation`
- проверяет соответствие кода операции от клиента с данными из хранилища `TransactionRepository`
- проверяет код потверждения 
- выкидывает 400 и 500 ошибку, если проверки не пройдены
- и делает откат операции, возвращает деньги отправителю
- есди все данные корректны, проводит транзакцию, переводит деньги получателю
### Уровень репозитория
`CardsRepository`
- хранит `ConcurrentHashMap` с данными карт класса `Card`
- возвращает карты по Number или Number, CVV, ValidTill или null 
В хранилище создаются 2 карты для теста приложения:
- number:1111 1111 1111 1111, CVV: 111, ValidTill: 01/25, Balance: 100000
- number:2222 2222 2222 2222, CVV: 222, ValidTill: 02/25, Balance: 100000

`TransactionRepository`
- хранит `ConcurrentHashMap` с информацией о транзакции `TransactionInfo`
- возвращает транзакцию по ее `operationId`

### Логгер
- выводит всю информацию с датой в файл из контроллера и сервисов