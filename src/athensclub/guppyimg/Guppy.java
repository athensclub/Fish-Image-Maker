package athensclub.guppyimg;

import java.awt.Image;
import java.awt.image.BufferedImage;

import athensclub.guppyimg.images.Images;

/**
 * A class representing guppy image
 * 
 * @author Athensclub
 *
 */
public class Guppy {

    private int r, g, b;

    private Image image;

    public Guppy(int width, int height, BufferedImage img) {
	image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	int color = img.getRGB(img.getWidth() / 2, img.getHeight() / 2);
	r = Images.red(color);
	g = Images.green(color);
	b = Images.blue(color);
    }

    /**
     * Get the difference between this guppy color and the given color
     * 
     * @param r
     * @param g
     * @param b
     * @return
     */
    public int distance(int r, int g, int b) {
	return Math.abs(r - this.r) + Math.abs(g - this.g) + Math.abs(b - this.b);
    }

    public Image getImage() {
	return image;
    }

}
