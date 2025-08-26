package com.example.JournalApp.service;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.Scheduler.UserWelcome;
import com.example.JournalApp.repository.userRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserService {

    // controller -->service --> repository

    @Autowired
    private userRepo userRepo;

    @Autowired
    private UserWelcome userWelcome;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // this  can  save  only  admin for admin and  user
    public void saveAdminUser(Users adminEntry) {
        adminEntry.setPassword(passwordEncoder.encode(adminEntry.getPassword()));
        adminEntry.setRoles(List.of("User","Admin"));
        adminEntry.setJoinDate(LocalDateTime.now());
        userRepo.save(adminEntry);
        userWelcome.findUserAndSendSAMail(adminEntry.getUsername(),adminEntry.getEmail());
    }

    // this  can  save  only  admin for admin
    public void saveAdmin(Users adminEntry) {
        adminEntry.setPassword(passwordEncoder.encode(adminEntry.getPassword()));
        adminEntry.setRoles(List.of("Admin"));
        adminEntry.setJoinDate(LocalDateTime.now());
        userRepo.save(adminEntry);
        userWelcome.findUserAndSendSAMail(adminEntry.getUsername(),adminEntry.getEmail());
    }

    // all public user can save this  and create the account
    public void saveNewUser(Users myEntry) {
        myEntry.setPassword(passwordEncoder.encode(myEntry.getPassword()));
        myEntry.setRoles(List.of("User"));
        myEntry.setJoinDate(LocalDateTime.now());
        userRepo.save(myEntry);
        userWelcome.findUserAndSendSAMail(myEntry.getUsername(),myEntry.getEmail());
    }

    //  saving journal id for any particular user
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

    public Users findUserByJournal(ObjectId journalId) {
        return userRepo.findByJournalEntriesListContains(journalId);
    }

    public List<Users> findUserName(String regex) {
        return userRepo.findByUsernameRegex(regex);
    }

}
