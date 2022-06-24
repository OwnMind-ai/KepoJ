package ai.engine.kepoj.anotations;

import java.lang.annotation.*;

/**
 * Refers to a method of entity class that will be used as agent reaction for input data
 * @since 1.2
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
    String name();

    int id() default -1;

    double threshold() default 0;
}
