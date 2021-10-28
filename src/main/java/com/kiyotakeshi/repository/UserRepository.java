package com.kiyotakeshi.repository;

import com.kiyotakeshi.model.User;

import java.util.List;

public interface UserRepository {

    boolean saveUser(User user);

    List<User> fetchAllUser();

    User fetchUserById(Long id);

    boolean deleteUser(Long id);

    boolean updateUser(Long id, User user);
}
