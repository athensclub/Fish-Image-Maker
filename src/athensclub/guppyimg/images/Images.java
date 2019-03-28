package athensclub.guppyimg.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A class containing all images
 * 
 * @author Athensclub
 *
 */
public class Images {

    public static final ArrayList<BufferedImage> images = new ArrayList<>();

    /**
     * Get red component of color
     * 
     * @param col
     * @return
     */
    public static int red(int col) {
	return (col >> 16) & 0xFF;
    }

    /**
     * Get green component of color
     * 
     * @param col
     * @return
     */
    public static int green(int col) {
	return (col >> 8) & 0xFF;
    }

    /**
     * Get blue component of color
     * 
     * @param col
     * @return
     */
    public static int blue(int col) {
	return col & 0xFF;
    }

    static {
	for (int i = 1; i <= 32; i++) {
	    try {
		images.add(ImageIO.read(Images.class.getResourceAsStream("fish" + i + ".png")));
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
