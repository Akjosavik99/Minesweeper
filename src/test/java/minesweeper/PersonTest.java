package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersonTest {

    private Person lett1;
    private Person lett2;
    private Person normal1;
    private Person normal2;
    private Person normal3;
    private Person vanskelig1;
    private Person vanskelig2;
    private Person umulig1;
    private Person umulig2;
    private Person random;

    @BeforeEach
    public void setup(){
        lett1 = new Person("Lett1", 0, "Lett");

        normal1 = new Person("Normal1", 0, "Normal");

        vanskelig1 = new Person("Vanskelig1", 0, "Vanskelig");

        umulig1 = new Person("Umulig1", 0, "Umulig");
    }

    @Test
    @DisplayName("Tester at personer på samme nivå blir sortert riktig")
    public void testSammeNivå() {
        lett2 = new Person("Lett2", 999, "Lett");
        normal2 = new Person("Normal2", -100, "Normal");
        vanskelig2 = new Person("Vanskelig2", 0, "Vanskelig"); //Lik vanskelig1
        assertEquals(-999, lett1.compareTo(lett2));
        assertEquals(100, normal1.compareTo(normal2));
        assertEquals(0, vanskelig1.compareTo(vanskelig2));
    }


    @Test
    @DisplayName("Tester at personer med forskjellig vanskelighetsgrad blir sortert riktig")
    public void testForskjelligVanskelighetsgrad() {
        assertEquals(1, lett1.compareTo(normal1));
        assertEquals(1, lett1.compareTo(vanskelig1));
        assertEquals(1, lett1.compareTo(umulig1));

        assertEquals(-1, normal1.compareTo(lett1));
        assertEquals(1, normal1.compareTo(vanskelig1));
        assertEquals(1, normal1.compareTo(umulig1));

        assertEquals(-1, vanskelig1.compareTo(lett1));
        assertEquals(-1, vanskelig1.compareTo(normal1));
        assertEquals(1, vanskelig1.compareTo(umulig1));

        assertEquals(-1, umulig1.compareTo(lett1));
        assertEquals(-1, umulig1.compareTo(normal1));
        assertEquals(-1, umulig1.compareTo(vanskelig1));
    }

    @Test
    @DisplayName("Tester feilstavet/ukjent vanskelighetsgrad blir sortert dårligst")
    public void testFeilIString() {
        umulig2 = new Person("umulig2", -100, "umulig"); //Legg merke til liten u
        assertEquals(-1, vanskelig1.compareTo(umulig2));

        normal3 = new Person("normal3", 100, "NORMAL");
        assertEquals(-1, vanskelig1.compareTo(normal3));

        random = new Person("random", 0, "RandomBokstaverIVanskelighetsgraden");
        assertEquals(-1, lett1.compareTo(random));
    }

    @Test
    @DisplayName("Tester at toString returnerer riktig string")
    public void testToString(){
        assertEquals("Lett1 (0, Lett)", lett1.toString());
        assertEquals("Normal1 (0, Normal)", normal1.toString());
        assertEquals("Vanskelig1 (0, Vanskelig)", vanskelig1.toString());
        assertEquals("Umulig1 (0, Umulig)", umulig1.toString());
    }
}
