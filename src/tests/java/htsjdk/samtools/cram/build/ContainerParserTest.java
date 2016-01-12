package htsjdk.samtools.cram.build;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.cram.structure.Container;
import htsjdk.samtools.cram.structure.CramCompressionRecord;
import htsjdk.samtools.cram.structure.Slice;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by vadim on 11/01/2016.
 */
public class ContainerParserTest {

    @Test
    public void testSingleRefContainer() throws IOException, IllegalAccessException {
        SAMFileHeader samFileHeader = new SAMFileHeader();
        ContainerFactory factory = new ContainerFactory(samFileHeader, 10);
        List<CramCompressionRecord> records = new ArrayList<>();
        for (int i=0; i<10; i++) {
            CramCompressionRecord record = new CramCompressionRecord();
            record.readBases="AAA".getBytes();
            record.qualityScores="!!!".getBytes();
            record.setSegmentUnmapped(false);
            record.readName=""+i;
            record.sequenceId=0;
            record.setLastSegment(true);
            record.readFeatures = Collections.emptyList();

            records.add(record);
        }

        Container container = factory.buildContainer(records);
        Assert.assertEquals(container.nofRecords, 10);
        Assert.assertEquals(container.sequenceId, 0);

        ContainerParser parser = new ContainerParser(samFileHeader);
        final Set<Integer> referenceSet = parser.getReferences(container);
        Assert.assertNotNull(referenceSet);
        Assert.assertEquals(referenceSet.size(), 1);
        Assert.assertEquals(referenceSet.iterator().next().intValue(), 0);
    }

    @Test
    public void testUnmappedContainer() throws IOException, IllegalAccessException {
        SAMFileHeader samFileHeader = new SAMFileHeader();
        ContainerFactory factory = new ContainerFactory(samFileHeader, 10);
        List<CramCompressionRecord> records = new ArrayList<>();
        for (int i=0; i<10; i++) {
            CramCompressionRecord record = new CramCompressionRecord();
            record.readBases="AAA".getBytes();
            record.qualityScores="!!!".getBytes();
            record.setSegmentUnmapped(true);
            record.readName=""+i;
            record.sequenceId= SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX;
            record.setLastSegment(true);

            records.add(record);
        }

        Container container = factory.buildContainer(records);
        Assert.assertEquals(container.nofRecords, 10);
        Assert.assertEquals(container.sequenceId, Slice.UNMAPPED_OR_NO_REFERENCE);

        ContainerParser parser = new ContainerParser(samFileHeader);
        final Set<Integer> referenceSet = parser.getReferences(container);
        Assert.assertNotNull(referenceSet);
        Assert.assertEquals(referenceSet.size(), 1);
        Assert.assertEquals(referenceSet.iterator().next().intValue(), Slice.UNMAPPED_OR_NO_REFERENCE);

    }

    @Test
    public void testMappedAndUnmappedContainer() throws IOException, IllegalAccessException {
        SAMFileHeader samFileHeader = new SAMFileHeader();
        ContainerFactory factory = new ContainerFactory(samFileHeader, 10);
        List<CramCompressionRecord> records = new ArrayList<>();
        for (int i=0; i<10; i++) {
            CramCompressionRecord record = new CramCompressionRecord();
            record.readBases="AAA".getBytes();
            record.qualityScores="!!!".getBytes();
            record.readName=""+i;
            record.alignmentStart=i+1;

            record.setMultiFragment(false);
            if (i%2==0) {
                record.sequenceId = SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX;
                record.setSegmentUnmapped(true);
            } else {
                record.sequenceId=0;
                record.readFeatures = Collections.emptyList();
                record.setSegmentUnmapped(false);
            }
            records.add(record);
        }



        Container container = factory.buildContainer(records);
        Assert.assertEquals(container.nofRecords, 10);
        Assert.assertEquals(container.sequenceId, Slice.MULTI_REFERENCE);

        ContainerParser parser = new ContainerParser(samFileHeader);
        final Set<Integer> referenceSet = parser.getReferences(container);
        Assert.assertNotNull(referenceSet);
        Assert.assertEquals(referenceSet.size(), 2);
        Assert.assertTrue(referenceSet.contains(Slice.UNMAPPED_OR_NO_REFERENCE));
        Assert.assertTrue(referenceSet.contains(0));
    }

    @Test
    public void testMultirefContainer() throws IOException, IllegalAccessException {
        SAMFileHeader samFileHeader = new SAMFileHeader();
        ContainerFactory factory = new ContainerFactory(samFileHeader, 10);
        List<CramCompressionRecord> records = new ArrayList<>();
        for (int i=0; i<10; i++) {
            CramCompressionRecord record = new CramCompressionRecord();
            record.readBases="AAA".getBytes();
            record.qualityScores="!!!".getBytes();
            record.readName=""+i;
            record.alignmentStart=i+1;

            record.setMultiFragment(false);
            if (i < 9) {
                record.sequenceId=i;
                record.readFeatures = Collections.emptyList();
                record.setSegmentUnmapped(false);
            } else {
                record.sequenceId = SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX;
                record.setSegmentUnmapped(true);
            }
            records.add(record);
        }

        Container container = factory.buildContainer(records);
        Assert.assertEquals(container.nofRecords, 10);
        Assert.assertEquals(container.sequenceId, Slice.MULTI_REFERENCE);

        ContainerParser parser = new ContainerParser(samFileHeader);
        final Set<Integer> referenceSet = parser.getReferences(container);
        Assert.assertNotNull(referenceSet);
        Assert.assertEquals(referenceSet.size(), 10);
        Assert.assertTrue(referenceSet.contains(Slice.UNMAPPED_OR_NO_REFERENCE));
        for (int i=0; i<9; i++) {
            Assert.assertTrue(referenceSet.contains(i));
        }
    }
}
