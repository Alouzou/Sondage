package com.alouzou.sondage.services.Impl;

import com.alouzou.sondage.dto.QuestionDTO;
import com.alouzou.sondage.dto.SurveyDTO;
import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.entities.Question;
import com.alouzou.sondage.entities.Survey;
import com.alouzou.sondage.entities.User;
import com.alouzou.sondage.exceptions.EntityNotFoundException;
import com.alouzou.sondage.exceptions.ResourceAlreadyUsedException;
import com.alouzou.sondage.repositories.CategoryRepository;
import com.alouzou.sondage.repositories.SurveyRepository;
import com.alouzou.sondage.repositories.UserRepository;
import com.alouzou.sondage.services.SurveyService;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {
    private static final Logger log = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Survey createSurvey(SurveyDTO surveyDTO) {

        surveyRepository.findByTitle(surveyDTO.getTitle()).ifPresent(s -> {
            throw new ResourceAlreadyUsedException("Le titre est déjà utilisé. Veuillez choisir un autre titre.");
        });

        User creator = userRepository.findById(surveyDTO.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException("Créateur non trouvé"));
        log.info("Création d'un sondage par {}", creator.getEmail());

        Category category = categoryRepository.findById(surveyDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée !"));

        Survey survey = new Survey();
        survey.setTitle(surveyDTO.getTitle());
        survey.setCreator(creator);
        survey.setCategory(category);
        return surveyRepository.save(survey);
    }

    @Override
    public List<Survey> getSurveysByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée !"));

        List<Survey> surveys = surveyRepository.findByCategory(category);

        if (surveys.isEmpty()) {
            throw new EntityNotFoundException("Aucun sondage trouvé pour cette catégorie !");
        }

        return surveys;
    }

    @Override
    public List<Survey> getSurveysByCreator(Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Créateur non trouvé avec l'ID : " + creatorId));

        return surveyRepository.findByCreator(creator);
    }

    @Override
    public Optional<Survey> getSurveyById(Long id) {
        surveyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sondage non trouvé avec l'ID : " + id));
        return surveyRepository.findById(id);
    }

    @Override
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @Override
    public void deleteSurvey(Long idSurvey) {
        surveyRepository.findById(idSurvey)
                .orElseThrow(() -> new EntityNotFoundException("Sondage non trouvé avec id : " + idSurvey));
        surveyRepository.deleteById(idSurvey);

    }
}
