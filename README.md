Этот проект является веб-приложением, разработанным с использованием Spring Framework. Приложение реализует функционал для управления пользователями и их желаниями. Основные возможности включают:

Управление пользователями: создание, обновление, удаление и получение пользователей.
Управление желаниями: пользователи могут создавать, обновлять, удалять и просматривать свои желания. Для каждого желания можно указать категорию, приоритет, ожидаемую дату завершения и статус (в процессе или завершено).
Аутентификация и авторизация: доступ к управлению желаниями возможен только после аутентификации.
Логирование: логирование действий приложения для отслеживания состояния работы.

Инструкция по сборке и запуску приложения:
git clone git remote add origin https://github.com/aksjondla/Spring.git



cd SpringExample

docker-compose up -d

mvn clean install

mvn spring-boot:run


Основные технологии
Spring HATEOAS
Spring Boot Starter Security
Hibernate Validator
Spring Boot Starter Validation
Liquibase
PostgreSQL
Spring Boot Starter Data JPA
Spring Boot Starter Web
Lombok
Spring Boot
