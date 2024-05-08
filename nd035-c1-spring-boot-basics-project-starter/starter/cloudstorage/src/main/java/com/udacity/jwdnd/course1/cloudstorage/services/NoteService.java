package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.data.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService
{
    private final NoteMapper noteMapper;
    public NoteService(NoteMapper noteMapper)
    {
        this.noteMapper = noteMapper;
    }
    public List<Note> getUserNotes(int userId)
    {
        return noteMapper.getAllNotes(userId);
    }
    public void insertNote(String notetitle,String notedescription,int userId)
    {
        Note note = new Note(0,notetitle,notedescription,userId);
        noteMapper.insert(note);
    }
    public void editNote(int noteId,String notetitle,String notedescription,int userId)
    {
        Note note = new Note(noteId,notetitle,notedescription,userId);
        noteMapper.updateNoteById(note);
    }
    public void deleteNote(int noteId)
    {
        noteMapper.deleteNoteById(noteId);
    }
}
