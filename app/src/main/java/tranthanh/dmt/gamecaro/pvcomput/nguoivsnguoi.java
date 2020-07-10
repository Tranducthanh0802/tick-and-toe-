package tranthanh.dmt.gamecaro.pvcomput;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tranthanh.dmt.gamecaro.R;
import tranthanh.dmt.gamecaro.chuyendulieu;
import tranthanh.dmt.gamecaro.dong_thuoc_tinh;
import tranthanh.dmt.gamecaro.pvp.EvalBoard;

public class nguoivsnguoi extends Fragment implements chuyendulieu {
    RecyclerView recyclerView;
    custom_adapter1 adapter;
    List<dong_thuoc_tinh> list;
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag";

    private int x, y;
    public EvalBoard eval;
    private int playerFlag;
    private int fEnd;

    public static final int INT_MAX = Integer.MAX_VALUE;
    public int[][] arrBoard;
    private ArrayList<Point> listUndo = new ArrayList<>();
    Button btn;
    ImageButton back;
    TextView txtnguoi,txtmay;
    int count=0;

    public static nguoivsnguoi newInstance() {
        
        Bundle args = new Bundle();
        
        nguoivsnguoi fragment = new nguoivsnguoi();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_main2,container,false);
        btn=view.findViewById(R.id.btnagain);
        txtnguoi=view.findViewById(R.id.txttenNguoi);
        txtmay=view.findViewById(R.id.txttenMay);
        txtnguoi.setText("Player1:");
        txtmay.setText("Player2:");
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 20);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new custom_adapter1(list,getContext(),this);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void chuyendulieu(int row, int col) {

            if(playerFlag==1){
                playerFlag=2;
                arrBoard[row][col] = 1;
                listUndo.add(new Point(row, col));
                list.get(row*20+col).setAnh(R.drawable.ic_clear_black_24dp);


                if (checkWin(row, col) == 1) {
                    fEnd = 1;
                    dialogEnd1 dialog = dialogEnd1.newInstance(this, "Player1");
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getSupportFragmentManager(), GAME_END_DIALOG_TAG);
                }
            }else{
                playerFlag=1;
                arrBoard[row][col] = 2;
                listUndo.add(new Point(row, col));
                list.get(row*20+col).setAnh(R.drawable.ic_radio_button_unchecked_black_24dp);

                if(checkWin(row,col)==2) {
                    fEnd = 2;
                    dialogEnd1 dialog = dialogEnd1.newInstance(this, "Player2");
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getSupportFragmentManager(), GAME_END_DIALOG_TAG);
                }
            }

            adapter.notifyDataSetChanged();
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
        count++;
        if (leg > 1) {
            Point p = listUndo.get(leg - 1);
            listUndo.remove(listUndo.size() - 1);
            arrBoard[p.x][p.y] = 0;
            list.get(p.x*20+p.y).setAnh(0);
            list.get(p.x*20+p.y).setComputer(false);
            fEnd = 0;

            if(playerFlag==1 && count%2!=0){
                playerFlag=2;
                count=0;

            }
            if(playerFlag==2 && count%2!=0){
                playerFlag=1;
                count=0;

            }
        }


        adapter.notifyDataSetChanged();
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
}
