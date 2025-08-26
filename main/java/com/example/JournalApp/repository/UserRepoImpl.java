//package com.example.JournalApp.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class UserRepoImpl {
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    public List<User> getUserForSA() {
//
//        System.out.println("getUserForSA");
//        Query query = new Query();
//        query.addCriteria(Criteria.where("username").is(""));
//
//        System.out.println("ii " +
//                "h");
//        System.out.println(mongoTemplate.find(query, User.class));
//        return mongoTemplate.find(query, User.class);
//    }
//}
