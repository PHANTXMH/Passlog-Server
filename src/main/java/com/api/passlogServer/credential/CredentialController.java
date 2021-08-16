package com.api.passlogServer.credential;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Slf4j
public class CredentialController {

    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    @RequestMapping(path = "/addcredential")
    public void addCredential(@RequestBody Credentials credentials){
        log.info("HTTP POST /addcredential "+credentials.toString());
        credentialService.addCredential(credentials);
    }

    @PutMapping
    @RequestMapping(path = "/editcredential")
    public void editCredential(@RequestBody Credentials credentials){
        log.info("HTTP PUT /editcredential "+credentials.toString());
        credentialService.editCredential(credentials);
    }

    @GetMapping
    @RequestMapping(path = "/viewcredential/{credentialId}")
    public Credentials viewCredentialById(@PathVariable("credentialId")Integer credentialId){
        log.info("HTTP GET /viewcredential/"+credentialId);
        return credentialService.viewCredentialById(credentialId);
    }

    @GetMapping
    @RequestMapping(path = "/viewcredential/all/{userId}")
    public List<Credentials> viewAllUserCredential(@PathVariable("userId")Integer userId){
        log.info("HTTP GET /viewcredential/all/"+userId);
        return credentialService.viewAllUserCredential(userId);
    }

    @GetMapping
    @RequestMapping(path = "/searchcredential/{userId}")
    public List<Credentials> searchCredential(@RequestBody Credentials credential, @PathVariable(value = "userId") Integer userId){
        log.info("HTTP GET /searchcredential/"+userId+" "+credential.toString());
        return credentialService.searchCredential(credential,userId);
    }

    @DeleteMapping
    @RequestMapping(path = "/delete/credential/{credentialId}")
    public void deleteCredential(@PathVariable(value = "credentialId")Integer credentialId){
        log.info("HTTP DELETE /delete/credential/"+credentialId);
        credentialService.deleteCredential(credentialId);
    }
}
