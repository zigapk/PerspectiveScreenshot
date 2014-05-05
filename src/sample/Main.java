package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.PerspectiveTransform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
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

        File img = new File("/Users/ziga/Desktop/old.png");
        try {
            BufferedImage bimg = ImageIO.read(img);
            int w = bimg.getWidth(); //Set to the original width of the image
            int h = bimg.getHeight(); //Set to the original height of image

            bimg = rotateCw(bimg);
            ParameterBlock params = new ParameterBlock();
            params.addSource(bimg); //source is the input image

            PerspectiveFilter filter = new PerspectiveFilter();
            BufferedImage bimg2 = filter.createCompatibleDestImage(bimg, ColorModel.getRGBdefault());
            //filter.setCorners(0, 0, w, 0, 100, h-100, w, h);
            //filter.setCorners(197, 0, 520, 0, 1363, 1352, 848, 674);
            filter.setCorners(948, 747, 100, 297, 610, 100, 1463, 450);
            filter.filter(bimg, bimg2);

            File outputfile = new File("/Users/ziga/Desktop/new.png");
            ImageIO.write(bimg2, "png", outputfile);

            /*
            bimg = rotateCw(bimg);
            File test = new File("/Users/ziga/Desktop/new.png");
            ImageIO.write(bimg, "png", test);*/

            Platform.exit();

            /*Point tl = new Point(10, 10); //The new top left corner
            Point tr = new Point(w - 10, h + 10); //The new top right corner
            Point bl = new Point(10, h - 10); //The new bottom left corner
            Point br = new Point(w - 10, h - 50); //The new bottom right corner
            params.add(new WarpPerspective(PerspectiveTransform.getQuadToQuad(0, 0, 0, h, w, h, w, 0, tl.x, tl.y, bl.x, bl.y, br.x, br.y, tr.x, tr.y).createInverse()));
            params.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC)); //Change the interpolation if you need more speed
            RenderedOp dest = JAI.create("warp", params); //dest is now the output*/

        }catch (Exception e){

            String neki = e.toString();
        }


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


}
