package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 insert student
        Connection conn=DbUtils.getConnection();
        int count=0;

        try {
            count=conn.createStatement().executeUpdate("INSERT INTO jdbc_students VALUES('"+student.getId()+"','"+student.getName()+"','"+student.getGender()+"','"+student.getAge()+"','"+student.getCreatedAt()+"')");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회
        Connection conn=DbUtils.getConnection();
        Optional<Student> result=Optional.empty();

        try{
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM jdbc_students WHERE id='"+id+"'");
            if(resultSet.next()){
                String name=resultSet.getString("name");
                String genderString=resultSet.getString("gender");
                int age=resultSet.getInt("age");
                LocalDateTime createdAt=resultSet.getTimestamp("created_at").toLocalDateTime();

                Student.GENDER gender=null;
                for(Student.GENDER g:Student.GENDER.values()){
                    if(g.name().equals(genderString)){
                        gender=g;
                        break;
                    }
                }
                result=Optional.of(new Student(id,name,gender,age,createdAt));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.
        Connection conn=DbUtils.getConnection();
        int count=0;

        try{
            count=conn.createStatement().executeUpdate("UPDATE jdbc_students SET name='"+student.getName()+"',age="+student.getAge()+",gender='"+student.getGender()+"' WHERE id='"+student.getId()+"'");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public int deleteById(String id){
       //todo#4 student 삭제
        Connection conn=DbUtils.getConnection();
        int count=0;

        try{
            count=conn.createStatement().executeUpdate("DELETE FROM jdbc_students WHERE id='"+id+"'");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

}
