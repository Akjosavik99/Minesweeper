package minesweeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;

public class Board {

    private List<List<Square>> boardList = new ArrayList<List<Square>>();
    private SaveToFile saveToFile = new SaveToFile();
    private String file;
    private String feilIFil = ""; //Legger til evnetuell feilkode på filen her

    private int totalBombs;
    private int rowAmount;
    private int blankSquares;
    private String difficulty;
    private boolean gameOver = false;
    private boolean wonGame = false;
    private int totalSquares;

    //Konstruktør og hjelpe-metoder til den
    public Board(String difficulty, int rowAmount){
        //Setter antall rader (minimum 6)
        if (rowAmount < 6){
            this.rowAmount = 6;
            this.totalSquares = 6*6;
        }
        else if(rowAmount > 20){ //Makismum 20
            this.rowAmount = 20;
            this.totalSquares = 20*20;
        }
        else{
            this.rowAmount = rowAmount;
            this.totalSquares = rowAmount*rowAmount;
        }

        //Setter vanskelighetsgrad
        this.difficulty = difficulty;

        //Lager brettet
        generateBoardList();
    }

    private void generateBoardList(){
        for (int i = 0; i < this.rowAmount; i++){
            boardList.add(new ArrayList<Square>());
        }

        for (int x = 0; x < this.rowAmount; x++){
            for (int y = 0; y < this.rowAmount; y++){
                boardList.get(x).add(y, new Square(false, 0, x, y, 0, 0));
            }
        }

        //Setter antall bomber alt etter vanskelighetsgrad
        if (this.getDifficulty().equals("Umulig")){ //Umulig nivå
            this.setTotalBombs(30);
        }
        else if (this.getDifficulty().equals("Vanskelig")){ //Vanskelig nivå
            this.setTotalBombs(24);
        }
        else if (this.getDifficulty().equals("Normal")){ //Normalt nivå
            this.setTotalBombs(18);
        }
        else { //Lett nivå
            this.setTotalBombs(12);
        }

        //Legger til bomber på tilfeldige plasser på brettet
        int bombsSet = 0; //Antall bomber lagt til på brettet
        while (bombsSet < this.getTotalBombs()){
                //Genererer random tall for x og y posisjon
            int x = ThreadLocalRandom.current().nextInt(0, this.getRowAmount());
            int y = ThreadLocalRandom.current().nextInt(0, this.getRowAmount());

            //Legger til bomben på plass (x,y) hvis det ikke er bombe fra før av
            if (!boardList.get(x).get(y).isBomb()){
                boardList.get(x).get(y).setBomb(true); //Legger til bomber på plassen
                bombsSet ++; //Øker antall bomber satt
            }
        }

        //Setter antall bomber på random plasser
        this.setBlankSquares(this.getTotalSquares()-this.getTotalBombs());

        //Legger til antall bomber i nærheten
        findBombsNearby();
    }

    private void findBombsNearby(){
        for (int x = 0; x < this.rowAmount; x++) {
            for (int y = 0; y < this.rowAmount; y++) {
                if (!boardList.get(x).get(y).isBomb()){ //Hvis ruten ikke er bombe sjekker man bomber i nærheten

                    int bombsNearby = 0;

                    //Sjekker venstre rute over
                    if (x > 0 && y > 0 && boardList.get(x-1).get(y-1).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker rute over 
                    if (x > 0 && boardList.get(x-1).get(y).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker høyre rute over
                    if (x > 0 && y < rowAmount-1 && boardList.get(x-1).get(y+1).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker høyre rute
                    if (y < rowAmount-1 && boardList.get(x).get(y+1).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker høyre rute under
                    if (x < rowAmount-1 && y < rowAmount-1 && boardList.get(x+1).get(y+1).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker rute under
                    if (x < rowAmount-1 && boardList.get(x+1).get(y).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker venstre rute under
                    if (x < rowAmount-1 && y > 0 && boardList.get(x+1).get(y-1).isBomb()){
                        bombsNearby++;
                    }

                    //Sjekker venstre rute
                    if (y > 0 && boardList.get(x).get(y-1).isBomb()){
                        bombsNearby++;
                    }

                    //Legger til antallet for ruten
                    boardList.get(x).get(y).setBombsNearby(bombsNearby);
                }
            }
        }
    }

    //"Vanlige" metoder
    public List<Person> wonGame(String navn, int sekunder, String vanskelighetsgrad, String fil){
        boolean gyldignavn = true;
        List<Person> toppliste = new ArrayList<>();

        if (wonTheGame()){
            try{
                saveToFile.readFile(fil, toppliste);
            } catch (FileNotFoundException e){
                //Lager en feilkode som kommer med på topplisten
                setFeilIFil("NB: Fant ikke topplisten fra tidligere. Viser kun dette forsøket.");
                System.out.println("Fant ikke toppliste-filen, oppretter ny.");
            }
            catch (Exception ex){
                //Lager en feilkode som kommer med på topplisten
                setFeilIFil("NB: Kan finnes feil i filen. Topplisten kan være ugyldig/ufullstendig");
                System.out.println("Finnes feil i filen. Topplisten kan være ugyldig/ufullstendig");
            }

            //Sjekker at navnet ikke er tomt eller inneholder '-' og at sekunder > 0
            if (navn == null || navn.strip().equals("") || navn.contains("-") || sekunder < 0){
                gyldignavn = false;
            }
        
            if (gyldignavn){
                Person p1 = new Person(navn, sekunder, vanskelighetsgrad);
                toppliste.add(p1);
            }

            //Sorterer listen
            Collections.sort(toppliste);
            
            try {
                saveToFile.writeFile(fil, toppliste);
            } catch (FileNotFoundException e) {
                System.out.println("Feil i fil-path");
            }
        }
        return toppliste;
    }

    public List<String> generateTopList(List<Person> toppliste) {
        List<String> returnList = new ArrayList<>();
        String topplisteText = "";
        int lengde;
        if (toppliste.size() > 10){
            lengde = 10;
        }
        else{
            lengde = toppliste.size();
        }

        //Sorterer listen
        Collections.sort(toppliste);

        boolean firstLine = true;
        for (int i = 0; i < lengde; i++) {
            Person p = toppliste.get(i);

            //Legger til linjeskift mellom alle linjer
            if (firstLine){
                firstLine = false;
            }
            else{
                topplisteText += "\n";
            }

            //Legger med eventuell feilkode på starten av listen
            if (!getFeilIFil().equals("")){
                topplisteText += getFeilIFil() + "\n\n";
                setFeilIFil(""); //Resetter feilkoden
            }
            
            //Maks 20 bokstaver i navnet blir printet
            int navnelengde = p.getNavn().length();
            if (navnelengde > 30){
                topplisteText += p.getNavn().substring(0, 30) + "... - " + p.getHighscore() + " - " + p.getVanskelighetsgrad();
            }
            else{
                topplisteText += p.getNavn() + " - " + p.getHighscore() + " - " + p.getVanskelighetsgrad();
            }
            
        }
        returnList.add(Integer.toString(lengde));
        returnList.add(topplisteText);

        //Returnerer topplisten og lengden på topplisten
        return returnList;
    }

    private void countDownBlankSquares(){
        this.blankSquares--;
    }

    public void rightClick(Square square){
        if (!gameOver()&& square.isHidden()){
            if (square.isFlagged()){
                square.setFlagged(false);
            }
            else{
                square.setFlagged(true);
            }
        }
    }

    public void leftClick(Square square) {
        if (!gameOver() && square.isHidden() && !square.isFlagged()){
            if (square.isBomb()){
                square.setHidden(false);
                setGameOver(true);
            }
            else{
                squarePressed(square);
            }
        }
    }

    private void changeSquare(Square square){
        if (!gameOver() && square.isHidden() && !square.isFlagged() && !square.isBomb()){
            countDownBlankSquares();
            square.setHidden(false);

            //Kaller på seg selv indirekte (rekursjon)
            squarePressed(square);
        }
    }

    private void squarePressed(Square square) {
        int row = getRowAmount();
        int xCord = square.getXCord();
        int yCord = square.getYCord();

        //Hvis ruten ikke har bomber i nærheten
        if (square.getBombsNearby() == 0){

            //Egen rute
            changeSquare(square);
            
            //Venstre rute over
            if (xCord > 0 && yCord > 0){
                changeSquare(getBoardList().get(xCord-1).get(yCord-1));
            }

            //Rute over
            if (yCord > 0){
                changeSquare(getBoardList().get(xCord).get(yCord-1));
            }

            //Høyre rute over
            if (xCord < row-1 && yCord > 0){
                changeSquare(getBoardList().get(xCord+1).get(yCord-1));
            }
            
            //Høyre rute 
            if (xCord < row-1){
                changeSquare(getBoardList().get(xCord+1).get(yCord));
            }

            //Høyre rute under
            if (xCord < row-1 && yCord < row-1){
                changeSquare(getBoardList().get(xCord+1).get(yCord+1));
            }

            //Rute under
            if (yCord < row-1){
                changeSquare(getBoardList().get(xCord).get(yCord+1));
            }

            //Venstre rute under
            if (xCord > 0 && yCord < row-1){
                changeSquare(getBoardList().get(xCord-1).get(yCord+1));
            }

            //Venstre rute 
            if (xCord > 0){
                changeSquare(getBoardList().get(xCord-1).get(yCord));
            }
        }
        
        //Hvis ruten har bomber i nærheten
        else if (square.getBombsNearby() > 0 && square.isHidden() ){
            countDownBlankSquares();
            square.setHidden(false);
        }

        if (getBlankSquares() < 1){
            setWonGame(true);
        }
    }

    public void setWonGame(Boolean wonGame){
        this.wonGame = wonGame;
        if (wonGame){
            this.gameOver = true;
        }
    }

    @Override
    public String toString(){
        if (this.gameOver){
            if (this.wonTheGame()){
                return "GAME OVER\nDu vant!";
            }
            return "GAME OVER\nDu tapte!";
        }
        String kolonne = "";
        for (int x = 0; x < this.getRowAmount(); x++) {
            String rad = "";
            for (int y = 0; y < this.getRowAmount(); y++) {
                rad += (this.getBoardList().get(x).get(y).toString() + ", ");
            }
            kolonne += rad + "\n";
        }
        return kolonne;
    }
    
    //Vanlige gettere og Settere
    public String getDifficulty() {
        return this.difficulty;
    }

    public int getRowAmount() {
            return this.rowAmount;
        }

    public boolean gameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean wonTheGame(){
        return this.wonGame;
    }

    public void setBlankSquares(int blankSquares){
        this.blankSquares = blankSquares;
    }

    public int getBlankSquares(){
        return this.blankSquares;
    }    

    public int getTotalSquares() {
        return this.totalSquares;
    }

    public int getTotalBombs(){
        return this.totalBombs;
    }

    public void setTotalBombs(int totalBombs){
        this.totalBombs = totalBombs;
    }

    public List<List<Square>> getBoardList(){
        return this.boardList;
    }

    public String getFeilIFil(){
        return this.feilIFil;
    }

    public void setFeilIFil(String FeilIFil){
        this.feilIFil = FeilIFil;
    }

    public String getFile(){
        return this.file;
    }

    public void setFile(String file){
        this.file = file;
    }
}

