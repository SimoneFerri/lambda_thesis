package parallelism;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.junit.Test;

import use_cases.Person;
import use_cases.Person.Sex;

public class ParallelStreamTest {
	
    List<Person> roster = Person.createRoster();
    String numbers = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 ";
	List<Integer> integers = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
    
	@Test
	public void testInit() {
		int parallelSum = roster.parallelStream().mapToInt(p -> p.getAge()).sum();
		int serialSum = roster.stream().mapToInt(p -> p.getAge()).sum();
		
		int noLambdaSum=0;
		for(Person p : roster){
			noLambdaSum += p.getAge();
		}
		
		assertEquals(parallelSum,serialSum);
		assertEquals(parallelSum,noLambdaSum);
		assertEquals(serialSum,noLambdaSum);
		
	}

	
	@Test
	public void testBaseStream(){
		
		List<Person> parallel = roster.stream().parallel().
												filter(p -> p.getGender() == Sex.MALE).
												collect(Collectors.toList());
	
		List<Person> noParallel = roster.stream().
										filter(p -> p.getGender() == Sex.MALE).
										collect(Collectors.toList());
	
		assertEquals(parallel,noParallel);
		
	}
	
	
	//TO WATCH: Concurrent.Collections(ConcurrentMap) and Collectors(groupingByConcurrent)
	@Test
	public void testConcurrentReduction(){
		Map<Person.Sex, List<Person>> byGender =
			    roster
			        .stream()
			        .collect(
			            Collectors.groupingBy(Person::getGender));
		
		ConcurrentMap<Person.Sex, List<Person>> byGenderConcu =
			    roster
			        .parallelStream()
			        .collect(
			            Collectors.groupingByConcurrent(Person::getGender));
		

		/* It's unsafe
		Map<Person.Sex, List<Person>> byGenderParallNoDet = 
				roster
					.parallelStream() 
					.collect(Collectors.groupingBy(Person::getGender));
		*/
		
		//Deterministic because is one female
		assertEquals(byGender.get(Sex.FEMALE),byGenderConcu.get(Sex.FEMALE));
	
		//Isn't deterministic
		Collections.sort(byGender.get(Sex.MALE));
		Collections.sort(byGenderConcu.get(Sex.MALE));
		assertEquals(byGender.get(Sex.MALE),byGenderConcu.get(Sex.MALE));
	
	}
	
	
	@Test //forEach and peek have side effect
	public void testOrderingSideEffect(){
				
		// IT'S RANDOM
		//integers.parallelStream().forEach(e -> System.out.print(e + " "));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		
		//forEachordered haven't side effect
		integers.parallelStream().forEachOrdered(e -> System.out.print(e + " "));
		assertEquals(baos.toString(),numbers);
		
		//release output
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		
	}
	
	@Test	//MAP don't have side effect with parallelStream
	public void testOrderingNoSideEffect(){
		
		String numbersParallel = integers.parallelStream().
										  map(s -> s.toString() + " ").
										  collect(Collectors.joining());
		
		assertEquals(numbersParallel,numbers);
	}
	
	@Test (expected = ConcurrentModificationException.class)
	public void testInterference(){
	
		    List<String> listOfStrings =
		        new ArrayList<>(Arrays.asList("one", "two"));
		         
		    listOfStrings.stream()
						 .peek(s -> listOfStrings.add("three"))
					     .reduce((a, b) -> a + " " + b)
					     .get();
		    
	}	//ESECUZIONE INIZIA QUANDO GET è INVOCATA E FINISCE QUANDO L'OPERAZIONE GET è COMPLETATA
		//PERCHè LE OPERAZIONI INTERMEDIE SONO LAZY
		//L'ARGOMENTO DI PEEK MODIFICA LO STREAM DURANTE L'ESECUZIONE DELLA PIPELINES E QUINDI ECCEZIONE 
	
	@Test
	public void testStateful(){
		
		//serial stream5
		List<Integer> serialStorage = new ArrayList<>();
	     
		integers
		    .stream()
		    // Don't do this! It uses a stateful lambda expression.
		    .map(e -> { serialStorage.add(e); return e; }).
		    forEachOrdered(e -> e.intValue());
		
		  assertTrue(integers.equals(serialStorage));  
		     
		//Parallel stream with synchronized so parallelStorage is thread safe
		List<Integer> parallelStorage = Collections.synchronizedList(new ArrayList<>());
		integers
		    .parallelStream()
		    // Don't do this! It uses a stateful lambda expression.
		    .map(e -> { parallelStorage.add(e); return e; })		    
		    .forEachOrdered(e -> e.intValue());

		assertFalse(integers.equals(parallelStorage));
	
		//Parallel stream without synchronized
		List<Integer> parallelNoSynch = new ArrayList<>();
		integers
				.parallelStream()
				.map(e -> { parallelStorage.add(e); return e;})
				.forEachOrdered(e -> e.intValue());
		
		//could have null value or not have all the values
		assertFalse(integers.equals(parallelNoSynch));
		
		
	}
	
	
}
