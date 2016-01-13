package htsjdk.samtools.cram.encoding.reader;

import htsjdk.samtools.SAMRecord;

/**
* Created by vadim on 12/01/2016.
*/
public class AlignmentSpan {
    private int start;
	private int span;


    private int count;

    public AlignmentSpan(int start, int span) {
        this.setStart(start);
        this.setSpan(span);
        this.count = 1;
    }

    public AlignmentSpan(int start, int span, int count) {
        this.setStart(start);
        this.setSpan(span);
        this.count = count;
    }

    public void add(int start, int span, int count) {
        if (this.getStart() > start) {
            this.setSpan(Math.max(this.getStart() + this.getSpan(), start + span) - start);
            this.setStart(start);
        } else if (this.getStart() < start) {
            this.setSpan(Math.max(this.getStart() + this.getSpan(), start + span) - this.getStart());
        } else
            this.setSpan(Math.max(this.getSpan(), span));

        this.count += count;
    }

    public void addSingle(int start, int span) {
        add(start, span, 1);
    }

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getSpan() {
		return span;
	}

	public void setSpan(int span) {
		this.span = span;
	}

    public int getCount() {
        return count;
    }

    public static final AlignmentSpan UNMAPPED_SPAN = new AlignmentSpan(SAMRecord.NO_ALIGNMENT_START, 0) ;
}
