package detail;

public class TestObserver {

	public static void main(String[] args) {
		
		Home f = new Home(false);
		f.addListener(() -> System.out.println("Dad 0"));
		f.setAlarm(false);
		f.setAlarm(true);
				
		f.addListener(() -> System.out.println("Mum 1"));
		f.setAlarm(true);
		f.setAlarm(false);
		f.removeListener(0);
		f.setAlarm(true);
	}
	
}
