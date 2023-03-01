# Дипломный проект по профессии «Тестировщик»
**Инструкции по запуску автотестов**

*Для работы с БД MySQL нужно выполнить следующие шаги:*
1. Скачать архив с помощью команды `git clone https://github.com/julyy5/DiplomaQA.git`.
2. Запустить Docker.
3. Открыть скачанный проект в Intellij IDEA.
4. В терминале Intellij IDEA запустить контейнеры с помощью команды `docker-compose -f docker-compose-mysql.yml up`.
5. Открыв новую вкладку терминала Intellij IDEA запустить SUT, при помощи команды `java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:mysql://localhost:3306/mysql_db`.
6. Запустить автотесты в headless-режиме, используя команду `./gradlew clean test -D selenide.headless=true`.
7. Отчёт Gradle можно найти в проекте по пути build\reports\tests\test\index.html.

*Для работы с БД PostgreSQL нужно выполнить следующие шаги:*
1. Скачайть архив с помощью команды `git clone https://github.com/julyy5/DiplomaQA.git`.
2. Запустить Docker.
3. Открыть скачанный проект в Intellij IDEA.
4. В файле [*application.properties*](https://github.com/julyy5/DiplomaQA/blob/main/application.properties) раскомментировать строку с подключением к БД PostgreSQL, а строку подключения к БД MySQL наоборот закомментировать.
5. В файле [*SQLHelper.java*](https://github.com/julyy5/DiplomaQA/blob/main/src/test/java/data/SQLHelper.java) аналогично раскомментировать строку с подключением к БД PostgreSQL, а строку подключения к БД MySQL наоборот закомментировать.
6. В терминале Intellij IDEA запустить контейнеры с помощью команды `docker-compose -f docker-compose-postgresql.yml up`.
8. Открыв новую вкладку терминала Intellij IDEA запустить SUT, при помощи команды `java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:postgresql://localhost:5432/postgresql_db`.
9. Запустить автотесты в headless-режиме (без открытия браузера), используя команду `./gradlew clean test -D selenide.headless=true`.
10. Отчёт Gradle можно найти в проекте по пути build\reports\tests\test\index.html.
