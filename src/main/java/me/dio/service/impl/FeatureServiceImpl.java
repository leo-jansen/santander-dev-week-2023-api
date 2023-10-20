package me.dio.service.impl;

import me.dio.domain.model.Feature;
import me.dio.domain.repository.FeatureRepository;
import me.dio.service.FeatureService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImpl implements FeatureService {

    /**
     * ID de feature utilizado na Santander Dev Week 2023.
     * Por isso, vamos criar algumas regras para mantê-lo integro.
     */
    private static final Long UNCHANGEABLE_FEATURE_ID = 1L;

    @Autowired
    FeatureRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Feature> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Feature findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Feature create(Feature featureToCreate) {
        this.validateChangeableId(featureToCreate.getId(), "created");
        Optional<Feature> feature = repository.findById(featureToCreate.getId());
        if (feature.isPresent()) {
            throw new BusinessException("This feature already exists.");
        } else {
            return repository.save(featureToCreate);
        }
    }

    @Override
    @Transactional
    public Feature update(Long id, Feature featureToUpdate) {
        this.validateChangeableId(id, "updated");
        Feature dbFeature = this.findById(id);
        if (!dbFeature.getId().equals(featureToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        dbFeature.setDescription(featureToUpdate.getDescription());
        dbFeature.setIcon(featureToUpdate.getIcon());

        return repository.save(dbFeature);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.validateChangeableId(id, "deleted");
        Feature feature = this.findById(id);
        repository.save(feature);
    }

    private void validateChangeableId(Long id, String operation) {
        if (UNCHANGEABLE_FEATURE_ID.equals(id)) {
            throw new BusinessException("User with ID %d can not be %s.".formatted(UNCHANGEABLE_FEATURE_ID, operation));
        }
    }
}
