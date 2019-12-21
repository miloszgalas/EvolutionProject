package Project1;

public class Grass extends AbstractWorldMapElement {

    public Grass(Position position) {
        this.position = position;
    }

    public String toString() {
        return "*";
    }
}
