package detail;

public class TestObserver {

	public static void main(String[] args) {
		
		Home f = new Home(false);
		f.addListener(() -> System.out.println("First Observer"));
		f.setAlarm(false);
		f.setAlarm(true);
				
		f.addListener(() -> System.out.println("Second Observer"));
		f.setAlarm(true);
		f.setAlarm(false);
	}
	
}
