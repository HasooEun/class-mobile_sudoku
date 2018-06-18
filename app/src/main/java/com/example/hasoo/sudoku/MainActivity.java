package com.example.hasoo.sudoku;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Button[] button = null;
    TextView [][] textView = null;
    boolean [][][] flags = null;
    int level = 1;
    int lastRow = -1, lastCol = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView stateMessage = findViewById(R.id.stateMessage);
        stateMessage.setText("빈칸을 클릭 후, 숫자 버튼을 눌러 주세요");
        initBoard();
        fillBoard();
    }

    // 보드 초기화
    private void initBoard(){
        button = new Button[10];
        textView = new TextView[9][9];
        flags = new boolean[9][9][3]; // [][][0] means question, [][][1] means not Empty, [][][2] means not error

        textView[0][0] = findViewById(R.id.textView00);
        textView[0][1] = findViewById(R.id.textView01);
        textView[0][2] = findViewById(R.id.textView02);
        textView[0][3] = findViewById(R.id.textView03);
        textView[0][4] = findViewById(R.id.textView04);
        textView[0][5] = findViewById(R.id.textView05);
        textView[0][6] = findViewById(R.id.textView06);
        textView[0][7] = findViewById(R.id.textView07);
        textView[0][8] = findViewById(R.id.textView08);

        textView[1][0] = findViewById(R.id.textView10);
        textView[1][1] = findViewById(R.id.textView11);
        textView[1][2] = findViewById(R.id.textView12);
        textView[1][3] = findViewById(R.id.textView13);
        textView[1][4] = findViewById(R.id.textView14);
        textView[1][5] = findViewById(R.id.textView15);
        textView[1][6] = findViewById(R.id.textView16);
        textView[1][7] = findViewById(R.id.textView17);
        textView[1][8] = findViewById(R.id.textView18);

        textView[2][0] = findViewById(R.id.textView20);
        textView[2][1] = findViewById(R.id.textView21);
        textView[2][2] = findViewById(R.id.textView22);
        textView[2][3] = findViewById(R.id.textView23);
        textView[2][4] = findViewById(R.id.textView24);
        textView[2][5] = findViewById(R.id.textView25);
        textView[2][6] = findViewById(R.id.textView26);
        textView[2][7] = findViewById(R.id.textView27);
        textView[2][8] = findViewById(R.id.textView28);

        textView[3][0] = findViewById(R.id.textView30);
        textView[3][1] = findViewById(R.id.textView31);
        textView[3][2] = findViewById(R.id.textView32);
        textView[3][3] = findViewById(R.id.textView33);
        textView[3][4] = findViewById(R.id.textView34);
        textView[3][5] = findViewById(R.id.textView35);
        textView[3][6] = findViewById(R.id.textView36);
        textView[3][7] = findViewById(R.id.textView37);
        textView[3][8] = findViewById(R.id.textView38);

        textView[4][0] = findViewById(R.id.textView40);
        textView[4][1] = findViewById(R.id.textView41);
        textView[4][2] = findViewById(R.id.textView42);
        textView[4][3] = findViewById(R.id.textView43);
        textView[4][4] = findViewById(R.id.textView44);
        textView[4][5] = findViewById(R.id.textView45);
        textView[4][6] = findViewById(R.id.textView46);
        textView[4][7] = findViewById(R.id.textView47);
        textView[4][8] = findViewById(R.id.textView48);

        textView[5][0] = findViewById(R.id.textView50);
        textView[5][1] = findViewById(R.id.textView51);
        textView[5][2] = findViewById(R.id.textView52);
        textView[5][3] = findViewById(R.id.textView53);
        textView[5][4] = findViewById(R.id.textView54);
        textView[5][5] = findViewById(R.id.textView55);
        textView[5][6] = findViewById(R.id.textView56);
        textView[5][7] = findViewById(R.id.textView57);
        textView[5][8] = findViewById(R.id.textView58);

        textView[6][0] = findViewById(R.id.textView60);
        textView[6][1] = findViewById(R.id.textView61);
        textView[6][2] = findViewById(R.id.textView62);
        textView[6][3] = findViewById(R.id.textView63);
        textView[6][4] = findViewById(R.id.textView64);
        textView[6][5] = findViewById(R.id.textView65);
        textView[6][6] = findViewById(R.id.textView66);
        textView[6][7] = findViewById(R.id.textView67);
        textView[6][8] = findViewById(R.id.textView68);

        textView[7][0] = findViewById(R.id.textView70);
        textView[7][1] = findViewById(R.id.textView71);
        textView[7][2] = findViewById(R.id.textView72);
        textView[7][3] = findViewById(R.id.textView73);
        textView[7][4] = findViewById(R.id.textView74);
        textView[7][5] = findViewById(R.id.textView75);
        textView[7][6] = findViewById(R.id.textView76);
        textView[7][7] = findViewById(R.id.textView77);
        textView[7][8] = findViewById(R.id.textView78);

        textView[8][0] = findViewById(R.id.textView80);
        textView[8][1] = findViewById(R.id.textView81);
        textView[8][2] = findViewById(R.id.textView82);
        textView[8][3] = findViewById(R.id.textView83);
        textView[8][4] = findViewById(R.id.textView84);
        textView[8][5] = findViewById(R.id.textView85);
        textView[8][6] = findViewById(R.id.textView86);
        textView[8][7] = findViewById(R.id.textView87);
        textView[8][8] = findViewById(R.id.textView88);

        button[0] = findViewById(R.id.button0);
        button[1] = findViewById(R.id.button1);
        button[2] = findViewById(R.id.button2);
        button[3] = findViewById(R.id.button3);
        button[4] = findViewById(R.id.button4);
        button[5] = findViewById(R.id.button5);
        button[6] = findViewById(R.id.button6);
        button[7] = findViewById(R.id.button7);
        button[8] = findViewById(R.id.button8);
        button[9] = findViewById(R.id.button9);
    }

    private void fillBoard(){

        lastRow = lastCol = -1;

        // initialize text color and background color
        for(int i = 0; i<9; i++){
            for(int j=0; j<9; j++){
                textView[i][j].setTextColor(Color.BLACK);
                textView[i][j].setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // print questions to board
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.question_data);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String question = "";

            for(int i = 0; i < level ;i++)
                question = bufferedReader.readLine();
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    char cQuestion = question.charAt(i*9+j);
                    if(cQuestion != '0'){
                        String sQuestion = cQuestion + "";
                        textView[i][j].setText(sQuestion);
                        flags[i][j][0] = true; // is question
                        flags[i][j][1] = true; // is not empty
                        flags[i][j][2] = true; // is not error
                    } else {
                        textView[i][j].setText("");
                        flags[i][j][0] = false;
                        flags[i][j][1] = false;
                        flags[i][j][2] = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        toggleButtons(true);
    }

    // 텍스트 뷰를 누르면 동작
    public void onTextViewClicked(View v){

        // 텍스트뷰의 이름에서 위치를 가져옴

        // question일 경우를 대비한 가드 (return)

        // 만약 이전에 클릭한 기록이 없다면
            // 십자 색상 표시
            // 하단 버튼 활성화
            // 현재 위치를 lastRow, lastCol에 저장

        // 이전에 클릭한 기록이 있다면
            // 우선 십자 색상 제거

            // lastRow, lastCol과 같은 곳을 클릭 했다면?
                // 이전 클릭 내역 지우기
                // 버튼 비활성화

            // lastRow, lastCol과 다른 곳을 클릭 했다면?
                // 옮긴 위치에 대하여 십자 색상 표시
                // 옮긴 위치를 lastRow, lastCol에 저장
    }

    // 숫자 버튼을 누르면 동작
    public void onButtonClicked(View v){

        // 버튼의 text를 텍스트뷰의 text로 설정

        // flags[lastRow][lastCol][1]에 버튼의 text가 empty인지 저장
        // flags[lastRow][lastCol][2]에 lastRow, lastCol에 대한 오류 검사 메소드 결과를 저장

        // 만약 오류 검사를 했을 때, 오류가 없다면
            // textView[lastRow][lastCol]의 글자 색을 검은 색으로 설정
            // 게임 종료 여부를 판단했을 때, 끝났다면
                // "당신의 승리입니다"라는 메시지 출력
                // 보드 채우기 (fillBoard)
                // 메소드 나가기(return;)

        // 만약 오류 검사를 했을 때, 오류가 있다면
            // textView[lastRow][lastCol]의 글자 색을 빨간 색으로 설정

        // 십자 색상 제거
        // 이전 클릭 내역 지우기
        // 버튼 비활성화
    }

    @Override   // 메뉴 inflating
    public boolean onCreateOptionsMenu(Menu menu) {
        // attach menu
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override   // 메뉴 아이템 누르면 동작
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item1) level = 1;
        else if(item.getItemId() == R.id.item2) level = 2;
        else if(item.getItemId() == R.id.item3) level = 3;
        else if(item.getItemId() == R.id.item4) level = 4;

        switch (item.getItemId()){
            case R.id.item1:
            case R.id.item2:
            case R.id.item3:
            case R.id.item4:
                item.setChecked(true);
            case R.id.itemReset:
                initBoard();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // 텍스트뷰의 이름에서 배열 번호 가져오기
    private int[] getPosition(View v){
        String name = v.getResources().getResourceName(v.getId());
        char cRow = name.charAt(name.length() - 2);
        char cCol = name.charAt(name.length() - 1);
        int iRow = Integer.parseInt(cRow + "");
        int iCol = Integer.parseInt(cCol + "");

        return new int[]{iRow, iCol};
    }

    // 십자 색상 칠하기
    private void crossColoring(int row, int col, boolean flag){

        int red = 255, green = 255, blue = 255, alpha = 0;

        if(flag) {
            alpha = 50;
            red = green = blue = 150;
        }

        for (int k = 0; k < 9; k++) {
            textView[row][k].setBackgroundColor(Color.argb(alpha, red, green, blue));
            textView[k][col].setBackgroundColor(Color.argb(alpha, red, green, blue));
        }

        red = 255;

        // set background color for selected textView
        textView[row][col].setBackgroundColor(Color.argb(alpha, red, green, blue));

    }

    // 버튼 활성화/비활성화
    private void toggleButtons(boolean flag){
        for(int i=0; i<10; i++){
            button[i].setEnabled(flag);
        }
    }

    // 논리적 오류가 있는지 검사
    private boolean errorCheck(int row, int col) {

        // 지정한 위치에 해당하는 textView의 문자열을 가져옴
        // 빈 문자열 선택에 대한 가드 (empty 인 경우 return)

        // 가로 세로 오류 체크
        // 3 by 3 박스 오류 체크
        // 오류 검사 결과 return

        return true;
    }

    // 이겼는지 여부를 검사
    private boolean winningCheck(){
        // 반복문을 이용하여 모든 flags[i][j][1]과 flags[i][j][2]가 true인지 검사
        // 검사 결과 return
        return true;
    }
}

