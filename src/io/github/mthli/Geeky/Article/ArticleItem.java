package io.github.mthli.Geeky.Article;

import android.graphics.Bitmap;

public class ArticleItem implements Comparable<ArticleItem> {
    private String title;
    private String content;
    private String date;
    private String imgLink;
    private String articleLink;
    private boolean flag;
    private Bitmap bitmap;

    public ArticleItem(
            String title,
            String content,
            String date,
            String imgLink,
            String articleLink,
            boolean flag,
            Bitmap bitmap
    ) {
        super();

        this.title = title;
        this.content = content;
        this.date = date;
        this.imgLink = imgLink;
        this.articleLink = articleLink;
        this.flag = flag;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getImgLink() {
        return imgLink;
    }
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getArticleLink() {
        return articleLink;
    }
    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public boolean getFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int compareTo(ArticleItem item) {
        if (this.articleLink != null) {
            return item.getArticleLink().toLowerCase().compareTo(this.articleLink.toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
