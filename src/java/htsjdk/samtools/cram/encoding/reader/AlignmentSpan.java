package htsjdk.samtools.cram.encoding.reader;

/**
* Created by vadim on 12/01/2016.
*/
public class AlignmentSpan {
    private int start;
	private int span;

    public AlignmentSpan(int start, int span) {
        this.setStart(start);
        this.setSpan(span);
    }

    public void add(int start, int span) {
        if (this.getStart() > start) {
            this.setSpan(Math.max(this.getStart() + this.getSpan(), start + span) - start);
            this.setStart(start);
        } else if (this.getStart() < start) {
            this.setSpan(Math.max(this.getStart() + this.getSpan(), start + span) - this.getStart());
        } else
            this.setSpan(Math.max(this.getSpan(), span));
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
}
