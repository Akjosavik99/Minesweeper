package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class SquareTest {
    private Square vanligRute;
    private Square bombeRute;
    private Square tallRute;

    @BeforeEach
    public void setup(){
        vanligRute = new Square(false, 0, 0, 0, 0, 0);
        bombeRute = new Square(true, 0, 0, 0, 0, 0);
        tallRute = new Square(false, 6,0,0,0,0);
    }
    
    @Test
    @DisplayName("Tester at settere med logikk fungerer slik de skal")
    public void testSquareCondition(){
        assertTrue(vanligRute.isHidden());
        assertFalse(vanligRute.isFlagged());
        vanligRute.setHidden(false);
        vanligRute.setFlagged(true);
        assertFalse(vanligRute.isHidden());
        assertFalse(vanligRute.isFlagged());

        assertTrue(bombeRute.isHidden());
        assertFalse(bombeRute.isFlagged());
        bombeRute.setFlagged(true);
        bombeRute.setHidden(false);
        assertTrue(bombeRute.isHidden());
        assertTrue(bombeRute.isFlagged());

        bombeRute.setFlagged(false);
        bombeRute.setHidden(false);
        assertFalse(bombeRute.isHidden());
        assertFalse(bombeRute.isFlagged());
    }

    @Test
    @DisplayName("Tester at toString() fungerer slik den skal")
    public void testToString(){
        assertEquals("H", bombeRute.toString());
        assertEquals("H", vanligRute.toString());
        assertEquals("H", tallRute.toString());
        tallRute.setFlagged(true);
        assertEquals("F", tallRute.toString());
        bombeRute.setHidden(false);
        vanligRute.setHidden(false);
        tallRute.setFlagged(false);
        assertEquals("X", bombeRute.toString());
        assertEquals("0", vanligRute.toString());
        assertEquals("H", tallRute.toString());
    }

    //Grunnen til at jeg ikke gjør noe testing på x/y-koordinater samt gridPaneIndex og gridPaneIndexText er at disse attributtene ikke har noe bruksområde for annet enn JavaFX. De blir brukt til å lagre informasjon om hvor ruten skal tegnes, og eventuelt hvordan dette skal kobles sammen med tallet som skal tegnes på ruta.
}
