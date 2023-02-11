/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Okno extends JFrame implements ActionListener {
    // создаем объект при создании главного окна
    IBattleShip game = new BattleShip();
    public Okno() {
        setTitle("Игра Морской бой");
        setBounds(10, 10, 900, 600);
        //
        getContentPane().add(createPanel());
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public JPanel createPanel() {
        JPanel p = new Pole(game);
        // Добавить 2 кнопки: "Новая игра" и "Выход"
        JButton startButton = new JButton("Новая игра");
        startButton.addActionListener(this);
        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(this);
        p.add(startButton);
        p.add(exitButton);
        return p;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Выход")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("Новая игра")){
            // вызываем метод start
            game.start();
        }
    }
}
