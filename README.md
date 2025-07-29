# Домашнее задание 1

## Описание  
Проект демонстрирует работу с Apache Kafka на примере:
- `WeatherProducer` — генерирует и отправляет случайные данные о погоде в Kafka-топик
- `WeatherConsumer` — читает сообщения из Kafka и выводит простую аналитику

## Как запустить

1. Поднять Kafka и ZooKeeper с помощью [docker-compose](docker-compose.yml):
2. Запустить методы main у классов [WeatherProducer](src/main/java/producer/WeatherProducer.java), [WeatherConsumer](src/main/java/consumer/WeatherConsumer.java)

## Проверка работы WeatherProducer:<br>
<img width="1434" height="835" alt="image" src="https://github.com/user-attachments/assets/76e1488c-532a-4291-9dbf-713e433fa393" /><br>
## Проверка работы WeatherConsumer:<br>
<img width="1587" height="585" alt="image" src="https://github.com/user-attachments/assets/41e7ca5e-970c-4328-8107-645345cc5a24" /><br>
## Вывод мини-аналитики у WeatherConsumer:<br>
<img width="477" height="224" alt="image" src="https://github.com/user-attachments/assets/8f7decd8-cd5d-46a3-a697-24bdccd9ce7e" /><br>

