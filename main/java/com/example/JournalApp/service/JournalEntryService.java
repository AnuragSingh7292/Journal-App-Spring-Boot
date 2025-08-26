package com.example.JournalApp.service;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    // controller -->service --> repository

    @Autowired
    private JournalEntryRepo repo;
    @Autowired
    private UserService userService;


    // this is used for when user  creating  new  own journal  after  logged
    @Transactional
    public void saveEntry(JournalEntry myEntry, String userName) {
        try {
            Users user = userService.findByUsername(userName);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            myEntry.setUserId(user.getId()); // ✅ add userId reference
            myEntry.setCreatedAt(LocalDateTime.now());
            JournalEntry saved = repo.save(myEntry);

            user.getJournalEntriesList().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving journal entry: " + e.getMessage(), e);
        }
    }

// this is used for when user  updating the  own journal  after  logged
    public void saveEntry(JournalEntry myEntry) {
        repo.save(myEntry);
    }

    public List<JournalEntry> findAll() {
        return repo.findAll();
    }

    public JournalEntry findById(ObjectId id){
            return repo.findById(id).get();
    }

    public void saveEntryById(JournalEntry myEntry, ObjectId id) {
        JournalEntry employeeData = repo.findById(id).get();
    }

    @Transactional
    public boolean deleteData(ObjectId id, String userName){
        boolean deleted = false;
        try {
            Users user = userService.findByUsername(userName);
            boolean removed = user.getJournalEntriesList().removeIf(x -> x.getId().equals( id));
            if (removed) {
                userService.saveUser(user);
                repo.deleteById(id);
            }
            deleted = removed;
        }catch (Exception e) {
            throw  new RuntimeException("The error occurred while deleting the entry" + e);
        }
        return deleted;
    }

    public Page<JournalEntry> findAllPublicEntries(Pageable pageable) {
        return repo.findByIsPrivateFalse(pageable);
    }


//    public Page<JournalEntry> findPublicJournalsByUser(Users user, Pageable pageable) {
//        return repo.findByUserAndIsPrivateFalse(user, pageable);
//    }

    public List<JournalEntry> findPublicByTitleRegex(String regex) {
        return repo.findPublicByTitleRegex(regex);
    }

}
