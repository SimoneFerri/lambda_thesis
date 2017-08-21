package detail;

public class TestObserver {

	public static void main(String[] args) {
		
		Subject f = new Subject("one",true);
		f.addListener(() -> System.out.println("First Observer"));
		f.setName("two");
		f.setName("three");
		
		f.addListener(() -> System.out.println("Second Observer"));
		f.setName("four");
		f.setState(false);
	}
	
}
