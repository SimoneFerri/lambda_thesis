package detail;

import java.util.ArrayList;
import java.util.List;

public class Subject {

	private String name;
	private boolean state;
	private List<MyObserver> list = new ArrayList<MyObserver>();
	
	public Subject(String n,boolean s){
		name = n;
		state = true;
		System.out.println("Init Subject_name : " + name + " state: " + state);
	}
	
	public void addListener(MyObserver o){
		list.add(o);
	}
	
	public void notifyObservers(){
		for(MyObserver o : list)
			o.update();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(!this.name.equals(name)){
			this.name = name;
			System.out.println("CHANGED new name : " + name + "\nMy observer are : ");
			notifyObservers();
		}
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		if(this.state != state){	
			this.state = state;
			System.out.println("CHANGED new state: " + state + "\nMy observer are : ");
			notifyObservers();
		}
	}

	
	
}
