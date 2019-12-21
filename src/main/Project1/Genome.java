package Project1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Genome {
    private int[] genes;

    public int[] getGenes() {
        return genes;
    }

    public Genome(int[] genes) {
        this.genes = genes;
    }

    static int[] randomGenes() {
        Random generator = new Random();
        int[] newGenes = new int[32];
        List<Integer> incrementOn = new LinkedList<>();
        int x;
        for (int i = 0; i < 7; i++) {
            do {
                x = generator.nextInt(31);
            } while (incrementOn.contains(x));
            incrementOn.add(x);
        }
//        System.out.println(incrementOn);
        int g = 0;
        for (int i = 0; i < 32; i++) {
            newGenes[i] = g;
            if (incrementOn.contains(i))
                g++;
        }
        return newGenes;
    }

    static Genome randomGenome(){
        return new Genome(randomGenes());
    }

    static int[] genomeSplit() {
        Random generator = new Random();
        int x1 = generator.nextInt(32);
        int x2;
        do {
            x2 = generator.nextInt(32);
        } while (x1 == x2);
        return new int[]{x1, x2};
    }

    Genome genomeMerge(Genome thatGenome) {

        int[] splits = genomeSplit();
        int[] newGenes = new int[32];
        for (int i = 0; i < 32; i++) {
            if (splits[0] < i && splits[1] >= i)
                newGenes[i] = thatGenome.getGenes()[i];
            else
                newGenes[i] = this.genes[i];
        }
        return new Genome(newGenes);
//        add genome control on merging
    }


}
