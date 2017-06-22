package intro;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeAndMethod {
   
        public static void showList(List<Integer>list){  
            if(!list.isEmpty()){
                //Java 7
                for(Integer n : list){
                    System.out.println(n);
                }
                //Java 8 with Lambda Expression
                list.forEach(item -> System.out.println(item));
               
                //Java 8 with Method Reference
                list.forEach(System.out::println);
               
            }else System.out.println("list is empty");  
        }  
       
        public static void main(String[] args) {  
            // An old approach(prior to Java 7) to create a list  
            List<Integer> list1 = new ArrayList<Integer>();  
            list1.add(11);  
            showList(list1);  
           
            // Java 7    
            List<Integer> list2 = new ArrayList<>(); // You can left it blank, compiler can infer type  
            list2.add(12);  
            showList(list2);  
           
            //Java 8 Type Inference
            showList(new ArrayList<>());
           
            //Java 7
             showList (Collections.<Integer>emptyList());
           
            //Target Types work only on Java 8 and in the Java 7 the following statement doesn’t compiled
            showList(Collections.emptyList());  
       
        }      
}
