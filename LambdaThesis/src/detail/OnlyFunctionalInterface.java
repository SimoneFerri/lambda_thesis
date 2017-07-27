package detail;

public class OnlyFunctionalInterface {
	
	public static void Printer(int i,Process p){
		p.println(i);
		p.print(i+1);
	}
	
	public static void main(String[] args) {
		Printer(10,new Process(){
						public void print(int i) {
							System.out.print(i);
							System.out.println(" Used Anonymous");
						}
						public void println(int i) {
							System.out.println(i);
						}
						
					});
		
		/* The target must be used with the functional interface
		Printer(10,i ->{ System.out.print(i);
						 System.out.println(" Used Lambda");
						});*/
	}
	
}

interface Process{
	void print(int i);
	void println(int i);
}

	
