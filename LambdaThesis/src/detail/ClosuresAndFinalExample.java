package detail;



public class ClosuresAndFinalExample {

	@FunctionalInterface 
	interface Process{
		void process(int i);
	}
	
	public static void main(String[] args) {
		int a = 10;
		/*final */ int b = 20;
		// java 7 must be final 
		doProcess(a, new Process(){
			public void process(int i) {
				//b = 10;
				//error must be final or effective final
				System.out.println( "Anonymous class : " + (b + i));
			}
		});
		
		doProcess(a, i ->{	//b =10; It's error, b must be final or effective final
							System.out.println("Lambda expression : " + (b+i));});
	
		//Another example
		int c[] = {20};
		//Ok, nothing changed
		doProcess(a, i -> System.out.println("Lambda expression with array : " + (i + c[0])));
		//But ...
		doProcess(a, i -> {c[0] = 10; //I have changed c[0] but i don't have an error
						   System.out.println ("Lambda expression with modified value : "+(i + c[0]));
							});
		
		
	}
	
	public static void doProcess(int  i, Process p){
		p.process(i);
	}
}
