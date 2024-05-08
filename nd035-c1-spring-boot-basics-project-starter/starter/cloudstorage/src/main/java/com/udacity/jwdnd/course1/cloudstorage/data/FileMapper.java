package com.udacity.jwdnd.course1.cloudstorage.data;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM Files WHERE filename=#{filename} and userid=#{userid}")
    File getFileByName(String filename,int userid);

    @Select("SELECT * FROM Files WHERE fileId=#{fileId}")
    File getFile(int fileId);
    @Insert("INSERT INTO Files (filename, contenttype,  filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contenttype},  #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);
    @Delete("Delete from Files where fileId=#{fileId}")
    public void deleteFileById(int fileId);
    @Select("select fileId,filename, filesize, userid, filedata from Files  where userid=#{userid}")
    List<File> getAllFiles(int userid);
}
