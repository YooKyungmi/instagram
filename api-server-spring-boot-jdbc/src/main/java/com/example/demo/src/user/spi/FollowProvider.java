package com.example.demo.src.user.spi;

import com.example.demo.config.BaseException;

import java.util.List;

public interface FollowProvider {

    int getFollowerCount(int userId) throws BaseException;
    int getFollowingCount(int userId) throws BaseException;
    int getConnectedFriendCount(int onlineUserId,int findingUserId) throws BaseException;
    List<Integer> getConnectedFollowId(int onlineUserId, int findingUserId) throws BaseException;

}
