//package com.kenzie.appserver.service;
//
//import com.kenzie.appserver.repositories.model.ExampleRecord;
//import com.kenzie.appserver.repositories.ExampleRepository;
//import com.kenzie.appserver.service.model.Example;
//
//import com.kenzie.capstone.service.model.ExampleData;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ExampleService {
//    private ExampleRepository exampleRepository;
//
//
//    public ExampleService(ExampleRepository exampleRepository) {
//        this.exampleRepository = exampleRepository;
//    }
//
//    public Example findById(String id) {
//
//        // Example getting data from the local repository
//        Example dataFromDynamo = exampleRepository
//                .findById(id)
//                .map(example -> new Example(example.getId(), example.getName()))
//                .orElse(null);
//
//        return dataFromDynamo;
//    }
//}
