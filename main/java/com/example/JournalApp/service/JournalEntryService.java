package com.example.JournalApp.service;

import com.example.JournalApp.JournalEntities.Users;
import com.example.JournalApp.JournalEntities.JournalEntry;
import com.example.JournalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JournalEntryService {

    // controller -->service --> repository

    @Autowired
    private JournalEntryRepo repo;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry myEntry, String userName) {
        try {
            Users user = userService.findByUsername(userName);
            myEntry.setCreatedAt(LocalDateTime.now());
            JournalEntry saved = repo.save(myEntry);
            user.getJournalEntriesList().add(saved);
            userService.saveUser(user);
        }
        catch (Exception e) {
            throw  new RuntimeException("The error occurred while saving the entry" + e);
        }


    }

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

}
