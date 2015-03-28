package bka.scouting;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bart Kampers
 */
public class Member {

    
    public Member(String firstName, String surnamePrefix, String surName) {
        this.firstName = firstName;
        this.surnamePrefix = surnamePrefix;
        this.surname = surName;         
    }
    
    
    public String getSurname() {
        return this.surname;
    }
    
    
    public String getSurnamePrefix() {
        return this.surnamePrefix;
    }


    public String getFirstName() {
        return this.firstName;
    }
    
    
    public Date getDateOfBirth() {
        java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Object dateObject = features.get("Geboortedatum");
        try {
            if (dateObject instanceof List && ((List) dateObject).size() > 0) {
                return format.parse(((List) dateObject).get(0).toString());
            }
        }
        catch (ParseException ex) {
            Logger.getLogger(Member.class.getName()).log(Level.WARNING, ex.getMessage());
        }
        return null;
    }
    
    
    public String getDomicile() {
        Object addressObject = getFeature("Adres");
        if (addressObject instanceof List && ((List) addressObject).size() > 2) {
            return ((List) addressObject).get(2).toString();
        }
        return null;
    }


    public Map getFeatures() {
        return new HashMap(features);
    } 
    
    
    public List getFeature(String tag) {
        return (List) features.get(tag);
    }
    
    
    public void addFeature(String tag, String value) {
        if (value != null) {
            List values = (List) features.get(tag);
            if (values == null) {
                values = new ArrayList();
                features.put(tag, values);
            }
            values.add(value);
        }
    }


    public String fullName() {
        String name = firstName + " ";
        if (surnamePrefix != null && surnamePrefix.length() > 0) {
            name += surnamePrefix + " ";
        }
        name += surname;
        return name;
    }
    
    
    public String fullSurname() {
        String name = surname;
        if (surnamePrefix != null && surnamePrefix.length() > 0) {
            name = Character.toUpperCase(surnamePrefix.charAt(0)) + surnamePrefix.substring(1) + " " + name;
        }
        return name;
    }
    
    
    public String alphabeticalSurname() {
        String name = surname;
        if (surnamePrefix != null && surnamePrefix.length() > 0) {
            name += ", " + surnamePrefix;
        }
        return name;
    }


    private final String surname;
    private final String surnamePrefix;
    private final String firstName;

    private final Map features = new HashMap();
    
}
