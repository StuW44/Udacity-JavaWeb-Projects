package com.udacity.jwdnd.course1.cloudstorage.data;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CredentialMapper {

     @Select("SELECT * FROM Credentials WHERE credentialid = #{credentialid}")
     Credential getCredential(int credentialid);
    @Insert("INSERT INTO Credentials (url, username, key,password,userid) " +
            "VALUES(#{url}, #{username}, #{key},#{password},#{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);
    @Delete("Delete from Credentials where credentialid = #{credentialid}")
    public void deleteCredentialById(int credentialid);
    @Update("Update Credentials set url=#{url}, username=#{username}, password=#{password} where credentialid = #{credentialid}")
    public void updateCredentialById(Credential credential);
    @Select("select * from Credentials where userid=#{userid}")
    ArrayList<Credential> getAllCredentials(int credentialid);
}
