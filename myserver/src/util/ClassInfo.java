package util;

public class ClassInfo {
    public int mClassId;
    public String mClassName;
    public int mFatherId;
    public int mGroup;
    /*
     * mStatus:
     * 0: Normal class containing articles
     * 1: Class containing another classes
     * 2: Top classes
     * 3: invalid classes
     */

    public ClassInfo() {
        mClassId = 0;
        mClassName = null;
        mFatherId = 0;
        mGroup = -1;
    }
    public ClassInfo(int classId, String className,
            int fatherId, int group) {
        mClassId = classId;
        mClassName = className;
        mFatherId = fatherId;
        mGroup = group;
    }

    public void setValue(int classId, String className,
            int fatherId, int group) {
        mGroup = classId;
        mClassName = className;
        mFatherId = fatherId;
        mGroup = group;
    }
}
