package edu.umd.cs.findbugs.detect;

import edu.umd.cs.findbugs.AbstractIntegrationTest;
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
    }

    @Test
    void testBadUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationSeveralFields.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationSeveralFields", "readObject");
    }

    @Test
    void testBadUnsafeDeserializationWrongOrder() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationWrongOrder.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationWrongOrder", "readObject");
    }

    /* Inheritance */

    @Test
    void testBadUnsafeDeserializationInheritance() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class",
                "unsafeDeserialization/BadUnsafeDeserializationInheritance.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserialization", "readObject");
    }

    /* Collections */

    @Test
    void testBadUnsafeDeserializationArray() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationArray.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationArray", "readObject");
    }

    @Test
    void testBadUnsafeDeserializationList() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationList.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationList", "readObject");
    }

    @Test
    void testBadUnsafeDeserializationHashMap() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationHashMap.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationHashMap", "readObject");
    }

    @Test
    void testBadUnsafeDeserializationFinalCollection() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationFinalCollection.class");
        assertBugTypeCount("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", 1);
        assertBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationFinalCollection", "readObject");
    }

    /**
     * The emum is count like an immutable already
     */
    @Test
    void testBadUnsafeDeserializationEnum() {
        performAnalysis("unsafeDeserialization/enums/BadUnsafeDeserializationEnum.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationEnum", "readObject");
    }

    /* Good examples */
    /* Basic examples */

    @Test
    void testGoodUnsafeDeserialization() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserialization.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserialization", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationWrongName() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongMethodName.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationWrongMethodName", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationNoReadObject() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationNoReadObject.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationNoReadObject", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationWrongSignature() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongSignature.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationWrongSignature", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationSeveralFields.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationSeveralFields", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationCopyConstructor() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor.class",
                "unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor$CopyClass.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationCopyConstructor", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationFactoryMethod() {
        performAnalysis("unsafeDeserialization/factory/GoodUnsafeDeserializationFactory.class",
                "unsafeDeserialization/factory/Car.class",
                "unsafeDeserialization/factory/MotorVehicle.class",
                "unsafeDeserialization/factory/CarFactory.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationFactory", "readObject");
    }

    /* Collections */

    @Test
    void testGoodUnsafeDeserializationCopyOf() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationCopyOf.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationCopyOf", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationArraycopy() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationArraycopy.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationArraycopy", "readObject");
    }

    @Test
    void testGoodUnsafeDeserializationImmutableListCopyOf() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationImmutableListCopyOf.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationImmutableListCopyOf", "readObject");
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
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "BadUnsafeDeserializationRecord", "readObject");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfList() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOf.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationListCopyOf", "readObject");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfSet() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationSetCopyOf.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationSetCopyOf", "readObject");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCopyOfMap() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationMapCopyOf.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationMapCopyOf", "readObject");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationCollectionsCopy() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationCollectionsCopy.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationCollectionsCopy", "readObject");
    }

    @Test
    @DisabledOnJre({ JRE.JAVA_8, JRE.JAVA_11 })
    void testGoodUnsafeDeserializationGenerics() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOfGenerics.class");
        assertNoBugInMethod("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES", "GoodUnsafeDeserializationListCopyOfGenerics", "readObject");
    }
}
