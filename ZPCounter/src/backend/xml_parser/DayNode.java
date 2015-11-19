package backend.xml_parser;

import org.w3c.dom.Node;

public class DayNode {
	
	private Node node;
	public String date = "";
	public Double workedHours = 0.0;
	public Double distance = 0.0;
	public Boolean dayOff = false;
	public Boolean duty8h = false;
	public Boolean dutyGPH = false;
	public Boolean duty500 = false;
	public Boolean duty250 = false;
	public Boolean avtoDay = false;
	public Boolean businessTrip = false;
	public Boolean sick = false;
	public Boolean vacation = false;
	public Boolean vacationWoPay = false;
	public Boolean vacationStudent = false;

	public DayNode(Node node) {
	    this.node = node;
	}
	
	public void parse() throws Exception{
		date = node.getAttributes().getNamedItem("date").getNodeValue().trim();
		workedHours = getDoubleAttribute("workedHours");
		if (workedHours >= 24.0){
            throw new Exception("More than 24h Worked Hours in one day.");
		}
		distance = getDoubleAttribute("distance");
		if (distance >= 3000.0){
            throw new Exception("More than 3000km Dictanse in one day.");
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
	
	private Boolean getBoolAttribute(String name){
		String atr = node.getAttributes().getNamedItem(name).getNodeValue().trim();
		Boolean boolAtr = false;
		if (atr.startsWith("1")){
			boolAtr = true;
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
