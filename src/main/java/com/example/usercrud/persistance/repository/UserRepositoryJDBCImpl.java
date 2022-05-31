package com.example.usercrud.persistance.repository;

import com.example.usercrud.business.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class UserRepositoryJDBCImpl implements UserRepository {
    private final Connection connection;

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        Optional<User> user = Optional.empty();
        try(var statement = connection.prepareStatement("SELECT * FROM users WHERE email=?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = Optional.of(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return user;
    }

    @Override
    public User getByEmail(String email) throws SQLException {
        User user = null;
        try (var statement = connection.prepareStatement("SELECT * FROM users WHERE email=?");) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return user;
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        boolean exists = false;
        try (var statement = connection.prepareStatement("SELECT * FROM users WHERE email=?")) {
            statement.setString(1, email);
            exists = statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return exists;
    }

    @Override
    public void deleteByEmail(String email) throws SQLException {
        try (
            PreparedStatement statement = connection.prepareStatement("DELETE * FROM users WHERE email=?")) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<User> findAllByCountry(String country) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE country=?")) {
            statement.setString(1, country);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                users.add(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return users;
    }

    @Override
    public List<User> findAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while(resultSet.next()) {
                users.add(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return users;
    }

    @Override
    public User save(User user) {
        User savedUser = null;
        try (var statement = connection.prepareStatement("INSERT INTO users (first_name, last_name, middle_name, country, gender, email, avatar) VALUES(?, ?, ?, ?, ?, ?, ?)"))  {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getMiddleName());
            statement.setString(4, user.getCountry());
            statement.setInt(5, user.getGender());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getAvatar());
            statement.executeUpdate();
            savedUser = getByEmail(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedUser;
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        Optional<User> user = Optional.empty();
        try (var statement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                user = Optional.of(new User(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8)));
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
        return user;
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        try (var statement = connection.prepareStatement("DELETE * FROM users WHERE id=?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean existsById(Long id) throws SQLException {
        boolean exists = false;
        try (var statement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            statement.setLong(1, id);
            exists = statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return exists;
    }
}
