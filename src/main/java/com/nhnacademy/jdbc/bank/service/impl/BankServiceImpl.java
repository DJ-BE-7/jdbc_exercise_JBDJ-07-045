package com.nhnacademy.jdbc.bank.service.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.repository.impl.AccountRepositoryImpl;
import com.nhnacademy.jdbc.bank.service.BankService;

import java.sql.Connection;
import java.util.Optional;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber){
        //todo#11 계좌-조회
        Optional<Account> findAccount=accountRepository.findByAccountNumber(connection,accountNumber);
        if(!findAccount.isPresent()){
            throw new AccountNotFoundException(accountNumber);
        }

        return findAccount.get();
    }

    @Override
    public void createAccount(Connection connection, Account account){
        //todo#12 계좌-등록
        if(accountRepository.save(connection,account)==0){
            throw new AccountAreadyExistException(account.getAccountNumber());
        }
    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount){
        //todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
        if(accountRepository.countByAccountNumber(connection,accountNumber)==0){
            throw new AccountNotFoundException(accountNumber);
        }
        Optional<Account> optionalAccount=accountRepository.findByAccountNumber(connection,accountNumber);

        if(accountRepository.deposit(connection,accountNumber,amount)==0){
            return false;
        }

        return true;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount){
        //todo#14 출금, 계좌가 존재하는지 체크 ->  출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
        if(accountRepository.countByAccountNumber(connection,accountNumber)==0){
            throw new AccountNotFoundException(accountNumber);
        }

        long afterAmount=accountRepository.findByAccountNumber(connection,accountNumber).get().getBalance()-amount;
        if(afterAmount<0){
            throw new BalanceNotEnoughException(accountNumber);
        }

        accountRepository.withdraw(connection,accountNumber,amount);

        return true;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount){
        //todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
//        if(accountRepository.countByAccountNumber(connection,accountNumberFrom)==0){
//            throw new AccountNotFoundException(accountNumberFrom);
//        }
//        if(accountRepository.countByAccountNumber(connection,accountNumberTo)==0){
//            throw new AccountNotFoundException(accountNumberTo);
//        }

        depositAccount(connection,accountNumberTo,amount);
        if(!withdrawAccount(connection,accountNumberFrom,amount)){
            throw new BalanceNotEnoughException(accountNumberFrom);
        }
    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber){
        //todo#16 Account가 존재하면 true , 존재하지 않다면 false
        if(accountRepository.countByAccountNumber(connection,accountNumber)>0){
            return true;
        }

        return false;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        //todo#17 account 삭제
        if(accountRepository.countByAccountNumber(connection,accountNumber)==0){
            throw new AccountNotFoundException(accountNumber);
        }
        int result=accountRepository.deleteByAccountNumber(connection,accountNumber);
        if(result==0){
            throw new RuntimeException();
        }
    }

}