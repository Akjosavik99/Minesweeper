package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveToFileTest {
    SaveToFile saveToFile = new SaveToFile();
    Board board;
    List<Person> toppliste = new ArrayList<>();

    @BeforeEach
    public void setup(){
        board = new Board("Lett", 5);
        board.setFeilIFil("");
        toppliste.add(new Person("Politimester Bastian", 15, "Lett"));
    }

    @Test
    @DisplayName("Tester at readFile legger til person i riktig rekkefølge og lager toppliste")
    public void testReadFile(){
        try {
            saveToFile.readFile("testFil", toppliste);
        } catch (Exception e) {
            fail("Fant ikke test-filen");
        }

        List<Person> topplisteFasit = new ArrayList<Person>();
        //Legger til person i vilkårlig rekkefølge slik at de må sorteres fra best til dårligst
        topplisteFasit.add(new Person("Jonatan", 20, "Lett"));
        topplisteFasit.add(new Person("Jesper", 10, "Lett"));
        topplisteFasit.add(new Person("Kasper", 10, "Normal"));
        topplisteFasit.add(new Person("Politimester Bastian", 15, "Lett"));
        

        //Må generer string for så å kunne sammenligne disse
        assertEquals(board.generateTopList(toppliste).get(1),board.generateTopList(topplisteFasit).get(1));
        assertEquals(board.generateTopList(toppliste).get(0),board.generateTopList(topplisteFasit).get(0));
    }

    @Test
    @DisplayName("Tester at man få en FileNotFoundException når man ikke finner filen")
    public void testFileNotFoundExceptionReadFile(){
        assertThrows(
            FileNotFoundException.class,
            () -> saveToFile.readFile("filSomIkkeEksisterer", toppliste),
            "Skulle kommet en FileNotFoundException");
    }

    @Test
    @DisplayName("Tester at man få en IndexOutOfBoundsException når man leser en fil med feil formatering (ekstra linjeskift).")
    public void testIndexOutOfBoundsExceptionReadFile(){
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> saveToFile.readFile("testFilIOB", toppliste),
            "Skulle kommet en IndexOutOfBoundException");
    }

    @Test
    @DisplayName("Tester at writeFile() oppretter filen om den ikke finnes")
    public void testFileNotFoundExceptionWriteFile(){
        byte[] nyFilFasit = null, nyFilOpprettet = null;

        // assertThrows(
        //     NoSuchFileException.class,
        //     () -> {byte[] nyFilFasiten = Files.readAllBytes(Path.of(SaveToFile.getFilePath("testNyFilen")));
        // });

        try {
            saveToFile.writeFile("testNyFilOpprettet", toppliste);
        } catch (FileNotFoundException e) {
            fail("Skal opprette filen om den ikke eksisterer");
        }

        List<Person> topplisteFasit = new ArrayList<Person>();
        topplisteFasit.add(new Person("Politimester Bastian", 15, "Lett"));

        try {
			nyFilFasit = Files.readAllBytes(Path.of(SaveToFile.getFilePath("testNyFil")));
		} catch (IOException e) {
			fail("Fant ikke original test-fil");
		}

        try {
			nyFilOpprettet = Files.readAllBytes(Path.of(SaveToFile.getFilePath("testNyFilOpprettet")));
		} catch (IOException e) {
			fail("Fant ikke nyopprettet test-fil");
		}

        assertNotNull(nyFilFasit);
        assertNotNull(nyFilOpprettet);
        assertTrue(Arrays.equals(nyFilFasit, nyFilOpprettet));
    }

    @AfterAll
	static void teardown() {
		File testNyFilOpprettet = new File(SaveToFile.getFilePath("testNyFilOpprettet"));
		testNyFilOpprettet.delete();
	}

    
}
