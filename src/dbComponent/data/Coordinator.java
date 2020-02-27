package dbComponent.data;

import java.util.Objects;

public class Coordinator {

    // Fields:
    private int id;
    private String email;

    // Constructors:
    protected Coordinator() {

    }

    public Coordinator(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Coordinator(String email) {
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinator that = (Coordinator) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
