package work.rabbi.afis.configurations;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JobRunrConfig {
    @Value("${spring.datasource.secondary.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.secondary.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.secondary.password}")
    private String dataSourcePassword;

    private DataSource createDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(dataSourceUrl);
        dataSourceBuilder.username(dataSourceUsername);
        dataSourceBuilder.password(dataSourcePassword);
        return dataSourceBuilder.build();
    }

    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        PostgresStorageProvider storageProvider = new PostgresStorageProvider(createDataSource());
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }

}
