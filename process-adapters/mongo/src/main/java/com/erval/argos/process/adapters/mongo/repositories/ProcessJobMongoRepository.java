package com.erval.argos.process.adapters.mongo.repositories;

import com.erval.argos.process.adapters.mongo.model.ProcessJobDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessJobMongoRepository extends MongoRepository<ProcessJobDocument, String> {

}
