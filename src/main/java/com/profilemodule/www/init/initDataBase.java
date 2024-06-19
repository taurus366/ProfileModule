package com.profilemodule.www.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profilemodule.www.model.dto.CountryDTO;
import com.profilemodule.www.model.entity.*;
import com.profilemodule.www.model.enums.LanguageEnum;
import com.profilemodule.www.model.enums.PermissionEnum;
import com.profilemodule.www.model.repository.*;
import com.profilemodule.www.shared.annotation.intlClass;
import org.hibernate.Hibernate;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Component
public class initDataBase implements CommandLineRunner {

    // ANSI color codes
    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BLACK = "\u001B[30m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_BLUE = "\u001B[34m";
    private final String ANSI_MAGENTA = "\u001B[35m";
    private final String ANSI_CYAN = "\u001B[36m";
    private final String ANSI_WHITE = "\u001B[37m";

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
    public static final String LOCATION_PREFIX = "MainModule/src/main/resources/i18n/translate_%s.properties";



    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initScopes();
        initLanguages();
        initAdmin();
        initCountry();
        initTranslations();
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

//        final List<CountryEntity> allCountry = countryRepository.findAll();
        final boolean isNotEmpty = countryRepository.existAny();
        if(!isNotEmpty) {
            try {
                // Read JSON file and map it to a list of Country objects
                CountryDTO[] countries = mapper.readValue(file, CountryDTO[].class);

                final List<LanguageEntity> allLanguages = languageRepository.findAll();

                // Now you have a list of Country objects
                for (CountryDTO country : countries) {

                    Map<Integer, String> name = new HashMap<>();
                    for (LanguageEntity language : allLanguages) {
//                        name.put(language.getId().intValue(), country.getName().toUpperCase());
                        name.put(language.getLanguageEnum().ordinal(), country.getName().toUpperCase());
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

    private void initTranslations() {
        Set<String> translations = new HashSet<>();
        Map<String, String> map = new HashMap<>();


        Reflections reflections = new Reflections("com"); // Scan all packages under com
        Set<Class<?>> intlClasses = reflections.getTypesAnnotatedWith(intlClass.class); // Assuming you annotate Intl classes with @IntlClass

        // Step 1: Collect all translations from @IntlClass annotated classes
        for (Class<?> intlClass : intlClasses) {
            Method[] methods = intlClass.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    try {
                        String key = (String) method.invoke(null); // Invoke static getter method
                        String fieldName = method.getName().substring(3); // Remove "get" prefix
                        if(!map.containsKey(fieldName)) {
                            map.put(fieldName, key);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Step 2: Check and add missing translations to properties files
        if(!map.isEmpty()) {

            List<LanguageEntity> activeLanguages = languageRepository.findAllByActiveIsTrue();

            for (LanguageEntity activeLanguage : activeLanguages) {

                final String locale = activeLanguage.getLanguageEnum().getCode();

                Properties existingProperties = loadOrCreateProperties(locale);

                if(existingProperties != null) {
                    for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                        if(!existingProperties.containsKey(stringStringEntry.getKey())) {
                            existingProperties.setProperty(stringStringEntry.getKey(), stringStringEntry.getValue());
                    }
                }
                    saveProperties(locale, existingProperties);
                }




            }




        }
    }
//
    private Properties loadOrCreateProperties(String locale) {
        String filePath = String.format(LOCATION_PREFIX, locale);
        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // Properties file doesn't exist, create a new one
            System.err.println("Properties file doesn't exist for locale: " + locale);
            properties = createPropertiesFile(filePath);
        }
        return properties;
    }

    private Properties createPropertiesFile(String filePath) {
        Properties properties = new Properties();

        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            Files.createFile(Paths.get(filePath));
            System.out.println(ANSI_YELLOW + "Created new properties file: " + filePath + ANSI_RESET);
        } catch (IOException e) {
            System.err.println("Failed to create properties file: " + filePath);
            e.printStackTrace();
        }

        return properties;
    }

    private void saveProperties(String locale, Properties properties) {
        String filePath = String.format(LOCATION_PREFIX, locale);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(filePath))) {
//            properties.store(outputStream, "Updated properties for " + locale);
            properties.store(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), "Updated properties for " + locale);
            System.out.println(ANSI_GREEN + "Updated properties file saved: " + filePath + ANSI_RESET);
        } catch (IOException e) {
            System.err.println("Failed to save updated properties file: " + filePath);
            e.printStackTrace();
        }
    }





}
