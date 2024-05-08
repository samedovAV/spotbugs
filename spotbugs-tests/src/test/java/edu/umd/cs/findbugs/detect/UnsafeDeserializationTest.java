package edu.umd.cs.findbugs.detect;

import edu.umd.cs.findbugs.AbstractIntegrationTest;

import edu.umd.cs.findbugs.BugAnnotation;
import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.StringAnnotation;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.jupiter.api.Test;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

class UnsafeDeserializationTest extends AbstractIntegrationTest {

    @Test
    void testBadUnsafeDeserialization() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserialization", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationWrongOrder() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationWrongOrder.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationWrongOrder", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationInheritance() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class",
                "unsafeDeserialization/BadUnsafeDeserializationInheritance.class");
        assertNumOfUDBugs(2);

        assertUDBug("BadUnsafeDeserialization", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationList() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationList.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationList", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationHashMap() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationHashMap.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationHashMap", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationArray() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationArray.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationArray", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationFinalCollection() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationFinalCollection.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationFinalCollection", "mutable");
    }

    @Test
    void testBadUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationSeveralFields.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationSeveralFields", "anotherMutable, mutable");
    }

    /**
     * The emum is count like an immutable already
     */
    @Test
    void testBadUnsafeDeserializationEnum() {
        performAnalysis("unsafeDeserialization/enums/BadUnsafeDeserializationEnum.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testBadUnsafeDeserializationRecord() {
        performAnalysis("../java17/unsafeDeserialization/BadUnsafeDeserializationRecord.class",
                "../java17/unsafeDeserialization/DateRecord.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationRecord", "date");
    }

    @Test
    void testGoodUnsafeDeserialization() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserialization.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationNoReadObject() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationNoReadObject.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationWrongSignature() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongSignature.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCopyOf() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationCopyOf.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCopyConstructor() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor.class",
                "unsafeDeserialization/GoodUnsafeDeserializationCopyConstructor$CopyClass.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationFactoryMethod() {
        performAnalysis("unsafeDeserialization/factory/GoodUnsafeDeserializationFactory.class",
                "unsafeDeserialization/factory/Car.class",
                "unsafeDeserialization/factory/MotorVehicle.class",
                "unsafeDeserialization/factory/CarFactory.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCopyOfList() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOf.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCopyOfSet() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationSetCopyOf.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCopyOfMap() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationMapCopyOf.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationCollectionsCopy() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationCollectionsCopy.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationArraycopy() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationArraycopy.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationImmutableListCopyOf() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationImmutableListCopyOf.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationClone() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationClone.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationWrongName() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationWrongMethodName.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationSeveralFields() {
        performAnalysis("unsafeDeserialization/GoodUnsafeDeserializationSeveralFields.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationGenerics() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationListCopyOfGenerics.class");
        assertNumOfUDBugs(0);
    }

    private void assertNumOfUDBugs(int num) {
        final BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder()
                .bugType("UD_UNSAFE_DESERIALIZATION_DEFENSIVE_COPIES")
                .build();

        assertThat(getBugCollection(), containsExactly(num, bugTypeMatcher));
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
                    if (bugAnnotation instanceof StringAnnotation pattern) {
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
