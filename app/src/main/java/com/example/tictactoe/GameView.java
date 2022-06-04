package com.example.tictactoe;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    public static final String TAG_ON_TOUCH_X = "tagOnTouchX";
    public static final String TAG_ON_TOUCH_Y = "tagOnTouchY";

    public static final int LINE_ROW_1 = 0;
    public static final int LINE_ROW_2 = 1;
    public static final int LINE_ROW_3 = 2;
    public static final int LINE_ROW_4 = 10;
    public static final int LINE_ROW_5 = 11;
    public static final int LINE_COLUMN_1 = 3;
    public static final int LINE_COLUMN_2 = 4;
    public static final int LINE_COLUMN_3 = 5;
    public static final int LINE_COLUMN_4 = 8;
    public static final int LINE_COLUMN_5 = 9;
    public static final int LINE_LEFT_D = 6;
    public static final int LINE_RIGHT_D = 7;

    public static final int PIECE_NONE = 0;
    public static final int PIECE_BLUE = 1;
    public static final int PIECE_RED = 2;

    public static final int TYPE_3X3 = 3;
    public static final int TYPE_4X4 = 4;
    public static final int TYPE_5X5 = 5;

    public float mDivision,screenSize;
    public int[][] boardState;
    public boolean[] hvLines;
    private Handler mHandler;
    private int PtrType = TYPE_3X3;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        boardState = new int[][]{
                {PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE},
                {PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE},
                {PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE},
                {PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE},
                {PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE,PIECE_NONE}
        };

        hvLines = new boolean[]{
                false,false,false,false,false,false,
                false,false,false,false,false,false
        };
    }

    public void onDraw(Canvas canvas){
        findMDivision();
        drawBoard(canvas);

        for (int i = 0; i < PtrType; i++) {
            for (int j = 0; j < PtrType; j++) {
                if (boardState[i][j] == PIECE_BLUE) {
                    drawBlueCross(canvas, i, j);
                }
                else if (boardState[i][j] == PIECE_RED) {
                    drawRedCircle(canvas, i, j);
                }
            }
        }

        if (hvLines[LINE_ROW_1]) drawWinLine(canvas, LINE_ROW_1, boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_ROW_2]) drawWinLine(canvas, LINE_ROW_2, boardState[0][1] == PIECE_BLUE);
        if (hvLines[LINE_ROW_3]) drawWinLine(canvas, LINE_ROW_3, boardState[0][2] == PIECE_BLUE);
        if (hvLines[LINE_ROW_4]) drawWinLine(canvas, LINE_ROW_4, boardState[0][3] == PIECE_BLUE);
        if (hvLines[LINE_ROW_5]) drawWinLine(canvas, LINE_ROW_5, boardState[0][4] == PIECE_BLUE);

        if (hvLines[LINE_COLUMN_1]) drawWinLine(canvas, LINE_COLUMN_1, boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_COLUMN_2]) drawWinLine(canvas, LINE_COLUMN_2, boardState[1][0] == PIECE_BLUE);
        if (hvLines[LINE_COLUMN_3]) drawWinLine(canvas, LINE_COLUMN_3, boardState[2][0] == PIECE_BLUE);
        if (hvLines[LINE_COLUMN_4]) drawWinLine(canvas, LINE_COLUMN_4, boardState[3][0] == PIECE_BLUE);
        if (hvLines[LINE_COLUMN_5]) drawWinLine(canvas, LINE_COLUMN_5, boardState[4][0] == PIECE_BLUE);

        if (hvLines[LINE_LEFT_D]) drawWinLine(canvas, LINE_LEFT_D, boardState[0][0] == PIECE_BLUE);
        if (hvLines[LINE_RIGHT_D]) drawWinLine(canvas, LINE_RIGHT_D, boardState[PtrType-1][0] == PIECE_BLUE);
    }

    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        screenSize = ((width < height)? width : height);
    }

    public void findMDivision() {
        mDivision = screenSize/(2*PtrType+2);
    }

    private void drawBoard(Canvas canvas){
        if(canvas == null){
            return;
        }
        canvas.drawColor(Color.BLACK);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5.0f);

        if(PtrType == TYPE_3X3){
            canvas.drawLine(mDivision*1,mDivision*3,mDivision*7,mDivision*3,paint);
            canvas.drawLine(mDivision*1,mDivision*5,mDivision*7,mDivision*5,paint);
            canvas.drawLine(mDivision*3,mDivision*1,mDivision*3,mDivision*7,paint);
            canvas.drawLine(mDivision*5,mDivision*1,mDivision*5,mDivision*7,paint);
        }else if(PtrType == TYPE_4X4){
            canvas.drawLine(mDivision*1,mDivision*3,mDivision*9,mDivision*3,paint);
            canvas.drawLine(mDivision*1,mDivision*5,mDivision*9,mDivision*5,paint);
            canvas.drawLine(mDivision*1,mDivision*7,mDivision*9,mDivision*7,paint);
            canvas.drawLine(mDivision*3,mDivision*1,mDivision*3,mDivision*9,paint);
            canvas.drawLine(mDivision*5,mDivision*1,mDivision*5,mDivision*9,paint);
            canvas.drawLine(mDivision*7,mDivision*1,mDivision*7,mDivision*9,paint);
        }else if(PtrType == TYPE_5X5){
            canvas.drawLine(mDivision*1,mDivision*3,mDivision*11,mDivision*3,paint);
            canvas.drawLine(mDivision*1,mDivision*5,mDivision*11,mDivision*5,paint);
            canvas.drawLine(mDivision*1,mDivision*7,mDivision*11,mDivision*7,paint);
            canvas.drawLine(mDivision*1,mDivision*9,mDivision*11,mDivision*9,paint);
            canvas.drawLine(mDivision*3,mDivision*1,mDivision*3,mDivision*11,paint);
            canvas.drawLine(mDivision*5,mDivision*1,mDivision*5,mDivision*11,paint);
            canvas.drawLine(mDivision*7,mDivision*1,mDivision*7,mDivision*11,paint);
            canvas.drawLine(mDivision*9,mDivision*1,mDivision*9,mDivision*11,paint);
        }

    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        //添加用于显示XO的
        if (mHandler == null || motionEvent.getAction() != MotionEvent.ACTION_DOWN)
            return false;
        int ptrCount = motionEvent.getPointerCount();
        for (int i = 0; i < ptrCount; i++) {
            if (PtrType == TYPE_3X3) {
                float tmpX = motionEvent.getX(i);
                float tmpY = motionEvent.getY(i);
                if (tmpX > mDivision && tmpX < mDivision * 7 && tmpY > mDivision && tmpY < mDivision * 7) {
                    int posX = 0;
                    int posY = 0;
                    if (tmpX > mDivision * 5) {
                        posX = 2;
                    } else if (tmpX > mDivision * 3) {
                        posX = 1;
                    }
                    if (tmpY > mDivision * 5) {
                        posY = 2;
                    } else if (tmpY > mDivision * 3) {
                        posY = 1;
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TAG_ON_TOUCH_X, posX);
                    bundle.putInt(TAG_ON_TOUCH_Y, posY);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            } else if (PtrType == TYPE_4X4) {
                float tmpX = motionEvent.getX(i);
                float tmpY = motionEvent.getY(i);
                if (tmpX > mDivision && tmpX < mDivision * 9 && tmpY > mDivision && tmpY < mDivision * 9) {
                    int posX = 0;
                    int posY = 0;
                    if (tmpX > mDivision * 7) {
                        posX = 3;
                    } else if (tmpX > mDivision * 5) {
                        posX = 2;
                    } else if (tmpX > mDivision * 3) {
                        posX = 1;
                    }
                    if (tmpY > mDivision * 7) {
                        posY = 3;
                    } else if (tmpY > mDivision * 5) {
                        posY = 2;
                    } else if (tmpY > mDivision * 3) {
                        posY = 1;
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TAG_ON_TOUCH_X, posX);
                    bundle.putInt(TAG_ON_TOUCH_Y, posY);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            } else if (PtrType == TYPE_5X5) {
                float tmpX = motionEvent.getX(i);
                float tmpY = motionEvent.getY(i);
                if (tmpX > mDivision && tmpX < mDivision * 11 && tmpY > mDivision && tmpY < mDivision * 11) {
                    int posX = 0;
                    int posY = 0;

                    if (tmpX > mDivision * 9) {
                        posX = 4;
                    } else if (tmpX > mDivision * 7) {
                        posX = 3;
                    } else if (tmpX > mDivision * 5) {
                        posX = 2;
                    } else if (tmpX > mDivision * 3) {
                        posX = 1;
                    }

                    if (tmpY > mDivision * 9) {
                        posY = 4;
                    } else if (tmpY > mDivision * 7) {
                        posY = 3;
                    } else if (tmpY > mDivision * 5) {
                        posY = 2;
                    } else if (tmpY > mDivision * 3) {
                        posY = 1;
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putInt(TAG_ON_TOUCH_X, posX);
                    bundle.putInt(TAG_ON_TOUCH_Y, posY);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
        }
        return true;
    }

    //画红圈
    private void drawRedCircle(Canvas canvas, int posX, int posY) {
        if (canvas == null)
            return;
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5.0f);

        canvas.drawCircle(mDivision * (posX * 2 + 2), mDivision * (posY * 2 + 2), mDivision - 10, paint);
    }

    //画蓝线
    private void drawBlueCross(Canvas canvas, int posX, int posY) {
        if (canvas == null)
            return;
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5.0f);
        canvas.drawLine(mDivision * (posX * 2 + 1) + 10, mDivision * (posY * 2 + 1) + 10.0f, mDivision * (posX * 2 + 3) - 10, mDivision *
                (posY * 2 + 3) - 10.0f, paint);
        canvas.drawLine(mDivision * (posX * 2 + 3) - 10, mDivision * (posY * 2 + 1) + 10.0f, mDivision * (posX * 2 + 1) + 10, mDivision *
                (posY * 2 + 3) - 10.0f, paint);
    }

    //画赢家的连线
    private void drawWinLine(Canvas canvas, int line, boolean blue) {
        if (canvas == null)
            return;
        Paint paint = new Paint();
        paint.setColor((blue)? Color.BLUE : Color.RED);
        paint.setStrokeWidth(10.0f);

        switch (line) {
            case LINE_ROW_1 :
                canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 2*PtrType, mDivision * 2, paint);
                break;
            case LINE_ROW_2 :
                canvas.drawLine(mDivision * 2, mDivision * 4, mDivision * 2*PtrType, mDivision * 4, paint);
                break;
            case LINE_ROW_3 :
                canvas.drawLine(mDivision * 2, mDivision * 6, mDivision * 2*PtrType, mDivision * 6, paint);
                break;
            case LINE_COLUMN_1 :
                canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 2, mDivision * 2*PtrType, paint);
                break;
            case LINE_COLUMN_2 :
                canvas.drawLine(mDivision * 4, mDivision * 2, mDivision * 4, mDivision * 2*PtrType, paint);
                break;
            case LINE_COLUMN_3 :
                canvas.drawLine(mDivision * 6, mDivision * 2, mDivision * 6, mDivision * 2*PtrType, paint);
                break;
            case LINE_LEFT_D :
                canvas.drawLine(mDivision * 2, mDivision * 2, mDivision * 2*PtrType, mDivision * 2*PtrType, paint);
                break;
            case LINE_RIGHT_D :
                canvas.drawLine(mDivision * 2, mDivision * 2*PtrType, mDivision * 2*PtrType, mDivision * 2, paint);
                break;

            case LINE_ROW_4 :
                canvas.drawLine(mDivision * 2, mDivision * 8, mDivision * 2*PtrType, mDivision * 8, paint);
                break;
            case LINE_ROW_5 :
                canvas.drawLine(mDivision * 2, mDivision * 10, mDivision * 2*PtrType, mDivision * 10, paint);;
                break;
            case LINE_COLUMN_4 :
                canvas.drawLine(mDivision * 8, mDivision * 2, mDivision * 8, mDivision * 2*PtrType, paint);
                break;
            case LINE_COLUMN_5 :
                canvas.drawLine(mDivision * 10, mDivision * 2, mDivision * 10, mDivision * 2*PtrType, paint);
                break;
        }
    }

    //重置
    public void cleanAll() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardState[i][j] = PIECE_NONE;
            }
        }
        for (int i = 0; i < 12; i++) {
            hvLines[i] = false;
        }
    }

    public void setBlueCross(int posX, int posY) {
        boardState[posX][posY] = PIECE_BLUE;
    }

    public void setRedCircle(int posX, int posY) {
        boardState[posX][posY] = PIECE_RED;
    }

    public void setPtrType(int ptrType){
        PtrType = ptrType;
    }

    public int getPtrType(){
        return PtrType;
    }

    public void setWinLine(int line) {
        if (line < 0 || line >= 12)
            return;
        hvLines[line] = true;
    }

    public void setHandler(Handler handler){
        mHandler = handler;
    }
}
