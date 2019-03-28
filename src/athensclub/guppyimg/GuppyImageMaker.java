package athensclub.guppyimg;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import athensclub.guppyimg.images.Images;

/**
 * A main class for creating guppy images
 * 
 * @author Athensclub
 *
 */
public class GuppyImageMaker {

    public static final int DIVERSITY_LOW = 8, DIVERSITY_MEDIUM = 16, DIVERSITY_HIGH = 32;

    private ArrayList<Guppy> guppies;

    private int diversity = DIVERSITY_HIGH;

    private int gwidth = 65, gheight = 60;

    /**
     * Constructor
     * 
     * @param gwidth
     *                    the width of one guppy
     * @param gheight
     *                    the height of one guppy
     * @param div
     */
    public GuppyImageMaker() {
	guppies = new ArrayList<Guppy>();
	for (int i = 0; i < diversity; i++) {
	    BufferedImage img = Images.images.get(i);
	    guppies.add(new Guppy(gwidth, gheight, img));
	}
    }

    /**
     * Get the guppy with color closest to given color
     * 
     * @param color
     * @return
     */
    public Guppy getClosestGuppy(int color) {
	int md = Integer.MAX_VALUE;
	Guppy result = null;
	int r = Images.red(color);
	int g = Images.green(color);
	int b = Images.blue(color);
	for (int i = 0; i < diversity; i++) {
	    Guppy guppy = guppies.get(i);
	    int d = guppy.distance(r, g, b);
	    if (d < md) {
		result = guppy;
		md = d;
	    }
	}
	return result;
    }

    /**
     * Set the size of 1 guppy
     * 
     * @param width
     * @param height
     */
    public void setGuppySize(int width, int height) {
	if (width == gwidth && height == gheight) {
	    return;
	}
	guppies.clear();
	gwidth = width;
	gheight = height;
	for (BufferedImage img : Images.images) {
	    guppies.add(new Guppy(width, height, img));
	}
    }

    public int getGuppyWidth() {
	return gwidth;
    }

    public int getGuppyHeight() {
	return gheight;
    }

    /**
     * Set the amount of different kind of fishes there are
     * 
     * @param div
     */
    public void setDiversity(int div) {
	if (div < 1 || div > 32) {
	    throw new IllegalArgumentException("" + div);
	}
	diversity = div;
    }

    /**
     * Create new task to be run.
     * 
     * @param image
     * @return
     */
    public GuppyImageMakeTask makeTask(BufferedImage image) {
	return new GuppyImageMakeTask(this, image);
    }

    /**
     * Make guppy image from the given image
     * 
     * @param image
     * @return
     */
    public BufferedImage make(BufferedImage image) {
	BufferedImage result = new BufferedImage(gwidth * image.getWidth(), gheight * image.getHeight(),
		BufferedImage.TYPE_INT_ARGB);
	Graphics2D g = result.createGraphics();
	for (int x = 0; x < image.getWidth(); x++) {
	    for (int y = 0; y < image.getHeight(); y++) {
		Guppy closest = getClosestGuppy(image.getRGB(x, y));
		g.drawImage(closest.getImage(), x * gwidth, y * gheight, null);
	    }
	}
	g.dispose();
	return result;
    }

}
