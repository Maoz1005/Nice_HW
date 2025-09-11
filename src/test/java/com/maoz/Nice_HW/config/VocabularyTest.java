package com.maoz.Nice_HW.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VocabularyTest {

    private Vocabulary vocab;

    @BeforeEach
    void setUp() {
        vocab = new Vocabulary();
    }

    @Test
    void addSentence_createsIndexes() {
        vocab.addSentence("Hello world");

        // מילון מכיל שתי מילים
        double[] vecHello = vocab.toVector("Hello");
        double[] vecWorld = vocab.toVector("world");

        assertEquals(2, vecHello.length);
        assertEquals(1.0, vecHello[0], 1e-9);
        assertEquals(0.0, vecHello[1], 1e-9);

        assertEquals(0.0, vecWorld[0], 1e-9);
        assertEquals(1.0, vecWorld[1], 1e-9);
    }

    @Test
    void toVector_countsWordsCorrectly() {
        vocab.addSentence("a a b");

        double[] vec = vocab.toVector("a b a");
        assertEquals(2, vec.length);
        assertEquals(2.0, vec[0], 1e-9); // "a" מופיעה פעמיים
        assertEquals(1.0, vec[1], 1e-9); // "b" מופיעה פעם אחת
    }

    @Test
    void toVector_unknownWord_ignored() {
        vocab.addSentence("known");

        double[] vec = vocab.toVector("unknown");
        assertEquals(1, vec.length); // מילון עם מילה אחת
        assertEquals(0.0, vec[0], 1e-9); // מילה לא קיימת במילון → 0
    }

    @Test
    void addSentence_expandsVocabulary() {
        vocab.addSentence("foo bar");
        vocab.addSentence("baz qux");

        // כעת המילון צריך להכיל 4 מילים
        double[] vec = vocab.toVector("foo baz qux unknown");
        assertEquals(4, vec.length);

        // "foo" באינדקס 0
        assertEquals(1.0, vec[0], 1e-9);
        // "bar" לא מופיעה במשפט → 0
        assertEquals(0.0, vec[1], 1e-9);
        // "baz" באינדקס 2
        assertEquals(1.0, vec[2], 1e-9);
        // "qux" באינדקס 3
        assertEquals(1.0, vec[3], 1e-9);
    }

    @Test
    void addSentence_duplicateWords_doesNotDuplicateInVocabulary() {
        vocab.addSentence("hello world hello");

        // מילון צריך להכיל רק שתי מילים
        double[] vec = vocab.toVector("hello world");
        assertEquals(2, vec.length);
        assertEquals(1.0, vec[1], 1e-9); // "world"
        assertEquals(1.0, vec[0], 1e-9); // "hello" בספירה ראשונה (לוקטור ספירה)
    }

    @Test
    void multipleSentences_correctVectorCounts() {
        vocab.addSentence("cat dog");
        vocab.addSentence("dog mouse");

        double[] vec = vocab.toVector("cat dog mouse dog");
        assertEquals(3, vec.length);
        assertEquals(1.0, vec[0], 1e-9); // cat
        assertEquals(2.0, vec[1], 1e-9); // dog
        assertEquals(1.0, vec[2], 1e-9); // mouse
    }
}
