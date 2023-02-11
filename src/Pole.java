/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Pole extends JPanel implements MouseListener {

    // левый верхний угол - координаты для поля Компьютера
    static final int COMPUTER_FIELD_X1 = 100;
    static final int COMPUTER_FIELD_Y1 = 100;
    // шаг сетки (размер ячейки игрового поля)
    static final int GRID_STEP = 30;
    // размер поля - количество ячеек игрового поля
    static final int F_SIZE = 10;
    // отступ между полями компьютера и игрока
    static final int INDENT = 100;
    // левый верхний угол - координаты для поля Игрока
    static final int PLAYER_FIELD_X1 = COMPUTER_FIELD_X1 + GRID_STEP * F_SIZE + INDENT;
    static final int PLAYER_FIELD_Y1 = COMPUTER_FIELD_Y1;
    static final int COMPUTER_FIELD_X2 = COMPUTER_FIELD_X1 + F_SIZE*GRID_STEP;
    static final int COMPUTER_FIELD_Y2 = COMPUTER_FIELD_Y1 + F_SIZE*GRID_STEP;
    //
    static final String FON_IMAGE = "/sea.jpg"; // fon.png
    static final String PALUBA_IMAGE = "/paluba.png";
    static final String RANEN_IMAGE = "/ranen.png";
    static final String UBIT_IMAGE = "/ubit.png";
    static final String BOMBA_IMAGE = "/bomba.png";
    static final String END1_IMAGE = "/end1.png";
    static final String END2_IMAGE = "/end2.png";
    //
    Image fon;
    Image paluba, ranen, ubit, bomba;
    Image end1, end2;
    // переменная класса game
    IBattleShip game;
    // таймер для автмомат-го обновления полей
    Timer tmDraw;

    public Pole(IBattleShip game) {
        this.game = game;
        // добавляем обработчик нажатия на кнопку мыши
        addMouseListener(this);
        // загружаем из-е и сохр в переменной fon
        fon = ImageUtil.loadResourceImage(FON_IMAGE);
        paluba = ImageUtil.loadResourceImage(PALUBA_IMAGE);
        ranen = ImageUtil.loadResourceImage(RANEN_IMAGE);
        ubit = ImageUtil.loadResourceImage(UBIT_IMAGE);
        bomba = ImageUtil.loadResourceImage(BOMBA_IMAGE);
        end1 = ImageUtil.loadResourceImage(END1_IMAGE);
        end2 = ImageUtil.loadResourceImage(END2_IMAGE);
        // запускаем таймер для обновления панели (автомат будет вызван метод paintComponent)
        tmDraw = new Timer(50, ev -> repaint());
        tmDraw.start();
    }

    // paluba
    public static boolean isPaluba(int[][] a, int i, int j) {
        return a[i][j] >= 1 && a[i][j] <= 4;
    }
    public static boolean isRanen(int[][] a, int i, int j){
        return a[i][j] >= 8 && a[i][j] <= 11;
    }
    public static boolean isUbit(int[][] a, int i, int j){
        return a[i][j] >= 15;
    }
    public static boolean isBomba(int[][] a, int i, int j){
        return a[i][j] >= 5;
    }
    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        // отрисовка фона
        gr.drawImage(fon, 0, 0, null);
        // заголовки Компьютер и Игрок
        gr.setColor(Color.blue);
        gr.setFont(new Font("serif", 3, 40));
        gr.drawString("Компьютер", 150, 50);
        gr.drawString("Игрок", 590, 50);
        // отрисовка состояния кораблей игрока и компьютера
        for(int i = 0; i < F_SIZE; i++) {
            for (int j = 0; j < F_SIZE; j++) {
//                computer
                if(isRanen(game.computer(), i, j)){
                    gr.drawImage(ranen, COMPUTER_FIELD_X1 + j*GRID_STEP, COMPUTER_FIELD_Y1+ i*GRID_STEP, null);
                }
                if(isUbit(game.computer(), i, j)){
                    gr.drawImage(ubit, COMPUTER_FIELD_X1 + j*GRID_STEP, COMPUTER_FIELD_Y1+ i*GRID_STEP, null);
                }
                if(isBomba(game.computer(), i, j)){
                    gr.drawImage(bomba, COMPUTER_FIELD_X1 + j*GRID_STEP, COMPUTER_FIELD_Y1+ i*GRID_STEP, null);
                }
                // player
                if (isPaluba(game.player(), i, j)) {
                    gr.drawImage(paluba, PLAYER_FIELD_X1 + j*GRID_STEP,
                            PLAYER_FIELD_Y1 + i*GRID_STEP, null);
                }
                else if(isRanen(game.player(), i, j)){
                    gr.drawImage(ranen, PLAYER_FIELD_X1 + j*GRID_STEP, PLAYER_FIELD_Y1+ i*GRID_STEP, null);
                }
                else if(isUbit(game.player(), i, j)){
                    gr.drawImage(ubit, PLAYER_FIELD_X1 + j*GRID_STEP, PLAYER_FIELD_Y1+ i*GRID_STEP, null);
                }
                if(isBomba(game.player(), i, j)){
                    gr.drawImage(bomba, PLAYER_FIELD_X1 + j*GRID_STEP, PLAYER_FIELD_Y1+ i*GRID_STEP, null);
                }
            }
        }

        // Игровое поле - сетка для компьютера
        drawGrid(gr, COMPUTER_FIELD_X1, COMPUTER_FIELD_Y1, GRID_STEP);
        // Игровое поле - сетка для игрока
        drawGrid(gr, PLAYER_FIELD_X1, PLAYER_FIELD_Y1, GRID_STEP);
        if(game.playerWin()){
            gr.drawImage(end1, 300, 200, null);
        }
        if(game.computerWin()){
            gr.drawImage(end2, 300, 200, null);
        }
    }

    // метод для отрисовки сетки игрового поля
    // x1 - коорд x верхнего левого угла сетки // y1 - коорд y верхнего левого угла сетки // step - расстояние между линиями сетки
    public void drawGrid(Graphics gr, int x1, int y1, int step) {
        gr.setColor(Color.blue);
        // отрисовка сетки поля
        for (int i = 0; i <= F_SIZE; i++) {
            // вертикальные линии
            gr.drawLine(x1 + i * step, y1, x1 + i * step, y1 + F_SIZE * step);
            // горизантальные линии
            gr.drawLine(x1, y1 + i * step, x1 + F_SIZE * step, y1 + i * step);
        }
        // отрисовка названий для колонок и строк
        gr.setFont(new Font("serif", 0, 20));
        gr.setColor(Color.red);
        for (int i = 1; i <= F_SIZE; i++) {
            //Для строк: номера строк от 1 до 10
            gr.drawString(String.valueOf(i), x1 - 27, y1 - 7 + i * step);
            //Для колонок: буквы от A до J получаем строку которая соотв буквам алфавита
            String letter = String.valueOf((char) ('A' + i - 1));
            gr.drawString(letter, x1 - 22 + i * step, y1 - 7);
        }
    }
    public static boolean insideComputerField(int x, int y){
        if((x <= COMPUTER_FIELD_X2) && (y <= COMPUTER_FIELD_Y2) && (x >= COMPUTER_FIELD_X1) && (y >= COMPUTER_FIELD_Y1)){
            return true;
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // левую кнопку мыши и только одинарное нажатие
        int button = e.getButton();
        int clickCount = e.getClickCount();
        //System.out.println("mousePressed!");
        //System.out.println("button=" + button);
        //System.out.println("clickCount=" + clickCount);
        if (button == 1 && clickCount == 1 && insideComputerField(e.getX(), e.getY())) {
            int x = e.getX();
            int y = e.getY();
            System.out.println("mousePressed_x=" + x + "_y" + y);
            int i = (y - COMPUTER_FIELD_Y1) / GRID_STEP;
            int j = (x - COMPUTER_FIELD_X1) / GRID_STEP;
            game.shot(i, j);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
