package concurrency.part2.concurrent.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieReader {

	public Set<Movie> readMovies() {
		try (Stream<String> lines = Files.lines(Paths.get("mawyekran.txt"), StandardCharsets.ISO_8859_1)) {

			Set<Movie> movies = lines.map((String line) -> {
				String[] elements = line.split("/");
				String title = extractTitle(elements[0]);
				String releaseYear = extractReleaseYear(elements[0]);

				Movie movie = new Movie(title, Integer.valueOf(releaseYear));

				Arrays.stream(elements).skip(1).map(MovieReader::extractActor).forEach(movie::addActor);

				return movie;
			}).collect(Collectors.toSet());

			return movies;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void addActorsToMap(Map<Actor, Set<Movie>> map) {

		Set<Movie> movies = readMovies();
		for (Movie movie : movies) {
			for (Actor actor : movie.actors()) {
				map.computeIfAbsent(actor, a -> new HashSet<>()).add(movie);
			}
		}
	}

	private static Actor extractActor(String elements) {
		String[] nameElements = elements.split(", ");
		String lastName = extractLastName(nameElements);
		String firstName = extractFirstName(nameElements);

		return new Actor(lastName, firstName);
	}

	private static String extractFirstName(String[] nameElements) {
		String firstName = "";
		if (nameElements.length > 1) {
			firstName = nameElements[1].trim();
		}
		return firstName;
	}

	private static String extractLastName(String[] name) {
		return name[0].trim();
	}

	private static String extractReleaseYear(String element) {
		String releaseYear = element.substring(element.lastIndexOf("(") + 1, element.lastIndexOf(")"));
		if (releaseYear.contains(","))
			releaseYear = releaseYear.substring(0, releaseYear.indexOf(","));
		return releaseYear;
	}

	private static String extractTitle(String element) {
		return element.substring(0, element.lastIndexOf("(")).trim();
	}

}

class Movie {
	private String title;
	private int releaseYear;

	private Set<Actor> actors = new HashSet<>();

	public Movie(String title, int releaseYear) {
		this.title = title;
		this.releaseYear = releaseYear;
	}

	public String title() {
		return this.title;
	}

	public int releaseYear() {
		return this.releaseYear;
	}

	public void addActor(Actor actor) {
		this.actors.add(actor);
	}

	public Set<Actor> actors() {
		return this.actors;
	}

	@Override
	public String toString() {
		return "Movie{" + "title=" + title + ", releaseYear=" + releaseYear + ", actors=" + actors + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.title);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Movie other = (Movie) obj;
		return Objects.equals(this.title, other.title);
	}
}

class Actor {
	private String lastName, firstName;

	public Actor(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public String lastName() {
		return this.lastName;
	}

	public String firstName() {
		return this.firstName;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.lastName);
		hash = 67 * hash + Objects.hashCode(this.firstName);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Actor other = (Actor) obj;
		if (!Objects.equals(this.lastName, other.lastName)) {
			return false;
		}
		return Objects.equals(this.firstName, other.firstName);
	}

	@Override
	public String toString() {
		return "Actor{" + "lastName=" + lastName + ", firstName=" + firstName + '}';
	}
}