package com.example.albbamon.dto.request;

public class CreatePostRequestDto {

    private long userid;
    private String title;
    private String contents;
    private String file;

    public CreatePostRequestDto(long userid, String title, String contents, String file) {
        this.userid = userid;
        this.title = title;
        this.contents = contents;
        this.file = file;
    }

    public long getUserId() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getfile() {
        return file;
    }
}
