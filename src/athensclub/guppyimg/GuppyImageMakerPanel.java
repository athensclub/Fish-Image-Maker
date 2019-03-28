package athensclub.guppyimg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.PlainDocument;

public class GuppyImageMakerPanel extends JPanel {

    private GuppyImageMaker maker = new GuppyImageMaker();

    private JPanel progress;

    private JLabel progressLabel;

    private JProgressBar progressBar;

    private JLabel makeImagePath, outputFilePath;

    private File inputFile, outputFile;

    private int diversity = GuppyImageMaker.DIVERSITY_HIGH;

    private JFrame frameToPack;

    private JFileChooser fileChooser = new JFileChooser();

    private ButtonGroup diversityGroup;

    private JRadioButton diversityLow, diversityMedium, diversityHigh;

    private int fwidth = 65, fheight = 60;

    private boolean making;

    public GuppyImageMakerPanel() {

	setLayout(new BorderLayout());

	// Progress
	progress = new JPanel();
	progress.setLayout(new BoxLayout(progress, BoxLayout.Y_AXIS));
	progressLabel = new JLabel("Processing Image...");
	progress.add(progressLabel);

	// File Chooser
	fileChooser.setAcceptAllFileFilterUsed(false);

	// SPLITTING COMPONENTS
	JPanel top = new JPanel();
	top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
	JPanel middle = new JPanel();
	middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
	add(top, BorderLayout.NORTH);
	add(middle, BorderLayout.CENTER);

	// ENTER FISH SIZE
	JPanel fishSize = new JPanel();
	fishSize.setLayout(new BoxLayout(fishSize, BoxLayout.Y_AXIS));
	top.add(fishSize);

	// ENTER WIDTH : FISH SIZE
	JPanel width = new JPanel();
	width.setLayout(new BoxLayout(width, BoxLayout.X_AXIS));
	JLabel widthLabel = new JLabel("Width of 1 fish (pixels)");
	JTextField widthTF = new JTextField("65");
	widthTF.getDocument().addDocumentListener(new DocumentListener() {
	    @Override
	    public void removeUpdate(DocumentEvent e) {
		fwidth = Integer.parseInt(widthTF.getText());
	    }

	    @Override
	    public void insertUpdate(DocumentEvent e) {
		fwidth = Integer.parseInt(widthTF.getText());
	    }

	    @Override
	    public void changedUpdate(DocumentEvent e) {
		fwidth = Integer.parseInt(widthTF.getText());
	    }
	});
	widthTF.setPreferredSize(new Dimension(40, 24));
	widthTF.setMaximumSize(new Dimension(40, 24));
	((PlainDocument) widthTF.getDocument()).setDocumentFilter(new IntFilter());
	width.add(widthLabel);
	width.add(widthTF);
	fishSize.add(width);

	// ENTER HEIGHT : FISH SIZE
	JPanel height = new JPanel();
	height.setLayout(new BoxLayout(height, BoxLayout.X_AXIS));
	JLabel heightLabel = new JLabel("Height of 1 fish (pixels)");
	JTextField heightTF = new JTextField("60");
	heightTF.getDocument().addDocumentListener(new DocumentListener() {

	    @Override
	    public void removeUpdate(DocumentEvent e) {
		fheight = Integer.parseInt(heightTF.getText());
	    }

	    @Override
	    public void insertUpdate(DocumentEvent e) {
		fheight = Integer.parseInt(heightTF.getText());
	    }

	    @Override
	    public void changedUpdate(DocumentEvent e) {
		fheight = Integer.parseInt(heightTF.getText());
	    }
	});
	heightTF.setPreferredSize(new Dimension(40, 24));
	heightTF.setMaximumSize(new Dimension(40, 24));
	((PlainDocument) heightTF.getDocument()).setDocumentFilter(new IntFilter());
	height.add(heightLabel);
	height.add(heightTF);
	fishSize.add(height);

	// SELECT DIVERSITY
	JPanel diversity = new JPanel();
	diversity.setLayout(new BoxLayout(diversity, BoxLayout.X_AXIS));
	JLabel diversityLabel = new JLabel("Diversity of Fish Colors");
	diversity.add(diversityLabel);
	middle.add(diversity);

	// SELECT DIVERSITY BUTTONS : DIVERSITY
	JPanel diversityPanel = new JPanel();
	diversityGroup = new ButtonGroup();
	diversityPanel.setLayout(new BoxLayout(diversityPanel, BoxLayout.Y_AXIS));
	diversityLow = new JRadioButton("Low (8 colors)");
	diversityMedium = new JRadioButton("Medium (16 colors)");
	diversityHigh = new JRadioButton("High (32 colors)");
	diversityLow.addItemListener(this::diversityLowStateChanged);
	diversityMedium.addItemListener(this::diversityMediumStateChanged);
	diversityHigh.addItemListener(this::diversityHighStateChanged);
	diversityGroup.add(diversityLow);
	diversityGroup.add(diversityMedium);
	diversityGroup.add(diversityHigh);
	diversityPanel.add(diversityLow);
	diversityPanel.add(diversityMedium);
	diversityPanel.add(diversityHigh);
	diversityGroup.setSelected(diversityHigh.getModel(), true);
	diversity.add(diversityPanel);

	// SELECT PATHS
	JPanel selectImageToMake = new JPanel();
	selectImageToMake.setLayout(new BoxLayout(selectImageToMake, BoxLayout.X_AXIS));
	makeImagePath = new JLabel("Path to Image to make: none");
	JButton selectMakeImage = new JButton("Choose Image");
	selectMakeImage.addActionListener(this::selectMakeImageClicked);
	selectImageToMake.add(makeImagePath);
	selectImageToMake.add(selectMakeImage);
	top.add(selectImageToMake);

	// SELECT OUTPUT FILE
	JPanel selectOutputFile = new JPanel();
	selectOutputFile.setLayout(new BoxLayout(selectOutputFile, BoxLayout.X_AXIS));
	outputFilePath = new JLabel("Path to output file: none");
	JButton pickOutputFile = new JButton("Choose Path");
	pickOutputFile.addActionListener(this::pickOutputFileClicked);
	selectOutputFile.add(outputFilePath);
	selectOutputFile.add(pickOutputFile);
	top.add(selectOutputFile);

	JButton makeImage = new JButton("Start Making Image");
	makeImage.addActionListener(this::makeImageClicked);
	middle.add(makeImage);
    }

    /**
     * Make that frame pack automatically every time this panel wants to relayout.
     * 
     * @param f
     */
    public void setAutoPackBy(JFrame f) {
	frameToPack = f;
    }

    private void clearFileChooser() {
	while (fileChooser.getChoosableFileFilters().length > 0) {
	    fileChooser.removeChoosableFileFilter(fileChooser.getChoosableFileFilters()[0]);
	}
    }

    private void repack() {
	if (frameToPack != null) {
	    frameToPack.pack();
	}
    }

    private void makeImageClicked(ActionEvent e) {
	if (!making) {
	    if (inputFile == null) {
		JOptionPane.showMessageDialog(null, "Please select image to make.", "Warning",
			JOptionPane.WARNING_MESSAGE);
		return;
	    }
	    if (outputFile == null) {
		JOptionPane.showMessageDialog(null, "Please select path to output file", "Warning",
			JOptionPane.WARNING_MESSAGE);
		return;
	    }
	    maker.setGuppySize(fwidth, fheight);
	    maker.setDiversity(diversity);
	    try {
		try {
		    GuppyImageMakeTask task = maker.makeTask(ImageIO.read(inputFile));
		    if (progressBar != null) {
			progress.remove(progressBar);
		    }
		    progressLabel.setText("Processing Image...");
		    progressBar = new JProgressBar(0, task.getResultPixelsCount());
		    progressBar.setStringPainted(true);
		    progress.add(progressBar);
		    add(progress, BorderLayout.SOUTH);
		    repack();
		    task.setProgressListener((prog, maxprog) -> progressBar.setValue(prog));
		    task.setOnFinished(() -> {
			saveImage(task);
			remove(progress);
			repack();
		    });
		    task.start();
		    making = true;
		} catch (OutOfMemoryError oome) {
		    JOptionPane.showMessageDialog(null,
			    "Not enough memory (RAM) to process Image.Try using smaller size fish or allocate more space to JVM.",
			    "Error: Out of memory", JOptionPane.ERROR_MESSAGE);
		    return;
		}
	    } catch (IOException e1) {
		JOptionPane.showMessageDialog(null, "Unable read file: " + inputFile.getParent(),
			"Error: Unable to read file", JOptionPane.ERROR_MESSAGE);
		return;
	    }
	} else {
	    JOptionPane.showMessageDialog(null, "Please Wait for current image to finish", "Warning",
		    JOptionPane.WARNING_MESSAGE);
	}
    }

    private void saveImage(GuppyImageMakeTask task) {
	progressLabel.setText("Saving result image...");
	do {
	    try {
		ImageIO.write(task.getResultImage(), "png", outputFile);
		JOptionPane.showMessageDialog(null, "Finished", "Finished", JOptionPane.INFORMATION_MESSAGE);
		break;
	    } catch (IOException e1) {
		if (JOptionPane.showConfirmDialog(null, "Unable to save file.Do you want to pick another file?",
			"Error: Unable to save file", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		    pickOutputFileClicked(null); // quick pick file
		} else {
		    break;
		}
	    }
	} while (true);
	making = false;
    }

    private void diversityLowStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
	    diversity = GuppyImageMaker.DIVERSITY_LOW;
	}
    }

    private void diversityMediumStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
	    diversity = GuppyImageMaker.DIVERSITY_MEDIUM;
	}
    }

    private void diversityHighStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
	    diversity = GuppyImageMaker.DIVERSITY_HIGH;
	}
    }

    private void pickOutputFileClicked(ActionEvent e) {
	clearFileChooser();
	fileChooser.setFileFilter(new FileNameExtensionFilter("png file", "png"));
	if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    File file = fileChooser.getSelectedFile();
	    if (!file.getName().endsWith(".png")) {
		file = new File(file.getPath() + ".png");
	    }
	    outputFilePath.setText("Path to output file: " + file.getPath());
	    outputFile = file;
	    repack();
	}
    }

    private void selectMakeImageClicked(ActionEvent e) {
	clearFileChooser();
	fileChooser.setFileFilter(new FileNameExtensionFilter("Java ImageIO readable images(png jpeg bmp etc.)",
		ImageIO.getReaderFormatNames()));
	if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    makeImagePath.setText("Path to Image to make: " + fileChooser.getSelectedFile().getPath());
	    inputFile = fileChooser.getSelectedFile();
	    repack();
	}
    }

}
