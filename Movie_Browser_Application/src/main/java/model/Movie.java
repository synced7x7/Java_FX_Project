package model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String duration;
    private double imdbRating;
    private String plot;
    private String cast;
    private String posterUrl;
    private String detailImage1;
    private String detailImage2;

    public Movie(int id, String title, String genre, String cast, String duration, double imdbRating, String plot, String posterUrl, String detailImage1, String detailImage2) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.cast = cast;
        this.duration = duration;
        this.imdbRating = imdbRating;
        this.plot = plot;
        this.posterUrl = posterUrl;
        this.detailImage1 = detailImage1;
        this.detailImage2 = detailImage2;
    }

    public String getDetailImage1() { return detailImage1; }
    public String getDetailImage2() { return detailImage2; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }

    public String getCast() { return cast; }

    public String getPosterUrl() { return posterUrl; }

    public String getDuration() { return duration; }
    public double getImdbRating() { return imdbRating; }
    public String getPlot() { return plot; }
}
