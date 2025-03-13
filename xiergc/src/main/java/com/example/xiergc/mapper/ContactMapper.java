package com.example.xiergc.mapper;

import com.example.xiergc.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    
    void addContact(Long id, Long contactId);

    void removeContact(Long id, Long contactId);

    List<User> getContacts(Long id);
}
