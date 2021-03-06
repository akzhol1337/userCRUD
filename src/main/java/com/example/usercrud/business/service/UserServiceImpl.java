package com.example.usercrud.business.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.usercrud.business.entity.UserRequestDto;
import com.example.usercrud.business.entity.annotations.Loggable;
import com.example.usercrud.business.entity.annotations.Metric;
import com.example.usercrud.business.entity.User;
import com.example.usercrud.persistance.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Loggable
@Slf4j
@Data
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AmazonS3 s3Client;

    public Optional<User> addUser(User user)  {
        Optional<User> newUser = Optional.empty();
        try {
            if (!userRepo.existsByEmail(user.getEmail())) {
                newUser = Optional.of(userRepo.save(user));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }


    public String getCountryByHttpServletRequest(HttpServletRequest request) {
        String userIP = request.getRemoteAddr();
        if (Objects.equals(userIP, "0:0:0:0:0:0:0:1") || Objects.equals(userIP, "127.0.0.1")) {
            return "Kazakhstan";
        } else {
            String result = restTemplate.getForObject("http://ip-api.com/json/" + userIP + "?fields=status,country",
                String.class);
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map map = mapper.readValue(result, Map.class);
                if (map.get("status").equals("fail")) {
                    return "Kazakhstan";
                }
                return (String) map.get("country");
            } catch (JsonProcessingException e) {
                return "undefined";
            }
        }
    }

    public String getUserAvatarLinkAWSS3(String decodedBase64, String userEmail) {
        byte[] encodedAvatar = Base64.decodeBase64(decodedBase64.substring(decodedBase64.indexOf(',') + 1).getBytes());

        InputStream inputStream = new ByteArrayInputStream(encodedAvatar);
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType("image/png");
        metadata.setContentLength(encodedAvatar.length);

        s3Client.putObject("usercrud-avatars", userEmail + ".png", inputStream, metadata);
        s3Client.setObjectAcl("usercrud-avatars", userEmail + ".png", CannedAccessControlList.PublicRead);

        return "https://storage.yandexcloud.net/usercrud-avatars/" + userEmail + ".png";
    }

    public String getUserAvatarLinkAWSS3(MultipartFile multipartFile, String userEmail) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();

        String contentType = multipartFile.getContentType();

        metadata.setContentType(contentType);
        metadata.setContentLength(multipartFile.getBytes().length);

        String extension = '.' + contentType.substring(contentType.indexOf('/') + 1);

        s3Client.putObject("usercrud-avatars", userEmail + extension, multipartFile.getInputStream(), metadata);
        s3Client.setObjectAcl("usercrud-avatars", userEmail + extension, CannedAccessControlList.PublicRead);

        return "https://storage.yandexcloud.net/usercrud-avatars/" + userEmail + extension;
    }

    public Optional<User> addUser(User user, HttpServletRequest request) {
        try {
            if (userRepo.existsByEmail(user.getEmail())) {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        user.setCountry(getCountryByHttpServletRequest(request));
        if(user.getAvatar() != null) {
            user.setAvatar(getUserAvatarLinkAWSS3(user.getAvatar(), user.getEmail()));
        }
        return Optional.of(userRepo.save(user));
    }

    @Override
    public Optional<User> addUser(UserRequestDto userRequestDto, HttpServletRequest request) {
        try {
            if (userRepo.existsByEmail(userRequestDto.getEmail())) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        User user = new User(userRequestDto);
        user.setCountry(getCountryByHttpServletRequest(request));

        if(userRequestDto.getAvatar() != null) {
            try {
                user.setAvatar(getUserAvatarLinkAWSS3(userRequestDto.getAvatar(), userRequestDto.getEmail()));
            } catch (IOException e) {
                user.setAvatar("https://static.wikia.nocookie.net/fnaf-fanon-animatronics/images/4/40/??????????.png/revision/latest?cb=20190614113143&path-prefix=ru");
            }
        }
        return Optional.of(userRepo.save(user));
    }

    @Metric(name="retrieveById")
    public Optional<User> findById(Long id){
        try {
            return userRepo.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            return userRepo.findByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Boolean deleteById(Long id){
        try {
            if (userRepo.existsById(id)) {
                userRepo.deleteById(id);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteByEmail(String email) {
        try {
            if (userRepo.existsByEmail(email)) {
                userRepo.deleteByEmail(email);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        try {
            return userRepo.existsByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> updateByEmail(String email, User newUser) {
        Optional<User> user;
        try {
            user = userRepo.findByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        if(user.isEmpty()) {
            return Optional.empty();
        }

        user.get().setEmail(newUser.getEmail());
        user.get().setCountry(newUser.getCountry());
        user.get().setFirstName(newUser.getFirstName());
        user.get().setLastName(newUser.getLastName());
        user.get().setMiddleName(newUser.getMiddleName());
        user.get().setGender(newUser.getGender());
        if(newUser.getAvatar() != null) {
            if(user.get().getAvatar() != null) {
                s3Client.deleteObject("usercrud-avatars", user.get().getEmail());
            }
            user.get().setAvatar(getUserAvatarLinkAWSS3(newUser.getAvatar(), newUser.getEmail()));
        }
        return user;
    }

//    public List<User> getPageByCountry(String country, Integer pageNumber, Integer pageSize) {
//        return userRepo.findAllByCountry(country, PageRequest.of(pageNumber, pageSize));
//    }

    public List<User> getAllByCountry(String country) {
        try {
            return userRepo.findAllByCountry(country);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public Page<User> getPage(Integer pageNumber, Integer pageSize) {
//        return userRepo.findAll(PageRequest.of(pageNumber, pageSize));
//    }

    public List<User> getAll(){
        try {
            return userRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<User> updateById(Long id, User newUser) {
        Optional<User> user = null;
        try {
            user = userRepo.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        if(user.isEmpty()) {
            return Optional.empty();
        }
        user.get().setEmail(newUser.getEmail());
        user.get().setCountry(newUser.getCountry());
        user.get().setFirstName(newUser.getFirstName());
        user.get().setLastName(newUser.getLastName());
        user.get().setMiddleName(newUser.getMiddleName());
        user.get().setGender(newUser.getGender());
        if(newUser.getAvatar() != null) {
            if(user.get().getAvatar() != null) {
                s3Client.deleteObject("usercrud-avatars", user.get().getEmail());
            }
            user.get().setAvatar(getUserAvatarLinkAWSS3(newUser.getAvatar(), newUser.getEmail()));
        }
        return user;
    }

//    @Override
//    public void setRepository(UserRepository userRepository) {
//        this.userRepo = userRepository;
//    }
}
