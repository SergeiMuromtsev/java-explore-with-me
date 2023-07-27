# java-explore-with-me
Учебный проект

Архитектура приложения.
Приложение включает в себя два микросервиса: основной сервис и сервис статистики. Сервис статистики содержит модули client, dto и server. Модуль client является шлюзом между основным модудем и модулем статистики. Основной сервис ewm-service содержит основную логику, а хранение данных происходит в базе данных.

Основной функционал приложения.
Основной сервис включает в себя функции, распределенные по категориям доступа.
Администраторы могут управлять категориями для событий, подборками мероприятий, осуществлять модерирование события, размещённых пользователями,
Пользователи могут осуществлять добавление, редактирование и просмотр мероприятий, оформление заявки на участие в мероприятиях, подтверждение заявки на свое мероприятие другим пользователям,
просмотр подробной информации о выбранном событии. Сервис статистики собирает информацию о количестве обращений пользователей к спискам событий.

Спецификация сервисов содержится в файлах ewm-main-service-spec.json и ewm-stats-service-spec.json

Запуск приложения:
В проекте используется 11 версия Java. Скачайте проект, откройте его в среде разработки (IDE) и запустите оба оба модуля. Для запуска в контейнерах Docker откройте терминал и впишите команду mvn clean package. После сборки проекта запуск можно осуществить через команду docker compose up. 

Стек технологий:
Java 11, Spring Boot, Maven, REST API, JDBC, H2, PostgreSQL, SQL, Hibernate - ORM, Docker
