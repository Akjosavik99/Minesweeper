package minesweeper;

import java.io.FileNotFoundException;
import java.util.List;

public interface SaveToFileInterface {
    public List<Person> readFile(String filnavn, List<Person> toppliste) throws Exception;
    public void writeFile(String filnavn, List<Person> toppliste) throws FileNotFoundException;
}
