package cn.lovike.elastic.job.spring.boot.starter.autoconfigure;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 简单作业配置
 *
 * @author lovike
 * @since 2020/6/30
 */
@Configuration
@ConditionalOnBean(CoordinatorRegistryCenter.class)
@AutoConfigureAfter(ZooKeeperAutoConfiguration.class)
public class SimpleJobAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CoordinatorRegistryCenter zookeeperRegistryCenter;

    /**
     * @PostConstruct 注解的方法将会在依赖注入完成后被自动调用：
     * 执行顺序为：Constructor > @Autowired > @PostConstruct
     */
    @PostConstruct
    public void initSimpleJob() {
        /**
         * 获取 Spring 上下文中添加了 @SimpleJobScheduled 注解的 Bean
         * Map 中的 Key 为 Bean 的名称小写，Value 为 Bean 的实例对象
         */
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(SimpleJobScheduled.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            // 获取实例对象实现的接口
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> implInterface : interfaces) {
                // 若实例实现的接口为 SimpleJob，则获取添加了该注解的实例中注解的信息，根据相关信息构建任务并启动定时任务
                if (implInterface == SimpleJob.class) {
                    SimpleJobScheduled annotation = instance.getClass().getAnnotation(SimpleJobScheduled.class);
                    String                                                                     jobName    = annotation.jobName();
                    String cron = annotation.cron();
                    int shardingTotalCount = annotation.shardingTotalCount();
                    boolean enabled = annotation.enabled();
                    boolean overwrite = annotation.overwrite();

                    // 根据传入的参数（任务名，时间，分片数）构建 JobCoreConfiguration
                    JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
                            jobName,
                            cron,
                            shardingTotalCount).failover(true).build();

                    // 根据 JobCoreConfiguration 及自定义实现类构建 SimpleJobConfiguration
                    SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(
                            jobCoreConfiguration,
                            instance.getClass().getCanonicalName()); // 全限定类名

                    // 根据 SimpleJobConfiguration 构建 LiteJobConfiguration
                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                            .newBuilder(simpleJobConfiguration)
                            .disabled(!enabled)
                            .overwrite(overwrite).build();

                    // 根据 LiteJobConfiguration 创建作业定时器
                    new SpringJobScheduler((ElasticJob) instance,zookeeperRegistryCenter, liteJobConfiguration).init();
                }
            }
        }
    }
}
