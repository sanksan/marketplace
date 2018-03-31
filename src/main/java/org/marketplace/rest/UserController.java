package org.marketplace.rest;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.dto.UserDetail;
import org.marketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDetail> getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserDetail createNewUser(@Valid @RequestBody UserDetail userDetail) {
        log.debug("createNewUser() - userDetail="+ userDetail);
        return userService.createNewUser(userDetail);
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.PUT)
    public UserDetail updateUser(@PathVariable(value = "userid") Long userid,
                                 @Valid @RequestBody UserDetail userDetail) {
        log.debug("updateUser() - userid="+userid + "\tuserDetail="+ userDetail);
        return userService.updateUser(userid, userDetail);
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    public UserDetail getUser(@PathVariable(value = "userid") Long userid) {
        log.debug("getUser() - userid="+userid);
        return userService.getUser(userid);
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "userid") Long userid) {
        log.debug("deleteUser() - userid="+userid);
        userService.deleteUser(userid);
    }

}