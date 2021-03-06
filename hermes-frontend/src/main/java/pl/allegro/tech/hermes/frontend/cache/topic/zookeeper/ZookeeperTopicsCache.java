package pl.allegro.tech.hermes.frontend.cache.topic.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import pl.allegro.tech.hermes.api.Topic;
import pl.allegro.tech.hermes.api.TopicName;
import pl.allegro.tech.hermes.common.config.ConfigFactory;
import pl.allegro.tech.hermes.common.cache.zookeeper.ZookeeperCacheBase;
import pl.allegro.tech.hermes.common.di.CuratorType;
import pl.allegro.tech.hermes.common.exception.InternalProcessingException;
import pl.allegro.tech.hermes.frontend.cache.topic.TopicCallback;
import pl.allegro.tech.hermes.frontend.cache.topic.TopicsCache;

import javax.inject.Named;
import java.util.Collection;
import java.util.Optional;

public class ZookeeperTopicsCache extends ZookeeperCacheBase implements TopicsCache {

    private final GroupsNodeCache groupsNodeCache;

    public ZookeeperTopicsCache(
            @Named(CuratorType.HERMES) CuratorFramework curatorClient,
            ConfigFactory configFactory,
            ObjectMapper objectMapper) {

        super(configFactory, curatorClient, objectMapper);

        groupsNodeCache = new GroupsNodeCache(curatorClient, objectMapper, paths.groupsPath(), executorService);
    }

    @Override
    public void start(final Collection<? extends TopicCallback> callbacks) {
        Preconditions.checkNotNull(callbacks);
        checkBasePath(() -> groupsNodeCache.start(callbacks));
    }

    @Override
    public void stop() {
        try {
            groupsNodeCache.stop();
            executorService.shutdown();
        } catch (Exception ex) {
            throw new InternalProcessingException(ex);
        }
    }

    @Override
    public Optional<Topic> getTopic(TopicName topicName) {
        return groupsNodeCache.getTopic(topicName);
    }
}
