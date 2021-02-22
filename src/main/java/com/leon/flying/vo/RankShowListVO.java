package com.leon.flying.vo;

import java.util.List;

public class RankShowListVO {
    List<RankTypeVO> ranks;

    List<RankShowVO> rankList;

    public List<RankTypeVO> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankTypeVO> ranks) {
        this.ranks = ranks;
    }

    public List<RankShowVO> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankShowVO> rankList) {
        this.rankList = rankList;
    }
}
