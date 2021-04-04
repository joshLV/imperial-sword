package cn.lovike.tool.basic.framework.common.util.excel;

/**
 *
 * @author bodhi 2015年3月9日 上午11:42:35 <br>
 * @title 文件标题（以“|”分隔的字符串，形如：“日期|姓名|账号”，没有可为空）<br>
 * @content 文件的内容（数组元素为“|”分隔的字符串组成的数组）：没有可为空，生成的就是空的模板文件<br>
 * @fileType 文件预设格式，以“|”分隔的字符串，目前仅支持"n|n2|n4|n6|s|m|d|"七种格式 n为整数（例：0） n2位两位小数（例：0.00） n4为4位小数（例：0.0000）
 * n6为6位小数（例：0.000000） s为文本（例：abc123） m为货币（例：-9,999.99） d为日期（例：2013-1-2）<br>
 * @head 正文大标题<br>
 * @plusHead 正文副标题：数组中每个元素的字段以"|"分隔 例如：content[0] = "流水号:XXXXX|批次号:XXXX|业务日期:XXXXX|"; content[1] =
 * "操作员:XXXXX|授权员:XXXX|打印日期:XXXXX|";<br>
 * @tail 正文尾部内容（格式同正文副标题）
 */
public class SheetData {

    private String   title;
    private String   head;
    private String[] plusHead;
    private String[] content;
    private String[] tail;
    private String   fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String[] getPlusHead() {
        return plusHead;
    }

    public void setPlusHead(String[] plusHead) {
        this.plusHead = plusHead;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public String[] getTail() {
        return tail;
    }

    public void setTail(String[] tail) {
        this.tail = tail;
    }
}
