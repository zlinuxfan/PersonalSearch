package Utils;

public class GuidOfElement {
    private int guid = 0;

    public GuidOfElement(int numberOfStartGuid) {
        this.guid = numberOfStartGuid;
    }

    public int next() {
        return ++this.guid;
    }
}
