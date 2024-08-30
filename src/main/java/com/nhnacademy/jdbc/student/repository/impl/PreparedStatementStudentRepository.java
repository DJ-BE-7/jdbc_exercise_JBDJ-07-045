package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 학생 등록
        int count=0;
        try(Connection conn=DbUtils.getConnection();
            PreparedStatement statement=conn.prepareStatement("INSERT INTO jdbc_students VALUES(?,?,?,?,?)");){
            statement.setString(1,student.getId());
            statement.setString(2,student.getName());
            statement.setString(3,student.getGender().toString());
            statement.setInt(4,student.getAge());
            statement.setString(5,student.getCreatedAt().toString());

            count=statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 학생 조회
        Optional<Student> result=Optional.empty();

        try(PreparedStatement statement=DbUtils.getConnection().prepareStatement("SELECT * FROM jdbc_students WHERE id=?")){
            statement.setString(1,id);
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                Student student=new Student(rs.getString("id"),rs.getString("name"),Student.GENDER.valueOf(rs.getString("gender")),rs.getInt("age"),rs.getTimestamp("created_at").toLocalDateTime());
                result=Optional.of(student);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(Student student){
        //todo#3 학생 수정 , name 수정
        int count=0;

        try(PreparedStatement statement=DbUtils.getConnection().prepareStatement("UPDATE jdbc_students SET name=?,gender=?,age=? where id=?")){
            statement.setString(1,student.getName());
            statement.setString(2,student.getGender().toString());
            statement.setInt(3,student.getAge());
            statement.setString(4,student.getId());

            count=statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int deleteById(String id){
        //todo#4 학생 삭제
        int count=0;

        try(PreparedStatement statement=DbUtils.getConnection().prepareStatement("DELETE FROM jdbc_students WHERE id=?")){
            statement.setString(1,id);
            count=statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

}
