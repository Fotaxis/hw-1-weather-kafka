package model;

import java.time.LocalDate;

public class WeatherData {
    public String city;
    public int temperature;
    public String condition;
    public LocalDate date;

    public WeatherData() {
    }

    public WeatherData(String city, int temperature, String condition, LocalDate date) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.date = date;
    }
}
