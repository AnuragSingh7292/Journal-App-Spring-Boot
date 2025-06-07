//package com.example.employeesApp.service;
//
//import com.example.employeesApp.employeeEntities.Users;
//import com.example.employeesApp.repository.userRepo;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import static org.mockito.Mockito.*;
//
//public class Testing {
//
//    @InjectMocks
//    private UserDetailsServiceImpl userDetailsService;
//    @Mock
//    private userRepo userRepo;
//    @BeforeEach
//    public void setup()
//    {
//        MockitoAnnotations.initMocks(this);
//    }
//    @Test
//    public void loadUserByUsernameTest(){
//        when(userRepo.findByUsername(ArgumentMatchers.anyString())).thenReturn((Users) User.builder().username("ram").password("inkahodh").roles("admin").build());
//        UserDetails user = userDetailsService.loadUserByUsername("ram");
//        Assertions.assertNotNull(user);
//
//    }
//
//}
