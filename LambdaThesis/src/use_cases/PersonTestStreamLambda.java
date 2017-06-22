package use_cases;
import java.util.List;

public class PersonTestStreamLambda {
 
    public static void main(String[] args){
       
        List<Person> roster = Person.createRoster();
       
        roster.stream().
            filter(p -> p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21).
                forEach(System.out::println);;
       
        roster.stream().
           	filter(p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 21).
            	map(p -> p.getEmailAddress()).
            		forEach(System.out::println);
           
                
    }
}
