package ai.engine.kepoj.anotations;

import java.lang.annotation.*;

/**
 * Refers to a field that will be used as an input parameter for agent. Only for double values.
 * @since 1.2
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parameter {
    int id() default -1;
}
