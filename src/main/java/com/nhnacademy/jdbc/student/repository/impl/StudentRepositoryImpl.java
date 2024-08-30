package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.Optional;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student){
        //todo#2 학생등록
        int count=0;
        try{
            PreparedStatement statement=connection.prepareStatement("INSERT INTO jdbc_students VALUES(?,?,?,?,?)");
            statement.setString(1,student.getId());
            statement.setString(2,student.getName());
            statement.setString(3,student.getGender().toString());
            statement.setInt(4,student.getAge());
            statement.setString(5,student.getCreatedAt().toString());

            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException();
        }

        return count;
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회
        Optional<Student> result=Optional.empty();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM jdbc_students WHERE id=?");
            statement.setString(1,id);
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                Student student=new Student(id,rs.getString("name"),Student.GENDER.valueOf(rs.getString("gender")),rs.getInt("age"),rs.getTimestamp("created_at").toLocalDateTime());
                result=Optional.of(student);
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }

        return result;
    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("UPDATE jdbc_students SET name=?,gender=?,age=? WHERE id=?");
            statement.setString(1,student.getName());
            statement.setString(2,student.getGender().toString());
            statement.setInt(3,student.getAge());
            statement.setString(4,student.getId());

            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException();
        }

        return count;
    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("DELETE FROM jdbc_students WHERE id=?");
            statement.setString(1,id);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException();
        }

        return count;
    }

}