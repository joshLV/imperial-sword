package cn.lovike.elastic.job.spring.boot.starter.autoconfigure;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lovike
 * @since 2020/6/30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DataFlowJobScheduled {
    String jobName() default "";

    String cron() default "";

    int shardingTotalCount() default 1;

    boolean enabled() default true;

    boolean overwrite() default true;

    boolean streamingProcess() default false;
}
