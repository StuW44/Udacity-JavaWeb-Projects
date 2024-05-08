package com.udacity.jwdnd.course1.cloudstorage.data;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface NoteMapper
{
    @Select("SELECT * FROM Notes WHERE noteid = #{noteid}")
    Note getNote(int noteId);
    @Insert("INSERT INTO Notes (notetitle, notedescription, userid) " +
            "VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);
    @Delete("Delete from Notes where noteid=#{noteid}")
    public void deleteNoteById(Integer noteid);
    @Update("Update Notes set notetitle=#{notetitle},notedescription=#{notedescription} where noteid=#{noteid}")
    public void updateNoteById(Note note);
    @Select("select * from Notes where userid=#{userid}")
    ArrayList<Note> getAllNotes(Integer noteid);
}
