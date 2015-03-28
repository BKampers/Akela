package bka.scouting;


import bka.dom.Find;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class Akela {
    

    public static final String YOUTH_MEMBER_FUNCTION = "jeugdlid";
    public static final String LEADER_FUNCTION       = "leid(st)er";
    public static final String TEAM_LEADER_FUNCTION  = "teamleid(st)er";
    
    public static final String MANAGEMENT_SECTION = "Bestuur";
    
    
    public Akela(String dataPath) {
        String separator = System.getProperty("file.separator");
        if (! dataPath.endsWith(separator)) {
            dataPath += separator;
        }
        xmlSource = dataPath + xmlSource;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.parse(xmlSource);
            Element element = document.getDocumentElement();
            initSections(element);
            initMembers(element);
        }
        catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }


    public Member[] getMembers() {
        Member[] array = new Member[members.size()];
        int i = 0;
        Iterator it = members.iterator();
        while (it.hasNext()) {
            array[i] = (Member) it.next();
            i++;
        }
        return array;
    }
    
    
    public Section[] getSections() {
        Section[] array = new Section[sections.size()];
        int i = 0;
        Enumeration en = sections.elements();
        while (en.hasMoreElements()) {
            array[i] = (Section) en.nextElement();
            i++;
        }
        return array;
    }


    public Function[] getFunctions() {
        Function[] array = new Function[functions.size()];
        int i = 0;
        Iterator it = functions.iterator();
        while (it.hasNext()) {
            array[i] = (Function) it.next();
            i++;
        }
        return array;
    }
    
    
    public Function[] getFunctions(Member member) {
        ArrayList list = new ArrayList();
        Iterator it = functions.iterator();
        while (it.hasNext()) {
            Function function = (Function) it.next();
            if (function.getMember() == member) {
                list.add(function);
            }
        }
        int count = list.size();
        Function[] array = new Function[count];
        for (int i = 0; i < count; i++) {
            array[i] = (Function) list.get(i);
        }
        return array;
    } 


    public Member[] getMembers(Section section, String functionName) {
        List list = new Vector();
        Iterator it = functions.iterator();
        while (it.hasNext()) {
            Function function = (Function) it.next();
            if (function.getSection() == section && functionName.equals(function.getName())) {
                list.add(function.getMember());
            }
        }
        Member[] array = new Member[list.size()];
        int i = 0;
        it = list.iterator();
        while (it.hasNext()) {
            array[i] = (Member) it.next();
            i++;
        }
        return array;
    }


    public Member[] getStaffMembers() {
        Vector list = new Vector();
        Iterator it = functions.iterator();
        while (it.hasNext()) {
            Function function = (Function) it.next();
            if (! YOUTH_MEMBER_FUNCTION.equals(function.getName())) {
                Member member = function.getMember();
                if (! list.contains(member)) {
                    list.add(member);
                }
            }
        }
        int size = list.size();
        Member[] array = new Member[size];
        for (int i = 0; i < size; i++) {
            array[i] = (Member) list.elementAt(i);
        }
        return array;
    }
    
    
    private void initSections(Element element) {
        NodeList maSectionList = Find.nodeList(element, "ma_section");
        for (int i = 0; i < maSectionList.getLength(); i++) {
            Node maSectionItem = maSectionList.item(i);
            if (maSectionItem.getNodeName().equals("section")) {  
                String ageId = Find.content(maSectionItem, "sec_age_id");
//                if (Integer.valueOf(ageId) < 10) {
                    String sectionName = Find.attributeValue(maSectionItem, "fullname");
                    String secId = Find.content(maSectionItem, "sec_id");
                    Integer minimumAge;
                    Integer maximumAge;
                    String ageString = Find.content(maSectionItem, "sec_min_age");
                    try {
                        minimumAge = new Integer(Integer.parseInt(ageString));
                    }
                    catch (NumberFormatException ex) {
                        minimumAge = null;
                    }
                    ageString = Find.content(maSectionItem, "sec_max_age");
                    try {
                        maximumAge = new Integer(Integer.parseInt(ageString));
                    }
                    catch (NumberFormatException ex) {
                        maximumAge = null;
                    }
                    Section section = new Section(sectionName, ageId, secId, minimumAge, maximumAge);
                    sections.put(ageId + secId, section);
//                    System.out.println(ageId + secId + ": " + sectionName);
//                }
            }
        }
    }
    
    
    private void initMembers(Element element) {
        NodeList maPersonList = Find.nodeList(element, "ma_person");
        for (int i = 0; i < maPersonList.getLength(); i++) {
            Node maPersonItem = maPersonList.item(i);
            if (maPersonItem.getNodeName().equals("person")) {
                Member member = new Member(
                    Find.content(maPersonItem, "per_nm"),
                    Find.content(maPersonItem, "per_infix"),
                    Find.content(maPersonItem, "per_surname"));
                members.add(member);
                member.addFeature("Lidnummer", Find.content(maPersonItem, "per_id"));
                member.addFeature("Geboortedatum", Find.content(maPersonItem, "per_birthday"));
                String street =
                    Find.content(maPersonItem, "per_street") +
                    " " +
                    Find.content(maPersonItem, "per_house_no");
                String addition = Find.content(maPersonItem, "per_house_add");
                if (addition != null) {
                    street += addition;
                }
                member.addFeature("Adres", street);
                member.addFeature("Adres", Find.content(maPersonItem, "per_zip"));
                member.addFeature("Adres", Find.content(maPersonItem, "per_city"));
                member.addFeature("Telefoon", Find.content(maPersonItem, "per_telephone"));
                member.addFeature("Telefoon", Find.content(maPersonItem, "per_mobilephone"));
                member.addFeature("Telefoon", Find.content(maPersonItem, "per_free_phone_txt"));
                member.addFeature("Zorgverzekeraar", Find.content(maPersonItem, "per_healthpolicy"));
                member.addFeature("Polisnummer", Find.content(maPersonItem, "per_healthpolicy_no"));
                NodeList maFeature = Find.nodeList(maPersonItem, "ma_feature");
                if (maFeature != null) {
                    for (int f = 0; f < maFeature.getLength(); f++) {
                        Node featureItem = maFeature.item(f);
                        NamedNodeMap map = featureItem.getAttributes();
                        if (map != null) {
                            Node attributeNode = map.getNamedItem("fullname");
                            Node feaTxt = Find.node(featureItem.getChildNodes(), "fea_txt");
                            //member.addFeature(attributeNode.getNodeValue(), feaTxt.getTextContent()); // JDK 1.6
                            member.addFeature(attributeNode.getNodeValue(), Find.textValue(feaTxt)); // JDK 1.4
                        }
                    }
                }
//                System.out.println(member.getFirstName() + " " + member.getInfix() + " " + member.getSurName());
//                Dictionary dictionary = member.getFeatures();
//                Enumeration keys = dictionary.keys();
//                while (keys.hasMoreElements()) {
//                    Object key = keys.nextElement();
//                    Object valueList = dictionary.get(key);
//                    Iterator it = ((java.util.List) valueList).iterator();
//                    while (it.hasNext()) {
//                        System.out.println(" + " + key + ": " + it.next());
//                    }
//                }
                NodeList maFunctionList = Find.nodeList(maPersonItem, "ma_function");
                Node functionNode = Find.node(maFunctionList, "function");
                String functionName = Find.attributeValue(functionNode, "fullname");
//                if (Find.attributeValue(functionNode, "fullname").equals(YOUTH_MEMBER_FUNCTION)) {
                    String ageId = Find.content(functionNode, "fun_age_id");
                    String secId = Find.content(functionNode, "fun_sec_id");
                    Section section = (Section) sections.get(ageId + secId);
                    if (section != null) {
                        Function function = new Function(functionName, section, member);
                        functions.add(function);
//                        section.addMember(member);
                    }
                    else {
                        System.out.println(member.fullName() + ": " + functionName);
                    }
//                }
            }
        }
    }
    
    
    private Hashtable sections = new Hashtable();
    private Vector members = new Vector();
    private Vector functions = new Vector();

    private String xmlSource = "scouting.xml";

}
