package com.maoz.Nice_HW.service;

import com.maoz.Nice_HW.config.Constants;
import com.maoz.Nice_HW.suggestTaskUtils.TaskDictionary;
import com.maoz.Nice_HW.suggestTaskUtils.Vocabulary;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;
import smile.math.MathEx;

import java.util.*;

/**
 * Service implementation of SuggestTaskInterface that uses a classification approach.
 *
 * This service uses the shared TaskDictionary to train a Logistic Regression model for
 * multi-class classification. The model predicts which task corresponds to a given
 * user utterance.
 */
@Service
public class SuggestTaskClassifierService implements SuggestTaskInterface {

    private static final Logger logger = LoggerFactory.getLogger(SuggestTaskClassifierService.class);
    private LogisticRegression model; // Logistic Regression model (multi-class softmax)
    private final Vocabulary vocab = new Vocabulary(); // Vocabulary used to convert text to vectors
    private final Map<Integer, String> labelToTask = new HashMap<>(); // Mapping of label indices to task names
    private final TaskDictionary taskDictionary; // Central dictionary of task to synonyms, shared across services

    public SuggestTaskClassifierService(TaskDictionary taskDictionary) {
        this.taskDictionary = taskDictionary;
    }

    /**
     * Train the Logistic Regression model. Steps:
     * 1. Collect training data (utterance → task) from the dictionary.
     * 2. Build vocabulary from all utterances (used to convert text → numeric vectors).
     * 3. Convert sentences to vectors (X) and tasks to integer labels (y).
     * 4. Fit Logistic Regression with softmax for multi-class classification.
     */
    @PostConstruct
    public void train() {
        // Step 1: Prepare training data
        List<String[]> trainingData = new ArrayList<>();
        taskDictionary.getDictionary().forEach((task, synonyms) -> {
            for (String utterance : synonyms) {
                trainingData.add(new String[]{utterance, task});
            }
        });

        // Step 2: Build vocabulary
        for (String[] sample : trainingData) {
            vocab.addSentence(sample[0]);
        }

        // Step 3: Convert sentences to vectors and tasks to labels
        double[][] X = new double[trainingData.size()][];
        int[] y = new int[trainingData.size()];

        Map<String, Integer> taskToLabel = new HashMap<>();
        int labelIndex = 0;

        for (int i = 0; i < trainingData.size(); i++) {
            String sentence = trainingData.get(i)[0];
            String task = trainingData.get(i)[1];

            if (!taskToLabel.containsKey(task)) {
                taskToLabel.put(task, labelIndex);
                labelToTask.put(labelIndex, task);
                labelIndex++;
            }

            y[i] = taskToLabel.get(task);
            X[i] = vocab.toVector(sentence);
        }

        // Step 4: Fit Logistic Regression (softmax) for multi-class classification
        MathEx.setSeed(123); // reproducibility
        model = LogisticRegression.fit(X, y);

        logger.info("Classifier trained on {} utterances across {} tasks", X.length, labelToTask.size());
    }

    /**
     * Suggest a task for the given utterance using the trained classifier. Steps:
     * 1. Convert utterance to vector using vocabulary.
     * 2. Predict probabilities for all tasks using Logistic Regression.
     * 3. Identify task with the highest probability.
     * 4. If the probability exceeds the threshold, return task. otherwise, return NoTaskFound.
     *
     * @param utterance the user input text
     * @return predicted task or NoTaskFound
     */
    @Override
    public String suggestTask(String utterance) {
        if (utterance == null || utterance.isBlank()) {
            logger.warn("Received empty utterance");
            return Constants.NO_TASK_FOUND;
        }

        // Step 1: Convert to vector
        double[] vector = vocab.toVector(utterance);

        // Step 2: Predict probabilities
        double[] probs = new double[labelToTask.size()];
        int predictedLabel = model.predict(vector, probs);

        logger.info("Probabilities for utterance '{}':", utterance);
        for (int i = 0; i < probs.length; i++) {
            logger.info("  Task '{}' -> {}%", labelToTask.get(i), String.format("%.2f", probs[i] * 100));
        }

        // Step 3: Identify max probability
        double maxProb = Arrays.stream(probs).max().orElse(-1);
        int maxIdx = -1;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] == maxProb) {
                maxIdx = i;
                break;
            }
        }

        // Step 4: Apply threshold
        String predictedTask = maxProb < Constants.CLASSIFIER_THRESHOLD
                ? Constants.NO_TASK_FOUND
                : labelToTask.get(maxIdx);

        logger.info("Predicted task: '{}' (max probability {}%)", predictedTask, String.format("%.2f", maxProb * 100));
        return predictedTask;
    }
}
