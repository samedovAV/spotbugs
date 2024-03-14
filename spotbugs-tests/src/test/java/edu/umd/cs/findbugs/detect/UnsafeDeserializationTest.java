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

public class UnsafeDeserializationTest extends AbstractIntegrationTest {

    @Test
    void testBadUnsafeDeserializationOne() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserialization", "date");
    }

    @Test
    void testBadUnsafeDeserializationInheritance() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserialization.class",
                "unsafeDeserialization/BadUnsafeDeserializationInheritance.class");
        assertNumOfUDBugs(2);

        assertUDBug("BadUnsafeDeserializationInheritance", "date");
    }

    @Test
    void testBadUnsafeDeserializationList() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationList.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationList", "date");
    }

    @Test
    void testBadUnsafeDeserializationHashMap() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationHashMap.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationHashMap", "date");
    }

    @Test
    void testBadUnsafeDeserializationArray() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationArray.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationArray", "date");
    }

    @Test
    void testBadUnsafeDeserializationSixFinalCollection() {
        performAnalysis("unsafeDeserialization/BadUnsafeDeserializationFinalCollection.class");
        assertNumOfUDBugs(1);

        assertUDBug("BadUnsafeDeserializationFinalCollection", "date");
    }

    /*@Test
    void testBadUnsafeDeserializationSevenEnum() { // todo: fix case
        performAnalysis("unsafeDeserialization/enums/BadUnsafeDeserializationEnum.class");
        assertNumOfUDBugs(1);
    
        assertUDBug("BadUnsafeDeserializationSevenEnum", "date");
    }*/

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

    /*@Test
    void testGoodUnsafeDeserializationFactoryMethod() { // todo: rework
        performAnalysis("unsafeDeserialization/factory/GoodUnsafeDeserializationFactory.class",
            "unsafeDeserialization/factory/FactoryInterface.class",
            "unsafeDeserialization/factory/ClassFactory.class",
            "unsafeDeserialization/factory/ClassFactoryItem.class",
            "unsafeDeserialization/factory/FactoryInterfaceImplementation.class");
        assertNumOfUDBugs(0);
    }*/

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
    void testGoodUnsafeDeserializationCollectionsCopy() {
        performAnalysis("../java17/unsafeDeserialization/GoodUnsafeDeserializationCollectionsCopy.class");
        assertNumOfUDBugs(0);
    }

    @Test
    void testGoodUnsafeDeserializationArraycopy() {
        performAnalysis("unsafeDeserialization/arrays/GoodUnsafeDeserializationArraycopy.class");
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
