package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.ExUser.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExUser.class.getName() + ".merchants");
            createCache(cm, com.mycompany.myapp.domain.Merchant.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Merchant.class.getName() + ".vehicles");
            createCache(cm, com.mycompany.myapp.domain.Merchant.class.getName() + ".exUsers");
            createCache(cm, com.mycompany.myapp.domain.Merchant.class.getName() + ".employeeAccounts");
            createCache(cm, com.mycompany.myapp.domain.Employee.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Employee.class.getName() + ".images");
            createCache(cm, com.mycompany.myapp.domain.EmployeeType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.EmployeeType.class.getName() + ".employees");
            createCache(cm, com.mycompany.myapp.domain.CashBook.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CashBookBalance.class.getName());
            createCache(cm, com.mycompany.myapp.domain.TransactionType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.EmployeeAccount.class.getName());
            createCache(cm, com.mycompany.myapp.domain.EmployeeAccountBalance.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExpenseAccount.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExpenseAccountBalance.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Expense.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Banners.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Banners.class.getName() + ".images");
            createCache(cm, com.mycompany.myapp.domain.ExpenseType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Vehicle.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Images.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Images.class.getName() + ".merchants");
            createCache(cm, com.mycompany.myapp.domain.Images.class.getName() + ".banners");
            createCache(cm, com.mycompany.myapp.domain.Employee.class.getName() + ".vehicles");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
