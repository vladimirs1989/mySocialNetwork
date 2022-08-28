package com.projects.mySocialNetwork.repository;

import com.projects.mySocialNetwork.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByTag(String tag);

    List<Message> findByText(String text);
}
