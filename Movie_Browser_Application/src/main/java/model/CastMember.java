package model;

public class CastMember {
    private int id;
    private int movieId;
    private String name;
    private String role;
    private String imagePath;

    public CastMember(int id, int movieId, String name, String role, String imagePath) {
        this.id = id;
        this.movieId = movieId;
        this.name = name;
        this.role = role;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public int getMovieId() { return movieId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getImagePath() { return imagePath; }
}
