/*
** Copyright © Bart Kampers
*/

package bka.scouting;


public class Function {

    public Function(String name, Section section, Member member) {
        this.name = name;
        this.section = section;
        this.member = member;
    }


    public String getName() {
        return name;
    }

    
    public Section getSection() {
        return section;
    }

    
    public Member getMember() {
        return member;
    }

    
    private String name;
    private Section section;
    private Member member;

}
