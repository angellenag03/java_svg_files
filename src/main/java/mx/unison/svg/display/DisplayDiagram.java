package mx.unison.svg.display;

import javax.swing.*;
import java.io.File;

public class DisplayDiagram {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DisplayDiagram <svg-file>");
            System.exit(1);
        }

        File file = new File(args[0]);
        System.out.println(file.toPath().toAbsolutePath());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SVG Diagram Viewer");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SVGDiagram(file));
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
