package com.erval.argos.process.adapters.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import com.erval.argos.process.core.domain.PageRequest;
import com.erval.argos.process.core.domain.SortDirection;
import com.erval.argos.process.core.domain.job.JobStatus;
import com.erval.argos.process.core.domain.job.JobType;
import com.erval.argos.process.core.domain.job.ProcessJob;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import com.erval.argos.process.adapters.mongo.model.ProcessJobDocument;
import com.erval.argos.process.adapters.mongo.repositories.ProcessJobMongoRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.convert.Jsr310Converters;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class MongoProcessJobRepositoryAdapterIT {

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    private static MongoClient client;
    private static ProcessJobMongoRepository repo;
    private static MongoProcessJobRepositoryAdapter adapter;

    @BeforeAll
    static void setup() {
        client = MongoClients.create(mongo.getReplicaSetUrl());
        var factory = new SimpleMongoClientDatabaseFactory(client, "process-test");
        var conversions = new MongoCustomConversions(List.copyOf(Jsr310Converters.getConvertersToRegister()));
        var context = new MongoMappingContext();
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        context.afterPropertiesSet();
        var converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), context);
        converter.setCustomConversions(conversions);
        converter.afterPropertiesSet();
        var template = new MongoTemplate(factory, converter);
        var repoFactory = new MongoRepositoryFactory(template);
        repo = repoFactory.getRepository(ProcessJobMongoRepository.class);
        adapter = new MongoProcessJobRepositoryAdapter(repo);
    }

    @AfterAll
    static void teardown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    void saveAndFindById_roundTrip() {
        var job = new ProcessJob("job-1", JobType.REPORT_PDF, "dev-1", "Device", JobStatus.REQUESTED, Instant.now());

        adapter.save(job);
        var found = adapter.findById("job-1");

        assertTrue(found.isPresent());
        assertEquals(JobStatus.REQUESTED, found.get().status());
    }

    @Test
    void findAllPage_returnsPage() {
        repo.deleteAll();
        repo.save(ProcessJobDocument.fromDomain(
                new ProcessJob("job-1", JobType.REPORT_PDF, "dev-1", "Device", JobStatus.REQUESTED, Instant.now())));
        repo.save(ProcessJobDocument.fromDomain(
                new ProcessJob("job-2", JobType.REPORT_PDF, "dev-2", "Device", JobStatus.DONE, Instant.now())));

        var pageRequest = new PageRequest(0, 1, "createdAt", SortDirection.DESC);
        var page = adapter.findAll(pageRequest);

        assertNotNull(page);
        assertEquals(1, page.content().size());
        assertEquals(0, page.page());
    }

    @Test
    void findAll_returnsAllJobs() {
        repo.deleteAll();
        repo.save(ProcessJobDocument.fromDomain(
                new ProcessJob("job-1", JobType.REPORT_PDF, "dev-1", "Device", JobStatus.REQUESTED, Instant.now())));
        repo.save(ProcessJobDocument.fromDomain(
                new ProcessJob("job-2", JobType.REPORT_PDF, "dev-2", "Device", JobStatus.DONE, Instant.now())));

        var jobs = adapter.findAll();

        assertEquals(2, jobs.size());
    }
}
