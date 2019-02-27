package com.example.jpa.dao;

import com.example.jpa.entity.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public interface TestDao extends PagingAndSortingRepository<Test,Integer> {

}

