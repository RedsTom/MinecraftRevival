package me.redstom.revival.api;

import javax.annotation.RegEx;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Load {
}
