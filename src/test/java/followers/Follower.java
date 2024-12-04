package followers;

public class Follower {
    private String name;
    private String gender;
    private String nivel;

    public Follower(String name, String gender, String nivel) {
        this.name = name;
        this.gender = gender;
        this.nivel = nivel;
    }

    public boolean matches(String name, String gender, String nivel) {
        return this.name.equals(name) && this.gender.equals(gender) && this.nivel.equals(nivel);
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getNivel() {
        return nivel;
    }
}
