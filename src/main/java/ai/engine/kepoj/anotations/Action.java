package ai.engine.kepoj.anotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
    String name();

    int id() default -1;

    double threshold() default 0;
}
