package org.superbiz.moviefun;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public DataSource albumsDataSource(
            @Value("${moviefun.datasources.albums.url}") String url,
            @Value("${moviefun.datasources.albums.username}") String username,
            @Value("${moviefun.datasources.albums.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public DataSource moviesDataSource(
            @Value("${moviefun.datasources.movies.url}") String url,
            @Value("${moviefun.datasources.movies.username}") String username,
            @Value("${moviefun.datasources.movies.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesContainerEntityManagerFactoryBean(
            DataSource moviesDataSource,
            JpaVendorAdapter hibernateJpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(moviesDataSource);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.movies");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("movies");

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsContainerEntityManagerFactoryBean(
            DataSource albumsDataSource,
            JpaVendorAdapter hibernateJpaVendorAdapter
    ) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(albumsDataSource);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setPackagesToScan("org.superbiz.moviefun.albums");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("albums");

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(
            @Value("${spring.jpa.generate-ddl}") String ddl,
            @Value("${spring.jpa.properties.hibernate.dialect}") String dialect
    ) {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(Boolean.valueOf(ddl));
        jpaVendorAdapter.setDatabasePlatform(dialect);
        jpaVendorAdapter.setDatabase(Database.MYSQL);

        return jpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(
            @Qualifier("moviesContainerEntityManagerFactoryBean")
                        LocalContainerEntityManagerFactoryBean moviesContainerEntityManagerFactoryBean) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(moviesContainerEntityManagerFactoryBean.getObject());

        return jpaTransactionManager;
    }

    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(
            @Qualifier("albumsContainerEntityManagerFactoryBean")
                    LocalContainerEntityManagerFactoryBean albumsContainerEntityManagerFactoryBean) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(albumsContainerEntityManagerFactoryBean.getObject());

        return jpaTransactionManager;
    }
}
