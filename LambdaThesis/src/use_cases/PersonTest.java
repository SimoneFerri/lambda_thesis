package use_cases;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;



public class PersonTest {

	
    List<Person> roster = Person.createRoster();

    
    @Test
    public void testMethodprintPersonWithPredicate(){
        
    	//with lambda
    	String lambda = Person.printPersonsWithPredicate(roster,
        				p -> p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21);
        
        //withuot lambda
    	String noLambda = "";
    	for (Person p : roster) {
            if (p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21) {
                noLambda+= p.toString();
            }
        }
        
    	assertEquals("Jane 26 ",lambda);
        assertEquals(lambda,noLambda);
        
    }
	
    @Test
    public void testStreamFilter(){
    	
    	String lambda = roster.stream().
    						filter(p -> p.getGender() ==  Person.Sex.FEMALE && p.getAge() >= 21).
    							map(p -> p.toString()).
    								collect(Collectors.joining());
    	
    	assertEquals(lambda, "Jane 26 ");
            	
    }
    
    @Test
    public void testAverage(){
	    
    	//With lambda
    	Double lambda = roster.stream().
    						filter(p -> p.getGender() == Person.Sex.MALE ).
    							mapToDouble(p -> p.getAge()).
    								average().
    									getAsDouble();

    	Double noLambda = 0.;
    	int cont = 0;
    	
    	//without lambda
    	for (Person p : roster) {
            if (p.getGender() == Person.Sex.MALE) {
                noLambda+= p.getAge();
                cont++;
            }
        }
    	noLambda = noLambda/cont;
    	
    	assertEquals(lambda,noLambda);
    	
    }
    
    
    @Test
    public void testLastWithReduce(){
    	//With lambda
    	Person lambdaLast = roster.stream().
           						filter(p -> p.getGender() == Person.Sex.MALE).
           							reduce((x,y) -> y).
           								get();
    	
    	//without lambda
    	Person noLambdaLast = null;
    	for (Person p : roster) {
            if (p.getGender() == Person.Sex.MALE) {
                noLambdaLast = p;
            }
        }
    	
    	//without lambda better version
    	Person betterNoLambda = roster.get(roster.size() - 1);
    	
    	assertEquals(lambdaLast,noLambdaLast);
    	assertEquals(lambdaLast,betterNoLambda);
    	assertEquals(betterNoLambda,noLambdaLast);
    	
    }
    
  
    @Test
    public void testReduceSum(){
    	
    	int lambdaSum = roster.stream().
          					filter(p -> p.getGender() == Person.Sex.MALE).
          				  		mapToInt(p -> p.getAge()).
          				  			reduce(0,(x,y) -> x + y);
           
    	int noLambdaSum =0;
    	for (Person p : roster) {
    		if (p.getGender() == Person.Sex.MALE) {
    			noLambdaSum+= p.getAge();
            }
        }
      
    	assertEquals(lambdaSum,noLambdaSum);
    	
    }
    
    
	@Test
	public void testCollect() {
		
		ArrayList<LocalDate> lambda = roster.stream().
								  	  	filter(p -> p.getGender() == Person.Sex.MALE).
								  	  		map(p -> p.getBirthday()).
								  	  			collect(() -> new ArrayList<LocalDate>(),
								  	  					(c,e) -> c.add(e), 
								  	  					(c1,c2) -> c1.addAll(c2));
	
		ArrayList<LocalDate> noLambda = new ArrayList<LocalDate>();
		for(Person p : roster){
			if(p.getGender() == Person.Sex.MALE){
				noLambda.add(p.getBirthday());
			}
		}
		
		assertEquals(lambda,noLambda);
		
	}
	
}
