package korweb.model.dto;

import lombok.Builder;

@Builder
public class PageDto {

    private long totalcount;
    private int page;
    private int totalpage;
    private int startbtn;
    private int endbtn;
    // + Object는 자바 최상위 클래스, 모든 타입들의 자료들을 저장할 수 있다.
    // 즉 data에는 List<board> 혹은 List<Reply> 다양한 페이징한 정보를 대입하기 위해서
    private Object data;

    public PageDto() {}

    public PageDto(long totalcount, int page, int totalpage, int startbtn, int endbtn, Object data) {
        this.totalcount = totalcount;
        this.page = page;
        this.totalpage = totalpage;
        this.startbtn = startbtn;
        this.endbtn = endbtn;
        this.data = data;
    }

    public long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(long totalcount) {
        this.totalcount = totalcount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getStartbtn() {
        return startbtn;
    }

    public void setStartbtn(int startbtn) {
        this.startbtn = startbtn;
    }

    public int getEndbtn() {
        return endbtn;
    }

    public void setEndbtn(int endbtn) {
        this.endbtn = endbtn;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "totalcount=" + totalcount +
                ", page=" + page +
                ", totalpage=" + totalpage +
                ", startbtn=" + startbtn +
                ", endbtn=" + endbtn +
                ", data=" + data +
                '}';
    }
}
