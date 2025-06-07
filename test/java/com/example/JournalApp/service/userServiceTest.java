//package com.example.employeesApp.service;
//
//import com.example.employeesApp.repository.userRepo;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//public class userServiceTest {
//
//    @Autowired
//    private userRepo userRepo;
//
//    @Disabled
//    @Test
//    public void testUserServiceName() {
//        assertEquals(1,1);
//        assertNotNull(userRepo.findByUsername(""));
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "1,2,3",
//            "3, 4, 7",
//            "1,1,5"
//    })
//    public void test(int a , int b,int expected){
//        assertEquals(expected,a+b);
//    }
//}
