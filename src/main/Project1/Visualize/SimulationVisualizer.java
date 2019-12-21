package Project1.Visualize;

import Project1.Position;
import Project1.StartParameters;
import Project1.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationVisualizer extends JPanel {

    private WorldMap map;
    private StartParameters parameters;

    public SimulationVisualizer(WorldMap map, StartParameters parameters, JFrame frame) {
        this.setSize((int) ((frame.getWidth()) * 0.5), frame.getHeight() - 38);
        this.map = map;
        this.parameters = parameters;
    }

    public void paintComponent(Graphics g) {
        int cellHeight = this.getHeight() / parameters.height;
        int cellWidth = this.getWidth() / parameters.width;
        super.paintComponent(g);
        super.setBackground(new Color(214, 244, 126));
        g.setColor(new Color(71, 130, 56));
        g.fillRect(map.jungleLowerLeft.x * cellWidth, map.jungleLowerLeft.y * cellHeight, map.jungleWidth * cellWidth, map.jungleHeight * cellHeight);
        List<Position> positionList = map.getPositions();

        for (int i = 0; i < positionList.size(); i++) {
            g.setColor(map.colorAt(positionList.get(i)));
            g.fillOval(positionList.get(i).x * cellWidth, positionList.get(i).y * cellHeight, cellWidth, cellHeight);
        }
    }

}
