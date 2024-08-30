package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        //todo#3 club 조회
        Optional<Club> result=Optional.empty();

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM jdbc_club WHERE club_id=?");
            statement.setString(1,clubId);
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                Club club=new Club(clubId,rs.getString("club_name"),rs.getTimestamp("club_created_at").toLocalDateTime());
                result=Optional.of(club);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("INSERT INTO jdbc_club VALUES(?,?,?)");
            statement.setString(1,club.getClubId());
            statement.setString(2,club.getClubName());
            statement.setString(3,club.getClubCreatedAt().toString());

            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("UPDATE jdbc_club SET club_name=? WHERE club_id=?");
            statement.setString(1,club.getClubName());
            statement.setString(2,club.getClubId());

            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("DELETE FROM jdbc_club WHERE club_id=?");
            statement.setString(1,clubId);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT COUNT(*) AS count FROM jdbc_club WHERE club_id=?");
            statement.setString(1,clubId);
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                count=rs.getInt("count");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }
}
