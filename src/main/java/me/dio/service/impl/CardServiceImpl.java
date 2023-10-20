package me.dio.service.impl;

import me.dio.domain.model.Card;
import me.dio.domain.repository.CardRepository;
import me.dio.service.CardService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    /**
     * ID de card utilizado na Santander Dev Week 2023.
     * Por isso, vamos criar algumas regras para mantê-lo integro.
     */
    private static final Long UNCHANGEABLE_CARD_ID = 1L;

    @Autowired
    CardRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Card findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Card create(Card cardToCreate) {
        this.validateChangeableId(cardToCreate.getId(), "created");
        Optional<Card> card = repository.findById(cardToCreate.getId());
        if (card.isPresent()) {
            throw new BusinessException("This card already exists.");
        } else {
            return repository.save(cardToCreate);
        }
    }

    @Override
    @Transactional
    public Card update(Long id, Card cardToUpdate) {
        this.validateChangeableId(id, "deleted");
        Card dbCard = this.findById(id);
        if (!dbCard.getId().equals(cardToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbCard.setLimit(cardToUpdate.getLimit());
        dbCard.setNumber(cardToUpdate.getNumber());

        return repository.save(dbCard);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Card card = this.findById(id);
        repository.delete(card);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_CARD_ID.equals(id)) {
            throw new BusinessException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_CARD_ID, operation));
        }
    }
}
