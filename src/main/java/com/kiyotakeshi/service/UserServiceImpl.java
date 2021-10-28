package com.kiyotakeshi.service;

import com.kiyotakeshi.model.User;
import com.kiyotakeshi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean saveUser(User user) {
        User found = this.fetchUserById(user.getId());
        if (found != null) {
            System.out.println("User already exists");
            return false;
        }
        return userRepository.saveUser(user);
    }

    @Override
    public List<User> fetchAllUser() {
        return userRepository.fetchAllUser();
    }

    @Override
    public User fetchUserById(Long id) {
        return userRepository.fetchUserById(id);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }

    @Override
    public boolean updateUser(Long id, User user) {
        User found = this.fetchUserById(user.getId());
        if (found == null) {
            System.out.println("User not found");
            return false;
        }
        return userRepository.updateUser(id, user);
    }
}
