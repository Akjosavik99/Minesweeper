package minesweeper;

public class Person implements Comparable<Person>{
    private String navn;
    private int highscore;
    private String vanskelighetsgrad;

    public Person(String navn, int highscore, String vanskelighetsgrad) {
        this.navn = navn;
        this.highscore = highscore;
        this.vanskelighetsgrad = vanskelighetsgrad;
    }

    @Override
    public int compareTo(Person p2) {
        if (this.getVanskelighetsgrad().equals(p2.getVanskelighetsgrad())){
            return this.getHighscore() - p2.getHighscore();
        }

        if (this.getVanskelighetsgrad().equals("Umulig")){
            return -1; 
        }

        if (this.getVanskelighetsgrad().equals("Vanskelig") && !p2.getVanskelighetsgrad().equals("Umulig")){
            return -1;
        }

        if (this.getVanskelighetsgrad().equals("Normal") && !p2.getVanskelighetsgrad().equals("Vanskelig") && !p2.getVanskelighetsgrad().equals("Umulig")){
            return -1;
        }

        if (this.getVanskelighetsgrad().equals("Lett") && !p2.getVanskelighetsgrad().equals("Normal") && !p2.getVanskelighetsgrad().equals("Vanskelig") && !p2.getVanskelighetsgrad().equals("Umulig")){
            return -1;
        }

        //Feil i vanskelighetsgrad-stringen sorteres dårligst
        return 1;
    }

    @Override
    public String toString(){
        return this.getNavn() + " (" + this.getHighscore() + ", " + this.getVanskelighetsgrad() + ")";
    }

    //Gettere og settere
    public String getNavn() {
    return navn;
    }

    public int getHighscore() {
        return highscore;
    }
    
    public String getVanskelighetsgrad() {
        return vanskelighetsgrad;
    }
}

    
