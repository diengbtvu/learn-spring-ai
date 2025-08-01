package learn.spring_ai.returnentity;

import java.util.List;

public class ActorFilmsEntity {
    private String name;
    private List<String> movie;

    public List<String> getMovie() {
        return movie;
    }

    public void setMovie(List<String> movie) {
        this.movie = movie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
