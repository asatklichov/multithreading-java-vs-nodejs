package org.paumard.collections.model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieReader {

    public Set<Movie> readMovies() {
        try (Stream<String> lines = Files.lines(Paths.get("files/movies-mpaa.txt"), StandardCharsets.ISO_8859_1)) {

            Set<Movie> movies = lines.map(
                    (String line) -> {
                        String[] elements = line.split("/");
                        String title = extractTitle(elements[0]);
                        String releaseYear = extractReleaseYear(elements[0]);

                        Movie movie = new Movie(title, Integer.valueOf(releaseYear));

                        Arrays.stream(elements)
                                .skip(1)
                                .map(MovieReader::extractActor)
                                .forEach(movie::addActor);

                        return movie;
                    }
            ).collect(Collectors.toSet());

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