package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.data.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.data.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    public FileService(FileMapper fileMapper)
    {
        this.fileMapper=fileMapper;
    }
    public long Insert(File file) {return fileMapper.insert(file);}
    public void deleteFile(int fileId) { fileMapper.deleteFileById(fileId);}
    public List<File> getUserFiles(int userId)
    {
        return fileMapper.getAllFiles(userId);
    }
    public File getFile(int fileId) {return fileMapper.getFile(fileId);}
    public boolean doesFileExist(String filename,int userid) { return fileMapper.getFileByName(filename,userid)!=null;}
}
