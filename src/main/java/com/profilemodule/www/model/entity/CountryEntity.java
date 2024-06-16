package com.profilemodule.www.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "country")
public class CountryEntity extends BaseEntity {

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, String> name = new HashMap<>();
    @Column
    private String code;

//    @OneToMany(mappedBy = "country")
//    private List<CityEntity> city = new ArrayList<>();

}
