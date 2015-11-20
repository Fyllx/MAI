package backend.xml_parser;

import org.w3c.dom.Node;

public class DayNode {
	
	private Node node;
	public String date = "";
	public Double workedHours = 0.0;
	public Double distance = 0.0;
	public int dayOff = 0;
	public int duty8h = 0;
	public int dutyGPH = 0;
	public int duty500 = 0;
	public int duty250 = 0;
	public int avtoDay = 0;
	public int businessTrip = 0;
	public int sick = 0;
	public int vacation = 0;
	public int vacationWoPay = 0;
	public int vacationStudent = 0;

	public DayNode(Node node) {
	    this.node = node;
	}
	
	public void parse() throws Exception{
		date = node.getAttributes().getNamedItem("date").getNodeValue().trim();
		workedHours = getDoubleAttribute("workedHours");
		if (workedHours >= 24.0){
            throw new Exception("More than 24h Worked Hours in one day: " + workedHours);
		}
		distance = getDoubleAttribute("distance");
		if (distance >= 3000.0){
            throw new Exception("More than 3000km Dictanse in one day:" + distance);
		}
		dayOff = getBoolAttribute("dayOff");
		duty8h = getBoolAttribute("duty");
		dutyGPH = getBoolAttribute("dutyGph");
		duty500 = getBoolAttribute("reserv");
		duty250 = getBoolAttribute("reserv2");
		avtoDay = getBoolAttribute("avtoDay");
		businessTrip = getBoolAttribute("businessTrip");
		sick = getBoolAttribute("sick");
		vacation = getBoolAttribute("vacation");
		vacationWoPay = getBoolAttribute("vacationWoPay");
		vacationStudent = getBoolAttribute("vacationStudent");
	}
	
	private int getBoolAttribute(String name){
		String atr = node.getAttributes().getNamedItem(name).getNodeValue().trim();
		int boolAtr = 0;
		if (atr.startsWith("1")){
			boolAtr = 1;
		}
		return boolAtr;
	}
	
	private Double getDoubleAttribute(String name){
		Double d;
		try{
			d = Double.parseDouble(node.getAttributes().getNamedItem(name).getNodeValue().trim());
		}
		catch(Exception e){
			d=0.0;
		}
		return d;
	}

	public StringBuffer show(){
		 StringBuffer content = new StringBuffer();
		 String spase = "	";
		 content.append(spase + spase + date + "\n");
		 content.append(spase +spase +spase + "Worked Hours " + workedHours + "\n");
		 content.append(spase +spase +spase + "Distance " + distance + "\n");
		 content.append(spase +spase +spase + "Day Off " + dayOff + "\n");
		 content.append(spase +spase +spase + "Duty 8h " + duty8h + "\n");
		 content.append(spase +spase +spase + "Duty GPX " + dutyGPH + "\n");
		 content.append(spase +spase +spase + "Duty 500p " + duty500 + "\n");
		 content.append(spase +spase +spase + "Duty 250p " + duty250 + "\n");
		 content.append(spase +spase +spase + "Avto Day " + avtoDay + "\n");
		 content.append(spase +spase +spase + "Business Trip " + businessTrip + "\n");
		 content.append(spase +spase +spase + "Sick " + sick + "\n");
		 content.append(spase +spase +spase + "Vacation " + vacation + "\n");
		 content.append(spase +spase +spase + "Vacation Without Pay " + vacationWoPay + "\n");
		 content.append(spase +spase +spase + "Vacation Student " + vacationStudent + "\n");
		 return content;
	}
	
}
