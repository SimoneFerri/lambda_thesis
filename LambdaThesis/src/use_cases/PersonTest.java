package use_cases;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
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
    public void testLastWithReduceOneArg(){
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
    public void testReduceSum2Args(){
    	//With Lambda
    	int lambdaSum = roster.stream().
          					filter(p -> p.getGender() == Person.Sex.MALE).
          				  		mapToInt(p -> p.getAge()).
          				  			reduce(0,(x,y) -> x + y);
        
    	//Without lambda
    	int noLambdaSum =0;
    	for (Person p : roster) {
    		if (p.getGender() == Person.Sex.MALE) {
    			noLambdaSum+= p.getAge();
            }
        }
      
    	assertEquals(lambdaSum,noLambdaSum);
    }
    
    @Test
    public void testReduce3Args(){
    	//With Lambda
    	int lambdaSum = roster.stream().
          					filter(p -> p.getGender() == Person.Sex.MALE).
          				  			reduce(0,(a,b) ->  b.getAge() + a,(x,y) -> x + y);
 
    	//Without lambda
    	int noLambdaSum =0;
    	for (Person p : roster) {
    		if (p.getGender() == Person.Sex.MALE) {
    			noLambdaSum+= p.getAge();
            }
        }
      
    	assertEquals(lambdaSum,noLambdaSum);
    }
    
    
	@Test
	public void testCollectThreeArg() {
		//With lambda
		ArrayList<LocalDate> lambda = roster.stream().
								  	  	filter(p -> p.getGender() == Person.Sex.MALE).
								  	  		map(p -> p.getBirthday()).
								  	  			collect(() -> new ArrayList<LocalDate>(),
								  	  					(c,e) -> c.add(e), 
								  	  					(c1,c2) -> c1.addAll(c2));
	
		//Without Lambda
		ArrayList<LocalDate> noLambda = new ArrayList<LocalDate>();
		for(Person p : roster){
			if(p.getGender() == Person.Sex.MALE){
				noLambda.add(p.getBirthday());
			}
		}
		
		assertEquals(lambda,noLambda);
	}
	
	
	private static ArrayList<LocalDate> toArrayList(LocalDate data){
		ArrayList<LocalDate> r = new ArrayList<LocalDate>();
		r.add(data);
		return r;
	}
	
	private static ArrayList<LocalDate> AddAll(ArrayList<LocalDate> l1,ArrayList<LocalDate> l2){
		for(int i = 0; i<l2.size(); i++){
			l1.add(l2.get(i));
		}
		return l1;
	}
	
	
	@Test
	public void testReduceWorksLCollect2Args(){
		ArrayList<LocalDate> lambda = new ArrayList<LocalDate>();
		
		
		lambda = roster.stream().
			   	filter(p -> p.getGender() == Person.Sex.MALE).
			   		map(p ->  PersonTest.toArrayList(p.getBirthday())).
			   			reduce(lambda,(c1,c2) -> PersonTest.AddAll(c1, c2));
			   			
		ArrayList<LocalDate> noLambda = new ArrayList<LocalDate>();
		for(Person p : roster){
			if(p.getGender() == Person.Sex.MALE){
				noLambda.add(p.getBirthday());
			}
		}
		
		assertEquals(lambda,noLambda);
	}
	
	@Test
	public void testCollectGroupBy(){
		//With Lambda
		Map<Person.Sex, List<Person>> byGenderLambda =
			    roster
			        .stream()
			        	.collect(Collectors.groupingBy(Person::getGender));


		//Without lambda
		Map<Person.Sex, List<Person>> byGenderNoLambda =  new HashMap<Person.Sex , List<Person>>();
		
		for (Person s : roster) {
		    Person.Sex g  = s.getGender();
		    if(byGenderNoLambda.containsKey(g)){
		        List<Person> list = byGenderNoLambda.get(g);
		        list.add(s);

		    }else{
		        List<Person> list = new ArrayList<Person>();
		        list.add(s);
		        byGenderNoLambda.put(g, list);
		    }		
		}
			
		assertEquals(byGenderLambda,byGenderNoLambda);
	}
	
	
	@Test
	public void testPartitionBy(){
		//lambda
		Map<Boolean, List<Person>> byGenderLambda =
			    roster
			        .stream()
			        	.collect(Collectors.partitioningBy(p -> p.getGender() == Person.Sex.MALE));

		//no lambda
		
		List<Person> male = new ArrayList<Person>();
		List<Person> female = new ArrayList<Person>();
		
		for(Person p : roster){
			if(p.getGender() == Person.Sex.MALE)
				male.add(p);
			else
				female.add(p);
		}
		
		assertEquals(byGenderLambda.get(true), male );
		assertEquals(byGenderLambda.get(false), female);
	}
     
}
