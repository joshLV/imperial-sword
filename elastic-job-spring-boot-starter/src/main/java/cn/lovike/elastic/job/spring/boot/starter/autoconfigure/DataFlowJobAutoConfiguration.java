package cn.lovike.elastic.job.spring.boot.starter.autoconfigure;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
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
 * 数据流作业配置
 *
 * @author lovike
 * @since 2020/6/30
 */
@Configuration
@ConditionalOnBean(CoordinatorRegistryCenter.class)
@AutoConfigureAfter(ZooKeeperAutoConfiguration.class)
public class DataFlowJobAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CoordinatorRegistryCenter zookeeperRegistryCenter;

    @PostConstruct
    public void initDataFlowJob() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DataFlowJobScheduled.class);
        for (Map.Entry<String, Object> bean : beans.entrySet()) {
            Object instance = bean.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> implInterface : interfaces) {
                if (implInterface == DataflowJob.class) {
                    DataFlowJobScheduled annotation = instance.getClass().getAnnotation(DataFlowJobScheduled.class);
                    String                                                                       jobName    = annotation.jobName();
                    String cron = annotation.cron();
                    int shardingTotalCount = annotation.shardingTotalCount();
                    boolean enabled = annotation.enabled();
                    boolean overwrite = annotation.overwrite();
                    boolean streamingProcess = annotation.streamingProcess();

                    // 根据传入的参数（任务名，时间，分片数）构建 JobCoreConfiguration
                    JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
                            jobName,
                            cron,
                            shardingTotalCount).failover(true).build();

                    // 根据 JobCoreConfiguration 、自定义实现类及是否流处理构建 DataflowJobConfiguration
                    DataflowJobConfiguration dataflowJobConfiguration = new DataflowJobConfiguration(
                            jobCoreConfiguration,
                            instance.getClass().getCanonicalName(),// 全限定类名
                            streamingProcess);

                    // 根据 DataflowJobConfiguration 及相关参数（定时器是否开启、同名作业是否以最新配置覆盖）构建 LiteJobConfiguration
                    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                            .newBuilder(dataflowJobConfiguration)
                            .disabled(!enabled)
                            .overwrite(overwrite).build();
                    // 根据 LiteJobConfiguration 创建作业定时器
                    new SpringJobScheduler((ElasticJob) instance,zookeeperRegistryCenter, liteJobConfiguration).init();
                }
            }
        }
    }
}
