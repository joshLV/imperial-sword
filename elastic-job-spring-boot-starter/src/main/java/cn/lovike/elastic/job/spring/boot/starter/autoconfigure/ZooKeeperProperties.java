package cn.lovike.elastic.job.spring.boot.starter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 从配置文件注入 ZooKeeper 参数
 *
 * @author lovike
 * @since 2020/6/30
 */
@Data
@ConfigurationProperties(prefix = "elastic.job.zookeeper")
public class ZooKeeperProperties {
    /**
     * ZooKeeper 服务器地址
     */
    private String serverLists;
    /**
     * ZooKeeper 命名空间
     */
    private String namespace;
}
