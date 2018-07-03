package de.gedelmann.reqman.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(de.gedelmann.reqman.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMRequirement.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMRequirement.class.getName() + ".attachements", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMRequirement.class.getName() + ".tags", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMRequirement.class.getName() + ".categories", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMTag.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMTag.class.getName() + ".names", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMCategory.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMCategory.class.getName() + ".names", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMPage.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMPage.class.getName() + ".attachements", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMProject.class.getName(), jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMProject.class.getName() + ".requirements", jcacheConfiguration);
            cm.createCache(de.gedelmann.reqman.domain.RMAttachement.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
