package android.icu.text;

import android.icu.impl.CharacterIteratorWrapper;
import android.icu.impl.coll.Collation;
import android.icu.impl.coll.CollationData;
import android.icu.impl.coll.CollationIterator;
import android.icu.impl.coll.CollationSettings;
import android.icu.impl.coll.ContractionsAndExpansions;
import android.icu.impl.coll.ContractionsAndExpansions.CESink;
import android.icu.impl.coll.FCDIterCollationIterator;
import android.icu.impl.coll.FCDUTF16CollationIterator;
import android.icu.impl.coll.IterCollationIterator;
import android.icu.impl.coll.UTF16CollationIterator;
import android.icu.impl.coll.UVector32;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Map;

public final class CollationElementIterator {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int IGNORABLE = 0;
    public static final int NULLORDER = -1;
    private byte dir_;
    private CollationIterator iter_;
    private UVector32 offsets_;
    private int otherHalf_;
    private RuleBasedCollator rbc_;
    private String string_;

    private static final class MaxExpSink implements CESink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Map<Integer, Integer> maxExpansions;

        static {
            Class cls = CollationElementIterator.class;
        }

        MaxExpSink(Map<Integer, Integer> h) {
            this.maxExpansions = h;
        }

        public void handleCE(long ce) {
        }

        public void handleExpansion(long[] ces, int start, int length) {
            if (length > 1) {
                int count = 0;
                for (int i = 0; i < length; i++) {
                    count += CollationElementIterator.ceNeedsTwoParts(ces[start + i]) ? 2 : 1;
                }
                long ce = ces[(start + length) - 1];
                long p = ce >>> 32;
                int lower32 = (int) ce;
                int lastHalf = CollationElementIterator.getSecondHalf(p, lower32);
                if (lastHalf == 0) {
                    lastHalf = CollationElementIterator.getFirstHalf(p, lower32);
                } else {
                    lastHalf |= 192;
                }
                Integer oldCount = (Integer) this.maxExpansions.get(Integer.valueOf(lastHalf));
                if (oldCount == null || count > oldCount.intValue()) {
                    this.maxExpansions.put(Integer.valueOf(lastHalf), Integer.valueOf(count));
                }
            }
        }
    }

    public static final int primaryOrder(int ce) {
        return (ce >>> 16) & DateTimePatternGenerator.MATCH_ALL_FIELDS_LENGTH;
    }

    public static final int secondaryOrder(int ce) {
        return (ce >>> 8) & 255;
    }

    public static final int tertiaryOrder(int ce) {
        return ce & 255;
    }

    private static final int getFirstHalf(long p, int lower32) {
        return ((((int) p) & -65536) | ((lower32 >> 16) & 65280)) | ((lower32 >> 8) & 255);
    }

    private static final int getSecondHalf(long p, int lower32) {
        return ((((int) p) << 16) | ((lower32 >> 8) & 65280)) | (lower32 & 63);
    }

    private static final boolean ceNeedsTwoParts(long ce) {
        return (281470698455103L & ce) != 0;
    }

    private CollationElementIterator(RuleBasedCollator collator) {
        this.iter_ = null;
        this.rbc_ = collator;
        this.otherHalf_ = 0;
        this.dir_ = (byte) 0;
        this.offsets_ = null;
    }

    CollationElementIterator(String source, RuleBasedCollator collator) {
        this(collator);
        setText(source);
    }

    CollationElementIterator(CharacterIterator source, RuleBasedCollator collator) {
        this(collator);
        setText(source);
    }

    CollationElementIterator(UCharacterIterator source, RuleBasedCollator collator) {
        this(collator);
        setText(source);
    }

    public int getOffset() {
        if (this.dir_ >= (byte) 0 || this.offsets_ == null || this.offsets_.isEmpty()) {
            return this.iter_.getOffset();
        }
        int i = this.iter_.getCEsLength();
        if (this.otherHalf_ != 0) {
            i++;
        }
        return this.offsets_.elementAti(i);
    }

    public int next() {
        if (this.dir_ > (byte) 1) {
            if (this.otherHalf_ != 0) {
                int oh = this.otherHalf_;
                this.otherHalf_ = 0;
                return oh;
            }
        } else if (this.dir_ == (byte) 1) {
            this.dir_ = (byte) 2;
        } else if (this.dir_ == (byte) 0) {
            this.dir_ = (byte) 2;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        this.iter_.clearCEsIfNoneRemaining();
        long ce = this.iter_.nextCE();
        if (ce == Collation.NO_CE) {
            return -1;
        }
        long p = ce >>> 32;
        int lower32 = (int) ce;
        int firstHalf = getFirstHalf(p, lower32);
        int secondHalf = getSecondHalf(p, lower32);
        if (secondHalf != 0) {
            this.otherHalf_ = secondHalf | 192;
        }
        return firstHalf;
    }

    public int previous() {
        int oh;
        int i = 0;
        if (this.dir_ < (byte) 0) {
            if (this.otherHalf_ != 0) {
                oh = this.otherHalf_;
                this.otherHalf_ = 0;
                return oh;
            }
        } else if (this.dir_ == (byte) 0) {
            this.iter_.resetToOffset(this.string_.length());
            this.dir_ = (byte) -1;
        } else if (this.dir_ == (byte) 1) {
            this.dir_ = (byte) -1;
        } else {
            throw new IllegalStateException("Illegal change of direction");
        }
        if (this.offsets_ == null) {
            this.offsets_ = new UVector32();
        }
        if (this.iter_.getCEsLength() == 0) {
            i = this.iter_.getOffset();
        }
        oh = i;
        long ce = this.iter_.previousCE(this.offsets_);
        if (ce == Collation.NO_CE) {
            return -1;
        }
        long p = ce >>> 32;
        int lower32 = (int) ce;
        int firstHalf = getFirstHalf(p, lower32);
        int secondHalf = getSecondHalf(p, lower32);
        if (secondHalf == 0) {
            return firstHalf;
        }
        if (this.offsets_.isEmpty()) {
            this.offsets_.addElement(this.iter_.getOffset());
            this.offsets_.addElement(oh);
        }
        this.otherHalf_ = firstHalf;
        return secondHalf | 192;
    }

    public void reset() {
        this.iter_.resetToOffset(0);
        this.otherHalf_ = 0;
        this.dir_ = (byte) 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOffset(int newOffset) {
        if (newOffset > 0 && newOffset < this.string_.length()) {
            int lastSafeOffset = newOffset;
            do {
                char c = this.string_.charAt(lastSafeOffset);
                if (this.rbc_.isUnsafe(c) && (!Character.isHighSurrogate(c) || this.rbc_.isUnsafe(this.string_.codePointAt(lastSafeOffset)))) {
                    lastSafeOffset--;
                } else if (lastSafeOffset < newOffset) {
                    int offset = lastSafeOffset;
                    do {
                        this.iter_.resetToOffset(lastSafeOffset);
                        int offset2;
                        do {
                            this.iter_.nextCE();
                            offset2 = this.iter_.getOffset();
                            offset = offset2;
                        } while (offset2 == lastSafeOffset);
                        if (offset <= newOffset) {
                            lastSafeOffset = offset;
                            continue;
                        }
                    } while (offset < newOffset);
                    newOffset = lastSafeOffset;
                }
            } while (lastSafeOffset > 0);
            if (lastSafeOffset < newOffset) {
            }
        }
        this.iter_.resetToOffset(newOffset);
        this.otherHalf_ = 0;
        this.dir_ = (byte) 1;
    }

    public void setText(String source) {
        CollationIterator newIter;
        this.string_ = source;
        boolean numeric = ((CollationSettings) this.rbc_.settings.readOnly()).isNumeric();
        if (((CollationSettings) this.rbc_.settings.readOnly()).dontCheckFCD()) {
            newIter = new UTF16CollationIterator(this.rbc_.data, numeric, this.string_, 0);
        } else {
            newIter = new FCDUTF16CollationIterator(this.rbc_.data, numeric, this.string_, 0);
        }
        this.iter_ = newIter;
        this.otherHalf_ = 0;
        this.dir_ = (byte) 0;
    }

    public void setText(UCharacterIterator source) {
        this.string_ = source.getText();
        try {
            CollationIterator newIter;
            UCharacterIterator src = (UCharacterIterator) source.clone();
            src.setToStart();
            boolean numeric = ((CollationSettings) this.rbc_.settings.readOnly()).isNumeric();
            if (((CollationSettings) this.rbc_.settings.readOnly()).dontCheckFCD()) {
                newIter = new IterCollationIterator(this.rbc_.data, numeric, src);
            } else {
                newIter = new FCDIterCollationIterator(this.rbc_.data, numeric, src, 0);
            }
            this.iter_ = newIter;
            this.otherHalf_ = 0;
            this.dir_ = (byte) 0;
        } catch (CloneNotSupportedException e) {
            setText(source.getText());
        }
    }

    public void setText(CharacterIterator source) {
        CollationIterator newIter;
        UCharacterIterator src = new CharacterIteratorWrapper(source);
        src.setToStart();
        this.string_ = src.getText();
        boolean numeric = ((CollationSettings) this.rbc_.settings.readOnly()).isNumeric();
        if (((CollationSettings) this.rbc_.settings.readOnly()).dontCheckFCD()) {
            newIter = new IterCollationIterator(this.rbc_.data, numeric, src);
        } else {
            newIter = new FCDIterCollationIterator(this.rbc_.data, numeric, src, 0);
        }
        this.iter_ = newIter;
        this.otherHalf_ = 0;
        this.dir_ = (byte) 0;
    }

    static final Map<Integer, Integer> computeMaxExpansions(CollationData data) {
        Map<Integer, Integer> maxExpansions = new HashMap();
        new ContractionsAndExpansions(null, null, new MaxExpSink(maxExpansions), true).forData(data);
        return maxExpansions;
    }

    public int getMaxExpansion(int ce) {
        return getMaxExpansion(this.rbc_.tailoring.maxExpansions, ce);
    }

    static int getMaxExpansion(Map<Integer, Integer> maxExpansions, int order) {
        if (order == 0) {
            return 1;
        }
        if (maxExpansions != null) {
            Integer num = (Integer) maxExpansions.get(Integer.valueOf(order));
            Integer max = num;
            if (num != null) {
                return max.intValue();
            }
        }
        if ((order & 192) == 192) {
            return 2;
        }
        return 1;
    }

    private byte normalizeDir() {
        return this.dir_ == (byte) 1 ? (byte) 0 : this.dir_;
    }

    public boolean equals(Object that) {
        boolean z = true;
        if (that == this) {
            return true;
        }
        if (!(that instanceof CollationElementIterator)) {
            return false;
        }
        CollationElementIterator thatceiter = (CollationElementIterator) that;
        if (!(this.rbc_.equals(thatceiter.rbc_) && this.otherHalf_ == thatceiter.otherHalf_ && normalizeDir() == thatceiter.normalizeDir() && this.string_.equals(thatceiter.string_) && this.iter_.equals(thatceiter.iter_))) {
            z = false;
        }
        return z;
    }

    @Deprecated
    public int hashCode() {
        return 42;
    }

    @Deprecated
    public RuleBasedCollator getRuleBasedCollator() {
        return this.rbc_;
    }
}
