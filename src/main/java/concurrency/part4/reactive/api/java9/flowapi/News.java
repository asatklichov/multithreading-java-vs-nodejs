package concurrency.part4.reactive.api.java9.flowapi;

import java.time.LocalDate;

public class News {

	String headline;

	LocalDate date;

	public News(String headline, LocalDate date) {
		this.headline = headline;
		this.date = date;
	}

	public String getHeadline() {
		return headline;
	}

	public LocalDate getDate() {
		return date;
	}

	public static News create(String string) {
		return new News(string, LocalDate.now());
	}

}
