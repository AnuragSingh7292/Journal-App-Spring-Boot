package com.example.JournalApp.service;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.repository.userRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    // controller -->service --> repository

    @Autowired
    private userRepo userRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveAdminUser(Users adminEntry) {
        adminEntry.setPassword(passwordEncoder.encode(adminEntry.getPassword()));
        adminEntry.setRoles(List.of("User","Admin"));
        userRepo.save(adminEntry);
    }

    public void saveAdmin(Users adminEntry) {
        adminEntry.setPassword(passwordEncoder.encode(adminEntry.getPassword()));
        adminEntry.setRoles(List.of("Admin"));
        userRepo.save(adminEntry);
    }

    public void saveNewUser(Users myEntry) {
        myEntry.setPassword(passwordEncoder.encode(myEntry.getPassword()));
        myEntry.setRoles(List.of("User"));
        userRepo.save(myEntry);
    }

    public void saveUser(Users myEntry) {
        userRepo.save(myEntry);
    }

    public List<Users> findAll() {
        return userRepo.findAll();
    }

    public Users findById(ObjectId id){
            return userRepo.findById(id).get();
    }

    public void saveEntryById(Users myEntry, ObjectId id) {
        Users employeeData = userRepo.findById(id).get();
    }

    public void deleteData(ObjectId id){
        userRepo.deleteById(id);
    }

    public Users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

}
