package com.api.passlogServer.credential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credentials,Integer> {


   @Query("SELECT c FROM credentials c WHERE c.user_id = ?1")
   List <Credentials> getAllByUser_id(Integer userid);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.user_id = ?2")
   List<Credentials> searchCredentialByA(String application, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.username = ?1 AND c.user_id = ?2")
   List<Credentials> searchCredentialByB(String username, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.password = ?1 AND c.user_id = ?2")
   List<Credentials> searchCredentialByC(String password, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.description LIKE %?1% AND c.user_id = ?2")
   List<Credentials> searchCredentialByD(String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.username = ?2 AND c.user_id = ?3")
   List<Credentials> searchCredentialByAB(String application, String username, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.password = ?2 AND c.user_id = ?3")
   List<Credentials> searchCredentialByAC(String application, String password, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.description LIKE %?2% AND c.user_id = ?3")
   List<Credentials> searchCredentialByAD(String application, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.username = ?1 AND c.password = ?2 AND c.user_id = ?3")
   List<Credentials> searchCredentialByBC(String username, String password, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.username = ?1 AND c.description LIKE %?2% AND c.user_id = ?3")
   List<Credentials> searchCredentialByBD(String username, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.password = ?1 AND c.description LIKE %?2% AND c.user_id = ?3")
   List<Credentials> searchCredentialByCD(String password, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.username = ?2 AND c.password = ?3 AND c.user_id = ?4")
   List<Credentials> searchCredentialByABC(String application, String username, String password, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.password = ?2 AND c.description LIKE %?3% AND c.user_id = ?4")
   List<Credentials> searchCredentialByACD(String application, String password, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.username = ?2 AND c.description LIKE %?3% AND c.user_id = ?4")
   List<Credentials> searchCredentialByABD(String application, String username, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.username = ?1 AND c.password = ?2 AND c.description LIKE %?3% AND c.user_id = ?4")
   List<Credentials> searchCredentialByBCD(String username, String password, String description, int user_id);

   @Query("SELECT c FROM credentials c WHERE c.application = ?1 AND c.username = ?2 AND c.password = ?3 AND c.description LIKE %?4% AND c.user_id = ?5")
   List<Credentials> searchCredentialByABCD(String application, String username, String password, String description, int user_id);
}
