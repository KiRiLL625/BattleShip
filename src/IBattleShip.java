/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

public interface IBattleShip {
    // методы которые позволяют управлять игрой и определять ее статус
    // начать игру
    void start();
    // выполнить выстрел
    void shot(int i, int j);
    // определяем заупущена игра или нет
    boolean gameStarted();

    // определяем ходит ли компьютер
    boolean computerStep();

    // определяем выиграл ли игрок
    boolean playerWin();
    // определяем выиграл ли компьютер
    boolean computerWin();

    // получаем состояние игрового поля игрока
    int[][] player();
    // получаем состояние игрового поля компьютера
    int[][] computer();
}
