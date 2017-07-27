package detail;

public class ThisReferenceLambdas {

	interface Process{
		void process(int i);
	}
	
	public void doProcess(int i, Process p){
		p.process(i);
	}
	
	public void execute(){
		doProcess(10, i -> {
							System.out.println("Value of i is: " + i);
							System.out.println(this);
							});
	}
	
	public String toString(){
		return "This is the main ThisReferenceLambdas class instance";
	}
	
	public static void main(String[] args) {
		ThisReferenceLambdas thisLambda = new ThisReferenceLambdas();
		//With Anonymous Class
		thisLambda.doProcess(10, new Process(){
									public void process(int i) {
										System.out.println("Values of i is: " + i);
										// this referred of toString of this anonymous class
										System.out.println(this);
									}
			
			public String toString(){
				return "This is anonymous class instance";
			}
		});
		
		//this.;
		thisLambda.doProcess(10, i ->{ 
								System.out.println("Values of i is: " + i);
								/*Error: cannot used this in a static context
								 * Because "main" is a static method and this is referred
								 * of the class, this is unmodified in a lambda expresson
								 * the values of this reference in lambda is the same as the 
								 * value of of this outside the lambda expression in every part 
								 * of code where it used. Lambda exp not overwrite the this reference
								System.out.println(this);*/});
			
		
		thisLambda.execute();
	}
	
}
