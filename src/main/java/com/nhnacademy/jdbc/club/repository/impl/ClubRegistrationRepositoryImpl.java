package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("INSERT INTO jdbc_club_registrations VALUES(?,?)");
            statement.setString(1,studentId);
            statement.setString(2,clubId);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("DELETE FROM jdbc_club_registrations WHERE student_id=? AND club_id=?");
            statement.setString(1,studentId);
            statement.setString(2,clubId);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT id,name,c.club_id,club_name FROM jdbc_club_registrations cr LEFT JOIN jdbc_students s ON cr.student_id=s.id NATURAL LEFT JOIN jdbc_club c WHERE student_id=?");
            statement.setString(1,studentId);
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(studentId,rs.getString("name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_club_registrations cr JOIN jdbc_students s ON cr.student_id=s.id JOIN jdbc_club c ON cr.club_id=c.club_id ORDER BY student_id ASC,club_id ASC");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,cr.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s LEFT JOIN jdbc_club_registrations cr ON s.id=cr.student_id LEFT JOIN jdbc_club c ON cr.club_id=c.club_id ORDER BY student_id ASC,club_id ASC");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s RIGHT JOIN jdbc_club_registrations cr ON s.id=cr.student_id RIGHT JOIN jdbc_club c ON cr.club_id=c.club_id ORDER BY club_id ASC,student_id ASC");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join = left join union right join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s LEFT JOIN jdbc_club_registrations cr ON s.id=cr.student_id LEFT JOIN jdbc_club c ON cr.club_id=c.club_id UNION SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s RIGHT JOIN jdbc_club_registrations cr ON s.id=cr.student_id RIGHT JOIN jdbc_club c ON cr.club_id=c.club_id");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s LEFT JOIN jdbc_club_registrations cr ON s.id=cr.student_id LEFT JOIN jdbc_club c ON cr.club_id=c.club_id WHERE cr.club_id IS NULL");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s RIGHT JOIN jdbc_club_registrations cr ON s.id=cr.student_id RIGHT JOIN jdbc_club c ON cr.club_id=c.club_id WHERE cr.club_id IS NULL");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join = left excluding join union right excluding join
        List<ClubStudent> list=Collections.emptyList();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s LEFT JOIN jdbc_club_registrations cr ON s.id=cr.student_id LEFT JOIN jdbc_club c ON cr.club_id=c.club_id WHERE cr.club_id IS NULL UNION SELECT s.id AS student_id,s.name AS student_name,c.club_id AS club_id,c.club_name AS club_name FROM jdbc_students s RIGHT JOIN jdbc_club_registrations cr ON s.id=cr.student_id RIGHT JOIN jdbc_club c ON cr.club_id=c.club_id WHERE cr.club_id IS NULL");
            ResultSet rs=statement.executeQuery();

            list=new ArrayList<>();
            while(rs.next()){
                ClubStudent cs=new ClubStudent(rs.getString("student_id"),rs.getString("student_name"),rs.getString("club_id"),rs.getString("club_name"));
                list.add(cs);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

}