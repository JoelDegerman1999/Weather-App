package se.joeldegerman.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class Weather {
	private final String APIkey = "f2e311b89b24ab2c5de19fc8b688a7a0";

	// Converts a given JSON String into to a map with
	// String as key and JsonElement as value.
	private Map<String, JsonElement> convertToMap(String jsonString) {
		return new Gson().fromJson(jsonString, new TypeToken<Map<String, JsonElement>>() {
		}.getType());
	}

	private List<Map<String, JsonElement>> convertToList(String jsonString) {
		return new Gson().fromJson(jsonString, new TypeToken<List<Map<String, JsonElement>>>() {
		}.getType());
	}

	// Returns a JAVA String object with JSON notations depending on cityID and
	// forecast or not
	private String getJsonString(int cityID, boolean forecast) throws MalformedURLException, IOException {
		String urlString = "";
		StringBuilder result = new StringBuilder();
		if (forecast) {
			urlString = "http://api.openweathermap.org/data/2.5/forecast?id=" + cityID + "&APPID=" + APIkey
					+ "&units=metric";
		} else {
			urlString = "http://api.openweathermap.org/data/2.5/weather?id=" + cityID + "&APPID=" + APIkey
					+ "&units=metric";
		}
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null)
			result.append(line);
		rd.close();
		return result.toString();
	}

	
	// Returns a map filled with current weather information (everything in a json
	// object called 'main')
	// depending on the cityID it will return the right map for the current location
	public Map<String, JsonElement> getCurrentWeather(int cityID) throws MalformedURLException, IOException {
		String jsonString = getJsonString(cityID, false);
		Map<String, JsonElement> resultMap = convertToMap(jsonString);
		return convertToMap(resultMap.get("main").toString());
	}

	public List<Map<String, JsonElement>> getForecastWeather(int cityID) throws MalformedURLException, IOException {
		String jsonString = getJsonString(cityID, true);
		
		//Makes a map of every object the forecast json contains
		Map<String, JsonElement> weatherMap = convertToMap(jsonString);
		
		//Called the map.get("list"); to get everything 
		List<Map<String, JsonElement>> forecastList = convertToList(weatherMap.get("list").toString());
		
		Map<String, JsonElement> forecastMap = forecastList.get(1);
		
		System.out.println(forecastMap);
		long dt = Long.parseLong(forecastMap.get("dt").toString());
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm  dd:MMM:yyyy");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GTM+2"));
		String dateString = simpleDateFormat.format(new Date(dt*1000));
		System.out.println(dateString);
		
		return forecastList;
	}
}
