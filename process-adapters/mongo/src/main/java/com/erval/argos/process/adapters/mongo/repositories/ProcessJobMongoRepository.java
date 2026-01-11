package com.erval.argos.process.adapters.mongo.repositories;

import com.erval.argos.process.adapters.mongo.model.ProcessJobDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data repository for process job documents.
 */
public interface ProcessJobMongoRepository extends MongoRepository<ProcessJobDocument, String> {

}
