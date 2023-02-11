/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

import java.awt.image.BufferedImage;

/**
 *
 * @author admin
 */
public class Test {

    // метод позволяет случайным образом получить индекс ячейки в интервале от 0 9
    public static int getRandomCell() {
        return (int) (Math.random() * 10.0);
    }

    //psvm+tab
    public static void main(String[] args) {
        //BufferedImage image = ImageUtil.loadResourceImage("/sea.jpg");
        //System.out.println("image=" + image);

        int result = getRandomCell();
        System.out.println("result=" + result);
    }
}
