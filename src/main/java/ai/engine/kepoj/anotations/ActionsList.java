package ai.engine.kepoj.anotations;

import java.lang.annotation.*;

/**
 * Refers to a method that receive name of action chosen by agent and performs it.
 * @since 1.2
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionsList {
    String[] names();
}
