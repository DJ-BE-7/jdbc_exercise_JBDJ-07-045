package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import java.sql.*;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회
        Optional<Account> result=Optional.empty();
        try{
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM jdbc_account WHERE account_number=?");
            statement.setLong(1,accountNumber);
            ResultSet rs=statement.executeQuery();

            if(rs.next()){
                Account account=new Account(accountNumber,rs.getString("name"),rs.getLong("balance"));
                result=Optional.of(account);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("INSERT INTO jdbc_account VALUES(?,?,?)");
            statement.setLong(1,account.getAccountNumber());
            statement.setString(2,account.getName());
            statement.setLong(3,account.getBalance());

            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        try{
            PreparedStatement statement=connection.prepareStatement("SELECT COUNT(*) AS count FROM jdbc_account WHERE account_number=?");
            statement.setLong(1,accountNumber);
            ResultSet rs=statement.executeQuery();
            if(rs.next()){
                count=rs.getInt("count");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
        int count=0;

        try{
            PreparedStatement statement=connection.prepareStatement("SELECT balance FROM jdbc_account WHERE account_number=?");
            statement.setLong(1,accountNumber);
            ResultSet rs=statement.executeQuery();
            long balance=0;
            if(rs.next()){
                balance=rs.getLong("balance");
            }
            balance+=amount;

            statement=connection.prepareStatement("UPDATE jdbc_account SET balance=? WHERE account_number=?");
            statement.setLong(1,balance);
            statement.setLong(2,accountNumber);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        int count=0;
        try{
            PreparedStatement statement=connection.prepareStatement("SELECT balance FROM jdbc_account WHERE account_number=?");
            statement.setLong(1,accountNumber);
            ResultSet rs=statement.executeQuery();
            long balance=0;
            if(rs.next()){
                balance=rs.getLong("balance");
            }
            balance-=amount;

            statement=connection.prepareStatement("UPDATE jdbc_account SET balance=? WHERE account_number=?");
            statement.setLong(1,balance);
            statement.setLong(2,accountNumber);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        int count=0;
        try{
            PreparedStatement statement=connection.prepareStatement("DELETE FROM jdbc_account WHERE account_number=?");
            statement.setLong(1,accountNumber);
            count=statement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return count;
    }
}
