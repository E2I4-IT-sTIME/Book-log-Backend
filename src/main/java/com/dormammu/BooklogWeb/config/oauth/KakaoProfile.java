package com.dormammu.BooklogWeb.config.oauth;

import lombok.Data;

@Data
public class KakaoProfile {

    public Long id;
    public String connected_at; // 서비스에 연결 완료된 시각
//    public Properties properties; // 사용자 프로퍼티(Property)
    public KakaoAccount kakao_account; // 카카오계정 정보

//    @Data
//    public class Properties {
//        public String nickname;
//    }

    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public class Profile {
            public String nickname;
            public String profile_image_url;  // 프로필 사진 URL
        }
    }

}
