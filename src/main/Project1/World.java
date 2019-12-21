package Project1;

import Project1.Visualize.SimulationVisualizer;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class World {


    public static void main(String[] args) {


        Gson gson = new Gson();

        try (Reader reader = new FileReader("src/parameters.json")) {

            StartParameters parameters = gson.fromJson(reader, StartParameters.class);

            WorldMap map = new WorldMap(parameters);

            JFrame frame = new JFrame("GUI");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
//            frame.setLayout(new GridBagLayout());

            SimulationVisualizer visualizer = new SimulationVisualizer(map, parameters, frame);

            frame.add(visualizer);

            frame.setVisible(true);

            JFrame frame2 = new JFrame("statistics");
            int day = 0;

            JPanel infoPanel = new JPanel();
            JLabel dayCount = new JLabel("Day: " + day);
            JLabel maxEnergy = new JLabel("Max energy(red): " + map.getMaxEnergy());
            JLabel avgEnergy = new JLabel("Average energy(red): " + map.getAverageEnergy());
            JLabel animalsAmount = new JLabel("Total number of animals: " + map.getTotalAnimals());
            JLabel grassesAmount = new JLabel("Number of Grasses: " + map.getGrasses());
            JLabel strongestGenome = new JLabel("Strongest genome: " + Arrays.toString(map.getStrongestGenome()));
            JLabel oldestAnimal = new JLabel("Oldest Animal: " + map.getOldest());
            JLabel oldestGenome = new JLabel("Oldest genome: " + Arrays.toString(map.getOldestGenome()));

            infoPanel.setSize((int) (0.5 * frame.getWidth()),500);
            infoPanel.add(dayCount);
            infoPanel.add(maxEnergy);
            infoPanel.add(avgEnergy);
            infoPanel.add(animalsAmount);
            infoPanel.add(grassesAmount);
            infoPanel.add(strongestGenome);
            infoPanel.add(oldestAnimal);
            infoPanel.add(oldestGenome);
            infoPanel.setLayout(new GridLayout(20, 1));
            infoPanel.setVisible(true);

            frame2.add(infoPanel);
            frame2.setSize(600,400);
            frame2.setVisible(true);
            while (map.animals.size() > 0) {
//            for (int i = 0; i < 50; i++) {
                day++;
                System.out.println("day: " + day);
                System.out.println(map.animals.size());
                map.processDay();
                frame.repaint();
                if (day % 10 == 0) {
                    dayCount.setText("Day: " + day);
                    maxEnergy.setText("Max energy(red): " + map.getMaxEnergy());
                    avgEnergy.setText("Average energy: " + map.getAverageEnergy());
                    animalsAmount.setText("Total number of animals: " + map.getTotalAnimals());
                    grassesAmount.setText("Number of grasses: " + map.getGrasses());
                    strongestGenome.setText("Strongest genome: " + Arrays.toString(map.getStrongestGenome()));
                    oldestAnimal.setText("Oldest Animal: " + map.getOldest());
                    oldestGenome.setText("Oldest genome: " + Arrays.toString(map.getOldestGenome()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
