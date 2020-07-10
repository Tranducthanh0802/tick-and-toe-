package tranthanh.dmt.gamecaro;

public class dong_thuoc_tinh {
    int anh;
    boolean player;
    boolean computer;

    public boolean isComputer() {
        return computer;
    }

    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    int row;
    int colum;

    public dong_thuoc_tinh(int anh, boolean player, int row, int colum) {
        this.anh = anh;
        this.player = player;
        this.row = row;
        this.colum = colum;
    }

    public dong_thuoc_tinh(int anh, int row, int colum) {
        this.anh = anh;
        this.row = row;
        this.colum = colum;
    }

    public dong_thuoc_tinh(int row, int colum) {
        this.row = row;
        this.colum = colum;
    }

    public int getAnh() {
        return anh;
    }

    public void setAnh(int anh) {
        this.anh = anh;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColum() {
        return colum;
    }

    public void setColum(int colum) {
        this.colum = colum;
    }
}
