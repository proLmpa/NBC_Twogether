package com.example.twogether.checklist.entity;

import com.example.twogether.card.entity.Card;
import com.example.twogether.checklist.dto.CheckListRequestDto;
import com.example.twogether.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "checklist")
public class CheckList extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public void update(CheckListRequestDto chlRequestDto) {
        this.title = chlRequestDto.getTitle();
    }

//    @Builder.Default
//    @OneToMany(mappedBy = "checklist", orphanRemoval = true)
//    private List<Check> checks = new ArrayList<>();

}
