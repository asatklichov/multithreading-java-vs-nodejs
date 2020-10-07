package concurrency.completablefuture.springboot.asyncmethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Spring uses the Jackson JSON library to convert GitHub’s JSON response into a
 * User object. The @JsonIgnoreProperties annotation tells Spring to ignore any
 * attributes not listed in the class. This makes it easy to make REST calls and
 * produce domain objects.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User {

	private String name;
	private String url;

	@Override
	public String toString() {
		return "User [name=" + name + ", url=" + url + "]";
	}

}
