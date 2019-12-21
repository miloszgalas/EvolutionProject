package Project1;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Animal extends AbstractWorldMapElement {

    private MapDirection direction;
    private WorldMap map;
    private Genome genome;
    private StartParameters parameters;
    int age;
//    private IWorldMap map;

    private int energy;

    Animal(Position position, WorldMap map, Genome genome, StartParameters parameters, int energy) {
        this.position = position;
        this.genome = genome;
        this.map = map;
        this.direction = MapDirection.randomDirection();
        this.parameters = parameters;
        this.energy = energy;
        age = 0;
    }

    static Animal randomAnimal(WorldMap map, StartParameters parameters) {
        Position randomPos;
        randomPos = Position.randomPosition(parameters.width, parameters.height);
        return new Animal(randomPos, map, Genome.randomGenome(), parameters, parameters.startEnergy);
    }

    public MapDirection getDirection() {
        return direction;
    }

    public int getEnergy() {
        return energy;
    }

    Genome getGenome() {
        return genome;
    }

    void move() {
        this.position = this.position.add(Objects.requireNonNull(direction.toUnitVector()));
        this.position.makePositionInBounds(parameters.width, parameters.height);
        this.energy -= parameters.moveEnergy;
        age++;
    }

    void turnGenome() {
        Random generator = new Random();
        turnDegree(generator.nextInt(8));
    }

    void eat(int energy) {
        this.energy += energy;
        if (this.energy>map.maxEnergy)
            map.maxEnergy=this.energy;
    }

    void turnDegree(int degree) {
        this.direction = this.direction.rotate(degree);
    }

    public Position getPosition() {
        return position;
    }

    Animal reproduce(Animal thatAnimal, Position position) {
        Animal tmp = new Animal(position, this.map, this.genome.genomeMerge(thatAnimal.getGenome()), parameters, this.energy / 4 + thatAnimal.getEnergy() / 4);
        tmp.direction = MapDirection.randomDirection();
        this.energy *= 0.75;
        thatAnimal.energy *= 0.75;
        return tmp;
    }

    public Color getColor(int maxEnergy) {

        if (this.energy < maxEnergy / 2) {
            return new Color(255*this.energy*2/maxEnergy, 0, 255);
        }
        else
            return new Color(255, 0, Math.max(255-510*this.energy/maxEnergy,0));
    }

    @Override
    public String toString() {
        return String.valueOf(energy);
    }
}
