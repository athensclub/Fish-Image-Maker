package athensclub.guppyimg;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A class that represent a task that can be run in a separate thread
 * 
 * @author Athensclub
 *
 */
public class GuppyImageMakeTask {

    @FunctionalInterface
    public static interface GuppyImageProgressListener {

	public void progress(int currentProgress, int maxProgress);

    }

    private GuppyImageMaker maker;

    private BufferedImage input, result;

    private int gwidth, gheight;

    private int pixels;

    private boolean start;

    private GuppyImageProgressListener progressListener;

    private Runnable onFinished;

    public GuppyImageMakeTask(GuppyImageMaker m, BufferedImage toMake) {
	gwidth = m.getGuppyWidth();
	gheight = m.getGuppyHeight();
	maker = m;
	pixels = toMake.getWidth() * toMake.getHeight();
	result = new BufferedImage(toMake.getWidth() * gwidth, toMake.getHeight() * gheight,
		BufferedImage.TYPE_INT_ARGB);
	input = toMake;
    }

    public void setProgressListener(GuppyImageProgressListener progressListener) {
	this.progressListener = progressListener;
    }

    /**
     * Return pixel count of the result image.
     * 
     * @return
     */
    public int getResultPixelsCount() {
	return pixels;
    }

    /**
     * Run when this task finished.
     * 
     * @param onFinished
     */
    public void setOnFinished(Runnable onFinished) {
	this.onFinished = onFinished;
    }

    /**
     * Get the image that is make or if is making is the current image.
     * 
     * @return
     */
    public BufferedImage getResultImage() {
	return result;
    }

    /**
     * Start this task in the new thread
     * 
     * @param thread
     */
    public void start() {
	if (!start) {
	    start = true;
	    Thread thread = new Thread(() -> {
		int progress = 0;
		Graphics2D g = result.createGraphics();
		for (int x = 0; x < input.getWidth(); x++) {
		    for (int y = 0; y < input.getHeight(); y++) {
			Guppy closest = maker.getClosestGuppy(input.getRGB(x, y));
			g.drawImage(closest.getImage(), x * gwidth, y * gheight, null);
			progress++;
			if (progressListener != null) {
			    progressListener.progress(progress, pixels);
			}
		    }
		}
		g.dispose();
		if (onFinished != null) {
		    onFinished.run();
		}
	    });
	    thread.start();
	} else {
	    throw new IllegalStateException("Start task more than once");
	}
    }

}
