package com.kiyotakeshi.repository;

import com.kiyotakeshi.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final RedisTemplate redisTemplate;

    private static final String KEY = "USER";

    public UserRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean saveUser(User user) {
        try {
            redisTemplate.opsForHash().put(KEY, user.getId().toString(), user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> fetchAllUser() {
        return redisTemplate.opsForHash().values(KEY);
    }

    @Override
    public User fetchUserById(Long id) {
        return (User) redisTemplate.opsForHash().get(KEY,id.toString());
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            redisTemplate.opsForHash().delete(KEY,id.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(Long id, User user) {
        try {
            redisTemplate.opsForHash().put(KEY, id, user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
