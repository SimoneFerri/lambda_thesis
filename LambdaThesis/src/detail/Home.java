package detail;

import java.util.ArrayList;
import java.util.List;

public class Home {

	private boolean alarm;
	private List<RemoteControl> list = new ArrayList<RemoteControl>();
	
	public Home(boolean w){
		alarm = w;
		System.out.println("Init alarm is : " + w);
	}
	
	public void addListener(RemoteControl o){
		list.add(o);
	}
	
	public void removeListener(int pos){
		list.remove(pos);
	}
	
	public void notifyObservers(){
		list.forEach(o -> o.alert());
	}
	
	public boolean isAlarm() {
		return alarm;
	}

	public void setAlarm(boolean a) {
		if(this.alarm != a){	
			this.alarm = a;
			System.out.println("CHANGED new state: " + alarm + "\nMy observer are : ");
			notifyObservers();
		}
	}
}
