package de.survey.demo.repository;

import de.survey.demo.domain.Survey;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SurveyRepository extends ListCrudRepository<Survey, UUID> {

    List<Survey> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);

    Optional<Survey> findByPublicId(String publicId);
}