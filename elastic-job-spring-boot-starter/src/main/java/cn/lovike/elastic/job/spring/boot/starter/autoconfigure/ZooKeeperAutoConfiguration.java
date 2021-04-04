package cn.lovike.elastic.job.spring.boot.starter.autoconfigure;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ZooKeeper 注册中心配置
 *
 * @author lovike
 * @since 2020/6/30
 */
@Configuration
@ConditionalOnProperty("elastic.job.zookeeper.server-lists")
@EnableConfigurationProperties(ZooKeeperProperties.class)
public class ZooKeeperAutoConfiguration {

    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter zookeeperRegistryCenter(ZooKeeperProperties zooKeeperProperties) {
        String serverLists = zooKeeperProperties.getServerLists();
        String namespace = zooKeeperProperties.getNamespace();
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, namespace);
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
