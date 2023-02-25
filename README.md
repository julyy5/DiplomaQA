# Дипломное задание
**Инструкции по запуску автотестов**

*Для работы с БД MySQL:*
1. Скачайте архив с помощью команды `git clone`.
2. Запустите Docker.
3. Откройте скачанный проект в Intellij IDEA.
4. В терминале Intellij IDEA запустите контейнеры с помощью команды `docker-compose up`.
5. Открыв новую вкладку терминала Intellij IDEA запустите SUT, при помощи команды `java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:mysql://localhost:3306/mysql_db`.
6. Запустите автотесты в headless-режиме, используя команду `gradlew test -Dselenide.headless=true`.

*Для работы с БД PostgreSQL:*
1. Скачайте архив с помощью команды `git clone`.
2. Запустите Docker.
3. Откройте скачанный проект в Intellij IDEA.
4. В файле [*application.properties*](https://github.com/julyy5/DiplomaQA/blob/main/application.properties) раскомментируйте строку с подключением к БД PostgreSQL, а строку подключения к БД MySQL наоборот закомментируйте.
5. В файле [*SQLHelper.java*](https://github.com/julyy5/DiplomaQA/blob/main/src/test/java/data/SQLHelper.java) аналогично раскомментируйте строку с подключением к БД PostgreSQL, а строку подключения к БД MySQL наоборот закомментируйте.
6. В терминале Intellij IDEA запустите контейнеры с помощью команды `docker-compose up`.
8. Открыв новую вкладку терминала Intellij IDEA запустите SUT, при помощи команды `java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:postgresql://localhost:5432/postgresql_db`.
9. Запустите автотесты в headless-режиме, используя команду `gradlew test -Dselenide.headless=true`.
