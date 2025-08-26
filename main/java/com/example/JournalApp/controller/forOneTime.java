// its doing adding all user id in there journal

//package com.example.JournalApp.controller;
//
//
//import com.example.JournalApp.JournalEntities.JournalEntry;
//import com.example.JournalApp.JournalEntities.Users;
//import com.example.JournalApp.repository.JournalEntryRepo;
//import com.example.JournalApp.repository.userRepo;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@Component
//public class forOneTime  implements CommandLineRunner {
//
//    @Autowired
//    private userRepo userRepo;
//
//    @Autowired
//    private JournalEntryRepo journalEntryRepo;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Starting Journal -> User migration...");
//
//        // Fetch all users
//        List<Users> users = userRepo.findAll();
//
//        for (Users user : users) {
//            List<JournalEntry> journals = user.getJournalEntriesList();
//
//            for (JournalEntry journal : journals) {
//                // Check if userId is already set to avoid overwriting
//                if (journal.getUserId() == null) {
//                    journal.setUserId(user.getId()); // <-- Add userId in JournalEntry class
//                    journalEntryRepo.save(journal);
//                }
//            }
//        }
//        System.out.println("Migration completed. All existing journals now have userId.");
//    }
//}
//
//

//
//package com.example.JournalApp.controller;
//
//import com.example.JournalApp.JournalEntities.JournalEntry;
//import com.example.JournalApp.repository.JournalEntryRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class forOneTime implements CommandLineRunner {
//
//    @Autowired
//    private JournalEntryRepo journalEntryRepo;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Starting journal update...");
//
//        // Predefined list of titles and contents (education + romantic examples)
//        List<String> titles = List.of(
//                "First Day of College", "A Walk in the Rain", "Learning Java Basics",
//                "Love in the Air", "Midnight Thoughts", "Study Break Fun",
//                "Romantic Sunset", "New Project Started", "Coffee & Memories",
//                "Library Adventures", "Heartfelt Moments", "Exam Stress Relief",
//                "Stargazing Night", "Lecture Notes Chaos", "A Cute Surprise",
//                "Campus Life", "Heart Skipped a Beat", "Learning Spring Boot",
//                "Sweet Laughter", "Morning Jog Inspiration", "First Crush",
//                "Coding Marathon", "Random Thoughts", "Love Letters",
//                "Group Study Fun", "Romantic Dinner", "Late Night Coding",
//                "Confession Time", "Motivation Boost", "Secret Admiration",
//                "Final Exam Prep", "Unexpected Compliment", "Library Escape",
//                "Romantic Movie Night", "Daily Journal Habit", "Sunrise Hike",
//                "Heartwarming Smile", "Class Presentation", "Sweet Notes",
//                "Learning Together"
//        );
//
//        List<String> contents = List.of(
//                "Today was my first day at college, everything felt so new and exciting.",
//                "It was raining and we ran through puddles laughing together.",
//                "Spent the afternoon learning Java basics, feeling accomplished!",
//                "Walking under the stars, I felt love in the air.",
//                "Midnight thoughts of you made me smile and feel happy.",
//                "Took a short study break and played some fun games.",
//                "The sunset reminded me of a romantic scene in a movie.",
//                "Started a new project today, hope it goes well.",
//                "Sipping coffee while reminiscing old memories.",
//                "Spent the day exploring the library, found hidden treasures.",
//                "Heartfelt moments with friends and laughter filled the day.",
//                "Stress from exams but shared jokes helped lighten the mood.",
//                "Stargazing with someone special made the night magical.",
//                "Lecture notes everywhere, but learning is fun.",
//                "A cute surprise from a friend made me giggle all day.",
//                "Campus life is full of small joys and challenges.",
//                "My heart skipped a beat when I saw them smile.",
//                "Learning Spring Boot is challenging but satisfying.",
//                "Shared sweet laughter over silly things with friends.",
//                "Morning jog inspired me to start the day positively.",
//                "Remembering my first crush brought back memories.",
//                "Coding marathon with friends, learning together.",
//                "Random thoughts about life and love filled my journal.",
//                "Writing a love letter felt nostalgic and heartwarming.",
//                "Group study was fun and productive.",
//                "Had a romantic dinner with candles and soft music.",
//                "Late night coding session, but feeling motivated.",
//                "Finally confessed something I've been holding inside.",
//                "Motivation boost from friends kept me going.",
//                "Secret admiration for someone made the heart flutter.",
//                "Final exam prep, notes everywhere, coffee in hand.",
//                "Unexpected compliment brightened my entire day.",
//                "Escaped to the library to find some peace and focus.",
//                "Romantic movie night, laughter and cuddles included.",
//                "Daily journal habit keeps my thoughts organized.",
//                "Sunrise hike with friends, breathtaking view.",
//                "Heartwarming smile from someone made me feel loved.",
//                "Class presentation went better than expected.",
//                "Left sweet notes for friends, small happiness moments.",
//                "Learning together makes every day memorable."
//        );
//
//        // Fetch all journals
//        List<JournalEntry> allJournals = journalEntryRepo.findAll();
//
//        // Update each journal with new title and content
//        for (int i = 0; i < allJournals.size(); i++) {
//            JournalEntry journal = allJournals.get(i);
//
//            // Loop titles/contents if there are more journals than items in the list
//            journal.setTitle(titles.get(i % titles.size()));
//            journal.setContent(contents.get(i % contents.size()));
//
//            // Save the updated journal
//            journalEntryRepo.save(journal);
//
//            System.out.println("Updated journal ID: " + journal.getId());
//        }
//
//        System.out.println("All journals updated successfully!");
//    }
//}
