package concurrency.part4.reactive.api.java9.flowapi;

import java.time.LocalDate;

public class News {

	private String headline;
	private LocalDate date;

	public News(String headline2, LocalDate now) {
		this.headline = headline2;
		this.date = now;
	}

	public static News create(String headline) {
		return new News(headline, LocalDate.now());
	}

	public String getHeadline() {
		return headline;
	}

	public LocalDate getDate() {
		return date;
	}

	// getter, setter, constructor omitted
}