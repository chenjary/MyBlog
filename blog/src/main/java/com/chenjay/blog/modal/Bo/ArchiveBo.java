package com.chenjay.blog.modal.Bo;

import com.chenjay.blog.modal.Vo.ContentVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chenjie
 * Created on 2017/2/23.
 */
public class ArchiveBo implements Serializable {

    private String date;
    private String count;
    private List<ContentVo> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ContentVo> getArticles() {
        return articles;
    }

    public void setArticles(List<ContentVo> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Archive [" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                ", articles=" + articles +
                ']';
    }
}
