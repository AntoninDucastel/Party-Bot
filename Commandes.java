package command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Commandes {

		public String name();
		public String description() default "Sans description.";
		public ExecutorType type() default ExecutorType.ALL;
		
		public int power() default 0;
		
		public enum ExecutorType{
			ALL, USER, CONSOLE;
		}

}
