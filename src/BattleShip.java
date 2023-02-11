/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package battleship;

public class BattleShip implements IBattleShip {
    // число - все корабли убиты
    static final int GAME_OVER_VALUE = 330;
    // размер поля
    static final int F_SIZE = 10;
    // поле игрока     (данные игрока)
    int[][] p = new int[F_SIZE][F_SIZE];
    // поле компьютера (данные компьютера)
    int[][] c = new int[F_SIZE][F_SIZE];
    // -1 - нет игры
    //  0 - игра запущена
    //  1 - игрок выиграл
    //  2 - выиграл компьютер
    int gameStatus = -1;
    // логическая переменная - кто ходит в данный момент
    boolean computerStep;

    public static void main(String[] args) {
        Okno o = new Okno();
    }

    @Override
    public void start() {
        // 1)
        System.out.println("START GAME!!!");
        gameStatus = 0;
        computerStep = false;
        // очистить массивы c, p  - заполнить их значением 0
        ArrayUtil.fillArr(p, 0);
        ArrayUtil.fillArr(c, 0);
        // вывести состояние игрового поля игрока на экран  - метод printArr
        ArrayUtil.printArr(p);
        // 2) - установка кораблей игрока и компьютера
        // 2.1)
        makePlayerShips();
        //  устанавливаем корабли компьютера
        makeComputerShips();
        // вывод состояния игровых полей
        System.out.println("Состояние игрового поля игрока:");
        ArrayUtil.printArr(p);
        System.out.println("Состояние игрового поля компьютера:");
        ArrayUtil.printArr(c);
    }

    // создание и расстановка кораблей игрока
    public void makePlayerShips() {
        makeShips(p);
    }

    public void makeComputerShips() {
        makeShips(c);
    }

    // метод позволяет случайным образом получить индекс ячейки в интервале от 0 9
    public static int getRandomCell() {
        return (int) (Math.random() * 10.0);
    }

    // устанавливаем корабли для массива
    static void makeShips(int[][] arr) {
        // создаем 1 4-х палубный корабль
        makeShip(arr, 4);
        // создаем 2 3-х палубных корабль
        for (int i = 0; i < 2; i++) {
            makeShip(arr, 3);
        }
        // создаем 3 2-х палубных корабль
        for (int i = 0; i < 3; i++) {
            makeShip(arr, 2);
        }
        // создаем 4 1-х палубных корабль
        for (int i = 0; i < 4; i++) {
            makeShip(arr, 1);
        }
    }

    // устанавливаем корабли для массива
    // numPalub - количество палуб у корабля
    static void makeShip(int[][] arr, int numPalub) {
        //
        System.out.println("makeShip:" + numPalub);
        if (numPalub == 1) {
            int i;
            int j;
            // получаем два случайных числа - координаты ячейки игрового поля
//            i = getRandomCell();
//            j = getRandomCell();
//            arr[i][j] = 1;
            do {
                i = getRandomCell();
                j = getRandomCell();
            } while (arr[i][j] != 0);
            arr[i][j] = 1;
            // кодируем пространство вокруг ячейки
            okrBegin(arr, i, j, -1);
        } else {
            // кол-во палуб > 1
            boolean flag;
            int i;
            int j;
            int napr;
            do {
                flag = false;
                i = getRandomCell();
                j = getRandomCell();
                napr = (int) (Math.random() * 4.0);
                if (testNewPaluba(arr, i, j)) {
                    if (napr == 0) {
                        if (testNewPaluba(arr, i - (numPalub - 1), j)) {
                            flag = true;
                        }
                    } else if (napr == 1) {
                        if (testNewPaluba(arr, i, j + (numPalub - 1))) {
                            flag = true;
                        }
                    } else if (napr == 2) {
                        if (testNewPaluba(arr, i + (numPalub - 1), j)) {
                            flag = true;
                        }
                    } else if (napr == 3) {
                        if (testNewPaluba(arr, i, j - (numPalub - 1))) {
                            flag = true;
                        }
                    }
                }
            } while (!flag);
            arr[i][j] = numPalub;
            okrBegin(arr, i, j, -2);
            if (napr == 0) {
                for (int k = numPalub - 1; k >= 1; k--) {
                    arr[(i - k)][j] = numPalub;
                    okrBegin(arr, i - k, j, -2);
                }
            }else if (napr == 1) {
                for (int k = numPalub - 1; k >= 1; k--) {
                    arr[i][(j + k)] = numPalub;
                    okrBegin(arr, i, j + k, -2);
                }
            }else if (napr == 2) {
                for (int k = numPalub - 1; k >= 1; k--) {
                    arr[(i + k)][j] = numPalub;
                    okrBegin(arr, i + k, j, -2);
                }
            }else if (napr == 3) {
                for (int k = numPalub - 1; k >= 1; k--) {
                    arr[i][(j - k)] = numPalub;
                    okrBegin(arr, i, j - k, -2);
                }
            }
            okrEnd(arr);
        }
    }

    @Override
    public void shot(int i, int j) {
        System.out.println("shot_i=" + i + "_j=" + j);
        if (gameStarted() && !computerStep() && c[i][j] <= 4) {
            // 2.2) Условия выполнения выстрела
            c[i][j] += 7;
            System.out.println("AFTER SHOT C:");
            ArrayUtil.printArr(c);
            // проверка уничтожения корабля
            testUbit(c, i, j);
            // проверка завершения игры
            testEndGame();
            // переход хода
            if(c[i][j] < 8) {
                computerStep = true;
                // компьютер выполняет ход до промаха
                while (computerStep) {
                    computerStep = compHodit();
                }
            }
        }
    }
    // 5 - ход компьютера
    boolean compHodit() {
        boolean rez = false;
        boolean flag = false;
        for (int i = 0; i < F_SIZE; i++) {
            for (int j = 0; j < F_SIZE; j++) {
                if (p[i][j] >= 9 && p[i][j] <= 11) {
                    flag = true;
                    if (testMasPoz(i - 1, j) && p[i - 1][j] <= 4 && p[i - 1][j] != -2) {
                        p[i - 1][j] += 7;
                        testUbit(p, i - 1, j);
                        if (p[i - 1][j] < 8) {
                            break;
                        }
                        rez = true;
                        break;
                    }
                    if (testMasPoz(i + 1, j) && p[i + 1][j] <= 4 && p[i + 1][j] != -2) {
                        p[i + 1][j] += 7;
                        testUbit(p, i + 1, j);
                        if (p[i + 1][j] < 8) {
                            break;
                        }
                        rez = true;
                        break;
                    }
                    if (testMasPoz(i, j - 1) && p[i][j - 1] <= 4 && p[i][j - 1] != -2) {
                        p[i][j - 1] += 7;
                        testUbit(p, i, j - 1);
                        if (p[i][j - 1] < 8) {
                            break;
                        }
                        rez = true;
                        break;
                    }
                    if (testMasPoz(i, j + 1) && p[i][j + 1] <= 4 && p[i][j + 1] != -2) {
                        p[i][j + 1] += 7;
                        testUbit(p, i, j + 1);
                        if (p[i][j + 1] < 8) {
                            break;
                        }
                        rez = true;
                        break;
                    }
                }
            }
        }
        if (!flag) { // 100 случайных попыток для выполнения выстрела
            for (int l = 1; l <= 100; l++) {
                int i = getRandomCell();
                int j = getRandomCell();
                if ((p[i][j] <= 4) && (p[i][j] != -2)) {
                    p[i][j] += 7;
                    testUbit(p, i, j);
                    if (p[i][j] >= 8) {
                        rez = true;
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {  // выполнение выстрела в свободную ячейку - пробегаем поле по порядку
                for (int i = 0; i < F_SIZE; i++) {
                    for (int j = 0; j < F_SIZE; j++) {
                        if ((p[i][j] <= 4) && (p[i][j] != -2)) {
                            p[i][j] += 7;
                            testUbit(p, i, j);
                            if (p[i][j] < 8) {
                                break;
                            }
                            rez = true;
                            break;
                        }
                    }
                }
            }
        }
        testEndGame();
        return rez;
    }


    @Override
    public boolean gameStarted() {
        return gameStatus == 0;
    }

    @Override
    public boolean computerStep() {
        return computerStep;
    }

    @Override
    public boolean playerWin() {
        //return false;
        //return true;
        return gameStatus == 1;
    }

    @Override
    public boolean computerWin() {
        //return false;
        //return true;
        return gameStatus == 2;
    }

    @Override
    public int[][] player() {
        return p;
    }

    @Override
    public int[][] computer() {
        return c;
    }

    //окружаем ячейку значением val
    static void okrBegin(int[][] mas, int i, int j, int val) {
        setOkr(mas, i - 1, j - 1, val);
        setOkr(mas, i - 1, j, val);
        setOkr(mas, i - 1, j + 1, val);
        setOkr(mas, i, j + 1, val);
        setOkr(mas, i + 1, j + 1, val);
        setOkr(mas, i + 1, j, val);
        setOkr(mas, i + 1, j - 1, val);
        setOkr(mas, i, j - 1, val);
    }

    //
    static void setOkr(int[][] mas, int i, int j, int val) {
        if (testMasPoz(i, j) && (mas[i][j] == 0)) {
            setMasValue(mas, i, j, val);
        }
    }

    //
    static boolean testMasPoz(int i, int j) {
        return (i >= 0) && (i <= 9) && (j >= 0) && (j <= 9);
    }

    static void setMasValue(int[][] mas, int i, int j, int val) {
        if (testMasPoz(i, j)) {
            mas[i][j] = val;
        }
    }

    // новые методы
    // 1)
    static boolean testNewPaluba(int[][] mas, int i, int j) {
        if (!testMasPoz(i, j)) {
            return false;
        }
        return (mas[i][j] == 0) || (mas[i][j] == -2);
    }

    // 2)
    static void okrEnd(int[][] mas) {
        for (int i = 0; i < F_SIZE; i++) {
            for (int j = 0; j < F_SIZE; j++) {
                if (mas[i][j] == -2) {
                    mas[i][j] = -1;
                }
            }
        }
    }

    // метод для проверки завершения игры (определение победителя)
    void testEndGame() {
        // необходимо пройти по всему игровому полю (для компьютера и игрока)
        // и получить сумму ячеек у которых код >=15
        //gameStatus  1 - игрок выиграл  2 - выиграл компьютер
        int kolComp = 0;
        // сумма ячеек для игрока
        int kolPlay = 0;
        for (int i = 0; i < F_SIZE; i++) {
            for (int j = 0; j < F_SIZE; j++) {
                if (p[i][j] >= 15) {
                    kolPlay += p[i][j];
                }
                if (c[i][j] >= 15) {
                    kolComp += c[i][j];
                }
            }
        }
        if (kolPlay == GAME_OVER_VALUE) {
            gameStatus = 2;
        } else if (kolComp == GAME_OVER_VALUE) {
            gameStatus = 1;
        }
    }
    // 1
    static void testUbit(int[][] mas, int i, int j) {
        if (mas[i][j] == 8) {
            mas[i][j] += 7;
            okrPodbit(mas, i, j);
        } else if (mas[i][j] == 9) {
            analizUbit(mas, i, j, 2);
        } else if (mas[i][j] == 10) {
            analizUbit(mas, i, j, 3);
        } else if (mas[i][j] == 11) {
            analizUbit(mas, i, j, 4);
        }
    }
    // 2 okrPodbit
    static void okrPodbit(int[][] mas, int i, int j) {
        setOkrPodbit(mas, i - 1, j - 1);
        setOkrPodbit(mas, i - 1, j);
        setOkrPodbit(mas, i - 1, j + 1);
        setOkrPodbit(mas, i, j + 1);
        setOkrPodbit(mas, i + 1, j + 1);
        setOkrPodbit(mas, i + 1, j);
        setOkrPodbit(mas, i + 1, j - 1);
        setOkrPodbit(mas, i, j - 1);
    }
    // 3 setOkrPodbit
    static void setOkrPodbit(int[][] mas, int i, int j) {
        if (testMasPoz(i, j)) {
            if ((mas[i][j] == -1) || (mas[i][j] == 6)) {
                mas[i][j] -= 1;
            }
        }
    }
    // 4 analizUbit
    static void analizUbit(int[][] mas, int i, int j, int kolPalub) {
        int kolRanen = 0;
        for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
            for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
                if ((testMasPoz(k, g)) && (mas[k][g] == kolPalub + 7)) {
                    kolRanen++;
                }
            }
        }
        if (kolRanen == kolPalub) {
            for (int k = i - (kolPalub - 1); k <= i + (kolPalub - 1); k++) {
                for (int g = j - (kolPalub - 1); g <= j + (kolPalub - 1); g++) {
                    if ((testMasPoz(k, g)) && (mas[k][g] == kolPalub + 7)) {
                        mas[k][g] += 7;
                        okrPodbit(mas, k, g);
                    }
                }
            }
        }
    }
}