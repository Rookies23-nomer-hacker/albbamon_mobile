package com.example.albbamon;

public class JobModel {
    private Long id;        // ✅ 공고 ID 추가
    private String title;   // 제목
    private String subtitle;  // 커뮤니티 게시글의 부제목 (작성자)
    private String salary;  // 일반 알바 데이터의 급여 정보
    private String imageUrl;  // 이미지 URL

    // 🔥 생성자 (급여 정보 있는 경우, 일반 알바용)
    public JobModel(Long id, String title, String salary, String imageUrl) {
        this.id = id;
        this.title = title;
        this.salary = salary;
        this.imageUrl = imageUrl;
        this.subtitle = null; // 커뮤니티가 아니므로 subtitle은 없음
    }

    // 🔥 생성자 (커뮤니티 게시글용)
    public JobModel(Long id, String title, String subtitle, String imageUrl, boolean isCommunity) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.salary = null; // 알바 데이터가 아니므로 salary는 없음
    }

    // 🔥 Getter 메서드 추가
    public Long getId() { return id; }  // ✅ ID Getter 추가
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; } // 커뮤니티용
    public String getSalary() { return salary; } // 알바 데이터용
    public String getImageUrl() { return imageUrl; }
}
