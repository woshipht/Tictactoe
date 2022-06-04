package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final int PIECE_NONE = 0;
    public static final int PIECE_BLUE = 1;
    public static final int PIECE_RED = 2;

    public static final int STATE_NOT_START = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_BLUE_WIN = 2;
    public static final int STATE_RED_WIN = 3;
    public static final int STATE_DRAW_GAME = 4;

    public static final String TAG_GAME_STATE = "tagGameState";
    public static final String TAG_IS_BLUE_TURN = "tagIsBlueTurn";
    public static final String TAG_LINE_1 = "tagLine1";
    public static final String TAG_LINE_2 = "tagLine2";
    public static final String TAG_LINE_3 = "tagLine3";
    public static final String TAG_LINE_4 = "tagLine4";
    public static final String TAG_LINE_5 = "tagLine5";
    public static final String TAG_WIN_LINE = "tagWinLine";
    public static final String TAG_GAME_CHANGE = "tagGameChange";

    public static final int TYPE_3X3 = 3;
    public static final int TYPE_4X4 = 4;
    public static final int TYPE_5X5 = 5;

    private GameView mGameView;
    private Button btnStart;

    public int[][] boardState = new int[5][5];
    public boolean[] hvWinLine = new boolean[12];
    public boolean isBlueTurn = true;
    public int gameState = STATE_NOT_START;
    public int PtrType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.wait_start);

        mGameView = findViewById(R.id.mGameView);
        mGameView.setHandler(new Handler(){
            public void handleMessage(Message msg){
                if (gameState != STATE_PLAYING)
                    return;
                int posX = msg.getData().getInt(GameView.TAG_ON_TOUCH_X);
                int posY = msg.getData().getInt(GameView.TAG_ON_TOUCH_Y);
                inputPiece(posX, posY);
                mGameView.invalidate();
            }
        });

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gameState = STATE_PLAYING;
                if (isBlueTurn)
                    setTitle(R.string.turn_blue);
                else
                    setTitle(R.string.turn_red);
                btnStart.setVisibility(View.INVISIBLE);

                cleanAll();
                mGameView.cleanAll();
                mGameView.invalidate();
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_GAME_STATE, gameState);
        outState.putBoolean(TAG_IS_BLUE_TURN, isBlueTurn);
        outState.putIntArray(TAG_LINE_1, boardState[0]);
        outState.putIntArray(TAG_LINE_2, boardState[1]);
        outState.putIntArray(TAG_LINE_3, boardState[2]);
        outState.putIntArray(TAG_LINE_4, boardState[3]);
        outState.putIntArray(TAG_LINE_5, boardState[4]);
        outState.putBooleanArray(TAG_WIN_LINE, hvWinLine);
        outState.putInt(TAG_GAME_CHANGE,PtrType);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose3x3:
                changeGamePtr(TYPE_3X3);
                return true;
            case R.id.choose4x4:
                changeGamePtr(TYPE_4X4);
                return true;
            case R.id.choose5x5:
                changeGamePtr(TYPE_5X5);
                return true;
        }
        return false;
    }

    public void changeGamePtr(int ptrType) {
        mGameView.setPtrType(ptrType);
        PtrType = ptrType;
        gameState = STATE_NOT_START;
        btnStart.setVisibility(View.VISIBLE);
        setTitle(R.string.wait_start);
        cleanAll();
        mGameView.cleanAll();
        mGameView.invalidate();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        gameState = savedInstanceState.getInt(TAG_GAME_STATE, STATE_NOT_START);
        isBlueTurn = savedInstanceState.getBoolean(TAG_IS_BLUE_TURN, true);
        boardState[0] = savedInstanceState.getIntArray(TAG_LINE_1);
        boardState[1] = savedInstanceState.getIntArray(TAG_LINE_2);
        boardState[2] = savedInstanceState.getIntArray(TAG_LINE_3);
        boardState[3] = savedInstanceState.getIntArray(TAG_LINE_4);
        boardState[4] = savedInstanceState.getIntArray(TAG_LINE_5);
        hvWinLine = savedInstanceState.getBooleanArray(TAG_WIN_LINE);
        PtrType = savedInstanceState.getInt(TAG_GAME_CHANGE);

        if (gameState == STATE_PLAYING) {
            btnStart.setVisibility(View.INVISIBLE);
            if (isBlueTurn)
                setTitle(R.string.turn_blue);
            else
                setTitle(R.string.turn_red);
        }
        else {
            btnStart.setVisibility(View.VISIBLE);
            switch (gameState) {
                case STATE_NOT_START : setTitle(R.string.wait_start); break;
                case STATE_BLUE_WIN : setTitle(R.string.win_blue); break;
                case STATE_RED_WIN : setTitle(R.string.win_red); break;
                case STATE_DRAW_GAME : setTitle(R.string.draw_game); break;
            }
        }
        if(PtrType == TYPE_3X3){
            mGameView.setPtrType(TYPE_3X3);
        }
        if(PtrType == TYPE_4X4){
            mGameView.setPtrType(TYPE_4X4);
        }
        if(PtrType == TYPE_5X5){
            mGameView.setPtrType(TYPE_5X5);
        }

        mGameView.cleanAll();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (boardState[i][j] == PIECE_BLUE)
                    mGameView.setBlueCross(i, j);
                else if (boardState[i][j] == PIECE_RED)
                    mGameView.setRedCircle(i, j);
            }
        }

        for (int i = 0; i < 12; i++)
            if (hvWinLine[i])
                mGameView.setWinLine(i);
        mGameView.invalidate();
    }

    private void inputPiece(int posX, int posY) {
        if (boardState[posX][posY] != PIECE_NONE)
            return;

        if (isBlueTurn) {
            boardState[posX][posY] = PIECE_BLUE;
            mGameView.setBlueCross(posX, posY);
            isBlueTurn = false;
            setTitle(R.string.turn_red);
        }
        else {
            boardState[posX][posY] = PIECE_RED;
            mGameView.setRedCircle(posX, posY);
            isBlueTurn = true;
            setTitle(R.string.turn_blue);
        }

        if(checkWinnerIsBlue(mGameView.getPtrType())){
            gameState = STATE_BLUE_WIN;
            setTitle(R.string.win_blue);
            btnStart.setVisibility(View.VISIBLE);
        }

        if(checkWinnerIsRed(mGameView.getPtrType())){
            gameState = STATE_RED_WIN;
            setTitle(R.string.win_red);
            btnStart.setVisibility(View.VISIBLE);
        }

        if(checkDrawGameChance(mGameView.getPtrType()) && !checkWinnerIsRed(mGameView.getPtrType()) && !checkWinnerIsBlue(mGameView.getPtrType())){
            gameState = STATE_DRAW_GAME;
            setTitle(R.string.draw_game);
            btnStart.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkWinnerIsBlue(int ptrType){
        int checkColumn1 = 0;
        int checkColumn2 = 0;
        int checkColumn3 = 0;
        int checkColumn4 = 0;
        int checkColumn5 = 0;
        int checkRow1 = 0;
        int checkRow2 = 0;
        int checkRow3 = 0;
        int checkRow4 = 0;
        int checkRow5 = 0;
        int checkLeft = 0;
        int checkRight = 0;

        for(int i=0; i<ptrType; i++){
            if(boardState[0][i] == PIECE_BLUE){ checkColumn1++; }
            if(boardState[1][i] == PIECE_BLUE){ checkColumn2++; }
            if(boardState[2][i] == PIECE_BLUE){ checkColumn3++; }
            if(boardState[3][i] == PIECE_BLUE){ checkColumn4++; }
            if(boardState[4][i] == PIECE_BLUE){ checkColumn5++; }
            if(boardState[i][0] == PIECE_BLUE){ checkRow1++; }
            if(boardState[i][1] == PIECE_BLUE){ checkRow2++; }
            if(boardState[i][2] == PIECE_BLUE){ checkRow3++; }
            if(boardState[i][3] == PIECE_BLUE){ checkRow4++; }
            if(boardState[i][4] == PIECE_BLUE){ checkRow5++; }
            if(boardState[i][i] == PIECE_BLUE){ checkLeft++; }
            if(boardState[i][ptrType-1-i] == PIECE_BLUE){ checkRight++; }
        }

        if(checkColumn1 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_1);
            hvWinLine[GameView.LINE_COLUMN_1] = true;
            return true;
        }
        if(checkColumn2 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_2);
            hvWinLine[GameView.LINE_COLUMN_2] = true;
            return true;
        }
        if(checkColumn3 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_3);
            hvWinLine[GameView.LINE_COLUMN_3] = true;
            return true;
        }
        if(checkColumn4 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_4);
            hvWinLine[GameView.LINE_COLUMN_4] = true;
            return true;
        }
        if(checkColumn5 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_5);
            hvWinLine[GameView.LINE_COLUMN_5] = true;
            return true;
        }
        if(checkRow1 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_1);
            hvWinLine[GameView.LINE_ROW_1] = true;
            return true;
        }
        if(checkRow2 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_2);
            hvWinLine[GameView.LINE_ROW_2] = true;
            return true;
        }
        if(checkRow3 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_3);
            hvWinLine[GameView.LINE_ROW_3] = true;
            return true;
        }
        if(checkRow4 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_4);
            hvWinLine[GameView.LINE_ROW_4] = true;
            return true;
        }
        if(checkRow5 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_5);
            hvWinLine[GameView.LINE_ROW_5] = true;
            return true;
        }
        if(checkLeft == ptrType){
            mGameView.setWinLine(GameView.LINE_LEFT_D);
            hvWinLine[GameView.LINE_LEFT_D] = true;
            return true;
        }
        if(checkRight == ptrType){
            mGameView.setWinLine(GameView.LINE_RIGHT_D);
            hvWinLine[GameView.LINE_RIGHT_D] = true;
            return true;
        }
        return false;
    }

    private boolean checkWinnerIsRed(int ptrType){
        int checkColumn1 = 0;
        int checkColumn2 = 0;
        int checkColumn3 = 0;
        int checkColumn4 = 0;
        int checkColumn5 = 0;
        int checkRow1 = 0;
        int checkRow2 = 0;
        int checkRow3 = 0;
        int checkRow4 = 0;
        int checkRow5 = 0;
        int checkLeft = 0;
        int checkRight = 0;

        for(int i=0; i<ptrType; i++){
            if(boardState[0][i] == PIECE_RED){ checkColumn1++; }
            if(boardState[1][i] == PIECE_RED){ checkColumn2++; }
            if(boardState[2][i] == PIECE_RED){ checkColumn3++; }
            if(boardState[3][i] == PIECE_RED){ checkColumn4++; }
            if(boardState[4][i] == PIECE_RED){ checkColumn5++; }
            if(boardState[i][0] == PIECE_RED){ checkRow1++; }
            if(boardState[i][1] == PIECE_RED){ checkRow2++; }
            if(boardState[i][2] == PIECE_RED){ checkRow3++; }
            if(boardState[i][3] == PIECE_RED){ checkRow4++; }
            if(boardState[i][4] == PIECE_RED){ checkRow5++; }
            if(boardState[i][i] == PIECE_RED){ checkLeft++; }
            if(boardState[i][ptrType-1-i] == PIECE_RED){ checkRight++; }
        }

        if(checkColumn1 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_1);
            hvWinLine[GameView.LINE_COLUMN_1] = true;
            return true;
        }
        if(checkColumn2 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_2);
            hvWinLine[GameView.LINE_COLUMN_2] = true;
            return true;
        }
        if(checkColumn3 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_3);
            hvWinLine[GameView.LINE_COLUMN_3] = true;
            return true;
        }
        if(checkColumn4 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_4);
            hvWinLine[GameView.LINE_COLUMN_4] = true;
            return true;
        }
        if(checkColumn5 == ptrType){
            mGameView.setWinLine(GameView.LINE_COLUMN_5);
            hvWinLine[GameView.LINE_COLUMN_5] = true;
            return true;
        }
        if(checkRow1 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_1);
            hvWinLine[GameView.LINE_ROW_1] = true;
            return true;
        }
        if(checkRow2 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_2);
            hvWinLine[GameView.LINE_ROW_2] = true;
            return true;
        }
        if(checkRow3 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_3);
            hvWinLine[GameView.LINE_ROW_3] = true;
            return true;
        }
        if(checkRow4 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_4);
            hvWinLine[GameView.LINE_ROW_4] = true;
            return true;
        }
        if(checkRow5 == ptrType){
            mGameView.setWinLine(GameView.LINE_ROW_5);
            hvWinLine[GameView.LINE_ROW_5] = true;
            return true;
        }
        if(checkLeft == ptrType){
            mGameView.setWinLine(GameView.LINE_LEFT_D);
            hvWinLine[GameView.LINE_LEFT_D] = true;
            return true;
        }
        if(checkRight == ptrType){
            mGameView.setWinLine(GameView.LINE_RIGHT_D);
            hvWinLine[GameView.LINE_RIGHT_D] = true;
            return true;
        }
        return false;
    }

    private boolean checkDrawGameChance(int ptrType){
        boolean checkHaveEmpty = false;
        for(int i=0;i<ptrType;i++){
            for(int j=0;j<ptrType;j++){
                if(boardState[i][j] == PIECE_NONE){
                    checkHaveEmpty = true;
                }
            }
        }
        if(!checkHaveEmpty){
            return true;
        }
        return false;
    }

    public void cleanAll(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardState[i][j] = PIECE_NONE;
            }
        }
        for (int i = 0; i < 12; i++)
            hvWinLine[i] = false;
    }
}