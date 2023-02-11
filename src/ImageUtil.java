/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageUtil {
    // метод для загрузки изображения из jar архива
    public static BufferedImage loadResourceImage(String fileName){
        try{
            return ImageIO.read(ImageUtil.class.getResourceAsStream(fileName));
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
}
