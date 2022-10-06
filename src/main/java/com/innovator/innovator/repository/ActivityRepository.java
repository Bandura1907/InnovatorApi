package com.innovator.innovator.repository;

import com.innovator.innovator.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer>,
        PagingAndSortingRepository<Activity, Integer> {

//    @Query("select Activity from Activity a where a.cre")
//    List<Activity> findAllActivityDate();
}
