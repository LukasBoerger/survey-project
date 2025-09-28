package de.survey.demo.service;

import de.survey.demo.domain.Question;
import de.survey.demo.domain.Survey;
import de.survey.demo.repository.SurveyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SurveyService {
//    private final SurveyRepository repo;
//    public SurveyService(SurveyRepository repo){ this.repo = repo; }
//
//    @Transactional
//    public SurveyDto create(UUID ownerId, CreateSurveyRequest req) {
//        var s = Survey.newSurvey(ownerId, ShortId.next(10), req.title());
//        var qs = new ArrayList<Question>();
//        int pos=0;
//        for (var q : req.questions()) qs.add(new Question(null, q.type(), q.prompt(), pos++));
//        s.replaceQuestions(qs);
//        var saved = repo.save(s);
//        return new SurveyDto(saved.getId().toString(), saved.getTitle(), saved.getPublicId());
//    }
//
//    @Transactional(readOnly = true)
//    public List<SurveyDto> list(UUID ownerId) {
//        return repo.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
//                .map(s -> new SurveyDto(s.getId().toString(), s.getTitle(), s.getPublicId()))
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<PublicSurveyView> getPublic(String publicId) {
//        return repo.findByPublicId(publicId).map(s ->
//                new PublicSurveyView(
//                        s.getTitle(),
//                        s.getQuestions().stream()
//                                .sorted(Comparator.comparingInt(Question::getPosition))
//                                .map(q -> new QuestionDto(
//                                        q.getId()==null?null:q.getId().toString(), q.getType(), q.getPrompt(), q.getPosition()
//                                )).toList()
//                )
//        );
//    }
//
//    @Transactional
//    public void update(UUID ownerId, UUID id, UpdateSurveyRequest req) {
//        var s = repo.findById(id).orElseThrow();
//        if (!s.getOwnerId().equals(ownerId)) throw new RuntimeException("Forbidden");
//        var qs = new ArrayList<Question>();
//        for (var q : req.questions()) qs.add(new Question(null, q.type(), q.prompt(), q.position()));
//        s.replaceQuestions(qs);
//        s.touch();
//        s.setTitle(req.title());
//        repo.save(s);
//    }
//
//    @Transactional
//    public void delete(UUID ownerId, UUID id) {
//        var s = repo.findById(id).orElseThrow();
//        if (!s.getOwnerId().equals(ownerId)) throw new RuntimeException("Forbidden");
//        repo.deleteById(id);
//    }
}
