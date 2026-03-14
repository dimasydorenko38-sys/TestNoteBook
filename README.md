# Notes Management System (QoQ Test Project)

Сервіс для керування нотатками з підтримкою транзакційності та автоматичним підрахунком статистики.

## 🛠 Технологічний стек
* **Java 21**
* **Spring Boot 3.x** (Data MongoDB, Web, Validation)
* **MongoDB** (з підтримкою Replica Set для транзакцій ACID)
* **Docker & Docker Compose**
* **Lombok**

## 🚀 Швидкий запуск

### 1. Налаштування бази даних
Проект потребує MongoDB у режимі Replica Set для коректної роботи анотації `@Transactional`.

```bash
# Запуск контейнера
docker-compose up -d

# Ініціалізація репліки та створення користувача (виконати один раз)
docker exec -it mongodb_container mongosh --eval "rs.initiate({_id:'rs0', members:[{_id:0, host:'localhost:27017'}]})"
docker exec -it mongodb_container mongosh --eval "db.getSiblingDB('admin').createUser({user:'admin', pwd:'password', roles:[{role:'root', db:'admin'}]})"