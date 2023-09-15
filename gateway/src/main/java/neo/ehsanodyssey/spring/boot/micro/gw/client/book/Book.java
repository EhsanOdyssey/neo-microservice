package neo.ehsanodyssey.spring.boot.micro.gw.client.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import neo.ehsanodyssey.spring.boot.micro.gw.client.rating.Rating;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private Long id;
    private String author;
    private String title;
    private List<Rating> ratings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
