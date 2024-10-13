package edu.umd.cs.findbugs.detect;

import edu.umd.cs.findbugs.AbstractIntegrationTest;
import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.StringAnnotation;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;

class UnsafeDeserializationTest extends AbstractIntegrationTest {

    /* Bad examples */
    /* Basic examples */

    @Test
    void testBadUnsafeDeserialization() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserialization", "readObject");
        assertUDBug("BadUnsafeDeserialization", "mutable (unsafeDeserialization.BadUnsafeDeserialization)");
    }

    @Test
    void testBadUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationSeveralFields.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationSeveralFields", "readObject");
        assertUDBug("BadUnsafeDeserializationSeveralFields",
                "anotherMutable (unsafeDeserialization.BadUnsafeDeserializationSeveralFields), mutable (unsafeDeserialization.BadUnsafeDeserializationSeveralFields)");
    }

    @Test
    void testBadUnsafeDeserializationWrongOrder() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationWrongOrder.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationWrongOrder", "readObject");
        assertUDBug("BadUnsafeDeserializationWrongOrder", "mutable (unsafeDeserialization.BadUnsafeDeserializationWrongOrder)");
    }

    /* Inheritance */

    @Test
    void testBadUnsafeDeserializationInheritance() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class",
                "unsafeDeserialization/BadUnsafeDeserializationInheritance.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserialization", "readObject");
        assertUDBug("BadUnsafeDeserialization", "mutable (unsafeDeserialization.BadUnsafeDeserialization)");
    }

    /* Collections */

    @Test
    void testBadUnsafeDeserializationArray() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationArray.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationArray", "readObject");
        assertUDBug("BadUnsafeDeserializationArray", "mutable (unsafeDeserialization.BadUnsafeDeserializationArray)");
    }

    @Test
    void testBadUnsafeDeserializationList() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationList.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationList", "readObject");
        assertUDBug("BadUnsafeDeserializationList", "mutable (unsafeDeserialization.BadUnsafeDeserializationList)");
    }

    @Test
    void testBadUnsafeDeserializationHashMap() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationHashMap.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationHashMap", "readObject");
        assertUDBug("BadUnsafeDeserializationHashMap", "mutable (unsafeDeserialization.BadUnsafeDeserializationHashMap)");
    }

    @Test
    void testBadUnsafeDeserializationFinalCollection() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationFinalCollection.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationFinalCollection", "readObject");
        assertUDBug("BadUnsafeDeserializationFinalCollection", "mutable (unsafeDeserialization.BadUnsafeDeserializationFinalCollection)");
    }

    /**
     * The emum is count like an immutable already
     */
    @Test
    void testGoodUnsafeDeserializationEnum() {
        performAnalysis("unsafeDeserialization/enums/GoodUnsafeDeserializationEnum.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    /* Good examples */
    /* Basic examples */

    @Test
    void testGoodUnsafeDeserialization() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserialization.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationWrongName() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongMethodName.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationNoReadObject() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationNoReadObject.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationWrongSignature() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongSignature.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationSeveralFields.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationCopyConstructor() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor.class",
                "unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor$CopyClass.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationFactoryMethod() {
        performAnalysis("unsafeDeserialization/factory/GoodUnsafeDeserializationFactory.class",
                "unsafeDeserialization/factory/Car.class",
                "unsafeDeserialization/factory/MotorVehicle.class",
                "unsafeDeserialization/factory/CarFactory.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    /* Collections */

    @Test
    void testGoodUnsafeDeserializationCopyOf() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationCopyOf.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationArraycopy() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationArraycopy.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    void testGoodUnsafeDeserializationImmutableListCopyOf() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationImmutableListCopyOf.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    /**
     * There is a limitation with the detector because clone() creates a shallow copy, which is considered a bug
     * FP in lines 20-21: mutable = (Date) inDate.clone();
     */
    @Test
    void testBadUnsafeDeserializationClone() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationClone.class");
        assertBugTypeCountBetween("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 0, 1);
    }

    /* Java 17+ */

    /**
     * The record is count like an immutable already
     */
    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testBadUnsafeDeserializationRecord() {
        performAnalysis("../java17/unsafeDeserialization/BadUnsafeDeserializationRecord.class",
                "../java17/unsafeDeserialization/DateRecord.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfList() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOf.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfSet() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationSetCopyOf.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfMap() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationMapCopyOf.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCollectionsCopy() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationCollectionsCopy.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationGenerics() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOfGenerics.class");
        assertNoBugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES");
    }

    private void assertUDBug(String className, String fields) {
        final BugInstanceMatcher bugInstanceMatcher = new BugInstanceMatcherBuilder()
                .bugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES")
                .inClass(className)
                .inMethod("readObject")
                .build();

        BugCollection bugCollection = getBugCollection();
        for (BugInstance bugInstance : bugCollection.getCollection()) {
            if (bugInstanceMatcher.matches(bugInstance)) {
                for (BugAnnotation bugAnnotation : bugInstance.getAnnotations()) {
                    if (bugAnnotation instanceof StringAnnotation) {
                        StringAnnotation pattern = (StringAnnotation) bugAnnotation;
                        if (fields.equals(pattern.getValue())) {
                            return;
                        }
                    }
                }
            }
        }

        assert false;
    }
}
