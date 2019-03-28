package athensclub.guppyimg;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * A console to help make guppy image
 * 
 * @author Athensclub
 *
 */
public class GuppyImageMakerConsole {

    private GuppyImageMaker maker;

    private Clock clock;

    private PrintStream out;

    private Scanner in;

    public GuppyImageMakerConsole(PrintStream out, InputStream in) {
	this.out = out;
	this.in = new Scanner(in);
	clock = Clock.systemDefaultZone();
    }

    private int inputIntMoreThanZero(String text) {
	do {
	    out.print(text);
	    String info = in.nextLine();
	    try {
		int w = Integer.parseInt(info);
		if (w < 1) {
		    out.println("Invalid number input(< 1).Please try again");
		    continue;
		}
		return w;
	    } catch (NumberFormatException e) {
		out.println("Invalid number input.Please try again");
	    }
	} while (true);
    }

    private Path inputFile(String text, boolean isInputImage) {
	do {
	    out.print(text);
	    String info = in.nextLine();
	    Path file = null;
	    try {
		file = Paths.get(info);
	    } catch (InvalidPathException e) {
		out.println("Invalid file(invalid path).Please try again");
		continue;
	    }
	    if (Files.isDirectory(file)) {
		out.println("Invalid file(is directory).Please try again.");
		continue;
	    }
	    if (isInputImage) {
		if (!Files.exists(file)) {
		    out.println("Invalid file(does not exist).Please try again.");
		    continue;
		}
		if (!Files.isReadable(file)) {
		    out.println("Invalid file(unreadable).Please try again");
		    continue;
		}
	    } else {
		if (Files.exists(file)) {
		    out.print("File already exists.Do you want to replace the file?(yes/no):");
		    info = in.nextLine();
		    if (info.toLowerCase().equals("no")) {
			out.println("Then please try again.");
			continue;
		    } else if (info.toLowerCase().equals("yes")) {
			try {
			    Files.delete(file);
			} catch (IOException e) {
			    e.printStackTrace();
			    out.println("Please try again.");
			    continue;
			}
			return file;
		    } else {
			out.println("Unknown command.Please try again.");
			continue;
		    }
		}
	    }
	    return file;
	} while (true);
    }

    private int inputDiversity() {
	do {
	    System.out.print("Enter pixel color diversity(low,medium,high):");
	    String info = in.nextLine();
	    switch (info.toLowerCase()) {
	    case "low":
		return GuppyImageMaker.DIVERSITY_LOW;
	    case "medium":
		return GuppyImageMaker.DIVERSITY_MEDIUM;
	    case "high":
		return GuppyImageMaker.DIVERSITY_HIGH;
	    default:
		System.out.println("Invalid input.Please try again");
	    }
	} while (true);
    }

    private BufferedImage inputInputImage(int gwidth, int gheight,int div) {
	do {
	    Path inputPath = inputFile("Enter input image file path:", true);
	    out.print("Loading guppy images... ");
	    maker = new GuppyImageMaker();
	    maker.setDiversity(div);
	    maker.setGuppySize(gwidth, gheight);
	    out.println("Finished.");
	    try {
		out.print("Loading input image... ");
		BufferedImage result = ImageIO.read(Files.newInputStream(inputPath));
		out.println("Finished.");
		return result;
	    } catch (IOException e) {
		e.printStackTrace();
		out.println("Please try again.");
		continue;
	    }
	} while (true);
    }

    public void start() {
	int gwidth = inputIntMoreThanZero("Enter guppy width(recommended 65):");
	int gheight = inputIntMoreThanZero("Enter guppy height(recommended 60):");
	int diversity = inputDiversity();
	BufferedImage input = inputInputImage(gwidth, gheight,diversity);
	Path output = inputFile("Enter output file path:", false);
	Instant start = clock.instant();
	out.print("Processing image... ");
	BufferedImage result = maker.make(input);
	double elapsed = start.until(clock.instant(), ChronoUnit.MILLIS) / 1000.0;
	out.println("Finished in " + elapsed + " seconds.");
	out.print("Writing to output file... ");
	try {
	    Files.createFile(output);
	    ImageIO.write(result, "png", Files.newOutputStream(output));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	out.println("Finished.");
    }

}
