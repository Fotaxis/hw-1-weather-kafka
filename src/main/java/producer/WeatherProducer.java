package producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.WeatherData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class WeatherProducer {
    private static final String TOPIC = "weather-topic";
    private static final List<String> CITIES = List.of("Москва", "Питер", "Екатеринбург", "Тюмень", "Абакан");
    private static final List<String> CONDITIONS = List.of("солнечно", "облачно", "дождь");
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Генерирует данные о погоде за 1 неделю и отправляет их на топик "weather-topic" Kafka
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        mapper.registerModule(new JavaTimeModule());
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            for (int day = 0; day < 7; day++) {
                LocalDate date = LocalDate.now().minusDays(6 - day);
                for (String city : CITIES) {
                    int temp = ThreadLocalRandom.current().nextInt(0, 36);
                    String condition = CONDITIONS.get(ThreadLocalRandom.current().nextInt(CONDITIONS.size()));
                    WeatherData data = new WeatherData(city, temp, condition, date);
                    String json = mapper.writeValueAsString(data);

                    producer.send(new ProducerRecord<>(TOPIC, city, json));
                    System.out.println("Sent: " + json);
                }
                Thread.sleep(2000);
            }
        }
    }
}

