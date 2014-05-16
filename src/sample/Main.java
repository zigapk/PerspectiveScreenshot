package sample;

import com.jhlabs.image.CropFilter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.util.ArrayList;

import com.jhlabs.image.PerspectiveFilter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }{

        File img = new File("/Users/ziga/Desktop/old123.png");
        try {
            BufferedImage bimg = ImageIO.read(img);
            int w = bimg.getWidth(); //Set to the original width of the image
            int h = bimg.getHeight(); //Set to the original height of image
            bimg = scaleImage(bimg, 1370, 2463);
            bimg = rotateCw(bimg);
            ParameterBlock params = new ParameterBlock();
            params.addSource(bimg); //source is the input image
            PerspectiveFilter filter = new PerspectiveFilter();
            BufferedImage bimg2 = filter.createCompatibleDestImage(bimg, ColorModel.getRGBdefault());
            //filter.setCorners(0, 0, w, 0, 100, h-100, w, h);
            //filter.setCorners(197, 0, 520, 0, 1363, 1352, 848, 674);
            filter.setCorners(948, 747, 100, 297, 610, 100, 1463, 450);
            filter.filter(bimg, bimg2);


            double[] a = {0.0, 197.0};
            double[] b = {848.0, 647.0};

            double[] a2 = {848.0, 647.0};
            double[] b2 = {1363.0, 350.0};

            /*double[] a = {0.0, 190.0};
            double[] b = {848.0, 640.0};*/

            /*int[] a = {1, 1};
            int[] b = {100, 100};*/
            ArrayList<int[]> neki = diagonala(bimg2, a, b);
            ArrayList<int[]> neki2 = diagonala(bimg2, a2, b2);

            bimg2 = down(bimg2, neki, 13, 0.9);
            bimg2 = down(bimg2, neki2, 13, 0.7);

            bimg2 = addShadow(bimg2);

            bimg2 = crop(bimg2, 0, 0, 2000, 1200);

            File outputfile = new File("/Users/ziga/Desktop/new.png");
            ImageIO.write(bimg2, "png", outputfile);


            Platform.exit();


        }catch (Exception e){

            String neki = e.toString();
        }


    }

    public BufferedImage addShadow(BufferedImage img){

        try {
            BufferedImage shadow = ImageIO.read(getClass().getClassLoader().getResourceAsStream("resources/shadow.png"));
            int razlika = shadow.getWidth() - img.getWidth();



            for(int x = 0; x < img.getWidth(); x++){
                for(int y = 0; y < img.getHeight(); y++){
                    if(!isTransparent(img, x, y)){
                        Color current = new Color(img.getRGB(x, y));
                        shadow.setRGB(x + razlika, y, current.getRGB());
                    }
                }
            }

            return shadow;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return img;
    }

    public static BufferedImage down(BufferedImage img, ArrayList<int[]> pixels, int deepth, double koeficient){

        for(int i = 0; i < pixels.size(); i++){
            int x = pixels.get(i)[0];
            int y = pixels.get(i)[1];
            Color old = new Color(img.getRGB(x, y));
            old = darker(old, koeficient);

            for (int j = 0; j <= deepth; j++){
                img.setRGB(x, y+j, old.getRGB());
            }

        }

        return img;
    }


    public static BufferedImage rotateCw( BufferedImage img )
    {
        int         width  = img.getWidth();
        int         height = img.getHeight();
        BufferedImage   newImage = new BufferedImage( height, width, img.getType() );

        for( int i=0 ; i < width ; i++ )
            for( int j=0 ; j < height ; j++ )
                newImage.setRGB( height-1-j, i, img.getRGB(i,j) );

        return newImage;
    }


    public static ArrayList<int[]> diagonala(BufferedImage img, double[] a, double[] b){
        ArrayList<int[]> neki = new ArrayList<int[]>();

        double[] xneki = {a[0], b[0]};
        double[] yneki = {a[1], b[1]};
        double xveliki = vecji(xneki);
        double xmali = manjsi(xneki);
        double yvelik = vecji(yneki);
        double ymali = manjsi(yneki);

        double k = (b[1] - a[1]) / (b[0] - a[0]);
        //int n = a[1] / (k * a[0]);

        double n = -(k*a[0]) + a[1];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int result = (int) Math.round(k*x+n);
                if(y==result){
                    if((x>=xmali)&&(x<=xveliki)&&(y>=ymali)&&(y<=yvelik)){
                        int[] xiny = {x, y};
                        neki.add(xiny);
                    }
                }else if(y+1==result){
                    if((x>=xmali)&&(x<=xveliki)&&(y>=ymali)&&(y<=yvelik)){
                        int[] xiny = {x, y};
                        neki.add(xiny);
                    }
                }else if(y-1==result){
                    if((x>=xmali)&&(x<=xveliki)&&(y>=ymali)&&(y<=yvelik)){
                        int[] xiny = {x, y};
                        neki.add(xiny);
                    }
                }
            }
        }
        return neki;
    }


    public static double vecji(double[] neki){

        double x;
        if(neki[0]==neki[1]){
            x = neki[0];
        }else if(neki[1]<neki[0]){
            x = neki[0];
        }else {
            x = neki[1];
        }

        return x;

    }

    public static double manjsi(double[] neki){

        double x;
        if(neki[0]==neki[1]){
            x = neki[0];
        }else if(neki[1]<neki[0]){
            x = neki[1];
        }else {
            x = neki[0];
        }

        return x;

    }

    public static Color darker(Color color, double koeficient){

        double r = color.getRed();
        double g = color.getGreen();
        double b = color.getBlue();

        r = r * koeficient;
        g = g * koeficient;
        b = b * koeficient;

        int red = (int) Math.round(r);
        int green = (int) Math.round(g);
        int blue = (int) Math.round(b);

        Color darker = new Color(red, green, blue);

        return darker;
    }

    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        int type=0;
        type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height,type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    public boolean isTransparent(BufferedImage img, int x, int y ) {
        int pixel = img.getRGB(x,y);
        if( (pixel>>24) == 0x00 ) {
            return true;
        }else {
            return false;
        }
    }

    public static BufferedImage crop(BufferedImage image, int startX, int startY, int endX, int endY){

        BufferedImage img = image.getSubimage(startX, startY, endX, endY);
        return img;
    }

}
/*
y = k * x + n
-n + y = k*x
-n = k*x-y
n = -(k*x)+y
*/