package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.data.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService
{
    public CredentialService(EncryptionService encryptionService,
                             CredentialMapper credentialMapper) {
        this.encryptionService=encryptionService;
        this.credentialMapper = credentialMapper;
    }
    private final EncryptionService encryptionService;
    private final CredentialMapper credentialMapper;
    public List<Credential> getUserCredentials(int userId)
    {
        List<Credential> credentials=credentialMapper.getAllCredentials(userId);
        credentials.forEach(credential->credential.setPasswordText(decryptPassword(credential.getPassword(),credential.getKey())));
        return  credentials;
    }
    public void insertCredential(String url, String username,String key,String passwordText,int userid)
    {
        String encryptedPassword  = encryptionService.encryptValue(passwordText, key);
        Credential credential = new Credential(0,url,username,key,encryptedPassword,userid);
        credentialMapper.insert(credential);
    }
    public void editCredential(int credentialId,String url, String username,String passwordText,int userid)
    {
        String key=credentialMapper.getCredential(credentialId).getKey();
        String encryptedPassword  = encryptionService.encryptValue(passwordText, key);
        Credential credential = new Credential(credentialId,url,username,key,encryptedPassword,userid);
        credentialMapper.updateCredentialById(credential);
    }
    public void deleteCredential(int credentialId)
    {
        credentialMapper.deleteCredentialById(credentialId);
    }
    public String generateKey()
    {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
    public String decryptPassword(String encryptedPassword,String encodedKey)
    {
       return encryptionService.decryptValue(encryptedPassword, encodedKey);
    }
}
