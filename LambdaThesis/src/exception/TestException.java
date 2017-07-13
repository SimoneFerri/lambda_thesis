package exception;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.lambda.Unchecked;
import org.junit.Test;

import io.vavr.API;
import io.vavr.CheckedFunction1;
import io.vavr.collection.Stream;
import javaslang.control.Option;
import javaslang.control.Try;

public class TestException {

	List<Integer> numbers = Arrays.asList(3, 9, 7, 0, 10, 20);

	
	@Test
	public void testException() {
		
		ByteArrayOutputStream hideOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(hideOut));
		String s = "No exception occours";
		
		numbers.forEach(i -> {
			try {
				readFromFile(i);
			} catch (IOException e) {
				System.out.println("IOException");
			}
		});
		
		s = hideOut.toString().replaceAll("\\s+","");
		
		assertEquals("IOException",s);

		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

			
	}
	
	
	@Test  
	public void testUncheckedExc() {

		ByteArrayOutputStream hideOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(hideOut));
		
		ByteArrayOutputStream hideErr = new ByteArrayOutputStream();
		System.setErr(new PrintStream(hideErr));
		
		numbers.forEach(LambdaException.consumerWrapper
							(i -> System.out.println(50 / i), ArithmeticException.class));

		
		assertEquals(ArithmeticException.class + " ",
					 hideErr.toString().replaceAll("\\s+"," "));

		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));

		
		/*	
		Timeit.code(() ->
		System.out.println(
				integers.stream().
				mapToInt(e -> e).
				sum()));
		
		
		Timeit.code(() ->
				System.out.println("Parallel = " + integers.parallelStream().mapToInt(a -> a).sum())
				);
	*/	
	}
	
	@Test 
	public void testCheckedExc(){
		
		ByteArrayOutputStream hideOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(hideOut));
		
		ByteArrayOutputStream hideErr = new ByteArrayOutputStream();
		System.setErr(new PrintStream(hideErr));

		
		numbers.forEach(LambdaException.handlingConsumerWrapper(
											i -> readFromFile(i), IOException.class));
		
		
		assertEquals("Exception occured : IOException ",
					 hideErr.toString().replaceAll("\\s+"," "));

		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));


	}

	//Test with varv e javaslang
	@Test
	public void  testGivenValue(){
		Option<Object> noneOption = Option.of(null);
		Option<Object> someOption = Option.of("val");
		
		assertEquals("None", noneOption.toString());
		assertEquals("Some(val)", someOption.toString());
	}

	//Se non è null mi ritorna il valore che gli assegno, altrimenti se è null mi ritorna
	//il valore che gli dico nella parentesi getOrElse
	@Test
	public void  testGivenNull(){
		String name = null;
		Option<String> nameOption = Option.of(name);
		assertEquals("baeldung", nameOption.getOrElse("baeldung"));
		name = "Test123";
		nameOption = Option.of(name);
		assertEquals("Test123",nameOption.getOrElse("anotherValue"));
	}
	
	@Test
	public void testTry() {
		Try<Integer> computation = Try.of(() -> 1/0);
		
		int result = computation.getOrElse(-1);
		assertEquals(-1, result);			
	}
	
	@Test(expected = IOException.class)
	public void testChecked(){
		CheckedFunction1<Integer,Integer> readFunction = i -> readFromFile(i);
		numbers.stream().map(readFunction.unchecked()).forEach(i -> i.toString());
	}
	
	@Test (expected = IOException.class)
	public void testAPIUncheked(){
		numbers.stream().map(API.unchecked(i -> readFromFile(i))).forEach(i -> i.intValue());;
	}
	
	@Test
	public void testLiftOption(){
		List<Integer> doublesFromNumbers = new ArrayList<Integer>();
		numbers.stream().
				map(CheckedFunction1.lift(i -> readFromFile(i))).
				map(k -> k.getOrElse(-1)).forEach(p -> doublesFromNumbers.add(p));
		 
		List<Integer> doublesCorrect = Arrays.asList(9,81,49,-1,100,400);
		
		assertEquals(doublesFromNumbers,doublesCorrect);
	}
	
	//Test with JOOQ/JOOL
	
	@Test (expected = UncheckedIOException.class)
	public void testUnchekedJooQ(){
		numbers.stream().forEach(Unchecked.consumer(e -> readFromFile(e)));
	}
	
	//CHEKED EXCEPTION WITHOUT TRY/CATCH OR THROWS
	@Test (expected = Exception.class)
	public void testUnchekedWithoutNoticing(){
		int age = -1;
		
		if(age > 0)
			System.out.println("Your age is: " + age);
		else
			Unchecked.throwChecked(new Exception());
	}
	
	@Test  (expected = Exception.class)
	public void testPredicate(){
		Stream.of("a","b","c").filter(Unchecked.predicate( s -> { if (s.equals("a")) throw new Exception();
																				return true;}));		
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testPredicateWithoutJool(){
		Stream.of("a","b","c").filter( s -> { if (s.equals("a"))
			try {
				throw new Exception();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		return true;});	
	}
	
	@Test  (expected = IllegalStateException.class)
	public void testPredicateWithHandler(){
		Stream.of("a","b","c").filter(Unchecked.predicate( s -> { if (s.equals("a")) throw new Exception();
																				return true;},
																	e -> {throw new IllegalStateException(e);}));
	}
	
	private static Integer readFromFile(Integer i) throws IOException {
		if (i == 0) {
			throw new IOException("IOException"); // mock IOException
	    }
	    return i * i;
	}
	
	
	
}
