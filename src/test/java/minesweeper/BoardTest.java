package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardTest {
    private Board board;
    private Board boardLett;
    private Board boardNormal;
    private Board boardVanskelig;
    private Board boardUmulig;
    private Board boardUgyldigVanskelighetsgrad;

    @BeforeEach
    public void setup(){
        boardLett = new Board("Lett", 12);
        
        boardNormal = new Board("Normal", 12);

        boardVanskelig = new Board("Vanskelig", 12);

        boardUmulig = new Board("Umulig", 12);

        boardUgyldigVanskelighetsgrad = new Board("TilfeldigTekst", 12);

        board = new Board("Lett", 6);
    }

    @Test
    @DisplayName("Tester at konstruktøren og tilhørende hjelpemetoder fungerer slik de skal")
    public void testBoardConstructor(){
        //Mindre enn 5 rader
        Board smallBoard = new Board("Lett", 2);
        assertEquals(6, smallBoard.getRowAmount());

        //Antall rader
        assertEquals(6, board.getRowAmount());
        assertEquals(12, boardNormal.getRowAmount());

        //Lister med lister med squares (2d-lister)
        assertEquals(6, board.getBoardList().size());
        assertEquals(12, boardNormal.getBoardList().size());

        //Teller totalt antall ruter (blanke og bomber), sjekker at ingen ruter er null
        int boardTotalSquares = 0;
        int boardTotalBombs = 0;
        int boardTotalBlankSquares = 0;
        for (int x = 0; x < board.getRowAmount(); x++) {
            for (int y = 0; y < board.getRowAmount(); y++) {
                assertNotNull(board.getBoardList().get(x).get(y));
                boardTotalSquares++;
                if (board.getBoardList().get(x).get(y).isBomb()){
                    boardTotalBombs++;
                }
                else{
                    boardTotalBlankSquares++;
                }
            }
        }
        assertEquals((36-12), boardTotalBlankSquares);
        assertEquals(12, boardTotalBombs);
        assertEquals(36, boardTotalSquares);

        int boardNormalTotalSquares = 0;
        int boardNormalTotalBombs = 0;
        int boardNormalTotalBlankSquares = 0;
        for (int x = 0; x < boardNormal.getRowAmount(); x++) {
            for (int y = 0; y < boardNormal.getRowAmount(); y++) {
                assertNotNull(boardNormal.getBoardList().get(x).get(y));
                boardNormalTotalSquares++;
                if (boardNormal.getBoardList().get(x).get(y).isBomb()){
                    boardNormalTotalBombs++;
                }
                else{
                    boardNormalTotalBlankSquares++;
                }
            }
        }
        assertEquals((144-18), boardNormalTotalBlankSquares);
        assertEquals(18, boardNormalTotalBombs);
        assertEquals(144, boardNormalTotalSquares);

        //Totalt antall bomber
        assertEquals(12, board.getTotalBombs());
        assertEquals(18, boardNormal.getTotalBombs());

        //Tester at man har tall på rutene rundt bomben eller at de er bomber selv
        for (int x = 1; x < boardVanskelig.getRowAmount()-1; x++) {
            for (int y = 1; y < boardVanskelig.getRowAmount()-1; y++) {
                if (boardVanskelig.getBoardList().get(x).get(y).isBomb()){
                    //Venstre over
                    assertTrue(boardVanskelig.getBoardList().get(x-1).get(y-1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x-1).get(y-1).isBomb()); 

                    //Over
                    assertTrue(boardVanskelig.getBoardList().get(x-1).get(y).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x-1).get(y).isBomb());   

                    //Høyre over
                    assertTrue(boardVanskelig.getBoardList().get(x-1).get(y+1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x-1).get(y+1).isBomb()); 

                    //Høyre
                    assertTrue(boardVanskelig.getBoardList().get(x).get(y+1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x).get(y+1).isBomb());  

                    //Høyre ynder 
                    assertTrue(boardVanskelig.getBoardList().get(x+1).get(y-1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x+1).get(y-1).isBomb()); 

                    //Under  
                    assertTrue(boardVanskelig.getBoardList().get(x+1).get(y).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x+1).get(y).isBomb());  

                    //Venstre under  
                    assertTrue(boardVanskelig.getBoardList().get(x+1).get(y-1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x+1).get(y-1).isBomb()); 
                    
                    //Venstre
                    assertTrue(boardVanskelig.getBoardList().get(x).get(y-1).getBombsNearby() > 0 || boardVanskelig.getBoardList().get(x).get(y-1).isBomb());   
                }
            }
        }
    }

    @Test
    @DisplayName("Tester at toString fungerer slik den skal")
    public void testToString(){
        //Lager en string med et uåpnet brett jeg skal sammenligne med
        String kolonne = "";
        for (int x = 0; x < 12; x++) {
            String rad = "";
            for (int y = 0; y < 12; y++) {
                rad += "H" + ", ";
            }
            kolonne += rad + "\n";
        }
        assertEquals(boardLett.toString(), kolonne);

        //Sjekker at ved å trykke på rute (0,0 - venstre hjørne) så vil toStringen endre seg slik. (Tester kun ved tall > 0). Lager en for-loop for å være sikker på at det blir generert et brett med tall > 0 i venstre hjørne. Hopper ut av for-løkken når det er testet.
        Boolean tall = true;
        for (int i = 0; i < 100; i++) {
            Board boardToString = new Board("Umulig", 12);
            if (boardToString.getBoardList().get(0).get(0).getBombsNearby() > 0){
                String kolonne2 = "";
                for (int x = 0; x < 12; x++) {
                    String rad2 = "";
                    for (int y = 0; y < 12; y++) {
                        if (tall){
                            rad2 += String.valueOf(boardToString.getBoardList().get(0).get(0).getBombsNearby())  + ", ";
                            tall = false;
                        }
                        else{
                            rad2 += "H" + ", ";
                        }
                    }
                kolonne2 += rad2 + "\n";
                } 
                boardToString.leftClick(boardToString.getBoardList().get(0).get(0));
                assertEquals(kolonne2, boardToString.toString());
                break; //Hopper ut av loopen når det er testet
            }   
        }

        //Tester at man ved seier får opp riktig tekst
        String fasitSeier = "GAME OVER\nDu vant!";
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                if (!boardNormal.getBoardList().get(x).get(y).isBomb()){
                    boardNormal.leftClick(boardNormal.getBoardList().get(x).get(y));
                }
            }
        }
        assertEquals(fasitSeier, boardNormal.toString());

        //Tester at man ved tap får opp riktig tekst
        String fasitTap = "GAME OVER\nDu tapte!";
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                if (boardVanskelig.getBoardList().get(x).get(y).isBomb()){
                    boardVanskelig.leftClick(boardVanskelig.getBoardList().get(x).get(y));
                }
            }
        }
        assertEquals(fasitTap, boardVanskelig.toString());
    }

    @Test
    @DisplayName("Tester først at man får satt riktig antall bomber alt etter vanskelighetsgrad")
    public void testSetNumberOfBombs(){
        assertEquals(12, boardLett.getTotalBombs());
        assertEquals(18, boardNormal.getTotalBombs());
        assertEquals(24, boardVanskelig.getTotalBombs());
        assertEquals(30, boardUmulig.getTotalBombs());
        assertEquals(12, boardUgyldigVanskelighetsgrad.getTotalBombs());
    }

    @Test
    @DisplayName("Tester at man ved trykk på en skjult rute med ingen bomber i nærheten også vil åpne rutene i nærheten")
    public void testOpenAround(){
        int rader = boardNormal.getRowAmount();
        //Tester rute i venstre topp
        Square venstreTopp = boardNormal.getBoardList().get(0).get(0);
        if (!venstreTopp.isBomb() && venstreTopp.getBombsNearby() == 0 && venstreTopp.isHidden()){
            boardNormal.leftClick(venstreTopp);

            //Sjekker at egen + 3 ruter rundt er åpnet
            assertTrue(!boardNormal.getBoardList().get(0).get(0).isHidden()); //Egen
            assertTrue(!boardNormal.getBoardList().get(0).get(1).isHidden()); //Høyre
            assertTrue(!boardNormal.getBoardList().get(1).get(1).isHidden()); //Høyre ynder
            assertTrue(!boardNormal.getBoardList().get(1).get(0).isHidden()); //Under   
        }

        //Tester rute i høyre topp
        Square høyreTopp = boardNormal.getBoardList().get(0).get(0);
        if (!høyreTopp.isBomb() && høyreTopp.getBombsNearby() == 0 && høyreTopp.isHidden()){
            boardNormal.leftClick(høyreTopp);

            //Sjekker at egen + 3 ruter rundt er åpnet
            assertTrue(!boardNormal.getBoardList().get(0).get(rader).isHidden());   //Egen
            assertTrue(!boardNormal.getBoardList().get(1).get(rader).isHidden());   //Under 
            assertTrue(!boardNormal.getBoardList().get(1).get(rader-1).isHidden()); //Venstre under
            assertTrue(!boardNormal.getBoardList().get(0).get(rader-1).isHidden()); //Venstre
        }

        //Tester rute i høyre bunn
        Square høyreBunn = boardNormal.getBoardList().get(0).get(0);
        if (!høyreBunn.isBomb() && høyreBunn.getBombsNearby() == 0 && høyreBunn.isHidden()){
            boardNormal.leftClick(høyreBunn);

            //Sjekker at egen + 3 ruter rundt er åpnet
            assertTrue(!boardNormal.getBoardList().get(rader).get(rader).isHidden());     //Egen
            assertTrue(!boardNormal.getBoardList().get(rader).get(rader-1).isHidden());   //Venstre
            assertTrue(!boardNormal.getBoardList().get(rader-1).get(rader-1).isHidden()); //Venstre over
            assertTrue(!boardNormal.getBoardList().get(rader-1).get(rader).isHidden());   //Over
        }

        //Tester rute i venstre bunn
        Square venstreBunn = boardNormal.getBoardList().get(0).get(0);
        if (!venstreBunn.isBomb() && venstreBunn.getBombsNearby() == 0 && venstreBunn.isHidden()){
            boardNormal.leftClick(venstreBunn);

            //Sjekker at egen + 3 ruter rundt er åpnet
            assertTrue(!boardNormal.getBoardList().get(rader).get(0).isHidden());   //Egen
            assertTrue(!boardNormal.getBoardList().get(rader-1).get(0).isHidden()); //Over
            assertTrue(!boardNormal.getBoardList().get(rader-1).get(1).isHidden()); //Høyre over
            assertTrue(!boardNormal.getBoardList().get(rader).get(1).isHidden());   //Høyre
        }

        //Tester alle bortsett fra de på kanten av brettet
        for (int x = 1; x < rader-1; x ++){
            for (int y = 1; y < rader-1; y ++) {
                Square square = boardNormal.getBoardList().get(x).get(y);
                if (!square.isBomb() && square.getBombsNearby() == 0 && square.isHidden()){
                    boardNormal.leftClick(square);

                    //Sjekker at egen + 8 ruter rundt er åpnet
                    assertTrue(!boardNormal.getBoardList().get(x).get(y).isHidden());     //Egen
                    assertTrue(!boardNormal.getBoardList().get(x-1).get(y-1).isHidden()); //Venstre over
                    assertTrue(!boardNormal.getBoardList().get(x-1).get(y).isHidden());   //Over
                    assertTrue(!boardNormal.getBoardList().get(x-1).get(y+1).isHidden()); //Høyre over
                    assertTrue(!boardNormal.getBoardList().get(x).get(y+1).isHidden());   //Høyre
                    assertTrue(!boardNormal.getBoardList().get(x+1).get(y+1).isHidden()); //Høyre ynder
                    assertTrue(!boardNormal.getBoardList().get(x+1).get(y).isHidden());   //Under   
                    assertTrue(!boardNormal.getBoardList().get(x+1).get(y-1).isHidden()); //Venstre under
                    assertTrue(!boardNormal.getBoardList().get(x).get(y-1).isHidden());   //Venstre
                }
            }
        }
    }

    @Test
    @DisplayName("Tester at man ved å trykke på en bombe vil tape spillet")
    public void testBomb(){
        int rader = boardVanskelig.getRowAmount();
        assertFalse(boardVanskelig.gameOver());
        assertFalse(boardVanskelig.wonTheGame());
        for (int x = 0; x < rader; x ++){
            for (int y = 0; y < rader; y ++) {
                Square square = boardVanskelig.getBoardList().get(x).get(y);
                if (square.isBomb() && square.isHidden()){
                    boardVanskelig.leftClick(square);
                    break; //Går ut av for-løkken ved første trykk på bombe
                }
            }
        }
        assertTrue(boardVanskelig.gameOver());
        assertFalse(boardVanskelig.wonTheGame());
    }

    @Test
    @DisplayName("Tester at man vinner når alle rutene (bortsett fra bomber) er åpnet")
    public void testWonTheGame(){
        assertFalse(boardVanskelig.gameOver());
        assertFalse(boardVanskelig.wonTheGame());

        int rader = boardVanskelig.getRowAmount();
        for (int x = 0; x < rader; x ++){
            for (int y = 0; y < rader; y ++) {
                Square square = boardVanskelig.getBoardList().get(x).get(y);
                //"Åpner" alle rutene som ikke er bomber
                if (!square.isBomb()){
                    boardVanskelig.leftClick(square);
                }
                if (boardVanskelig.getBlankSquares() > 0){
                    assertFalse(boardVanskelig.gameOver());
                    assertFalse(boardVanskelig.wonTheGame());
                }
            }
        }
        assertEquals(boardVanskelig.getBlankSquares(), 0);
        assertTrue(boardVanskelig.gameOver());
        assertTrue(boardVanskelig.wonTheGame());
    }

    @Test
    @DisplayName("Tester at setWonGame() fungerer slik den skal")
    public void testSetWonTheGame(){
        assertFalse(boardUmulig.gameOver());
        assertFalse(boardUmulig.wonTheGame());
        boardUmulig.setWonGame(true);
        assertTrue(boardUmulig.gameOver());
        assertTrue(boardUmulig.wonTheGame());

        assertFalse(board.gameOver());
        assertFalse(board.wonTheGame());
        board.setGameOver(true);
        assertTrue(board.gameOver());
        assertFalse(board.wonTheGame());
    }

    @Test
    @DisplayName("Tester at generateTopList() returnerer riktig string og lengde på string")
    public void testGenerateTopList(){
        List<Person> toppliste = Arrays.asList(
            new Person("Jonatan", 30, "Normal"),
            new Person("Jesper", 20, "Lett"),
            new Person("Politimester Bastian og Tobias i tårnet", 10, "Lett"));
        
        List<String> returnString = List.of(
            "3", 
            "Jonatan - 30 - Normal\nPolitimester Bastian og Tobias... - 10 - Lett\nJesper - 20 - Lett");

        assertEquals(returnString, boardLett.generateTopList(toppliste));

        boardLett.setFeilIFil("Feilkode");
        List<String> returnString2 = List.of(
            "3", 
            "Feilkode\n\nJonatan - 30 - Normal\nPolitimester Bastian og Tobias... - 10 - Lett\nJesper - 20 - Lett");
        
        assertEquals(returnString2, boardLett.generateTopList(toppliste));
        assertEquals(returnString, boardLett.generateTopList(toppliste));
        boardLett.setFeilIFil("Feilkode");
        assertEquals(returnString2, boardLett.generateTopList(toppliste));
        assertEquals(returnString, boardLett.generateTopList(toppliste));
        assertEquals(returnString, boardLett.generateTopList(toppliste));
    }

    @Test
    @DisplayName("Tester at flagging av ruter fungerer skikkelig")
    public void testRightClick(){
        Square vanligRute = new Square(false, 0, 1, 1, 0, 0);
        
        assertFalse(vanligRute.isFlagged());
        board.rightClick(vanligRute); //Flagget
        assertTrue(vanligRute.isFlagged());
        board.rightClick(vanligRute); //Uflagget
        assertFalse(vanligRute.isFlagged());
        board.rightClick(vanligRute); //Flagget

        vanligRute.setHidden(false); //Vil ikke fungere da ruten er flagget
        assertTrue(vanligRute.isHidden());
        board.rightClick(vanligRute); //Uflagget
        assertFalse(vanligRute.isFlagged());
        vanligRute.setHidden(false);
        assertFalse(vanligRute.isFlagged());
        assertFalse(vanligRute.isHidden());
    }

    @Test
    @DisplayName("Tester om wonGame() fungerer slik den skal")
    //Mye av inspirasjonen fra denne testen er hentet fra snakebird-eksempelet
    public void testWonGame(){
        board.setWonGame(true); //Jukser og sier jeg har vunnet spillet allerede (må gjør det for å kunne kjøre wonGame())

        board.wonGame("Jonatan", 20, "Lett", "testFil2");
        board.wonGame("Jesper", 10, "Lett", "testFil2");
        board.wonGame("Kasper", 10, "Normal", "testFil2");

        byte[] testFil = null, testFil2 = null;

        try {
			testFil2 = Files.readAllBytes(Path.of(SaveToFile.getFilePath("testFil2")));
		} catch (IOException e) {
			fail("Fant ikke nyopprettet test-fil");
		}

        try {
			testFil = Files.readAllBytes(Path.of(SaveToFile.getFilePath("testFil")));
		} catch (IOException e) {
			fail("Fant ikke original test-fil");
		}

        assertNotNull(testFil);
        assertNotNull(testFil2);
        assertTrue(Arrays.equals(testFil, testFil2));
    }
    
    @AfterAll
	static void teardown() {
		File testFil2 = new File(SaveToFile.getFilePath("testFil2"));
		testFil2.delete();
	}
}
