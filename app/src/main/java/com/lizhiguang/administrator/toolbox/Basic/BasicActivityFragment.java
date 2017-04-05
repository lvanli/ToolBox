package com.lizhiguang.administrator.toolbox.Basic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lizhiguang.administrator.toolbox.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A placeholder fragment containing a simple view.
 */
public class BasicActivityFragment extends Fragment implements View.OnTouchListener {

    private static final String MYDEBUG = "myDebug";
    private static final String TAG = "BasicActivityFragment";
    private static final int ONE_PAGE_APP = 20;
    private static final int APP_BUFFER_SIZE = 40;
    private Context mContext;
    private RelativeLayout basicRelativeLayout;

    public BasicActivityFragment() {
        mContext = this.getActivity();
        System.out.println("****************init");
    }

    private TextView my;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        final View newView = inflater.inflate(R.layout.fragment_basic, container, false);
        basicRelativeLayout = (RelativeLayout) newView.findViewById(R.id.basicRelativeLayout);
        return newView;
    }

    static final private int MOVE_LEFT = -1;
    static final private int MOVE_RIGHT = 1;
    static final private int MOVE_INIT = 0;
    Handler moveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MOVE_LEFT) {
                Log.d(MYDEBUG, "move left");
                changeIcon(MOVE_LEFT);
            } else if (msg.what == MOVE_RIGHT) {
                Log.d(MYDEBUG, "move right");
                changeIcon(MOVE_RIGHT);
            }
            super.handleMessage(msg);
        }
    };

    GridLayout mainGrid[] = new GridLayout[2];
    BasicPermanentIcon newIcon[][] = new BasicPermanentIcon[2][ONE_PAGE_APP];
    private int page = 0;
    private int pageCount = 0;
    private int appCount = 0;
    private int textSize = 0;
    private int isFirst = 0;
    private ViewGroup.LayoutParams iconParams = null;
    private int defaultMarginHorizontal = 0, defaultMarginVertical = 0;
    private String[] appName = new String[APP_BUFFER_SIZE];
    private String[] appAction = new String[APP_BUFFER_SIZE];
    private String[] appImage = new String[APP_BUFFER_SIZE];

    private int getInformation(int begin, int end) {
        if (end - begin > APP_BUFFER_SIZE) {
            end = begin + APP_BUFFER_SIZE;
        }
        //begin read
        try {
            DocumentBuilderFactory readBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder readBuilder = readBuilderFactory.newDocumentBuilder();
            Log.d(MYDEBUG, "null?" + mContext.getAssets().open("data.xml"));
            Document readDocument = readBuilder.parse(mContext.getAssets().open("data.xml"));
            Element readElement = readDocument.getDocumentElement();
            Log.d(MYDEBUG, "null??" + readElement.getNodeName());
            NodeList readList = readElement.getElementsByTagName("information");
            Element myApplication;
            Log.d(MYDEBUG, "readL=" + readList.getLength());
            pageCount = (readList.getLength() + ONE_PAGE_APP - 1) / ONE_PAGE_APP;
            for (int i = begin; i < readList.getLength() && i < end; i++) {
                myApplication = (Element) readList.item(i);
                appName[i - begin] = myApplication.getElementsByTagName("name").item(0).getTextContent();
                appAction[i - begin] = myApplication.getElementsByTagName("action").item(0).getTextContent();
                appImage[i - begin] = myApplication.getElementsByTagName("image").item(0).getTextContent();
            }
            if (readList.getLength() < end)
                return readList.getLength() - begin;
            else
                return end - begin;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
        //end read
    }

    private void changeIcon(int move) {
        if (move == MOVE_INIT) {
            mainGrid[0].setColumnCount(4);
            mainGrid[1].setColumnCount(4);
            textSize = getResources().getInteger(R.integer.text_size);
            int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            Log.d(MYDEBUG, "width=" + width);
            defaultMarginHorizontal = (int) getResources().getDimension(R.dimen.default_margin_horizontal);
            defaultMarginVertical = (int) getResources().getDimension(R.dimen.default_margin_vertical);
            int iconWidth = (width) / 4;
            Log.d(MYDEBUG, "iconWidth=" + iconWidth);
            Paint paint = new Paint();
            paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            paint.setTextSize(textSize);
            Rect textRect = new Rect();
            paint.getTextBounds("JjfFYy", 0, "JjfFYy".length(), textRect);
            Log.d(MYDEBUG, "arheight=" + textRect.height());
            Log.d(MYDEBUG, "iconheight=" + iconWidth);
            iconParams = new ViewGroup.LayoutParams(iconWidth, iconWidth + textRect.height() - defaultMarginHorizontal * 2 + defaultMarginVertical);
            mainGrid[0].setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
            mainGrid[1].setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
            appCount = getInformation(0, APP_BUFFER_SIZE);
            Log.d(MYDEBUG, "appC=" + appCount);
            if (appCount == 0)
                return;
            if (iconParams != null) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < ONE_PAGE_APP; j++) {
                        newIcon[i][j] = new BasicPermanentIcon(mContext);
                        newIcon[i][j].setLayoutParams(iconParams);
                        newIcon[i][j].setTextSize(textSize);
                        newIcon[i][j].setPadding(defaultMarginHorizontal, defaultMarginVertical, defaultMarginHorizontal, defaultMarginVertical);
                        newIcon[i][j].setOnTouchListener(this);
                        mainGrid[i].addView(newIcon[i][j]);
                    }
                }
            }
            for (int i = 0; i < appCount && i < ONE_PAGE_APP; i++) {
                newIcon[0][i].setImgPath(appImage[i]);
                newIcon[0][i].setDescribe(appName[i]);
                newIcon[0][i].setTag(i);
            }
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(1000);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(alphaAnimation, 1000);
            layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
            basicRelativeLayout.setLayoutAnimation(layoutAnimationController);

        } else if (move == MOVE_LEFT || move == MOVE_RIGHT) {
            page = (page + move + pageCount) % pageCount;
            isFirst = 1 - isFirst;
            int appCountInOnePage = getInformation(page * ONE_PAGE_APP, (page + 1) * ONE_PAGE_APP);
            int i = 0;
            for (i = 0; i < appCountInOnePage; i++) {
                newIcon[isFirst][i].setImgPath(appImage[i]);
                newIcon[isFirst][i].setDescribe(appName[i]);
                newIcon[isFirst][i].setTag(i);
            }
            for (; i < ONE_PAGE_APP; i++) {
                newIcon[isFirst][i].setImgPath(null);
                newIcon[isFirst][i].setDescribe(null);
                newIcon[isFirst][i].setTag(-1);
            }
        }

        basicRelativeLayout.removeAllViews();
        basicRelativeLayout.addView(mainGrid[isFirst]);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainGrid[0] = new GridLayout(mContext);
        mainGrid[1] = new GridLayout(mContext);
        mainGrid[0].setOnTouchListener(this);
        mainGrid[1].setOnTouchListener(this);
        changeIcon(MOVE_INIT);
    }

    private float downX, downY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        Log.d(MYDEBUG,"Event="+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d(MYDEBUG, "iconX3=" + event.getX() + " iconY3=" + event.getY() + "!");
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getX() - downX) < 10 && Math.abs(event.getY() - downY) < 10) {
                    if (v.getTag() != null && (int) v.getTag() >= 0)
                        startActivity(new Intent(appAction[(int) v.getTag()]));
                } else if (downX - event.getX() > 50) {
                    Message message = new Message();
                    message.what = MOVE_RIGHT;
                    moveHandler.sendMessage(message);
                    Log.d(MYDEBUG, "x1=" + event.getX() + " y1=" + event.getY() + "!");
                } else if (event.getX() - downX > 50) {
                    Message message = new Message();
                    message.what = MOVE_LEFT;
                    moveHandler.sendMessage(message);
                    Log.d(MYDEBUG, "x1=" + event.getX() + " y1=" + event.getY() + "!");
                }
                break;
        }
        return true;
    }
}
