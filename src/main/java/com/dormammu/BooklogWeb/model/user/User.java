package com.dormammu.BooklogWeb.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserInfo userInfo;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    //@Column(nullable = false)
    private String email;

    private String imgHome;
    private String imgPath;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    @Column(nullable = true)
    private String job;

    //@Column(nullable = false)
    private String area;

    //@Column(nullable = false)
    private boolean active;

    //@Enumerated(EnumType.STRING)
    private String roles;  // USER, ADMIN
    // private RoleType role;
    @CreationTimestamp
    private Timestamp createDate;

    // 강의에서 추가한 내용
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
