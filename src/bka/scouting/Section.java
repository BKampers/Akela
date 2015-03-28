package bka.scouting;

/**
 *
 * @author bkampers
 */
public class Section {
    
    public Section(String name, String ageId, String secId, Integer minimumAge, Integer maximumAge) {
        this.name = name; 
        this.ageId = ageId;
        this.secId = secId;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public int hashCode() {
        return name.hashCode();
    }


    public boolean equals(Object other) {
        return (other instanceof Section && name.equals(((Section) other).name));
    }
    
    
    public String toString() {
        return name;
    }
    
    
    //private List<Member> members = new Vector<Member>();
    private String name;
    private String ageId;
    private String secId;
    
    private Integer minimumAge;
    private Integer maximumAge;
            
}
