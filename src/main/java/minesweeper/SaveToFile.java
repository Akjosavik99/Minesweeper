package minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SaveToFile implements SaveToFileInterface {

    @Override
    public List<Person> readFile(String filnavn, List<Person> toppliste) throws Exception {
        try (Scanner scanner = new Scanner(new File(getFilePath(filnavn)))){
            //Leser gjennom alle linjer og splitter på "-"
            while (scanner.hasNextLine()){
                String linje = scanner.nextLine();
                String linjeliste[] = linje.split("-");
                toppliste.add(new Person(linjeliste[0].strip(), Integer.parseInt(linjeliste[1].strip()), linjeliste[2].strip()));
            }
            scanner.close();
            
            //Sorterer topplisten og returnerer den
            Collections.sort(toppliste);
            
            return toppliste;
        }
    }

    @Override
    public void writeFile(String filnavn, List<Person> toppliste) throws FileNotFoundException {
        //Sorterer topplisten
        Collections.sort(toppliste);
        try (PrintWriter writer = new PrintWriter(getFilePath(filnavn))){
            boolean firstLine = true;
            //Legger til alle personene i topplisten
            for (Object object : toppliste) {
                Person person = (Person) object;

                //Legger til linjeskift for alle utenom første linje
                if (firstLine){
                    firstLine = false;
                }
                else{
                    writer.append("\n");
                }

                writer.append(person.getNavn() + " - " + person.getHighscore() + " - " + person.getVanskelighetsgrad());
            }
            writer.close();
        }
    }

    //Henter inspirasjon fra snakebird på denne metoden
    public static String getFilePath(String filnavn) {
		return SaveToFile.class.getResource("saves/").getFile() + filnavn + ".txt";
	}
}
