package athensclub.guppyimg;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedLookAndFeelException {
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	JFrame frame = new JFrame("Fish Image Maker");
	GuppyImageMakerPanel panel = new GuppyImageMakerPanel();
	panel.setAutoPackBy(frame);
	frame.add(panel);
	frame.pack();
	frame.setLocationByPlatform(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

}
