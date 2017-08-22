package exception;

import java.io.IOException;
import java.util.function.Consumer;

public class LambdaException {
	
	@FunctionalInterface
	public interface ThrowingConsumerIOE<T> {
		void accept(T t) throws IOException;
	}
	
	@FunctionalInterface
	public interface ThrowingConsumer<T, E extends Exception> {
		void accept(T t) throws E;
	}

	
	//For unchecked exception
	public static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> clazz) {
		return i -> {
			try {
				consumer.accept(i);
			} catch (Exception ex) {
				try {
					
					E exCast = clazz.cast(ex);
	                System.err.println(exCast.getClass());
				} catch (ClassCastException ccEx) {
					throw ex;
				}
				}
	    };
	 }
	
	
	//For checked exception
	
	public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
		
		ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
	  
			return i -> {
				try {
					throwingConsumer.accept(i);
			    } catch (Exception ex) {
			    	try {
			    		E exCast = exceptionClass.cast(ex);
			            System.err.println("Exception occured : " + exCast.getMessage());
			        } catch (ClassCastException ccEx) {
			            throw new RuntimeException(ex);
			        }
			    }
			};
	}
	
	public static <T> Consumer<T> handlingConsumerWrapperIOE(
			
			ThrowingConsumerIOE<T> throwingConsumer) {

				return i -> {
					try {
						throwingConsumer.accept(i);
				    } catch (IOException ex) {
				            System.err.println("Exception occured : IOException ");
				    }
				};
		}	
	
	
	 
	 
}
