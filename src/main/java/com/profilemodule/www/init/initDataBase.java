package com.profilemodule.www.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profilemodule.www.model.dto.CountryDTO;
import com.profilemodule.www.model.entity.*;
import com.profilemodule.www.model.enums.LanguageEnum;
import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.model.repository.*;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Component
public class initDataBase implements CommandLineRunner {

    public static final List<String> scopes = List.of(
            UserEntity.SCOPE,
            GroupEntity.SCOPE,
            LanguageEntity.SCOPE
    );
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ScopeRepository scopeRepository;
    private final ScopeCleanRepository scopeCleanRepository;
    private final LanguageRepository languageRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;




    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initScopes();
        initLanguages();
        initAdmin();
        initCountry();
    }

    public initDataBase(UserRepository userRepository, GroupRepository groupRepository, ScopeRepository scopeRepository, ScopeCleanRepository scopeCleanRepository, LanguageRepository languageRepository, PasswordEncoder passwordEncoder, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.scopeRepository = scopeRepository;
        this.scopeCleanRepository = scopeCleanRepository;
        this.languageRepository = languageRepository;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
    }

    protected void initScopes() {
        final List<ScopeCleanEntity> allScopes = scopeCleanRepository.findAll();
        scopes.forEach(s -> {
            final Set<String> collect = allScopes.stream().map(ScopeCleanEntity::getName).collect(Collectors.toSet());
            if(!collect.contains(s)){
                scopeCleanRepository.save(new ScopeCleanEntity(s));
            }
        });
    }

    private void initLanguages() {

        final List<LanguageEntity> all = languageRepository.findAll();

        if(all.isEmpty()) {
            final LanguageEnum[] languageValues = LanguageEnum.values();
            for (LanguageEnum value : languageValues) {
                LanguageEntity language = new LanguageEntity();
                language.setLanguageEnum(value);
                language.setActive(true);
                if(value.getCode().equals("en")) {
                    language.setDefault(true);
                }
                languageRepository.save(language);
            }
        }
        else if(all.size() < LanguageEnum.values().length) {
            for (LanguageEnum value : LanguageEnum.values()) {
                for (LanguageEntity entity : all) {
                    if(!Objects.equals(value, entity.getLanguageEnum())) {
                        LanguageEntity language = new LanguageEntity();
                        language.setLanguageEnum(value);
                        language.setActive(true);
                        languageRepository.save(language);
                    }
                }
            }
        }
    }



    protected void initAdmin() {
        final List<LanguageEntity> allActiveLanguages = languageRepository.findAllByIsDefaultTrue();

        if(userRepository.findAll().isEmpty()) {
            final UserEntity admin = new UserEntity();
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setUsername("admin");
            admin.setLanguage(allActiveLanguages.get(0));
            userRepository.save(admin);
        }
        final List<GroupEntity> all1 = groupRepository.findAll();
        if(all1.isEmpty()) {

            final List<ScopeCleanEntity> all = scopeCleanRepository.findAll();
            final List<ScopeEntity> empty = new ArrayList<>();

            for (ScopeCleanEntity scopeClean : all) {
                final ScopeEntity savedEntity = scopeRepository.save(new ScopeEntity(scopeClean.getName(), List.of(
                        PermissionEnum.DELETE,
                        PermissionEnum.VIEW,
                        PermissionEnum.UPDATE,
                        PermissionEnum.ADD,
                        PermissionEnum.READ)));
                empty.add(savedEntity);
            }

            GroupEntity entity = new GroupEntity();
            entity.setName("ADMIN");
            entity.getScopes().addAll(empty);
            groupRepository.save(entity);


            final List<GroupEntity> groups = groupRepository.findAll();

            final UserEntity admin = userRepository.findByUsername("admin");
            Hibernate.initialize(admin.getGroups());
            admin.getGroups().addAll(groups);
        }
    }

    private void initCountry() throws IOException {
        File file = ResourceUtils.getFile("classpath:json/country.json");
        ObjectMapper mapper = new ObjectMapper();


        final List<CountryEntity> allCountry = countryRepository.findAll();
        if(allCountry.isEmpty()) {
            try {
                // Read JSON file and map it to a list of Country objects
                CountryDTO[] countries = mapper.readValue(file, CountryDTO[].class);

                final List<LanguageEntity> allLanguages = languageRepository.findAll();

                // Now you have a list of Country objects
                for (CountryDTO country : countries) {

                    Map<Integer, String> name = new HashMap<>();
                    for (LanguageEntity language : allLanguages) {
                        name.put(language.getId().intValue(), country.getName().toUpperCase());
                    }

                    final CountryEntity build = CountryEntity
                            .builder()
                            .code(country.getCountryCode().toLowerCase())
                            .name(name)
                            .build();
                    countryRepository.save(build);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
