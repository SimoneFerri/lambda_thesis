package detail;

public class TestObserver {

	public static void main(String[] args) {
		
		Home myHome = new Home(false);
		myHome.addListener(() -> System.out.println("Dad 0"));
		myHome.setAlarm(false);
		myHome.setAlarm(true);
				
		myHome.addListener(() -> System.out.println("Mum 1"));
		myHome.setAlarm(false);
		myHome.removeListener(0);
		myHome.setAlarm(true);
	}
	
}
