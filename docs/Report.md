# Отчёт о проведённом тестировании
## Краткое описание
В ходе данного дипломного проекта было выполенено ручное исследовательское тестирование приложения, которое представляет собой веб-сервис для покупки тура по определённой цене двумя способами:оплата по дебетовой карте или выдача кредита по данным банковской карты. После чего был составлен [тест-план](https://github.com/julyy5/DiplomaQA/blob/main/docs/Plan.md).

По данному тест-плану написаны автотесты, включающие в себя позитивные и негативные сценарии:
- проверки пользовательского интерфейса,
- запросы в базу данных, проверяющие корректность внесения информации приложением. 

Также в систему автоматизации был интегрирован Gradle отчёт, наглядно демонстрирующий процент выполненных тестов. В завершении работы были оформлены [issue](https://github.com/julyy5/DiplomaQA/issues) по найденным багам.
## Количество тест-кейсов
Дипломный проект содержит 63 тест-кейса.
## Процент успешных и неуспешных тест-кейсов
Из 63 тест-кейсов: 52 успешных и 11 неуспешных. Процент успешного выполнения 78%.
![image](https://user-images.githubusercontent.com/106488742/221940547-23f0ac2f-07e1-4243-8760-581af1ea7387.png)
![image](https://user-images.githubusercontent.com/106488742/221943354-c6ba830b-9f6a-4e5c-93c3-594a97fc6893.png)
## Общие рекомендации
1. Написать документацию на приложение;
2. Сделать 2 отдельных сервиса, симулирующих работу PaymentGate и CreditGate;
3. Исправить выявленные в ходе тестирования [баги](https://github.com/julyy5/DiplomaQA/issues).
