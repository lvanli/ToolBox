package com.lizhiguang.administrator.toolbox.FiveInARow;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by lizhiguang on 16/6/30.
 */
public class FIRManager {
    private static final String TAG = "myDebug";

    public void setMap(short[][] map) {
        this.map = map;
    }

    public short[] getsMap() {
        short[] sMap = new short[mMaxLine * mMaxLine];
        for (int i = 0; i < mMaxLine; i++)
            for (int j = 0; j < mMaxLine; j++) {
                sMap[i * mMaxLine + j] = map[i][j];
            }
        return sMap;
    }

    public void setMap(short[] sMap) {
        for (int i = 0; i < mMaxLine; i++) {
            for (int j = 0; j < mMaxLine; j++) {
                map[i][j] = sMap[i * mMaxLine + j];
            }
        }
    }

    short map[][];
    int mMaxLine;
    int mGameMode;
    public static int BLACK = 1;
    public static int WRITE = 2;
    FIRMapView mMapView;

    public boolean isFirst() {
        return mIsFirst;
    }

    public void setIsFirst(boolean mIsFirst) {
        this.mIsFirst = mIsFirst;
    }

    boolean mIsFirst;

    public OnWinListener getWinListener() {
        return winListener;
    }

    public void setWinListener(OnWinListener winListener) {
        this.winListener = winListener;
    }

    private OnWinListener winListener;

    interface OnWinListener {
        void onWin(int who);
    }

    public FIRMapView getMapView() {
        return mMapView;
    }

    public short[][] getMap() {
        return map;
    }

    public void setMapView(FIRMapView mMapView) {
        this.mMapView = mMapView;
    }

    public int getGameMode() {
        return mGameMode;
    }

    public void setGameMode(int mGameMode) {
        this.mGameMode = mGameMode;
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public void setMaxLine(int mMaxLine) {
        this.mMaxLine = mMaxLine;
    }

    public FIRManager(int maxLine) {
        mMaxLine = maxLine;
        mIsFirst = false;
        initMap(maxLine);
    }

    public FIRManager() {
        mIsFirst = false;
    }

    public void initMap(int maxLine) {
        map = new short[maxLine][maxLine];
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                map[i][j] = 0;
            }
        }
    }

    public void setWhoFirst(int who) {
        if (who != BLACK)
            mIsFirst = true;
    }

    public void afterDraw() {
        if (mIsFirst)
            makeComputerPoint(BLACK);
    }

    public int getComputerPoint(int who) {
        return FIREasyComputer.getPoint(map, mMaxLine, who);
    }

    public void makeComputerPoint(int who) {
        mIsFirst = false;
        int point = getComputerPoint(who);
        map[point / mMaxLine][point % mMaxLine] = (short) (who);
        if (checkWin(point / mMaxLine, point % mMaxLine, (short) (who)))
            winListener.onWin(who);
        mMapView.invalidate();
    }

    public boolean makePoint(int x, int y, short who) {
        if (map[x][y] == 0) {
            mIsFirst = false;
            map[x][y] = who;
            mMapView.invalidate();
            if (checkWin(x, y, who) == true)
                winListener.onWin(who);
            else {
                if (mGameMode == 0) {
                    makeComputerPoint(BLACK + WRITE - who);
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkWin(int x, int y, short who) {
        int i;
        int xCount = 0, yCount = 0, lCount = 0, rCount = 0;
        boolean notOver[] = new boolean[]{true, true, true, true, true, true, true, true};
        for (i = 1; i < 5; i++) {
            if (x + i < mMaxLine) {
                if (notOver[0] && map[x + i][y] == who) xCount++;
                else notOver[0] = false;
                if (y + i < mMaxLine) {
                    if (notOver[1] && map[x + i][y + i] == who) lCount++;
                    else notOver[1] = false;
                }
                if (y - i >= 0) {
                    if (notOver[2] && map[x + i][y - i] == who) rCount++;
                    else notOver[2] = false;
                }
            }
            if (x - i >= 0) {
                if (notOver[3] && map[x - i][y] == who) xCount++;
                else notOver[3] = false;
                if (y + i < mMaxLine) {
                    if (notOver[4] && map[x - i][y + i] == who) rCount++;
                    else notOver[4] = false;
                }
                if (y - i >= 0) {
                    if (notOver[5] && map[x - i][y - i] == who) lCount++;
                    else notOver[5] = false;
                }
            }
            if (notOver[6] && y + i < mMaxLine && map[x][y + i] == who) yCount++;
            else notOver[6] = false;
            if (notOver[7] && y - i >= 0 && map[x][y - i] == who) yCount++;
            else notOver[7] = false;
        }
        Log.d(TAG, "x=" + xCount + ",y=" + yCount + ",l=" + lCount + ",r=" + rCount);
        if (xCount >= 4 || yCount >= 4 || lCount >= 4 || rCount >= 4)
            return true;
        return false;
    }
}
