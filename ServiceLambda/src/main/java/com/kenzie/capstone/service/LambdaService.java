package com.kenzie.capstone.service;

import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.model.UserData;
import com.kenzie.capstone.service.model.UserRecord;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LambdaService {

    private ExampleDao exampleDao;

    @Inject
    public LambdaService(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ExampleRecord record = exampleDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }

    public UserData getDisplayName(String displayName) {
        List<UserRecord> records = exampleDao.getUserData(displayName);
        if (records.size() > 0) {
            UserRecord record = records.get(0);

            UserData userData = new UserData();
            userData.setFollowers(record.getFollowers());
            userData.setFollowing(record.getFollowing());
            userData.setEmail(record.getEmail());
            userData.setUserId(record.getUserId());
            userData.setFavoriteAnime(record.getFavoriteAnime());
            userData.setFullName(record.getFullName());
            userData.setDisplayName(record.getDisplayName());
            userData.setAge(record.getAge());
            userData.setBio(record.getBio());

            return userData;
        }
        return null;
    }

    public List<UserData> getAllUsers() {
        List<UserRecord> records = exampleDao.getAllUsers();

        return records.stream()
                .map(record -> new UserData(
                        record.getFollowers(),
                        record.getFollowing(),
                        record.getEmail(),
                        record.getUserId(),
                        record.getFavoriteAnime(),
                        record.getFullName(),
                        record.getDisplayName(),
                        record.getAge(),
                        record.getBio()))
                .collect(Collectors.toList());
    }
}
//        if (records.size() > 0) {
            //List<UserData> userDataList = new ArrayList<>();
//            for (UserRecord record : records) {
//                UserData userData = new UserData();
//                userData.setFollowers(record.getFollowers());
//                userData.setFollowing(record.getFollowing());
//                userData.setEmail(record.getEmail());
//                userData.setUserId(record.getUserId());
//                userData.setFavoriteAnime(record.getFavoriteAnime());
//                userData.setFullName(record.getFullName());
//                userData.setDisplayName(record.getDisplayName());
//                userData.setAge(record.getAge());
//                userData.setBio(record.getBio());
//
//            }
//            return userData;
//        }
//        return null;
//    }
//}
