package use_cases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PersonTestLambda {
	 
    public static void printPersonsWithPredicate(List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) { 
                p.printPerson();
             }
        }      
    }
   
    public static void main(String[] args){

        // With predicate

        List<Person> roster = Person.createRoster();
        printPersonsWithPredicate(roster,p -> p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21);

        // With stream and filter
        
        roster.stream().
		        filter(p -> p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21).
		            forEach(System.out::println);
        
        // mapToDouble and average
        
        System.out.println(
        	    roster.stream().
        	        filter(p -> p.getGender() == Person.Sex.MALE ).
        	            mapToDouble(p -> p.getAge()).
        	                average().
        	                    getAsDouble()); 
        
        // reduce one parameter
        
        System.out.println(
        	    roster.stream().
        	        filter(p -> p.getGender() == Person.Sex.MALE).
        	            reduce((x,y) -> y).
        	                get());

        // reduce two parameter
        
        System.out.println(
        	    roster.stream().
        	        filter(p -> p.getGender() == Person.Sex.MALE).
        	            mapToInt(p -> p.getAge()).
        	                reduce(0,(x,y) -> x + y));

        // reduce three parameter
        
        System.out.println(
                roster.stream()
                    .filter(p -> p.getGender() == Person.Sex.MALE)
                        .reduce(0,(a,b) ->  b.getAge() + a,(x,y) -> x + y));

        // collect
        
        roster.stream().
        filter(p -> p.getGender() == Person.Sex.MALE).
            map(p -> p.getBirthday()).
                collect(() -> new ArrayList<LocalDate>(),
                        (c,e) -> c.add(e),
                        (c1,c2) -> c1.addAll(c2)).
                    forEach(System.out::println); 

        
        //IntStream
        IntStream firstFive = IntStream.range(0,6);
        System.out.println(firstFive.summaryStatistics());

        //DoubleStream
        DoubleStream dS = DoubleStream.of(2.0,4.0,8.0);
        Stream<Double> dStream = dS.boxed();
        dStream.forEach(System.out::println);
    }
}

