package com.api.passlogServer.credential;

import com.api.passlogServer.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service @Slf4j
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Autowired
    public CredentialService(CredentialRepository credentialRepository, UserRepository userRepository) {
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
    }

    public void addCredential(Credentials credentials) {
        boolean exist = userRepository.existsById(credentials.getUser_id());
        if(!exist){
            log.warn("User adding credential cannot be found in the databse");
        }else
        {
            credentials.setApplication(credentials.getApplication().toUpperCase());
            log.info("Saving to database "+credentials.toString());
            credentialRepository.save(credentials);
        }
    }

    @Transactional
    public void editCredential(Credentials credentials) {

        Optional<Credentials> databaseCredential = credentialRepository.findById(credentials.getId());

        if(databaseCredential.isPresent()){
            //Change username
            if(!Objects.equals(databaseCredential.get().getUsername(), credentials.getUsername()) &&
                    !credentials.getUsername().equals("")){
                log.info("Updating credential username to: "+ credentials.getUsername());
                databaseCredential.get().setUsername(credentials.getUsername());
            }

            //Change password
            if(!Objects.equals(databaseCredential.get().getPassword(), credentials.getPassword()) &&
                    !credentials.getPassword().equals("")){
                log.info("Updating credential password to: "+credentials.getPassword());
                databaseCredential.get().setPassword(credentials.getPassword());
            }

            // Change description
            if(!Objects.equals(databaseCredential.get().getDescription(), credentials.getDescription()) &&
                    !credentials.getDescription().equals("")){
                log.info("Updating credential description to: "+credentials.getDescription());
                databaseCredential.get().setDescription(credentials.getDescription());
            }

            //Change application
            if(!Objects.equals(databaseCredential.get().getApplication(), credentials.getApplication()) &&
            !credentials.getApplication().equals("")){
                log.info("Updating credential application to: "+credentials.getApplication());
                databaseCredential.get().setApplication(credentials.getApplication().toUpperCase());
            }
        }else
            log.warn("Credential to be updated could not be found!");
    }

    public Credentials viewCredentialById(Integer credentialId) {
        Optional<Credentials> credential = credentialRepository.findById(credentialId);
        if(credential.isEmpty()){
            log.warn("Credential with id: "+credentialId+" does not exist");
            throw new IllegalStateException("Credential does not exist!");
        }else
            log.info("Returning credential with id: "+credentialId);
        return credential.get();
    }

    public List<Credentials> viewAllUserCredential(Integer userId) {
        boolean exist = userRepository.existsById(userId);
        if(!exist){
            log.warn("User with id: "+userId+" does not exist in database");
            throw new IllegalStateException("User does not exist!");
        }else
            log.info("Returning all credentials from user with id: "+userId);
        return credentialRepository.getAllByUser_id(userId);
    }

    public void deleteCredential(Integer credentialId) {
        boolean exist = credentialRepository.existsById(credentialId);
        if(!exist){
            log.warn("Credential with id: "+credentialId+" does not exist");
            throw new IllegalStateException("Credential does not exists");
        }else
            log.info("Deleting credential with id: "+credentialId);
        credentialRepository.deleteById(credentialId);
    }

    public List<Credentials> searchCredential(Credentials credential,int userId) {
        boolean exist = userRepository.existsById(userId);
        if(!exist){
            log.warn("User with id: "+userId+" does not exist in the database");
            throw new IllegalStateException("User does not exist!");
        }else
        {
            log.info("Searching for "+credential.toString());
            //A
            if(!credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){

                return credentialRepository.searchCredentialByA(credential.getApplication(),userId);
            }else   //B
            if(credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByB(credential.getUsername(),userId);
            }else   //C
            if(credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && credential.getDescription().isEmpty())    {
                return credentialRepository.searchCredentialByC(credential.getPassword(),userId);
            }else   //D
            if(credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByD(credential.getDescription(),userId);
            }else //AB
            if(!credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByAB(credential.getApplication(),credential.getUsername(),userId);
            }else   //AC
            if(!credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByAC(credential.getApplication(),credential.getPassword(),userId);
            }else   //AD
            if(!credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByAD(credential.getApplication(),credential.getDescription(),userId);
            }else   //BC
            if(credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByBC(credential.getUsername(),credential.getPassword(),userId);
            }else   //BD
            if(credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByBD(credential.getUsername(),credential.getDescription(),userId);
            }else   //CD
            if(credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByCD(credential.getPassword(),credential.getDescription(),userId);
            }else   //ABC
            if(!credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByABC(credential.getApplication(),credential.getUsername(),
                        credential.getPassword(),userId);
            }else   //ACD
            if(!credential.getApplication().isEmpty() && credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByACD(credential.getApplication(),credential.getPassword(),
                        credential.getDescription(),userId);
            }else   //ABD
            if(!credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByABD(credential.getApplication(),credential.getUsername(),
                        credential.getDescription(),userId);
            }else   //BCD
            if(credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByBCD(credential.getUsername(),credential.getPassword(),
                        credential.getDescription(),userId);
            }else   //ABCD
            if(!credential.getApplication().isEmpty() && !credential.getUsername().isEmpty() &&
                    !credential.getPassword().isEmpty() && !credential.getDescription().isEmpty()){
                return credentialRepository.searchCredentialByABCD(credential.getApplication(),credential.getUsername(),
                        credential.getPassword(),credential.getDescription(),userId);
            }
        }
        return null;
    }
}
