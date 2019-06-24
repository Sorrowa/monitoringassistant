package cn.cdjzxy.monitoringassistant.mvp.model.entity.PagerInfo;

public class PagerInfoBean {

    /**
     * PageIndex : 1
     * PageSize : 10
     * StartIndex : 0
     * TotalPageCount : 70
     * TotalRowCount : 693
     */

    private int PageIndex;//当前页数
    private int PageSize;//当前页数的个数
    private int StartIndex;//
    private int TotalPageCount;//总条数
    private int TotalRowCount;//总个数

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int PageIndex) {
        this.PageIndex = PageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getStartIndex() {
        return StartIndex;
    }

    public void setStartIndex(int StartIndex) {
        this.StartIndex = StartIndex;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public int getTotalRowCount() {
        return TotalRowCount;
    }

    public void setTotalRowCount(int TotalRowCount) {
        this.TotalRowCount = TotalRowCount;
    }
}
