package use_cases;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.junit.Test;

import java.util.ArrayList;

public class NewTest {

	@Test
  public void testAverage1(){
	    /*
    	Person.Sex s = Person.Sex.MALE;
    	//With lambda
    	Double lambda = roster.stream().
    						filter(p -> p.getGender() == s ).
    							mapToDouble(p -> p.getAge()).
    								average().
    									getAsDouble();

    	Double noLambda = 0.;
    	int cont = 0;
    	
    	//without lambda
    	roster.stream().filter(new Predicate<Person>(){

			@Override
			public boolean test(Person p) {
				return p.getGender() == s;
			}
    		
    	} );*/
    	
		ArrayList<Integer> numbers =  new ArrayList<Integer>();
		numbers.add(1);
		numbers.add(2);
		numbers.add(3);
		int test = 4;
		
		Collections.sort(numbers, new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - test;
				
			}
			
		});
		
		
    }
    
}
