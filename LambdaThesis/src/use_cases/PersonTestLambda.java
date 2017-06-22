package use_cases;
import java.util.List;
import java.util.function.Predicate;

public class PersonTestLambda {
 
    public static void printPersonsWithPredicate(List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }      
    }
   
    public static void main(String[] args){
       
        List<Person> roster = Person.createRoster();
        printPersonsWithPredicate(roster,p -> p.getGender() == Person.Sex.FEMALE && p.getAge() >= 21);
    }
}
