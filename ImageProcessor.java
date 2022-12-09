package com.bham.pij.exercises.e2a;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.tk.FileChooserType;


/*
 * DO NOT IMPORT ANYTHING ELSE.
 */

public class ImageProcessor extends Application {

    // You can change these values if you want, to get a smaller or larger Window.
    private static final int STAGE_WIDTH = 400;
    private static final int STAGE_HEIGHT = 400;

    // These are the filters you must implement.
    private static final String[] filterTypes = {"IDENTITY","BLUR", "SHARPEN", "EMBOSS", "EDGE"};

    private Image image;
    private ImageView imgv;
    private VBox vbox;
    private Scene scene;
    private ArrayList<MenuItem> menuItems;
    private String currentFilename;

    public ImageProcessor() {

    }

    /*
     * You must complete the next four methods. You do not need to change
     * any other methods.
     */

    // You must complete this method.
    public Color[][] applyFilter(Color[][] pixels, float[][] filter) {
    	 double r = 0;
         double g = 0;
         double b = 0;
         double newr = 0;
         double newg = 0;
         double newb = 0;
         Color[][] result = new Color[pixels.length-2][pixels.length-2];
         for(int i=0; i < result.length; i++) {
             //the row of the pixels array
        	 for(int j=0; j<result.length; j++) {
                 //the colum of the pixels array
        		 newr = 0;
        		 newg = 0;
        		 newb = 0;
        		 for(int k=0; k < filter.length; k++) {
                     //the row of the filter array
	                 for(int l=0; l < filter.length; l++) {
                         //the colum of the filter array
	                      r = pixels[k+i][l+j].getRed();
	                      g = pixels[k+i][l+j].getGreen();
	                      b = pixels[k+i][l+j].getBlue();
	                      newr += r * filter[k][l];
	                      newg += g * filter[k][l];
	                      newb += b * filter[k][l];
                      }
	             }
                 if(newr > 1)
                    newr = 1;
                 if(newr < 0)
                    newr = 0;

                 if(newg > 1)
                    newg = 1;
                 if(newg < 0)
                    newg = 0;

                 if(newb > 1)
                    newb = 1;
                 if(newb < 0)
                    newb = 0;
                 result[i][j]= new Color(newr,newg,newb,1.0);
        	 }
         }
    return result;
    }

    // You must complete this method.
    public float[][] createFilter(String filterType) {
    	float IDENTITY[][] = {{0,0,0},{0,1,0},{0,0,0}};
    	float BLUR[][] = {{0.0625f,0.125f,0.0625f},{0.125f,0.25f,0.125f},{0.0625f,0.125f,0.0625f}};
    	float SHARPEN[][] = {{0,-1,0},{-1,5,-1},{0,-1,0}};
    	float EMBOSS[][] = {{-2,-1,0},{-1,0,1},{0,1,2}};
    	float EDGE[][] = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
    	String str = filterType;
    	switch (str) {
    	case "IDENTITY":
    		return IDENTITY;
    	case "BLUR":
    		return BLUR;
    	case "SHARPEN":
    		return SHARPEN;
    	case "EMBOSS":
    		return EMBOSS;
    	case "EDGE":
    		return EDGE;
    	default:
    		return null;
    	}
    }

    // You must complete this method.
    public Color[][] applySepia(Color[][] pixels) {
        double r = 0;
        double g = 0;
        double b = 0;
        double sr = 0;
        double sg = 0;
        double sb = 0;
        for (int i =0; i<pixels.length; i++) {
            for (int j = 0; j<pixels.length; j++) {
                r = pixels[i][j].getRed();
                g = pixels[i][j].getGreen();
                b = pixels[i][j].getBlue();
                sr = r * 0.393 + g * 0.769 + b * 0.189;
                sg = r * 0.349 + g * 0.686 + b * 0.168;
                sb = r * 0.272 + g * 0.534 + b * 0.131;
                if(sr>1)
                	sr=1;
                if(sg>1)
                	sg=1;
                if(sb>1)
                	sb =1;
                pixels[i][j]= new Color(sr,sg,sb,1.0);
            }
        }
        return pixels;
    }

    // You must complete this method.
    public Color[][] applyGreyscale(Color[][] pixels) {
        double r = 0;
        double g = 0;
        double b = 0;
        double gr = 0;
        for (int i =0; i<pixels.length; i++) {
            for (int j = 0; j<pixels.length; j++) {
                r = pixels[i][j].getRed();
                g = pixels[i][j].getGreen();
                b = pixels[i][j].getBlue();
                gr = (r+g+b) / 3;
                pixels[i][j]= new Color(gr,gr,gr,1.0);
            }
        }
        return pixels;
    }

    /*
     *
     * You can ignore the methods below.
     *
     */

    public void filterImage(String filterType) {

        Color[][] pixels = getPixelDataExtended();

        float[][] filter = createFilter(filterType);

        Color[][] filteredImage = applyFilter(pixels, filter);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, filteredImage[i][j]);
            }
        }

        File newFile = new File("filtered_" + filterType + "_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);
    }

    private void sepia() {

        Color[][] pixels = getPixelData();

        Color[][] newPixels = applySepia(pixels);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, newPixels[i][j]);
            }
        }

        File newFile = new File("filtered_SEPIA_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);
    }

    private void greyscale() {
        Color[][] pixels = getPixelData();

        Color[][] newPixels = applyGreyscale(pixels);

        WritableImage wimg = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelWriter pw = wimg.getPixelWriter();

        for (int i = 0; i < wimg.getHeight(); i++) {
            for (int j = 0; j < wimg.getWidth(); j++) {
                pw.setColor(i, j, newPixels[i][j]);
            }
        }

        File newFile = new File("filtered_GREYSCALE_" + this.currentFilename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wimg, null), "png", newFile);
        } catch (Exception s) {
        }

        initialiseVBox(false);

        image = wimg;
        imgv = new ImageView(wimg);
        vbox.getChildren().add(imgv);

    }

    private Color[][] getPixelData() {
        PixelReader pr = image.getPixelReader();
        Color[][] pixels = new Color[(int) image.getWidth()][(int) image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                pixels[i][j] = pr.getColor(i, j);
            }
        }

        return pixels;
    }

    private Color[][] getPixelDataExtended() {
        PixelReader pr = image.getPixelReader();
        Color[][] pixels = new Color[(int) image.getWidth() + 2][(int) image.getHeight() + 2];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                pixels[i][j] = new Color(1.0, 1.0, 1.0, 1.0);
            }
        }

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                pixels[i + 1][j + 1] = pr.getColor(i, j);
            }
        }

        return pixels;
    }

    private void initialiseStage(Stage stage) {
        stage.setTitle("Image Processor");
        scene = new Scene(new VBox(), STAGE_WIDTH, STAGE_HEIGHT);
        scene.setFill(Color.OLDLACE);
    }

    @Override
    public void start(Stage stage) {

        initialiseStage(stage);

        initialiseVBox(true);

        createMenuItems();

        enableMenuItem("open");

        createStage(stage);
    }

    private void createStage(Stage stage) {

        Menu menuFile = new Menu("File");

        MenuItem open = getMenuItem("open");

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Image File");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    enableAllMenuItems();
                    disableMenuItem("open");
                    openFile(file);
                }
            }
        });

        menuFile.getItems().add(open);

        MenuItem close = getMenuItem("close");

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                disableMenuItem("close");
                closeFile();
            }
        });

        menuFile.getItems().add(close);

        Menu menuTools = new Menu("Tools");

        MenuItem greyscale = getMenuItem("greyscale");

        greyscale.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                greyscale();
            }
        });

        menuTools.getItems().add(greyscale);

        MenuItem blur = getMenuItem("blur");

        blur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("BLUR");
            }
        });

        menuTools.getItems().add(blur);

        MenuItem sharpen = getMenuItem("sharpen");

        sharpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("SHARPEN");
            }
        });

        menuTools.getItems().add(sharpen);

        MenuItem edge = getMenuItem("edge");

        edge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("EDGE");
            }
        });

        menuTools.getItems().add(edge);

        MenuItem sepia = getMenuItem("sepia");

        sepia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                sepia();
            }
        });

        menuTools.getItems().add(sepia);

        MenuItem emboss = getMenuItem("emboss");

        emboss.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("EMBOSS");
            }
        });

        menuTools.getItems().add(emboss);

        MenuItem identity = getMenuItem("identity");

        identity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                filterImage("IDENTITY");
            }
        });

        menuTools.getItems().add(identity);

        MenuItem reset = getMenuItem("reset");

        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                reset();
            }
        });

        menuTools.getItems().add(reset);

        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(menuFile, menuTools);

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar, vbox);

        stage.setScene(scene);

        stage.show();
    }

    protected void reset() {
        initialiseVBox(false);
        openFile(new File(currentFilename));
    }

    private void initialiseVBox(boolean create) {

        final int LEFT = 10;
        final int RIGHT = 10;
        final int TOP = 10;
        final int BOTTOM = 10;


        if (create) {
            vbox = new VBox();
        }
        vbox.getChildren().clear();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(LEFT,TOP,RIGHT,BOTTOM));
    }

    private void createMenuItems() {
        menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("Open"));
        menuItems.add(new MenuItem("Close"));
        menuItems.add(new MenuItem("Greyscale"));
        menuItems.add(new MenuItem("Blur"));
        menuItems.add(new MenuItem("Sharpen"));
        menuItems.add(new MenuItem("Sepia"));
        menuItems.add(new MenuItem("Emboss"));
        menuItems.add(new MenuItem("Edge"));
        menuItems.add(new MenuItem("Identity"));
        menuItems.add(new MenuItem("Reset"));
        disableAllMenuItems();
    }

    private void disableAllMenuItems() {
        for (MenuItem m: menuItems) {
            m.setDisable(true);
        }
    }

    private void enableAllMenuItems() {
        for (MenuItem m: menuItems) {
            m.setDisable(false);
        }
    }

    private void disableMenuItem(String item) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(item)) {
                m.setDisable(true);
            }
        }
    }

    private void enableMenuItem(String item) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(item)) {
                m.setDisable(false);
            }
        }
    }

    private MenuItem getMenuItem(String name) {
        for (MenuItem m: menuItems) {
            if (m.getText().equalsIgnoreCase(name)) {
                return m;
            }
        }

        return null;
    }

    private void closeFile() {
        enableMenuItem("open");
        initialiseVBox(false);
    }

    private void openFile(File file) {

        image = new Image("file:" + file.getPath());

        if (image.getWidth() != image.getHeight()) {
            Alert alert = new Alert(AlertType.ERROR, "Image is not square.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        imgv = new ImageView();
        imgv.setImage(image);
        vbox.getChildren().add(imgv);
        currentFilename = file.getName();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
