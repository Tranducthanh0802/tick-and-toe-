package tranthanh.dmt.gamecaro.pvp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tranthanh.dmt.gamecaro.R;
import tranthanh.dmt.gamecaro.chuyendulieu;
import tranthanh.dmt.gamecaro.dong_thuoc_tinh;

public class playgame extends Fragment implements chuyendulieu {
    RecyclerView recyclerView;
    custom_adapter adapter;
    List<dong_thuoc_tinh> list;
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag";
    public String[] caseX = {"11", "101", "1112", "2111", "1011", "1101", "111", "11011", "10111", "11101", "11112", "21111", "1111", "11111"};
    public String[] caseO = {"22", "202", "2221", "1222", "2022", "2202", "222", "22022", "20222", "22202", "22221", "12222", "2222", "22222"};
    public int[] pointArr = {5, 5, 10, 10, 500, 500, 500, 600, 600, 600, 600, 600, 5000, 5000};
    public int[] DScore = new int[]{0, 1, 9, 81, 729};
    public int[] AScore = new int[]{0, 2, 18, 162, 1458};
    Random rand;
    private int x, y;
    public EvalBoard eval;
    private int playerFlag;
    private int fEnd;
    private static int maxDepth;
    public static int maxMove = 1;
    public int depth = 0;
    public static final int SIZE = 25;
    public static final int INT_MAX = Integer.MAX_VALUE;
    public int[][] arrBoard;
    private ArrayList<Point> listUndo = new ArrayList<>();
    Button btn;
    ImageButton back;

    public static playgame newInstance() {

        Bundle args = new Bundle();

        playgame fragment = new playgame();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_main2,container,false);
        btn=view.findViewById(R.id.btnagain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newgame();
            }
        });
        back=view.findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        recyclerView = view.findViewById(R.id.rycle);
        list = new ArrayList<>();
        for (int i=0;i<20;i++){
            for (int j=0;j<20;j++){
                list.add(new dong_thuoc_tinh(i,j));
                list.get(i*20+j).setComputer(false);
            }
        }
        eval = new EvalBoard();
        listUndo = new ArrayList<Point>();
        arrBoard = new int[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                arrBoard[i][j] = 0;
            }
        }
        playerFlag = 1;
        fEnd = 0;
        maxDepth=1;
        resetBoard();
        rand = new Random();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 20);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new custom_adapter(list,getContext(),this);
        recyclerView.setAdapter(adapter);

        return view;
    }
    public void newGame() {

        for (int i = 0; i < listUndo.size(); i++) {
            listUndo.remove(i);
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (arrBoard[i][j] != 0) {
                    arrBoard[i][j] = 0;
                }
            }
        }
        if (fEnd == 1) {
            playerFlag = 2;
        } else {
            playerFlag = 1;
        }
        if (playerFlag == 2) {
            x = rand.nextInt(3);
            y = rand.nextInt(3);
            arrBoard[x + 7][y + 7] = 2;
            listUndo.add(new Point(x + 7, y + 7));
            list.get(x*20+y).setAnh(R.drawable.ic_radio_button_unchecked_black_24dp);
            list.get(x*20+y).setPlayer(true);
            playerFlag = 1;
        }
        fEnd = 0;
    }
    @Override
    public void chuyendulieu(int row, int col) {

         boolean c= playChess(row,col);
            int a=row*20+col;
        adapter.notifyDataSetChanged();


    }
    public boolean playChess(int mouseX, int mouseY) {

            int row = mouseX;
            int col = mouseY ;

            if (arrBoard[row][col] != 0) {
                return false;
            }

            if (arrBoard[row][col] == 0) {
                arrBoard[row][col] = 1;
                listUndo.add(new Point(row, col));
                if (checkWin(row, col) == 1) {
                    fEnd = 1;
                    dialogEnd dialog = dialogEnd.newInstance(this, R.drawable.youwin);
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getSupportFragmentManager(), GAME_END_DIALOG_TAG);
                    return true;
                }
                // May di
                    EvalChessBoard(2, eval, arrBoard);
                Point temp = new Point();

                temp = findMoveOfCom(playerFlag, arrBoard);
                x = temp.x;
                y = temp.y;
                arrBoard[x][y] = 2;
                listUndo.add(new Point(x, y));
                list.get(x*20+y).setAnh(R.drawable.ic_radio_button_unchecked_black_24dp);
                list.get(x*20+y).setComputer(true);
                adapter.notifyDataSetChanged();
                if (checkWin(x, y) == 2) {
                    fEnd = 2;
                    dialogEnd dialog = dialogEnd.newInstance(this, R.drawable.youlose);
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getSupportFragmentManager(), GAME_END_DIALOG_TAG);
                    return true;
                }
                return true;
            }
        return false;
    }
    private int checkWin(int cl, int rw) {
        int r = 0, c = 0;
        int i;
        boolean human, pc;
        //Check hÃƒÂ ng ngang
        while (c < (20 - 5)) {
            human = true;
            pc = true;
            for (i = 0; i < 5; i++) {
                if (arrBoard[cl][c + i] != 1) {
                    human = false;
                }
                if (arrBoard[cl][c + i] != 2) {
                    pc = false;
                }
            }
            if (human) {
                return 1;
            }
            if (pc) {
                return 2;
            }
            c++;
        }
        //Check hÃƒÂ ng dÃ¡Â»ï¿½c
        while (r < (20 - 5)) {
            human = true;
            pc = true;
            for (i = 0; i < 5; i++) {
                if (arrBoard[r + i][rw] != 1) {
                    human = false;
                }
                if (arrBoard[r + i][rw] != 2) {
                    pc = false;
                }
            }
            if (human) {
                return 1;
            }
            if (pc) {
                return 2;
            }
            r++;
        }
        //Check duong cheo xuoi
        r = rw;
        c = cl;
        while (r > 0 && c > 0) {
            r--;
            c--;
        }
        while (r <= (20 - 5) && c <= (20- 5)) {
            human = true;
            pc = true;
            for (i = 0; i < 5; i++) {
                if (arrBoard[c + i][r + i] != 1) {
                    human = false;
                }
                if (arrBoard[c + i][r + i] != 2) {
                    pc = false;
                }
            }
            if (human) {
                return 1;
            }
            if (pc) {
                return 2;
            }
            r++;
            c++;
        }
        //Check duong cheo nguoc
        r = rw;
        c = cl;
        while (r < (20 - 1) && c > 0) {
            r++;
            c--;
        }
        while (r >= 4 && c <= (20 - 5)) {
            human = true;
            pc = true;
            for (i = 0; i < 5; i++) {
                if (arrBoard[r - i][c + i] != 1) {
                    human = false;
                }
                if (arrBoard[r - i][c + i] != 2) {
                    pc = false;
                }
            }
            if (human) {
                return 1;
            }
            if (pc) {
                return 2;
            }
            r--;
            c++;
        }
        return 0;
    }
    private void EvalChessBoard(int player, EvalBoard eBoard, int[][] boardArr) {
        int rw, cl, ePC, eHuman;
        resetBoard(eBoard);
        //Danh gia theo hang
        for (rw = 0; rw < 20; rw++) {
            for (cl = 0; cl < 20 - 4; cl++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (boardArr[rw][cl + i] == 1) {
                        eHuman++;
                    }
                    if (boardArr[rw][cl + i] == 2) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (boardArr[rw][cl + i] == 0) {
                            if (eHuman == 0) {
                                if (player == 1) {
                                    eBoard.eBoard[rw][cl + i] += DScore[ePC];
                                } else {
                                    eBoard.eBoard[rw][cl + i] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (player == 2) {
                                    eBoard.eBoard[rw][cl + i] += DScore[eHuman];
                                } else {
                                    eBoard.eBoard[rw][cl + i] += AScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.eBoard[rw][cl + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
        //Danh gia theo cot
        for (cl = 0; cl < 20; cl++) {
            for (rw = 0; rw < 20- 4; rw++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (boardArr[rw + i][cl] == 1) {
                        eHuman++;
                    }
                    if (boardArr[rw + i][cl] == 2) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (boardArr[rw + i][cl] == 0) {
                            if (eHuman == 0) {
                                if (player == 1) {
                                    eBoard.eBoard[rw + i][cl] += DScore[ePC];
                                } else {
                                    eBoard.eBoard[rw + i][cl] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (player == 2) {
                                    eBoard.eBoard[rw + i][cl] += DScore[eHuman];
                                } else {
                                    eBoard.eBoard[rw + i][cl] += AScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.eBoard[rw + i][cl] *= 2;
                            }
                        }
                    }

                }
            }
        }
        //Danh gia duong cheo xuoi
        for (cl = 0; cl < 20 - 4; cl++) {
            for (rw = 0; rw < 20 - 4; rw++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (boardArr[rw + i][cl + i] == 1) {
                        eHuman++;
                    }
                    if (boardArr[rw + i][cl + i] == 2) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (boardArr[rw + i][cl + i] == 0) {
                            if (eHuman == 0) {
                                if (player == 1) {
                                    eBoard.eBoard[rw + i][cl + i] += DScore[ePC];
                                } else {
                                    eBoard.eBoard[rw + i][cl + i] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (player == 2) {
                                    eBoard.eBoard[rw + i][cl + i] += DScore[eHuman];
                                } else {
                                    eBoard.eBoard[rw + i][cl + i] += AScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.eBoard[rw + i][cl + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
        //Danh gia duong cheo nguoc
        for (rw = 4; rw < 20; rw++) {
            for (cl = 0; cl < 20 - 4; cl++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (boardArr[rw - i][cl + i] == 1) {
                        eHuman++;
                    }
                    if (boardArr[rw - i][cl + i] == 2) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (boardArr[rw - i][cl + i] == 0) {
                            if (eHuman == 0) {
                                if (player == 1) {
                                    eBoard.eBoard[rw - i][cl + i] += DScore[ePC];
                                } else {
                                    eBoard.eBoard[rw - i][cl + i] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (player == 2) {
                                    eBoard.eBoard[rw - i][cl + i] += DScore[eHuman];
                                } else {
                                    eBoard.eBoard[rw - i][cl + i] += AScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.eBoard[rw - i][cl + i] *= 2;
                            }
                        }
                    }

                }
            }
        }
    }
    public Point findMoveOfCom(int player, int[][] arr) {
        int n = 20;
        int[][] b = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(arr[i], 0, b[i], 0, n);
        }
        playerFlag = player;
        EvalChessBoard(2, eval, b);
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < maxMove; i++) {
            list.add(getMaxPoint());
        }
        int maxp = -INT_MAX;
        ArrayList<Point> ListChoose = new ArrayList<>();
        for (Point list1 : list) {
            b[list1.x][list1.y] = playerFlag;
            int t = minVal(b, -INT_MAX, INT_MAX, 0);
            if (maxp < t) {
                maxp = t;
                ListChoose.clear();
                ListChoose.add(list1);
            } else if (maxp == t) {
                ListChoose.add(list1);
            }
            b[list1.x][list1.y] = 0;
        }
        int x = rand.nextInt(ListChoose.size());
        return ListChoose.get(x);
    }

    private int maxVal(int[][] arrBoard, int alpha, int beta, int depth) {
        int val = EvalDangerous(arrBoard);
        if (depth >= maxDepth || Math.abs(val) > 5000) {
            return val;
        }
        EvalChessBoard(2, eval, arrBoard);
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < maxMove; i++) {
            list.add(getMaxPoint());
        }
        for (Point list1 : list) {
            arrBoard[list1.x][list1.y] = 2;
            alpha = Math.max(alpha, minVal(arrBoard, alpha, beta, depth + 1));
            arrBoard[list1.x][list1.y] = 0;
            if (alpha > beta) {
                break;
            }
        }
        return alpha;
    }
    private void resetBoard(EvalBoard e) {
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                eval.eBoard[r][c] = 0;
            }
        }
    }
    private Point getMaxPoint() {
        ArrayList<Point> list = new ArrayList<>();
        int t = -INT_MAX;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (t < eval.eBoard[i][j]) {
                    t = eval.eBoard[i][j];
                    list.clear();
                    list.add(new Point(i, j));
                } else if (t == eval.eBoard[i][j]) {
                    list.add(new Point(i, j));
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            eval.eBoard[list.get(i).x][list.get(i).y] = 0;
        }
        int x = rand.nextInt(list.size());
        return list.get(x);
    }
    private int minVal(int[][] arrBoard, int alpha, int beta, int depth) {
        int val = EvalDangerous(arrBoard);
        if (depth >= maxDepth || Math.abs(val) > 5000) {
            return val;
        }
        EvalChessBoard(1, eval, arrBoard);
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < maxMove; i++) {
            list.add(getMaxPoint());
        }
        for (Point list1 : list) {
            arrBoard[list1.x][list1.y] = 1;
            beta = Math.min(beta, maxVal(arrBoard, alpha, beta, depth + 1));
            arrBoard[list1.x][list1.y] = 0;
            if (alpha >= beta) {
                break;
            }
        }
        return beta;
    }
    private int EvalDangerous(int[][] b) {
        int n = 20;
        String s = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += b[i][j];
            }
            s += ";";
            for (int j = 0; j < n; j++) {
                s += b[j][i];
            }
            s += ";";

        }
        for (int i = 0; i < n - 4; i++) {
            for (int j = 0; j < n - i; j++) {
                s += b[j][i + j];
            }
            s += ";";
        }
        for (int i = n - 5; i > 0; i--) {
            for (int j = 0; j < n - i; j++) {
                s += b[i + j][j];
            }
            s += ";";
        }
        for (int i = 4; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                s += b[i - j][j];
            }
            s += ";";
        }
        for (int i = n - 5; i > 0; i--) {
            for (int j = n - 1; j >= i; j--) {
                s += b[j][i + n - j - 1];
            }
            s += ";\n";
        }
        Pattern pattern1, pattern2;
        int diem = 0;
        for (int i = 0; i < caseO.length; i++) {
            pattern1 = Pattern.compile(caseX[i]);
            pattern2 = Pattern.compile(caseO[i]);
            Matcher m1 = pattern1.matcher(s);
            Matcher m2 = pattern2.matcher(s);
            int count1 = 0;
            int count2 = 0;
            while (m1.find()) {
                count1++;
            }
            while (m2.find()) {
                count2++;
            }
            diem += pointArr[i] * count2;
            diem -= pointArr[i] * count1;
        }
        System.out.println("Diem: " + diem);
        return diem;
    }
   void resetBoard() {
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                eval.eBoard[r][c] = 0;
            }
        }
    }
    void newgame(){
        for (int i = 0; i < listUndo.size(); i++) {
            listUndo.remove(i);
        }

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (arrBoard[i][j] != 0) {
                    arrBoard[i][j] = 0;
                }
            }
        }
        for (int i=0;i<20;i++){
            for (int j=0;j<20;j++){
                list.get(i*20+j).setAnh(0);
                list.get(i*20+j).setComputer(false);

            }
        }
        playerFlag = 1;
        fEnd = 0;
        adapter.notifyDataSetChanged();
    }
    void back(){
        int leg = listUndo.size();
        if (leg > 1) {
            Point p = listUndo.get(leg - 1);
            listUndo.remove(listUndo.size() - 1);
            arrBoard[p.x][p.y] = 0;
            list.get(p.x*20+p.y).setAnh(0);
            list.get(p.x*20+p.y).setComputer(false);

            if (listUndo.size() > 0) {
                p = listUndo.get(listUndo.size() - 1);
                listUndo.remove(listUndo.size() - 1);
                arrBoard[p.x][p.y] = 0;
                list.get(p.x*20+p.y).setAnh(0);
                list.get(p.x*20+p.y).setComputer(false);

            }
            fEnd = 0;
            adapter.notifyDataSetChanged();
        }
    }
}
