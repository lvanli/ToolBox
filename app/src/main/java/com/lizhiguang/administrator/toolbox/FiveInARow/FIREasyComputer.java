package com.lizhiguang.administrator.toolbox.FiveInARow;

/**
 * Created by lizhiguang on 16/7/3.
 */
public class FIREasyComputer {
    public static int getPoint(short map[][], int maxLine, int mycode) {
        int i = 0, j = 0, m = 0, n = 0, a = 0, h = 0, w = 0, linshi = 0;
        int value[][];
        long maxvalue = 0;
        char fourline[][];
        boolean isyou[], havenull[];
        int linnumber[][];
        int me, you;
        int maxlength = 0, length = 0;

        value = new int[maxLine][maxLine];
        fourline = new char[4][9];
        isyou = new boolean[8];
        havenull = new boolean[8];
        linnumber = new int[4][8];

        if (mycode == FIRManager.BLACK) {
            me = FIRManager.BLACK;
            you = FIRManager.WRITE;
        } else {
            you = FIRManager.WRITE;
            me = FIRManager.BLACK;
        }
        for (i = 0; i < maxLine; i++)
            for (j = 0; j < maxLine; j++) {
                value[i][j] = (Math.abs(Math.abs(i - maxLine / 2) - maxLine / 2) +
                        Math.abs(Math.abs(j - maxLine / 2) - maxLine / 2)) / 2;
                for (n = 0; n < 4; n++)
                    for (m = 0; m < 8; m++)
                        linnumber[n][m] = 0;
                if (map[i][j] == 0) {
                    for (a = 0; a < 2; a++) {
                        if (a == 1) {
                            n = me;
                            me = you;
                            you = n;
                        }
                        for (n = 0; n < 8; n++) {
                            isyou[n] = false;
                            havenull[n] = false;
                        }
                        for (n = 0; n < 4; n++)
                            for (m = 0; m < 9; m++)
                                fourline[n][m] = 'a';
                        linshi = me + 1;
                        for (n = 0; n < 5; n++) {
                            if (!isyou[0] && j - n >= 0)
                                if (map[i][j - n] != you || map[i][j - n] == 0) {
                                    if (map[i][j - n] == 0 && n != 0) havenull[0] = true;
                                    if (map[i][j - n] == me && map[i][j - n] != 0)
                                        fourline[0][4 - n] = (char) (linshi + '0');
                                    else
                                        fourline[0][4 - n] = '0';
                                } else
                                    isyou[0] = true;
                            if (!isyou[1] && i - n >= 0 && j - n >= 0)
                                if (map[i - n][j - n] != you || map[i - n][j - n] == 0) {
                                    if (map[i - n][j - n] == 0 && n != 0) havenull[1] = true;
                                    if (map[i - n][j - n] == me && map[i - n][j - n] != 0)
                                        fourline[1][4 - n] = (char) (linshi + '0');
                                    else
                                        fourline[1][4 - n] = '0';
                                } else
                                    isyou[1] = true;
                            if (!isyou[2] && i - n >= 0)
                                if (map[i - n][j] != you || map[i - n][j] == 0) {
                                    if (map[i - n][j] == 0 && n != 0) havenull[2] = true;
                                    if (map[i - n][j] == me && map[i - n][j] != 0)
                                        fourline[2][4 - n] = (char) (linshi + '0');
                                    else
                                        fourline[2][4 - n] = '0';
                                } else
                                    isyou[2] = true;
                            if (!isyou[3] && i - n >= 0 && j + n < maxLine)
                                if (map[i - n][j + n] != you || map[i - n][j + n] == 0) {
                                    if (map[i - n][j + n] == 0 && n != 0) havenull[3] = true;
                                    if (map[i - n][j + n] == me && map[i - n][j + n] != 0)
                                        fourline[3][4 - n] = (char) (linshi + '0');
                                    else
                                        fourline[3][4 - n] = '0';
                                } else
                                    isyou[3] = true;
                            if (!isyou[4] && j + n < maxLine)
                                if (map[i][j + n] != you || map[i][j + n] == 0) {
                                    if (map[i][j + n] == 0 && n != 0) havenull[4] = true;
                                    if (map[i][j + n] == me && map[i][j + n] != 0)
                                        fourline[0][4 + n] = (char) (linshi + '0');
                                    else
                                        fourline[0][4 + n] = '0';
                                } else
                                    isyou[4] = true;
                            if (!isyou[5] && j + n < maxLine && i + n < maxLine)
                                if (map[i + n][j + n] != you || map[i + n][j + n] == 0) {
                                    if (map[i + n][j + n] == 0 && n != 0) havenull[5] = true;
                                    if (map[i + n][j + n] == me && map[i + n][j + n] != 0)
                                        fourline[1][4 + n] = (char) (linshi + '0');
                                    else
                                        fourline[1][4 + n] = '0';
                                } else
                                    isyou[5] = true;
                            if (!isyou[6] && i + n < maxLine)
                                if (map[i + n][j] != you || map[i + n][j] == 0) {
                                    if (map[i + n][j] == 0 && n != 0) havenull[6] = true;
                                    if (map[i + n][j] == me && map[i + n][j] != 0)
                                        fourline[2][4 + n] = (char) (linshi + '0');
                                    else
                                        fourline[2][4 + n] = '0';
                                } else
                                    isyou[6] = true;
                            if (!isyou[7] && i + n < maxLine && j - n >= 0)
                                if (map[i + n][j - n] != you || map[i + n][j - n] == 0) {
                                    if (map[i + n][j - n] == 0 && n != 0) havenull[7] = true;
                                    if (map[i + n][j - n] == me && map[i + n][j - n] != 0)
                                        fourline[3][4 + n] = (char) (linshi + '0');
                                    else
                                        fourline[3][4 + n] = '0';
                                } else
                                    isyou[7] = true;
                        }
                        for (n = 0; n < 4; n++) {
                            fourline[n][4] = (char) (linshi + '0');
                            for (m = 0; m < 9; m++) {
                                if (fourline[n][m] == linshi + '0') {
                                    length++;
                                    if (maxlength < length)
                                        maxlength = length;
                                } else
                                    length = 0;
                            }
                            if (maxlength >= 5)
                                linnumber[me][0]++;
                            else if (maxlength == 4 && (havenull[n] && havenull[n + 4]))
                                linnumber[me][1]++;
                            else if (maxlength == 4 && (havenull[n] || havenull[n + 4]))
                                linnumber[me][2]++;
                            else if (maxlength == 3 && (havenull[n] && havenull[n + 4]))
                                linnumber[me][3]++;
                            else if (maxlength == 3 && (havenull[n] || havenull[n + 4]))
                                linnumber[me][4]++;
                            else if (maxlength == 2 && (havenull[n] && havenull[n + 4]))
                                linnumber[me][5]++;
                            else if (maxlength == 2 && (havenull[n] || havenull[n + 4]))
                                linnumber[me][6]++;
                            maxlength = 0;
                            length = 0;
                        }
                        while (linnumber[me][2] >= 2) {
                            linnumber[me][1]++;
                            linnumber[me][2] -= 2;
                        }
                        while (linnumber[me][3] >= 2) {
                            linnumber[me][7]++;
                            linnumber[me][3] -= 2;
                        }
                        while (linnumber[me][3] == 1 && linnumber[me][2] == 1) {
                            linnumber[me][1]++;
                            linnumber[me][3] -= 1;
                            linnumber[me][2] -= 1;
                        }
                    }
                    //zhengfan
                } else
                    value[i][j] = 0;
                if (mycode == FIRManager.BLACK) {
                    me = FIRManager.BLACK;
                    you = FIRManager.WRITE;
                } else {
                    you = FIRManager.BLACK;
                    me = FIRManager.WRITE;
                }
                value[i][j] = value[i][j]
                        + 100000 * linnumber[me][0]
                        + 10000 * linnumber[me][1]
                        + 5000 * linnumber[me][7]
                        + 500 * linnumber[me][2]
                        + 350 * linnumber[me][3]
                        + 50 * linnumber[me][4]
                        + 5 * linnumber[me][5]
                        + 3 * linnumber[me][6]
                        + 75000 * linnumber[you][0]
                        + 8000 * linnumber[you][1]
                        + 3500 * linnumber[you][7]
                        + 480 * linnumber[you][2]
                        + 320 * linnumber[you][3]
                        + 40 * linnumber[you][4]
                        + 4 * linnumber[you][5]
                        + 2 * linnumber[you][6];
                if (maxvalue < value[i][j]) {
                    maxvalue = value[i][j];
                    h = i;
                    w = j;
                }
            }

        return h * maxLine + w;
    }
}
