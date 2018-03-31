package org.marketplace.service;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.domain.User;
import org.marketplace.dto.UserDetail;
import org.marketplace.exception.BadRequestException;
import org.marketplace.exception.NotFoundException;
import org.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserDetail> getUsers() {
        Iterable<User> userIterable = userRepository.findAll();
        return StreamSupport.stream(userIterable.spliterator(), false)
                .map(UserDetail::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDetail createNewUser(UserDetail userDetail) {
        log.debug("createNewUser() - userDetail="+ userDetail);
        User user = userRepository.findByUsername(userDetail.getUsername());
        if(user != null)
            throw new BadRequestException(String.format("User %s already exists.", userDetail.getUsername()));
        User userModel = UserDetail.fromUserDto(userDetail);
        User persistentUser = projectRepository.save(userModel);
        return UserDetail.toUserDto(persistentUser);
    }

    public UserDetail updateUser(Long userId, UserDetail userDetail) {
        log.debug("updateUser() - userId="+userId+"\tuserDetail="+ userDetail);
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User userModel = user.get();
            userModel.setUsername(userDetail.getUsername());
            userModel.setDisplayName(userDetail.getDisplayName());
            userModel.setDescription(userDetail.getDescription());
            userModel.setReputation(userDetail.getReputation());
            User persistentUser = userRepository.save(userModel);
            return UserDetail.toUserDto(persistentUser);
        } else
            throw new NotFoundException(String.format("User with id %s doesn't exist", userId));
    }

    public UserDetail getUser(Long userId) {
        log.debug("getUser() - userId="+userId);
        Optional<User> optUser = userRepository.findById(userId);
        if(optUser.isPresent())
            return UserDetail.toUserDto(optUser.get());
        else
            throw new NotFoundException(String.format("User with id %s doesn't exist", userId));
    }

    public void deleteUser(Long userId) {
        log.debug("deleteUser() - userId="+userId);
        Optional<User> optUser = userRepository.findById(userId);
        if(!optUser.isPresent())
            throw new NotFoundException(String.format("User with id %s doesn't exist", userId));
        userRepository.deleteById(userId);
    }

}