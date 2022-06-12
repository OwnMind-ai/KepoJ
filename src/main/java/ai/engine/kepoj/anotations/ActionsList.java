package ai.engine.kepoj.anotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionsList {
    String[] names();
}
