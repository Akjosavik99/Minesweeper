package minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.List;

public class MinesweeperController {

    //Brettet
    private Board board;

    //Brett-egenskaper
    private int rowAmount = 12; //Brettet er laget for 12x12 med passelig antall bomber ift vanskelighetsgrad (kan endres mellom 6-20)
    private int boardSize = 400; //Standard størrelse på vindu
    private double squareSize;

    //Tilstand for spillet
    private String vanskelighetsgrad = "Lett";
    private int sekunder = 0;
    private boolean firstTime = true;

    @FXML
    private Pane boardGrid;

    @FXML
    private Text klokke, vanskelighetsgradTekst;

    @FXML
    Button lett, normal, vanskelig, umulig, restart;

    @FXML
    public void boardMaker() {
        for (int x = 0; x < board.getRowAmount(); x ++){
            for (int y = 0; y < board.getRowAmount(); y ++) {

                //Henter antall bomber for ruten
                int bombsNearby = board.getBoardList().get(x).get(y).getBombsNearby();

                //Transformerer x og y til koordinater for å generere rutenettet
                double xCord = x*squareSize;
                double yCord = y*squareSize;

                //Oppretter en rute på riktig plassering
                Rectangle square = new Rectangle(xCord, yCord, squareSize, squareSize);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);

                //Legger til museklikk-event på ruten
                square.setOnMouseClicked(event -> pressed(event, square));

                //Legger til gridPaneIndex på ruten
                int index = boardGrid.getChildren().size();
                board.getBoardList().get(x).get(y).setGridPaneIndex(index);

                //Legger til ruten i rutenettet
                boardGrid.getChildren().add(square);

                //Sjekker at ruten ikke er en bombe og at det finnes bomber i nærheten
                if (!board.getBoardList().get(x).get(y).isBomb() && bombsNearby > 0){
                    //Oppretter ny tekst med antall bomber i nærheten
                    Text text = new Text();
                    text.setText(Integer.toString(bombsNearby));
                    
                    //Setter diverse styling og koordinater på teksten
                    text.setFont(Font.font ("Verdana", 20));
                    text.setTextAlignment(TextAlignment.CENTER);
                    text.setX(xCord + squareSize/2 - text.getLayoutBounds().getWidth()/2);
                    text.setY(yCord + squareSize/2 + text.getLayoutBounds().getHeight()/3);
                    text.setFill(Color.BLACK);
                    text.setVisible(false);

                    //Legger til gridPaneIndex på teksten
                    int indexText = boardGrid.getChildren().size();
                    board.getBoardList().get(x).get(y).setGridPaneIndexText(indexText);

                    //Legger til teksten i rutenettet
                    boardGrid.getChildren().add(text);
                }
            }
        }
    }

    private void pressed(MouseEvent event, Rectangle square) {
        //Henter ut informasjon om ruten fra listen
        int x =  (int) (square.getX()/squareSize);
        int y = (int) (square.getY()/squareSize);
        Square squareList = board.getBoardList().get(x).get(y);;

        //Sjekker at ikke spillet er ferdig og at ruten er skjult
        if (!board.gameOver()){
            //Venstreklikk
            if (event.getButton().equals(MouseButton.PRIMARY) && !squareList.isFlagged()){
                board.leftClick(squareList);
            }

            //Høyreklikk
            if (event.getButton().equals(MouseButton.SECONDARY)){
                board.rightClick(squareList);
            } 

            //Oppdaterer brettet
            updateBoard(); 

            //Sjekker om spilleren har vunnet
            if (board.getBlankSquares() < 1){
                wonGame();
            }
        }
    }

    private void wonGame() {
        //Lager TextInputDialog som bruker kan skrive inn navn på
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Du vant!!");
        textInputDialog.getDialogPane().setContentText("\nGratulerer. Skriv inn ditt navn til topplisten:\n(NB: Ikke bruk bindestrek '-')");
        textInputDialog.setHeaderText(null);
        textInputDialog.setGraphic(null);

        TextField navnInput = textInputDialog.getEditor();
        textInputDialog.showAndWait();
        String navn = navnInput.getText();

        List<Person> toppliste = board.wonGame(navn, getSekunder(), getVanskelighetsgrad(), board.getFile());

        //Printer topplisten hvis den inneholder noe
        if (toppliste.size() > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Toppliste");
            alert.setGraphic(null);
            alert.setContentText((String) (board.generateTopList(toppliste)).get(1));
            alert.setHeaderText("Topp " + (String) (board.generateTopList(toppliste)).get(0) + ":");
            alert.showAndWait();
        }
        
    }

    private void updateBoard(){
        for (int x = 0; x < board.getRowAmount(); x ++){
            for (int y = 0; y < board.getRowAmount(); y ++) {
                Square square = board.getBoardList().get(x).get(y);
                Rectangle rectangle = (Rectangle) boardGrid.getChildren().get(square.getGridPaneIndex());

                //Endrer farge på alle åpnede ruter
                if (!square.isHidden()){
                    //Tegner tallet på ruten hvis det er bomber i nærheten
                    if (square.getGridPaneIndexText() > 0){
                        boardGrid.getChildren().get(square.getGridPaneIndexText()).setVisible(true);
                    }
                    rectangle.setStyle("-fx-fill: DARKGREY;");
                }

                //Endrer farge på eventuell flaggede/uflaggede ruter
                else if (square.isFlagged()){
                    rectangle.setFill(Color.RED);
                }
                else if (!square.isFlagged()){
                    rectangle.setFill(Color.WHITE);
                }

                if (square.isBomb() && board.gameOver() && !board.wonTheGame()){
                    if (!square.isFlagged()){
                        rectangle.setStyle("-fx-fill: WHITE;"); //Hvit bakgrunn på bomben
                    }
                    //Koordinater for bomben
                    double xCord = x*squareSize;
                    double yCord = y*squareSize;

                    //Lager en sirkel (bombe) som printes
                    double radius = squareSize / 3.0;
                    Circle circle = new Circle(xCord + squareSize/2, yCord + squareSize/2, radius);
                    circle.setFill(Color.BLACK);
                    circle.setStroke(Color.RED);
                    boardGrid.getChildren().add(circle);

                    //Printer X over flaggede bomber
                    if (square.isFlagged()){
                        Text text = new Text("X");
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setFont(Font.font ("Arial", 30));
                        text.setX(xCord + squareSize/2 - text.getLayoutBounds().getWidth()/2);
                        text.setY(yCord + squareSize/2 + text.getLayoutBounds().getHeight()/3);
                        text.setFill(Color.WHITE);
                        boardGrid.getChildren().add(text);
                    }
                }
            }
        }

        // Gir beskjed til spilleren hvis han har tapt
        if (board.gameOver() && !board.wonTheGame()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Du tapte");
            alert.setHeaderText(null);
            alert.setContentText("Du tapte, bedre lykke neste gang!\nFor å spille på ny, trykk på restart.");
            alert.showAndWait();
        }
    }

    public void setDifficulty(ActionEvent event) {
        //Henter ut streng-verdien fra button som trykket
        Button button = (Button) event.getSource();
        String vanskelighetsgrad = button.getText();
        setVanskelighetsgrad(vanskelighetsgrad);

        //Restarter spillet med ny vanskelighetsgrad
        initialize();
    }

    public void restart(){ //Restart-knapp
        initialize();
    }
    
    public void timer(){

        klokke.setText(Integer.toString(sekunder));

        if (firstTime){
            firstTime = false;
            Timeline time = new Timeline();
            time.setCycleCount(Timeline.INDEFINITE);
            KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!board.gameOver()){
                        sekunder++;
                    }
                    klokke.setText(Integer.toString(sekunder));
                }
            });

            time.getKeyFrames().add(frame);
            time.playFromStart();
        }
        else{
            sekunder = 0;
        }
    }
    
    private void printVanskelighetsgrad() {
        vanskelighetsgradTekst.setText(this.getVanskelighetsgrad());
    }
    
    //Gettere og settere
    private int getSekunder() {
        return this.sekunder;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void setVanskelighetsgrad(String vanskelighetsgrad){
        this.vanskelighetsgrad = vanskelighetsgrad;
    }

    public String getVanskelighetsgrad(){
        return this.vanskelighetsgrad;
    }

    private void setSquareSize() {
        this.squareSize = boardSize/board.getRowAmount();
    }

    //Initialize
    public void initialize() {
        Board board = new Board(vanskelighetsgrad, rowAmount);
        setBoard(board);
        board.setFile("highscore");

        //Lager brettet i JavaFX
        setSquareSize();
        boardMaker();

        //Kaller på klokken
        timer();

        //Printer vanskelighetsgraden for øyeblikket
        printVanskelighetsgrad();

        board.setGameOver(false);
    }

}