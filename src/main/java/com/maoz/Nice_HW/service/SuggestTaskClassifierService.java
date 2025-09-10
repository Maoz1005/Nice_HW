package com.maoz.Nice_HW.service;

import com.maoz.Nice_HW.config.Constants;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;
import smile.math.MathEx;

import java.util.*;

@Service
public class ClassifierService implements SuggestTaskInterface {

    private static final Logger logger = LoggerFactory.getLogger(ClassifierService.class);
    private LogisticRegression model;
    private Vocabulary vocab = new Vocabulary();
    private Map<Integer, String> labelToTask = new HashMap<>();

    @PostConstruct
    public void train() {
        // שלב 1: דאטה קטן (utterance → task)
        String[][] texts = {
                {"reset password", "ResetPasswordTask"},
                {"reset my password", "ResetPasswordTask"},
                {"reset my password please", "ResetPasswordTask"},
                {"I want to reset my password", "ResetPasswordTask"},
                {"I want to reset my password please", "ResetPasswordTask"},
                {"forgot password", "ResetPasswordTask"},
                {"forgot my password", "ResetPasswordTask"},
                {"I forgot my password", "ResetPasswordTask"},
                {"check order", "CheckOrderStatusTask"},
                {"check my order", "CheckOrderStatusTask"},
                {"check my order please", "CheckOrderStatusTask"},
                {"I want to check my order", "CheckOrderStatusTask"},
                {"I want to check my order please", "CheckOrderStatusTask"},
                {"check my orders status", "CheckOrderStatusTask"},
                {"I want to check my orders status", "CheckOrderStatusTask"},
                {"I want to check my orders status please", "CheckOrderStatusTask"},
                {"track order", "CheckOrderStatusTask"},
                {"track my order", "CheckOrderStatusTask"},
                {"track my order please", "CheckOrderStatusTask"},
                {"I want to track my order", "CheckOrderStatusTask"},
                {"I want to track my order please", "CheckOrderStatusTask"},
                {"Where is my order?", "CheckOrderStatusTask"},
                {"cancel order", "CancelOrderTask"},
                {"cancel my order", "CancelOrderTask"},
                {"cancel my order please", "CancelOrderTask"},
                {"I want to cancel my order", "CancelOrderTask"},
                {"I want to cancel my order please", "CancelOrderTask"},
                {"can I cancel?", "CancelOrderTask"},
                {"new order", "MakeOrderTask"},
                {"I want to order", "MakeOrderTask"},
                {"I want to place an order", "MakeOrderTask"},
                {"I want to make an order", "MakeOrderTask"},
                {"I want to order please", "MakeOrderTask"},
                {"I want to place an order please", "MakeOrderTask"},
                {"I want to make an order please", "MakeOrderTask"},
                {"can I order?", "MakeOrderTask"}
        };

        // שלב 2: בניית ווקבולרי
        for (String[] sample : texts) {
            vocab.addSentence(sample[0]);
        }

        // שלב 3: המרת טקסטים לוקטורים
        double[][] X = new double[texts.length][];
        int[] y = new int[texts.length];

        Map<String, Integer> taskToLabel = new HashMap<>();
        int labelIndex = 0;

        for (int i = 0; i < texts.length; i++) {
            String sentence = texts[i][0];
            String task = texts[i][1];

            if (!taskToLabel.containsKey(task)) {
                taskToLabel.put(task, labelIndex);
                labelToTask.put(labelIndex, task);
                labelIndex++;
            }

            y[i] = taskToLabel.get(task);
            X[i] = vocab.toVector(sentence);
        }

        // שלב 4: אימון Logistic Regression עם softmax (multiclass)
        MathEx.setSeed(123); // לשחזור
        model = LogisticRegression.fit(X, y);
    }

    public String suggestTask(String utterance) {
        if (utterance == null || utterance.isBlank()) {
            logger.warn("Received empty utterance");
            return Constants.NO_TASK_FOUND;
        }

        double[] vector = vocab.toVector(utterance);
        double[] probs = new double[labelToTask.size()];
        int label = model.predict(vector, probs);
        logger.info("Task: '{}', Probability: {}%", labelToTask.get(label), String.format("%.2f", probs[label] * 100));

        // חיפוש ההסתברות המקסימלית
        double maxProb = -1;
        int maxIdx = -1;
        for (int i = 0; i < probs.length; i++) {
            if (probs[i] > maxProb) {
                maxProb = probs[i];
                maxIdx = i;
            }
        }

        // לוגינג
        logger.info("Predicting task for utterance: '{}'", utterance);
        for (int i = 0; i < probs.length; i++) {
            logger.info("Task: '{}', Probability: {}%", labelToTask.get(i), String.format("%.2f", probs[i] * 100));
        }
        logger.info("Max probability: {}% for task '{}'", String.format("%.2f", maxProb * 100), labelToTask.get(maxIdx));

        // החלטה על NoTaskFound אם ההסתברות נמוכה מדי
        String predictedTask = maxProb < Constants.CLASSIFIER_THRESHOLD ? Constants.NO_TASK_FOUND : labelToTask.get(maxIdx);
        logger.info("Final predicted task: '{}'", predictedTask);

        return predictedTask;
    }


}
