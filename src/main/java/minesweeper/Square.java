package minesweeper;

public class Square {
    private boolean hidden = true; //Er ruten skjult (skjult som standard)
    private boolean bomb; //Inneholder ruten en bombe
    private boolean flagged; //Er ruten flagget?
    private int bombsNearby; //Antall bomber i nærheten

    //Koordinater for ruten inkl gridPane-index
    private int xCord;
    private int yCord;
    private int gridPaneIndex;
    private int gridPaneIndexText;

    public Square(boolean bomb, int bombsNearby, int xCord, int yCord, int gridPaneIndex , int gridPaneIndexText) {
        this.bomb = bomb;
        this.bombsNearby = bombsNearby;
        this.xCord = xCord;
        this.yCord = yCord;
        this.gridPaneIndex = gridPaneIndex;
        this.gridPaneIndexText = gridPaneIndexText;
    }

    //Settere med logikk
    public void setHidden(boolean hidden){
        //Får kun åpne ruter som ikke er flagget
        if (!this.isFlagged()){
            this.hidden = hidden;
        }
    }

    public void setFlagged(boolean flagged){
        if (this.isHidden()){
            this.flagged = flagged;
        }
    }
 
    // Vanlige settere og gettere
    public void setGridPaneIndex(int gridPaneIndex) {
        this.gridPaneIndex = gridPaneIndex;
    }

    public int getGridPaneIndex() {
        return gridPaneIndex;
    }

    public void setGridPaneIndexText(int gridPaneIndexText){
        this.gridPaneIndexText = gridPaneIndexText;
    }

    public int getGridPaneIndexText(){
        return this.gridPaneIndexText;
    }

    public int getXCord() {
        return xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public void setBombsNearby(int bombsNearby){
        this.bombsNearby = bombsNearby;
    }

    public int getBombsNearby() {
        return bombsNearby;
    }

    public boolean isFlagged(){
        return this.flagged;
    }

    @Override
    public String toString() {
        if (this.isFlagged()){
            return "F";
        }
        if (!this.isHidden()){
            if (this.isBomb()){
                return "X";
            }
            return String.valueOf(this.getBombsNearby()); 
        }
        return "H";
        
    }
}
