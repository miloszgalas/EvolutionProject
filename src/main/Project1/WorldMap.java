package Project1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.awt.*;
import java.util.*;
import java.util.List;

import static Project1.MapDirection.directionMap;

public class WorldMap {

    Multimap<Position, Animal> animals = ArrayListMultimap.create();
    private StartParameters parameters;
    private LinkedList<Animal> animalsList = new LinkedList<>();
    private Map<Position, Grass> grasses = new HashMap<>();
    private LinkedList<Position> jungleEmpty = new LinkedList<>();
    private LinkedList<Position> otherEmpty = new LinkedList<>();
    private Random generator = new Random();
    public Position jungleLowerLeft;
    public Position jungleUpperRight;
    public int jungleWidth;
    public int jungleHeight;
    int maxEnergy;
    int oldest;
    int[] oldestGenome = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int sumEnergy = 0;
    private int[] strongestGenome = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private List<Position> positionsTaken = new LinkedList<>();

    int[] getStrongestGenome() {
        return strongestGenome;
    }

    int getMaxEnergy() {
        return maxEnergy;
    }

    int getTotalAnimals() {
        return animals.size();
    }

    int getAverageEnergy() {
        return sumEnergy / animals.size();
    }

    public List<Position> getPositions() {
        return positionsTaken;
    }

    WorldMap(StartParameters parameters) {

        this.parameters = parameters;

        jungleLowerLeft = new Position(((int) (parameters.width / 2 - parameters.width / (2 * parameters.jungleRatio))),    //calculating jungle borders
                ((int) (parameters.height / 2 - parameters.height / (2 * parameters.jungleRatio))));
        jungleUpperRight = new Position(((int) (parameters.width / 2 + parameters.width / (2 * parameters.jungleRatio) - 1)),
                ((int) (parameters.height / 2 + parameters.height / (2 * parameters.jungleRatio) - 1)));
        jungleWidth = jungleUpperRight.x - jungleLowerLeft.x + 1;
        jungleHeight = jungleUpperRight.y - jungleLowerLeft.y + 1;

        maxEnergy = parameters.startEnergy;

        for (int i = 0; i < parameters.width; i++) {        //filling empty positions
            for (int j = 0; j < parameters.height; j++) {
                Position t = new Position(i, j);
                freePosition(t);
            }
        }

        directionMap.put(0, MapDirection.N);
        directionMap.put(1, MapDirection.NE);
        directionMap.put(2, MapDirection.E);
        directionMap.put(3, MapDirection.SE);
        directionMap.put(4, MapDirection.S);
        directionMap.put(5, MapDirection.SW);
        directionMap.put(6, MapDirection.W);
        directionMap.put(7, MapDirection.NW);

        for (int i = 0; i < parameters.startingAnimals; i++) {
            Animal tmp;
            do {
                tmp = Animal.randomAnimal(this, parameters);
            } while (isOccupied(tmp.position));
            animalPlace(tmp);
        }
    }

    boolean isOccupied(Position t) {
        return !(jungleEmpty.contains(t) || otherEmpty.contains(t));
    }

    int getOldest(){
        return oldest;
    }

    int[] getOldestGenome() {
        return oldestGenome;
    }

    Object objectAt(Position position) {
        if (isOccupied(position)) {
            if (animals.containsKey(position)) {
                return animals.get(position).toArray()[0];
            }
            return grasses.get(position);
        }
        return null;
    }

    private void freePosition(Position t) {  //frees up position in empty position lists
        if (!(jungleEmpty.contains(t) || otherEmpty.contains(t)))
            if (t.positionInBounds(jungleLowerLeft, jungleUpperRight))
                jungleEmpty.push(t);
            else
                otherEmpty.push(t);
        positionsTaken.remove(t);

    }

    private void positionTaken(Position t) {    //unmarks position as free
        jungleEmpty.remove(t);
        otherEmpty.remove(t);
        if (!positionsTaken.contains(t))
            positionsTaken.add(t);
    }

    void processDay() {
        deleteDeadAnimals();
        rotateAll();
        moveAll();
        eat();
        reproduce();
        replant();
    }

    private void deleteDeadAnimals() {
        if (animals.size() > 0) {
            LinkedList<Animal> dAnimals = new LinkedList<>();
            for (Animal a : animals.values()) {
                if (a.getEnergy() < parameters.moveEnergy) {
                    dAnimals.push(a);
                }
            }

            dAnimals.forEach(animal -> {
                animals.remove(animal.getPosition(), animal);
                if (!animals.containsKey(animal.getPosition())) {
                    freePosition(animal.getPosition());
                }
                animalsList.remove(animal);
            });
        }
    }

    private void rotateAll() {
        for (Animal a : animals.values()) {
            a.turnGenome();
        }
    }

    private void moveAll() {
        int newMax = 0;
        int newOldest = 0;
        int[] newOldestGenome = {0};
        sumEnergy = 0;
        int[] newGenome = {0};
        if (animalsList.size() > 0)
            for (Animal a : animalsList) {
                if (a.getEnergy() > newMax) {
                    newMax = a.getEnergy();
                    newGenome = a.getGenome().getGenes();
                }
                if (a.age > newOldest) {
                    newOldest = a.age;
                    newOldestGenome = a.getGenome().getGenes();
                }
                Position prevPos = a.getPosition();
                a.move();
                sumEnergy += a.getEnergy();
                animals.remove(prevPos, a);
                if (!animals.containsKey(prevPos))
                    freePosition(prevPos);
                animals.put(a.getPosition(), a);
                positionTaken(a.position);
            }
        oldest = newOldest;
        oldestGenome = newOldestGenome;
        maxEnergy = newMax;
        strongestGenome = newGenome;
    }

    private void eat() {
        for (Position p : animals.keySet()) {                                       //cannot access keys like that
            List<Animal> anim = (List<Animal>) animals.get(p);
            anim.sort(Comparator.comparing(Animal::getEnergy).reversed());
            if (grasses.containsKey(p)) {                                       //eating
//                System.out.println("energy before: " + Arrays.toString(animals.get(p).stream().map(Animal::getEnergy).toArray()));
                int e = anim.get(0).getEnergy();
                int a = (int) anim.stream().filter(animal -> animal.getEnergy() == e).count();
                for (int i = 0; i < a; i++) {
                    anim.get(i).eat(parameters.plantEnergy / a);
                }
                grasses.remove(p);
//                System.out.println("energy after    : " + Arrays.toString(animals.get(p).stream().map(Animal::getEnergy).toArray()));
            }
        }
    }

    private void reproduce() {
        LinkedList<Animal> animalsToAdd = new LinkedList<>();
        for (Position p : animals.keySet()) {                                       //cannot access keys like that (actually can)
            List<Animal> anim = (List<Animal>) animals.get(p);
            anim.sort(Comparator.comparing(Animal::getEnergy).reversed());
            if (anim.size() >= 2) {
                if (anim.get(1).getEnergy() > parameters.startEnergy / 2) {         //reproducing
                    LinkedList<Position> newPosList = new LinkedList<>();
                    for (int i = 0; i < 8; i++) {
                        Position tmp = p.add(directionMap.get(i).toUnitVector());
                        tmp.makePositionInBounds(parameters.width, parameters.height);
                        if (positionEmpty(tmp))
                            newPosList.add(tmp);
                    }
                    if (newPosList.size() != 0) {
                        Random generator = new Random();
                        Position newPos = newPosList.get(generator.nextInt(newPosList.size()));
                        positionTaken(p);
                        Animal child = anim.get(0).reproduce(anim.get(1), newPos);
                        animalsToAdd.add(child);
//                        System.out.println(child.getPosition());
//                        System.out.println(anim.get(1).getPosition());
                    }
                }
            }
        }
        for (Animal child : animalsToAdd) {
            animals.put(child.position, child);
            animalsList.push(child);
        }

    }

    private void replant() {
        if (jungleEmpty.size() != 0) {
            Position tmp = jungleEmpty.get(generator.nextInt(jungleEmpty.size()));
            grasses.put(tmp, new Grass(tmp));
            positionTaken(tmp);
        }
        System.out.println(jungleEmpty.size());
        if (otherEmpty.size() != 0) {
            Position tmp = otherEmpty.get(generator.nextInt(otherEmpty.size()));
            grasses.put(tmp, new Grass(tmp));
            positionTaken(tmp);
        }
        System.out.println(otherEmpty.size());
    }

    private void animalPlace(Animal a) {
        animals.put(a.getPosition(), a);
        animalsList.push(a);
        positionTaken(a.getPosition());
    }

    public Color colorAt(Position position) {
        if (animals.containsKey(position)) {
            List<Animal> anim = (List<Animal>) animals.get(position);
            anim.sort(Comparator.comparing(Animal::getEnergy).reversed());
            if (anim.size() > 0)
                return anim.get(0).getColor(maxEnergy);
        }
        if (grasses.containsKey(position)) {
            return new Color(71, 236, 26);
        }
        return null;
    }

    private boolean positionEmpty(Position pos) {
        return jungleEmpty.contains(pos) || otherEmpty.contains(pos);
    }

    int getGrasses() {
        return grasses.size();
    }
}
