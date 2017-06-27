package use_cases;
import java.util.ArrayList;
import java.util.List;

public class PersonTestStreamLambda {
 
    public static void main(String[] args){
       
        List<Person> roster = Person.createRoster();
       
        roster.stream().
            filter(p -> p.getGender() == Person.Sex.MALE).
                forEach(System.out::println);;
       
      
       System.out.println(
        	roster.stream().
        		filter(p -> p.getGender() == Person.Sex.MALE ).
        			mapToDouble(p -> p.getAge()).
        				average().
        					getAsDouble());

       
        System.out.println(
        	roster.stream().
       			filter(p -> p.getGender() == Person.Sex.MALE).
       				reduce((x,y) -> y).
       					get());
       
        System.out.println(
        		roster.stream().
        			filter(p -> p.getGender() == Person.Sex.MALE).
        				mapToInt(p -> p.getAge()).
        					reduce(0,(x,y) -> x + y));
        
                
    }
}
