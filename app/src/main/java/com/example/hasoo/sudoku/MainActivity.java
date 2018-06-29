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
        initVariables();
        fillBoard();
    }

    // 보드 초기화
    private void initVariables(){
        button = new Button[10];
        textView = new TextView[9][9];
        flags = new boolean[9][9][3]; // [][][0] means question, [][][1] means not Empty, [][][2] means no error

        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                String name = "textView" + i + j;
                int viewId = getResources().getIdentifier(name, "id", getPackageName());
                textView[i][j] = findViewById(viewId);
            }
        }

        for(int i=0; i<10; i++){
            String name = "button"+i;
            int viewId = getResources().getIdentifier(name, "id", getPackageName());
            button[i] = findViewById(viewId);
        }
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

        toggleButtons(false);
    }

    // 텍스트 뷰를 누르면 동작
    public void onTextViewClicked(View v){

        int [] pos = getPosition(v);

        // question일 경우를 대비한 가드
        if(flags[pos[0]][pos[1]][0]){
            return;
        }

        if(lastRow == -1 && lastCol == -1){ // 이전 클릭이 없다면
            // 십자 색상 표시
            crossColoring(pos[0], pos[1], true);
            // 하단 버튼 활성화
            toggleButtons(true);
            // 현재 위치를 이전 위치로 저장
            lastRow = pos[0];
            lastCol = pos[1];
        } else {            // 처음 클릭한게 아니라면?
            // 우선 십자 색상 제거
            crossColoring(lastRow, lastCol, false);

            // 같은 곳을 클릭 했다면?
            if(lastRow == pos[0] && lastCol == pos[1]){
                // 이전 클릭 내역 지우기
                lastRow = lastCol = -1;
                // 버튼 비활성화
                toggleButtons(false);

            }else{          // 다른 곳을 클릭 했다면?
                // 옮긴 위치에 대하여 십자 색상 표시
                crossColoring(pos[0], pos[1], true);
                // 옮긴 위치를 이전 위치로 저장
                lastRow = pos[0];
                lastCol = pos[1];

            }
        }
    }

    // 숫자 버튼을 누르면 동작
    public void onButtonClicked(View v){

        // 버튼의 text를 텍스트뷰의 text로 설정
        String buttonText = ((Button)v).getText().toString();
        textView[lastRow][lastCol].setText(buttonText);

        flags[lastRow][lastCol][1] = !buttonText.isEmpty();
        flags[lastRow][lastCol][2] = errorCheck(lastRow, lastCol);

        if(flags[lastRow][lastCol][2]){
            textView[lastRow][lastCol].setTextColor(Color.BLACK);
            if(winningCheck()){
                // show message
                Toast.makeText(getApplicationContext(), "당신의 승리입니다.", Toast.LENGTH_SHORT).show();
                fillBoard();
                return;
            }
        } else {
            textView[lastRow][lastCol].setTextColor(Color.RED);
        }

        // 십자 색상 제거
        crossColoring(lastRow, lastCol, false);
        // 이전 클릭 내역 지우기
        lastRow = lastCol = -1;
        // 버튼 비활성화
        toggleButtons(false);

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
                fillBoard();
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

        // set background color of selected textView
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
        boolean rtn = true; // 초기 값은 오류가 없다고 설정

        String buttonText = textView[row][col].getText().toString();

        // 빈 문자열 선택에 대한 가드
        if(buttonText.isEmpty()){
            return true;
        }
        int startRowIdx = (row / 3) * 3;
        int startColIdx = (col / 3) * 3;

        // 가로 세로 오류 체크
        for(int i=0; i<9; i++) {
            boolean bRow = textView[i][col].getText().toString().equals(buttonText);
            boolean bCol = textView[row][i].getText().toString().equals(buttonText);
            rtn = rtn && (row == i || !bRow) && (col == i || !bCol);
        }

        // 박스 체크
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                boolean bBox = textView[startRowIdx+i][startColIdx+j].getText().toString().equals(buttonText);
                rtn = rtn && ((row == startRowIdx+i && col == startColIdx+j) || !bBox);
            }
        }
        return rtn;
    }

    // 이겼는지 여부를 검사
    private boolean winningCheck(){
        boolean rtn = true;
        for(int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if(!flags[i][j][0]){
                    rtn = rtn && flags[i][j][1] && flags[i][j][2];
                }
            }
        }
        return rtn;
    }
}