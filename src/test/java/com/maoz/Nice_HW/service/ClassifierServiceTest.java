package com.maoz.Nice_HW.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassifierServiceTest {

    private ClassifierService classifier;

    @BeforeEach
    void setup() {
        classifier = new ClassifierService();
        classifier.train(); // לאימון המודל הקטן
    }

    @Test
    void testPredict_knownUtterances() {
        assertEquals("ResetPasswordTask", classifier.predict("reset password"));
        assertEquals("ResetPasswordTask", classifier.predict("I want to reset my password please"));
        assertEquals("CheckOrderStatusTask", classifier.predict("track my order"));
        assertEquals("CancelOrderTask", classifier.predict("cancel my order please"));
        assertEquals("MakeOrderTask", classifier.predict("I want to place an order"));
    }

    @Test
    void testPredict_unknownUtterances() {
        assertEquals("NoTaskFound", classifier.predict("random command"));
        assertEquals("NoTaskFound", classifier.predict(""));
        assertEquals("NoTaskFound", classifier.predict(null));
    }
}
