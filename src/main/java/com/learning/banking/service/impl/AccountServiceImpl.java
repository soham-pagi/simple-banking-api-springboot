package com.learning.banking.service.impl;

import com.learning.banking.dto.AccountDto;
import com.learning.banking.entity.Account;
import com.learning.banking.mapper.AccountMapper;
import com.learning.banking.repository.AccountRepository;
import com.learning.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists!"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists!"));

        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists!"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient amount!");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);

        var savedAccont = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccont);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        var accounts = accountRepository.findAll();

        return accounts.stream().map(AccountMapper::mapToAccountDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exists!"));

        accountRepository.deleteById(id);
    }
}
