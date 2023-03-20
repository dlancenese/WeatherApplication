import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WeatherApp extends JFrame {
    private final String API_KEY = "6ecd0140143f108085c8700fee4a8020"; //6029e6532748f523992ad8935dc8b7f6

    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=6ecd0140143f108085c8700fee4a8020";

    private JLabel cityLabel;
    private JLabel tempLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;

    public WeatherApp() {
        setTitle("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        setLayout(new GridLayout(4, 2));

        cityLabel = new JLabel("City:");
        tempLabel = new JLabel("Temperature:");
        humidityLabel = new JLabel("Humidity:");
        windLabel = new JLabel("Wind Speed:");

        add(new JLabel(""));
        add(new JLabel(""));

        add(new JLabel("City:"));
        add(cityLabel);

        add(new JLabel("Temperature:"));
        add(tempLabel);

        add(new JLabel("Humidity:"));
        add(humidityLabel);

        add(new JLabel("Wind Speed:"));
        add(windLabel);

        setLocationRelativeTo(null);
        setVisible(true);

        String city = JOptionPane.showInputDialog("Enter city name:");
        String apiUrl = String.format(API_URL, city.replaceAll("\\s", "+"), API_KEY);

        try {
            URL url = new URL(apiUrl);
            InputStream stream = url.openStream();
            Scanner scanner = new Scanner(stream);

            StringBuilder builder = new StringBuilder();
            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }

            String jsonString = builder.toString();
            System.out.println(jsonString);

            // Parse JSON data
            WeatherData weatherData = parseJson(jsonString);

            // Display weather data
            cityLabel.setText(weatherData.getName());
            tempLabel.setText(weatherData.getMain().getTemp() + "°C");
            humidityLabel.setText(weatherData.getMain().getHumidity() + "%");
            windLabel.setText(weatherData.getWind().getSpeed() + " m/s");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving weather data.");
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Error parsing JSON data: " + e.getMessage());
        }
    }


    private WeatherData parseJson(String jsonString) {
        Gson gson = new Gson();
        WeatherData weatherData = gson.fromJson(jsonString, WeatherData.class);
        return weatherData;
    }


    private class WeatherData {
        private String name;
        private MainData main;
        private WindData wind;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MainData getMain() {
            return main;
        }

        public void setMain(MainData main) {
            this.main = main;
        }

        public WindData getWind() {
            return wind;
        }

        public void setWind(WindData wind) {
            this.wind = wind;
        }

        private class MainData {
            private double temp;
            private int humidity;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }
        }

        private class WindData {
            private double speed;

            public double getSpeed() {
                return speed;
            }

            public void setSpeed(double speed) {
                this.speed = speed;
            }
        }
    }


    public static void main(String[] args) {
        new WeatherApp();
    }
}

