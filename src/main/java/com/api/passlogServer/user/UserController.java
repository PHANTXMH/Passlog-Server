package com.api.passlogServer.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = "/authenticate")
    public Users getUserLogin(@RequestHeader(value = "Authorization") String token){
        log.info("HTTP GET /authenticate accepted");
        return userService.getUserLogin(token);
    }

    @PostMapping(value = "/createaccount")
    public boolean createUser(@RequestBody Users users){
        log.info("HTTP POST /createaccount sent by username: "+users.getUsername());
        return userService.addUser(users);
    }

    @PutMapping(value = "/updateusertheme")
    public void setUserTheme(@RequestBody Users users){
        log.info("HTTP PUT /updateusertheme sent by username: "+users.getUsername()+" with theme: "+users.getTheme());
        userService.setUserTheme(users);
    }

    @PostMapping(value = "/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
        log.info("HTTP POST /role/addtouser sent by: "+form.getUsername()+" adding role: "+form.getRoleName());
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("HTTP GET /token/refresh accepted");
        userService.getRefreshToken(request, response);
    }
}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
