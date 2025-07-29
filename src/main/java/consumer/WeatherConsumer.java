package consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.WeatherData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WeatherConsumer {
    private static final String TOPIC = "weather-topic";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Map<String, Integer> rainDays = new ConcurrentHashMap<>();
    private static final Map<String, Integer> sunnyEKB = new ConcurrentHashMap<>();
    private static final Map<String, List<Integer>> cityTemps = new ConcurrentHashMap<>();

    /**
     * Получает из топика "weather-topic" данные о погоде, выводит мини-аналитику
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "weather-analytics");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    WeatherData data = mapper.readValue(record.value(), WeatherData.class);

                    if (data.condition.equals("дождь")) {
                        rainDays.merge(data.city, 1, Integer::sum);
                    }

                    if (data.city.equals("Екатернбург") && data.condition.equals("солнечно")) {
                        sunnyEKB.merge(data.date.toString(), 1, Integer::sum);
                    }

                    cityTemps.computeIfAbsent(data.city, k -> new ArrayList<>()).add(data.temperature);

                    System.out.printf("Погода в городе: %s: дата - %s, температура - %d°C,  %s%n", data.city, data.date, data.temperature, data.condition);
                }

                printAnalytics();
            }
        }
    }

    /**
     * Составляет и выводит мини-аналитику по погоде в разных городах
     */
    private static void printAnalytics() {
        System.out.println("=== Аналитика погоды ===");

        rainDays.forEach((city, count) -> {
            if (city.equals("Питер")) {
                System.out.println("Дней с дождём в Питере: " + count);
            }
        });

        System.out.println("Солнечных дней в Екатеринбуге: " + sunnyEKB.size());

        cityTemps.forEach((city, temps) -> {
            double avg = temps.stream().mapToInt(i -> i).average().orElse(0);
            System.out.printf("Средняя температура в %s: %.2f°C%n", city, avg);
        });
        System.out.println("=======================");
    }
}

