package use_cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamTest {


    List<Person> roster = Person.createRoster();

    Predicate<Person> OnlyMan = new Predicate<Person>(){
		public boolean test(Person t) {
			return t.getGender() == Person.Sex.MALE;
		}	
    };
    
    
	@Test
	public void testAllMatch() {
		assertFalse(roster.stream().allMatch(OnlyMan));
		assertTrue(roster.stream().anyMatch(OnlyMan));
		roster.remove(1);
		assertTrue(roster.stream().allMatch(OnlyMan));
		assertTrue(roster.stream().noneMatch(p -> p.getGender() == Person.Sex.FEMALE));
	}
	
	@Test
	public void testCount(){
		assertEquals(roster.stream().count(), roster.size() );
		assertEquals(roster.stream().filter(OnlyMan).count(), 3 );
	}
	
	@Test
	public void testConcat(){
		Stream<Person> DefaultRoster = Stream.generate(new Supplier<Person>(){
											public Person get() {
												return new Person(
											            "Default",
											            IsoChronology.INSTANCE.date(1980, 1, 1),
											            Person.Sex.MALE,
											            "default@default.com");
										}}).limit(5);
		
		assertEquals(Stream.concat(roster.stream(),DefaultRoster ).count(),9);
	}
	
	@Test
	public void testDistinct(){
		
		roster.add(new Person(
	            "Bob",
	            IsoChronology.INSTANCE.date(2000, 9, 12),
	            Person.Sex.MALE, "bob@example.com"));
		     
		
		assertEquals(roster.stream().distinct().count(), 4);
		
		assertNotEquals(roster.stream().collect(Collectors.toList()),Person.createRoster());
		
		assertEquals(roster.stream().distinct().collect(Collectors.toList()),Person.createRoster());
	}
	
	
	
	@Test
	public void testSortedByName(){
		List<Person> orderedRoster = roster.stream().sorted().collect(Collectors.toList());
		Collections.sort(roster);
		assertEquals(orderedRoster,	roster);
	}
	
	@Test
	public void testSorted2ArgByBirthday(){
		
		List<Person> orderedRosterByBirthday = roster.stream().sorted( new Comparator<Person>(){
													public int compare(Person o1, Person o2) {
														return o2.getBirthday().compareTo(o1.getBirthday());
													}
												}).collect(Collectors.toList());
		
		assertEquals(orderedRosterByBirthday.get(0).getName(),"Bob" );
		
	}
	
	@Test
	public void testFilter(){
		List<Person> menFilter = roster.stream().filter(OnlyMan).
						   						 	collect(Collectors.toList());
		
		List<Person> men = new ArrayList<Person>();
		
		for(Person p : roster){
			if(p.getGender() == Person.Sex.MALE)
				men.add(p);
		}
		
		assertEquals(menFilter,men);
	}
	
	@Test
	public void testFindAnyFirstEmpty(){
		//TEST CON CONTAINS PERCH� NO DETERMINISTICO
		assertTrue(roster.contains(roster.parallelStream().findAny().get()));
		assertEquals(roster.parallelStream().findFirst().get().getName(),"Fred");
		
		Stream <Person> s = Stream.empty();
		assertEquals(s.findAny(), Optional.empty());
	}
	
	@Test
	public void testAllFlatMap(){
	    List<Person> roster2 = Person.createRoster();

	    List<Person> together = Stream.of(roster,roster2).
	    							flatMap(List::stream).
	    								collect(Collectors.toList());
	    roster.addAll(roster2);
	    assertEquals(together,roster);
	    
	    //FLATMAPINT SUM OF AGE'S
	    assertEquals(104,roster2.stream().flatMapToInt(n -> IntStream.of(n.getAge())).sum());
	}
	
	@Test
	public void testForEachOrdered(){
		roster.stream().forEach(new Consumer<Person>(){
									public void accept(Person t) {
										t.setEmail("libero.it");
									}
								});
		
		List<String> newRoster = roster.stream().map(p -> p.getEmailAddress()).collect(Collectors.toList());
	
		assertTrue(newRoster.stream().allMatch(new Predicate<String>(){
													public boolean test(String t) {
														return t.contains("libero.it");
													}						
												}));
	}
	
	@Test
	public void testGenerate(){
		List<Person> DefaultRoster = Stream.generate(new Supplier<Person>(){
											public Person get() {
												return new Person(
											            "Default",
											            IsoChronology.INSTANCE.date(1980, 1, 1),
											            Person.Sex.MALE,
											            "default@default.com");
										}}).limit(5).collect(Collectors.toList());
		
		assertTrue(DefaultRoster.stream().allMatch(t -> t.getName().equals("Default")));
	}
	
	@Test
	public void testIterate(){
		//I primi 5 numeri dispari
		List<Integer> firstEven = Stream.iterate(1, n -> n+2).limit(5).collect(Collectors.toList());
		assertTrue(firstEven.stream().allMatch(n -> n%2 == 1));
		assertEquals(firstEven.toString(),"[1, 3, 5, 7, 9]");
	}
	
	@Test
	public void testMinMax(){
		
		Comparator<? super Object> comp = new Comparator<Object>(){
									public int compare(Object o1, Object o2) {
										Person p1 = (Person)o1;
										Person p2 = (Person)o2;
										return p2.getBirthday().compareTo(p1.getBirthday());
									}
								};
		
		Person p = roster.stream().min(comp ).get();
	
		assertEquals(p, new Person(
            "Bob",
            IsoChronology.INSTANCE.date(2000, 9, 12),
            Person.Sex.MALE, "bob@example.com") );
	
		assertEquals(Stream.empty().max(comp),Optional.empty());
		assertEquals(roster.stream().max(comp).get(),new Person(
											            "Fred",
											            IsoChronology.INSTANCE.date(1980, 6, 20),
											            Person.Sex.MALE,
											            "fred@example.com"));
	}
	
	@Test
	public void testOfandSkip(){
		Stream<Person> sPers = Stream.of(new Person("One", IsoChronology.INSTANCE.date(1980, 1, 1),
													Person.Sex.MALE, "one@example.com"),
										new Person("Two", IsoChronology.INSTANCE.date(1980, 1, 1),
													Person.Sex.MALE, "two@example.com"),
										new Person("Three", IsoChronology.INSTANCE.date(1980, 1, 1),
												Person.Sex.MALE, "three@example.com")).skip(2);
		assertEquals(sPers.count(),1);
		
	}
	
	@Test
	public void testPeek(){
		
		//Use for debugging pipeline
		roster.stream()
        .filter(e -> e.getGender() == Person.Sex.MALE)
        .peek(e -> System.out.println("Filtered value: " + e))
        .map(e -> e.getAge())
        .peek(e -> System.out.println("Mapped value: " + e));
	
	}

}
