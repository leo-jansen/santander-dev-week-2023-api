package me.dio.service.impl;

import me.dio.domain.model.Account;
import me.dio.domain.repository.AccountRepository;
import me.dio.service.AccountService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    /**
     * ID de account utilizado na Santander Dev Week 2023.
     * Por isso, vamos criar algumas regras para mantê-lo integro.
     */
    private static final Long UNCHANGEABLE_ACCOUNT_ID = 1L;

    @Autowired
    AccountRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Account create(Account entity) {
        this.validateChangeableId(entity.getId(), "created");
        Optional<Account> account = repository.findById(entity.getId());
        if (account.isPresent()) {
            throw new BusinessException("This account already exists.");
        } else {
            return repository.save(entity);
        }
    }

    @Override
    @Transactional
    public Account update(Long id, Account entity) {
        this.validateChangeableId(id, "updated");
        Optional<Account> account = repository.findById(id);
        if (account.isPresent() && account.get().getId().equals(entity.getId())) {
            account.get().setAgency(entity.getAgency());
            account.get().setBalance(entity.getBalance());
            account.get().setLimit(entity.getLimit());
            account.get().setLimit(entity.getLimit());

            return repository.save(account.get());
        } else {
            throw new BusinessException("Update IDs must be the same.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Account account = this.findById(id);
        repository.delete(account);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_ACCOUNT_ID.equals(id)) {
            throw new BusinessException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_ACCOUNT_ID, operation));
        }
    }
}
