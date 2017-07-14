package Utils;

public class GuidOfElement {
    private int guid = 0;

    public GuidOfElement(int numberOfStartGuid) {
        this.guid = numberOfStartGuid;
    }

    public int getGuid() {
        return this.guid;
    }

    public int next() {
        this.guid++;
        return getGuid();
    }
}
