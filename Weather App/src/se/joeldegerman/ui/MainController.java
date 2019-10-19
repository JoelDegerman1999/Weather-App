package se.joeldegerman.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.JsonElement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import se.joeldegerman.models.Weather;

public class MainController implements Initializable {
	@FXML
	private ChoiceBox<String> cities;
	@FXML
	private Label tempLabel;

	private Weather weather;

	public int getCity() {
		switch (cities.getValue()) {
		case "Stockholm":
			return 2673730;

		case "Piteå":
			return 603570;

		default:
			return 0;
		}
	}

	public void updateTemp() throws IOException {
		Map<String, JsonElement> mainMap = weather.getCurrentWeather(getCity());
		tempLabel.setText(mainMap.get("temp").toString());
		weather.getForecastWeather(getCity());
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cities.getItems().addAll("Stockholm", "Piteå");
		cities.setValue("Stockholm");
		weather = new Weather();
	}

}
