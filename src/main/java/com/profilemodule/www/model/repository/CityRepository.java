package com.profilemodule.www.model.repository;

import com.profilemodule.www.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query(value = "SELECT city_entity_id FROM city_entity_name WHERE name_key = ?1 AND UPPER(name) LIKE CONCAT('%', UPPER(?2), '%')", nativeQuery = true)
    List<Long> findIdsByNameKeyAndNameLike(int nameKey, String name);


}
